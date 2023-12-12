/*
 *  Copyright 2023 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.rps.model.domain;

import androidx.annotation.NonNull;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Encapsulates (for simulation) a simple intransitive ecosystem&mdash;as exemplified by certain E.
 * coli ecosystems, competition between male side-blotched lizards, and the game of
 * Rock-Paper-Scissors. The ecosystem in this model supports any odd {@code int} number (at least 3)
 * of breeds, and assumes equal strength between breeds&mdash;both in initial populations and in
 * competitive interactions.
 * <p>Key</p>
 * <ul><li>Individuals in this ecosystem do not move, but are placed in all cells of a grid (or
 * points of a lattice); this is the terrain of the ecosystem.</li>
 * <li>In each iteration of the simulation, a random individual is selected, along with one of that
 * individual's 4 immediately adjacent neighbors (i.e., in the Von Neumann neighborhood), also
 * selected at random.</li>
 * <li>If the two selected individuals are of the same breed, then there is no change; otherwise,
 * the loser of the competition between the two (e.g., rock losing to paper) is replaced by a copy
 * of the winner.</li>
 * <li>Optionally, competitive interaction may be preceded (with a specified probability) by random
 * selection of two (not necessarily adjacent) individuals, with the selected individuals trading
 * locations in the terrain.</li></ul>
 */
public class Ecosystem {

  private final int initialBreedCount;
  private final int size;
  private final boolean toroidal;
  private final Random rng;
  private final int[][] terrain;
  private final int[][] safeTerrain;
  private final int[] populations;
  private final int[] safePopulations;
  private final int absorptionThreshold;
  private final Occupant attacker;
  private final Occupant defender;
  private final Occupant change;

  private long iterationCount;
  private int currentBreedCount;
  private boolean absorbed;

  /**
   * Initializes this instance with the specified number of breeds, terrain size, and terrain
   * topology. Since selection of individuals for competition is stochastic, a source of randomness
   * must also be provided.
   *
   * @param numBreeds Number of distinct breeds to populate the ecosystem.
   * @param size      Terrain height and width.
   * @param toroidal  Flag specifying whether terrain is toroidal (with wrapping edges) or a box
   *                  (with closed edges).
   * @param rng       Source of randomness.
   */
  public Ecosystem(int numBreeds, int size, boolean toroidal, @NonNull Random rng) {
    initialBreedCount = numBreeds;
    currentBreedCount = numBreeds;
    this.size = size;
    this.toroidal = toroidal;
    this.rng = rng;
    populations = new int[numBreeds];
    safePopulations = new int[numBreeds];
    terrain = IntStream.range(0, size)
        .mapToObj((ignoredRowIndex) ->
            IntStream.range(0, size)
                .map((ignoredColIndex) -> rng.nextInt(numBreeds))
                .peek((breed) -> populations[breed]++)
                .toArray()
        )
        .toArray(int[][]::new);
    safeTerrain = new int[size][size];
    absorptionThreshold = size * size;
    attacker = new Occupant();
    defender = new Occupant();
    change = new Occupant();
  }

  /**
   * Executes a single iteration of the simulation. This consists of possible random selection (with
   * likelihood specified by {@code swapProbability} of a pair of individuals to swap locations,
   * followed by random selection of an individual and one of its neighbors for competitive
   * interaction.
   *
   * @param swapProbability Probability of preceding competitive interaction by swapping a randomly
   *                        selected pair of individuals.
   * @return Flag indicating whether competitive interaction resulted in a replacement of an
   * individual ({@code true}) or no change ({@code false}).
   */
  public boolean iterate(float swapProbability) {
    boolean terrainChanged = false;
    if (!absorbed) {
      if (swapProbability > 0 && rng.nextFloat() < swapProbability) {
        swapRandomPair();
        terrainChanged = true;
      }
      terrainChanged |= competeRandomPair();
      iterationCount++;
    }
    return terrainChanged;
  }

  /**
   * Invokes {@link #iterate(float)} repeatedly, {@code numIteration} times, and returns the count
   * of those iterations in which a competitive interaction resulted in replacement of one
   * individual by a copy of the other.
   *
   * @param numIterations   Number of invocations of {@link #iterate(float)} to perform.
   * @param swapProbability Likelihood of a randomly selected pair of individuals trading places in
   *                        each iteration.
   * @return Number of iterations resulting in a change in population sizes.
   */
  public int iterate(int numIterations, float swapProbability) {
    int changeCount = 0;
    for (int iteration = 0; iteration < numIterations && !absorbed; iteration++) {
      if (iterate(swapProbability)) {
        changeCount++;
      }
    }
    return changeCount;
  }

  /**
   * Returns the initial number of breeds in the simulation.
   */
  public int getInitialBreedCount() {
    return initialBreedCount;
  }

  /**
   * Returns the current extant (surviving) number of breeds in the simulation.
   */
  public int getCurrentBreedCount() {
    return currentBreedCount;
  }

