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
package edu.cnm.deepdive.rps.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * Custom view that renders the terrain of an intransitive ecosystem (e.g. Rock-Paper-Scissors) as a
 * lattice or grid. All cells in the grid are occupied by members of the competing breeds, and each
 * is displayed as a circle filled with a color specific to that breed.
 */
public class TerrainView extends View {

  private static final float COLOR_SATURATION = 1f;
  private static final float COLOR_VALUE = 1f;

  private int numBreeds;
  private Paint[] breedPaints;
  private int[][] terrain;

  /**
   * Chains to the corresponding superclass constructor to initialize the inherited state.
   *
   * @param context Android context in which this instance is created.
   */
  public TerrainView(Context context) {
    super(context);
  }

  /**
   * Chains to the corresponding superclass constructor to initialize the inherited state.
   *
   * @param context Android context in which this instance is created.
   * @param attrs Layout attributes.
   */
  public TerrainView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Chains to the corresponding superclass constructor to initialize the inherited state.
   *
   * @param context Android context in which this instance is created.
   * @param attrs Layout attributes.
   * @param defStyleAttr Style attributes.
   */
  public TerrainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * Chains to the corresponding superclass constructor to initialize the inherited state.
   *
   * @param context Android context in which this instance is created.
   * @param attrs Layout attributes.
   * @param defStyleAttr Style attributes.
   * @param defStyleRes Style resource.
   */
  public TerrainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  /**
   * Computes the dimensions of this instance, setting them to be those of the largest square that
   * conforms to the values of the {@code android:layout_width} and {@code android:layout_height}
   * attributes set in the layout.
   *
   * @param widthMeasureSpec  horizontal space requirements as imposed by the parent. The
   *                          requirements are encoded with {@link android.view.View.MeasureSpec}.
   * @param heightMeasureSpec vertical space requirements as imposed by the parent. The requirements
   *                          are encoded with {@link android.view.View.MeasureSpec}.
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = getSuggestedMinimumWidth();
    int height = getSuggestedMinimumHeight();
    width =
        resolveSizeAndState(getPaddingLeft() + getPaddingRight() + width, widthMeasureSpec, 0);
    height =
        resolveSizeAndState(getPaddingTop() + getPaddingBottom() + height, heightMeasureSpec, 0);
    int size = Math.max(width, height);
    setMeasuredDimension(size, size);
  }

  /**
   * Iterates over the terrain (specified as an argument to {@link #setTerrain(int[][])}, rendering
   * its contents to the {@code canvas}. Each row of the terrain array is rendered as a row of
   * circles, squares, or rounded rectangles, each drawn with the {@link Paint} instance
   * corresponding to the breed number of the corresponding element in the terrain.
   *
   * @param canvas Drawing surface.
   */
  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);

    // TODO Do the following ONLY IF neither terrain nor breedPaints is null. THIS IS IMPORTANT!

      // TODO Declare and initialize a float variable to hold the size of the individual ovals to be
      //  drawn. To compute the value assigned to this variable:
      //  1. Get the width (or height) of this view with getWidth() (or getHeight()),
      //  2. Get the number of rows (or columns) in the terrain using terrain.length (or
      //     terrain[0].length).
      //  3. Divide the value from #1 by the value from #2. IMPORTANT: Cast the numerator or
      //     denominator to float, so that the division will be done using floating-point
      //     arithmetic. For example, you might use (float) getHeight() / terrain.length.
      //  4. Assign the division result to the variable.

      // TODO Iterate over all of the rows and columns of terrain, using a traditional for loop
      //  (that is, with a row index and a column index). For each element, use canvas.drawOval to
      //  draw an oval for the current element of terrain.
      //  - The x coordinate of the left side of the oval can be computed by multiplying the oval
      //    size (above) with the current column index.
      //  - The x coordinate of the right side of the oval can be computed by multiplying the oval
      //    size (above) and (1 + column index); that is, the x coordinate of the right side of the
      //    oval is the same as the x coordinate of the left side of any oval in the next column to
      //    the right.
      //  - The y coordinate of the top of the oval can be computed by multiplying the oval size
      //    (above) and the current row index.
      //  - The y coordinate of the bottom of the oval can be computed by multiplying the oval size
      //    (above) and (1 + row index); that is, the y coordinate of the right side of the oval
      //    is the same as the y coordinate of the top of any oval in the next row down.
      //  - The Paint instance can be obtained from the breedPaints array, at the position
      //    corresponding to the terrain element value. For example, if the value of terrain at the
      //    current row and column index positions is 5, you would use the Paint instance found in
      //    breedPaints[5]).

      // TODO STRETCH GOAL: If the computed size of each oval is less than a threshold size of 20,
      //  draw rectangles (squares, in this case) instead of ovals (circles). However, do this
      //  WITHOUT including an if statement inside the loop. (Hint: Think about a lambda.)

  }

  /**
   * Constructs an array of {@link Paint} instances with a length equal to {@code numBreeds}. The
   * instance at a given position in of the array will be used when drawing each individual of the
   * breed identified by the same number as that position. For example, the {@link Paint} instance
   * in position 2 will be used to draw all members of breed 2 in the grid.
   * <p>All of the color values in the array are specified using the Hue-Saturation-Value (HSV)
   * color model: all have the same saturation and value, while the hues are distributed evenly
   * around the 360&#176; of the color wheel, {@code (360 / numBreeds)}&#176; apart.</p>
   *
   * @param numBreeds Initial number of competing breeds in the ecosystem.
   */
  public void setNumBreeds(int numBreeds) {
    float colorWidth = 360f / numBreeds;
    breedPaints = IntStream.range(0, numBreeds)
        .mapToObj((breed) -> {
          Paint paint = new Paint();
          paint.setColor(
              Color.HSVToColor(new float[]{colorWidth * breed, COLOR_SATURATION, COLOR_VALUE}));
          return paint;
        })
        .toArray(Paint[]::new);
  }

  /**
   * Sets the {@code int[][]} terrain to be rendered, as described in {@link #onDraw(Canvas)}. The
   * value of each element in the terrain is the breed number of the individual occupying that
   * position in the terrain.
   *
   * @param terrain {@code int[][]} to be rendered.
   */
  public void setTerrain(int[][] terrain) {
    this.terrain = terrain;
  }

}
