---
title: "<code>fragment_ecosystem.xml</code>"
description: "Layout resource for views controlled by <code>EcosystemFragment</code>"
---

```xml
<?xml version="1.0" encoding="utf-8"?>
<!--
  Copyright 2023 CNM Ingenuity, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<androidx.constraintlayout.widget.ConstraintLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:padding="@dimen/half_static_spacing"
  tools:context=".controller.EcosystemFragment">

  <edu.cnm.deepdive.rps.view.TerrainView
    android:id="@+id/terrain"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_margin="@dimen/half_static_spacing"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>

  <!--
      TODO Add a non-editable view (e.g. TextView, TextInputEditText w/ editable=false) to display
       the number of iterations executed (so far) in the ecosystem simulation. This view must be
       labeled (e.g., using another TextView or a TextInputLayout), so the user knows what they
       are seeing.
  -->

  <!--
      TODO Add a non-editable view (e.g. TextView, TextInputEditText w/ editable=false) to display
       the number of extant (surviving) breeds remaining in the ecosystem simulation. This view
       must be labeled (e.g., using another TextView or a TextInputLayout), so the user knows what
       they are seeing.
  -->

  <!--
      TODO Add a non-editable view (e.g. TextView, TextInputEditText w/ editable=false) to display
       the population counts of all breeds. This view must be labeled (e.g., using another
       TextView or a TextInputLayout), so the user knows what they are seeing. The population
       counts of all breeds should be displayed very simply, in a single text widget, with one
       value in each line.
  -->

  <!--
      IMPORTANT: For full credit, please do not include any hard-coded strings (e.g., for labels) or
      dimensions (sizing, margins, padding, etc.); these should be referenced from the appropriate
      resources. Also, pay close attention to alignment, spacing, margins, and padding, so that the
      additional widgets (including labels) are clearly legible, separated, and visually balanced.
  -->

  <!--
      TODO STRETCH GOAL: After completing the above steps, create a landscape variation of this
       layout (using the design view). In the landscape variation of the layout:
       - The TerrainView must be on the start (typically, the left-hand) side of the layout, and
         must be constrained to the start, top, and bottom of the layout, with the layout_height
         set to 0dp (i.e., stretched to fill the layout from top to bottom by the constraints),
         and the layout_width set to wrap_content (the existing code in the onMeasure(int, int)
         method of the TerrainView will take care of computing how much room will be needed to
         wrap the content).
       - The text widgets for displaying (and labeling) the number of iterations and the
         population counts must occupy the end (right-hand) side of the layout, but in the same
         position relative to each other that they are in the portrait mode layout.
       ===
       IMPORTANT: If, after creating the landscape variation, you add view objects of any type,
       with android:id attributes, to either of the two layout variations, YOU MUST ADD THEM TO
       BOTH. Similarly, if you remove a view widget (or change its ID) from one of the layout
       variations, you must make a corresponding change to the other variation(s).
  -->

</androidx.constraintlayout.widget.ConstraintLayout>
```