  /**
   * Returns the height and width of the ecosystem's terrain.
   */
  public int getSize() {
    return size;
  }

  /**
   * Returns a flag indicating the terrain's topology, where {@code true} denotes toroidal, and
   * {@code false} denotes a box with closed sides.
   */
  public boolean isToroidal() {
    return toroidal;
  }

  /**
   * Returns the contents of the terrain. This is a safe copy, in the sense that any changes made to
   * it by a consumer of this method will not reflect the underlying simulation. However, for
   * simulation performance, a new copy is not made each time this method is invoked; instead, the
   * contents of the array returned by this method are overwritten each time it is invoked.
   */
  public int[][] getTerrain() {
    for (int rowIndex = 0; rowIndex < size; rowIndex++) {
      System.arraycopy(terrain[rowIndex], 0, safeTerrain[rowIndex], 0, size);
    }
    return safeTerrain;
  }

  /**
   * Returns the current sizes of the ecosystem breed populations.
   */
  public int[] getPopulations() {
    System.arraycopy(populations, 0, safePopulations, 0, initialBreedCount);
    return safePopulations;
  }

  /**
   * Returns the number of iterations performed so far in the simulation.
   */
  public long getIterationCount() {
    return iterationCount;
  }

  /**
   * Returns a flag indicating whether the simulation has reached the absorbing state, in which only
   * one breed remains.
   */
  public boolean isAbsorbed() {
    return absorbed;
  }

  private void swapRandomPair() {
    randomize(attacker);
    do {
      randomize(defender);
    } while (attacker.getRow() == defender.getRow()
        && attacker.getColumn() == defender.getColumn());
    terrain[attacker.getRow()][attacker.getColumn()] = defender.getBreed();
    terrain[defender.getRow()][defender.getColumn()] = attacker.getBreed();
  }

  private boolean competeRandomPair() {
    randomize(attacker);
    Direction direction;
    int defenderRow;
    int defenderColumn;
    do {
      direction = Direction.random(rng);
      defenderRow = attacker.getRow() + direction.rowOffset;
      defenderColumn = attacker.getColumn() + direction.columnOffset;
    } while (!isInBounds(defenderRow, defenderColumn));
    if (toroidal) {
      defenderRow = normalize(defenderRow);
      defenderColumn = normalize(defenderColumn);
    }
    int defenderBreed = terrain[defenderRow][defenderColumn];
    defender.set(defenderRow, defenderColumn, defenderBreed);
    int comparison = compare(attacker.getBreed(), defender.getBreed());
    boolean changed = true;
    if (comparison < 0) {
      change.setFrom(attacker);
      change.setBreed(defenderBreed);
      update();
    } else if (comparison > 0) {
      change.setFrom(defender);
      change.setBreed(attacker.getBreed());
      update();
    } else {
      changed = false;
    }
    return changed;
  }

  private boolean isInBounds(int row, int column) {
    return toroidal || (row >= 0 && row < size && column >= 0 && column < size);
  }

  private void randomize(Occupant occupant) {
    int row = rng.nextInt(size);
    int column = rng.nextInt(size);
    occupant.setRow(row);
    occupant.setColumn(column);
    occupant.setBreed(terrain[row][column]);
  }

  private int normalize(int dimension) {
    dimension %= size;
    return (dimension < 0) ? (dimension + size) : dimension;
  }

  private int compare(int attackerBreed, int defenderBreed) {
    int distance = attackerBreed - defenderBreed;
    if (distance < 0) {
      distance += initialBreedCount;
    }
    return initialBreedCount - 2 * distance;
  }

  private void update() {
    int winningBreed = change.getBreed();
    int losingBreed = terrain[change.getRow()][change.getColumn()];
    terrain[change.getRow()][change.getColumn()] = winningBreed;
    if (--populations[losingBreed] <= 0) {
      currentBreedCount--;
    }
    if (++populations[winningBreed] >= absorptionThreshold) {
      absorbed = true;
    }
  }

  private static class Occupant {

    private int row;
    private int column;
    private int breed;

    public void setFrom(Occupant other) {
      set(other.row, other.column, other.breed);
    }

    public void set(int row, int column, int breed) {
      this.row = row;
      this.column = column;
      this.breed = breed;
    }

    public int getRow() {
      return row;
    }

    public void setRow(int row) {
      this.row = row;
    }

    public int getColumn() {
      return column;
    }

    public void setColumn(int column) {
      this.column = column;
    }

    public int getBreed() {
      return breed;
    }

    public void setBreed(int breed) {
      this.breed = breed;
    }

  }

  private enum Direction {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    private final int rowOffset;
    private final int columnOffset;

    Direction(int rowOffset, int columnOffset) {
      this.rowOffset = rowOffset;
      this.columnOffset = columnOffset;
    }

    public static Direction random(Random rng) {
      Direction[] values = values();
      return values[rng.nextInt(values.length)];
    }

  }

}
