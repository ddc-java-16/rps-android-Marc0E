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
package edu.cnm.deepdive.rps.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.rps.model.domain.Ecosystem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages and choreographs creation of, and subsequent operations performed on (and results
 * returned from) a simulation involving an instance of {@link Ecosystem}. Any mutation operations
 * performed on an ecosystem should be coordinated through an instance of this class, <em>not</em>
 * by direct invocation of mutator methods on the {@code Ecosystem} instance itself.
 */
@Singleton
public class EcosystemRepository {

  private static final String NO_ECOSYSTEM_OR_ABSORBING = "Ecosystem has not been created, or is already in the absorbing state (in which no further simulation is meaningful).";

  private final Random rng;
  private final Scheduler scheduler;
  private final MutableLiveData<Ecosystem> ecosystem;
  private final MutableLiveData<Boolean> running;

  private Subject<Integer> ticker;

  @Inject
  EcosystemRepository(@ApplicationContext Context context, Random rng) {
    this.rng = rng;
    scheduler = Schedulers.single();
    ecosystem = new MutableLiveData<>();
    running = new MutableLiveData<>();
  }

  /**
   * Creates and returns a {@link Single Single&lt;Ecosystem&gt;} which&mdash;when subscribed
   * to&mdash;creates an instance of {@link Ecosystem} and updates the value in the
   * {@link LiveData LiveData&lt;Ecosystem&gt;} container returned by {@link #getEcosystem()}.
   * <ul><li>The ecosystem terrain will be populated with approximately equal numbers of each
   * breed, with the total number of breeds specified by {@code numBreeds}.</li>
   * <li>The ecosystem's terrain is a lattice or grid, with height and width specified by
   * {@code size}, and with each cell occupied by an individual of one of the breeds.</li>
   * <li>The terrain may be thought of as a square box with closed
   * sides, or as a torus with wrapping edges, depending on the value of the {@code toroidal}
   * parameter.</li></ul>
   *
   * @param numBreeds Initial number of breeds inhabiting the terrain of the ecosystem.
   * @param size      Height and width of the terrain.
   * @param toroidal  Flag specifying whether the terrain is a torus ({@code true}) or box
   *                  ({@code false}.
   * @return {@link Single Single&lt;Ecosystem&gt;} that can be subscribed to, to create an
   * {@link Ecosystem}.
   */
  public Single<Ecosystem> create(int numBreeds, int size, boolean toroidal) {
    clearTicker();
    return Single.fromSupplier(() -> new Ecosystem(numBreeds, size, toroidal, rng))
        .subscribeOn(scheduler)
        .doOnSuccess(ecosystem::postValue);
  }

  /**
   * Creates and returns an {@link Observable Observable&lt;Integer&gt;} which&mdash;when subscribed
   * to&mdash;starts execution of the ecosystem simulation.
   * <ul><li>Each {@code onNext} event of the {@code Observable} consists of an invocation of
   * {@link Ecosystem#iterate(int, float) Ecosystem.iterate(iterationsPerBatch, swapProbability)}.
   * </li>
   * <li>The value passed to the subscribing
   * {@link io.reactivex.rxjava3.functions.Consumer Consumer&lt;Integer&gt;} is the number of
   * iterations in the batch in which a competitive interaction resulted in replacement of the
   * losing individual by a copy of the winning individual.</li>
   * <li>At the end of each batch of iterations, if the ecosystem has reached the absorbing state
   * (in which only a single breed is surviving), the {@code onComplete} event is triggered
   * automatically.</li></ul>
   *
   * @param iterationsPerBatch         Iterations of the simulation executed for each
   *                                   {@code onNext}.
   * @param swapProbability            Probability that a randomly selected pair is swapped at the
   *                                   start of each iteration.
   * @param millisecondsBetweenBatches Milliseconds by which each batch of iterations follows
   *                                   completion of the previous batch.
   * @return {@link Observable Observable&lt;Integer&gt;} that can be subscribed to, to start
   * execution of the simulation.
   * @throws IllegalStateException If the {@link Ecosystem} has not yet been created, or if it is
   *                               already in the absorbing state.
   */
  public Observable<Integer> run(
      int iterationsPerBatch, float swapProbability, int millisecondsBetweenBatches)
      throws IllegalStateException {
    if (ecosystem.getValue() == null || ecosystem.getValue().isAbsorbed()) {
      throw new IllegalStateException(NO_ECOSYSTEM_OR_ABSORBING);
    }
    clearTicker();
    ticker = BehaviorSubject.createDefault(0);
    running.postValue(true);
    return ticker
        .subscribeOn(scheduler)
        .doOnTerminate(() -> {
          ticker = null;
          running.postValue(false);
        })
        .delay(millisecondsBetweenBatches, TimeUnit.MILLISECONDS)
        .map((ignored) -> tick(iterationsPerBatch, swapProbability));
  }

  /**
   * Creates and returns a {@link Completable} which&mdash;when subscribed to&mdash;pauses execution
   * of the simulation. As a result of pausing the simulation, the {@code onComplete} event is
   * triggered for the {@link Observable Observable&lt;Integer&gt;} returned by the
   * {@link #run(int, float, int)} method.
   *
   * @return {@link Completable} that can be subscribed to, to pause execution of the simulation.
   */
  public Completable pause() {
    return Completable.fromAction(this::clearTicker)
        .subscribeOn(scheduler);
  }

  /**
   * Returns a reference to a {@link LiveData LiveData&lt;Ecosystem&gt;} containing the current
   * ecosystem.
   */
  public LiveData<Ecosystem> getEcosystem() {
    return ecosystem;
  }

  /**
   * Returns a reference to a {@link LiveData LiveData&lt;Boolean&gt;} containing a flag indicating
   * whether the simulation is running or paused.
   */
  public LiveData<Boolean> getRunning() {
    return running;
  }

  private void clearTicker() {
    if (ticker != null && !ticker.hasComplete()) {
      ticker.onComplete();
    }
  }

  private int tick(int numIterations, float swapProbability) {
    Ecosystem ecosystem = this.ecosystem.getValue();
    //noinspection DataFlowIssue
    int changes = ecosystem.iterate(numIterations, swapProbability);
    if (changes > 0) {
      this.ecosystem.postValue(ecosystem);
    }
    if (ecosystem.isAbsorbed()) {
      clearTicker();
    } else if (ticker != null && !ticker.hasComplete()) {
      ticker.onNext(0);
    }
    return changes;
  }

}
