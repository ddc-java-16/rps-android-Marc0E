---
title: "Animation rendering"
description: "Rendering the ecosystem terrain from the running simulation"
menu: "Animation"
order: 10
---

{% include ddc-abbreviations.md %}

## Overview

The terrain of the simulation model in this app forms a square---that is, the width and height are equal, and the term _size_ is used to refer to both. In the [`Ecosystem`](api/edu/cnm/deepdive/rps/model/domain/Ecosystem.html) model class, the terrain is represented as an `int[][]`, where the value of each element is a number denoting the breed (`0` indicates the first breed---e.g. rock---`1` is the second breed, and so on).

While it's not always necessary to have an animated display of a simulation, a well-done animation can increase the visual appeal significantly, and help the user grasp the dynamics in a real, almost tactile way. 

To render an animated display of the RPS terrain in an Android view, you must complete the implementation of the [`onDraw(Canvas)`](api/edu/cnm/deepdive/rps/view/TerrainView.html#onDraw(android.graphics.Canvas)) method of the [`TerrainView`](api/edu/cnm/deepdive/rps/view/TerrainView.html) class.

### Files to add

_(none)_

### Existing files to change

* `TerrainView.java`

## Render

When the `onDraw(Canvas)` method of an Android view is invoked, a `Canvas` instance reference is passed as a parameter value. `Canvas` includes several methods for drawing points, lines, shapes, and text. You will need to review the documentation of the `Canvas` class to decide which method suits your purpose best (there may be multiple methods that will suffice).

Your implementation of [`onDraw(Canvas)`](api/edu/cnm/deepdive/rps/view/TerrainView.html#onDraw(android.graphics.Canvas)) must do the following:

1. Check the `terrain` and `breedPens` fields of the `TerrainView` class for non-`null` values; no rendering should be attempted if either of these fields is `null`.

2. If both `terrain` and `breedPens` are non-`null`:

    1. Use the width and height of the view, along with the size of the `terrain` array, to compute the size of the circles to be drawn for each element.

    2. Iterate over the rows of `terrain`:

        1. Within the current row, iterate over the columns:

            1. Use the value in the current element of `terrain` (i.e., the array element in the current row and column) to identify the element of the `breedPens` array containing the `Paint` instance you'll use to render the current element of `terrain`. (This `Paint` instance is already configured with the desired color for the breed of the given element.)

            2. Use the row index and column index to compute the location on the canvas of the circle to draw.

            3. Draw a circle in the computed location, of the computed size, with the appropriate `Paint`. 

The result should appear something like this example rendering of a terrain or size 30, with individuals of 3 breeds:

![Example `TerrainView` rendering](img/terrain-view.jpg)

For more details, see the [`TODO` comments](api/src-html/edu/cnm/deepdive/rps/view/TerrainView.html#line-115) in the `onDraw` method.

## Stretch goal

As noted in the `TODO` comments, it would be preferable to change the shape used for each element from a circle to a square, when the computed size of each element is below 20. However, since each element is the same size, it would be computationally inefficient to test this condition for every element---i.e., inside your loops. On the other hand, it would not be good coding practice to duplicate the entire rendering loop, to have one version for circles, and another for squares. Thus, for this stretch goal, implement a solution that does not follow either of those approaches; that is, it _should_ check the computed size just once after step 2a (above), but _should not_ then duplicate all the steps included in 2b for each of the two possible shapes.