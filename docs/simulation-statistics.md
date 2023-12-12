---
title: "Simulation statistics"
description: "Displaying key statistics from the simulation"
menu: "Statistics"
order: 20
---

{% include ddc-abbreviations.md %}

## Overview

An animated display of an ongoing simulation can be useful and informative (even fun to watch)---but it can't do everything. In particular, such a display is less than ideal for presenting summary information.

For this task, you must add code to both the [`EcosystemFragment`](api/edu/cnm/deepdive/rps/controller/EcosystemFragment.html) class and the corresponding [`fragment_ecosystem`](fragment-ecosystem.md) layout file, to present 3 key statistical measures:

* The number of iterations executed so far in the simulation.

* The current number of extant (surviving) breeds in the simulation.

* The array of current population counts for all breeds in the simulation---including extinct breeds (for which the population counts are necessarily 0).

All of these statistics are already being computed by the code of the [`Ecosystem`](api/edu/cnm/deepdive/rps/model/domain/Ecosystem.html) model class; they are also available via `LiveData` published by the [`EcosystemViewModel`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html) class. However, they are not (yet) being observed by the UI controller and displayed in the layout.

### Files to add

_(none)_

### Existing files to change

* `fragment_ecosystem.xml`
* `EcosystemFragment.java`

## Add to layout

In order to present the required statistics to the user, the values must be displayed using view widgets in the [`fragment_ecosystem`](fragment-ecosystem.md) layout. Currently, that layout only includes the [`TerrainView`](api/edu/cnm/deepdive/rps/view/TerrainView.html) widget, so you'll have to add widgets of type `TextView` (or subclasses of `TextView`) to display them.

See the [`fragment_ecosystem.xml`](fragment-ecosystem.md) source code (including `TODO` comments with detailed instructions and specifications) [here](fragment-ecosystem.md).

## Observe `LiveData`

In order to present the required statistics to the user, the UI controller (the [`EcosystemFragment`](api/edu/cnm/deepdive/rps/controller/EcosystemFragment.html) class) must get access to the corresponding `LiveData` elements published by the [`EcosystemViewModel`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html) class, and invoke the `LiveData.observe` method to register `Observer` implementations (usually expressed as lambdas) on each. 

This code must be added (directly, or indirectly using helper methods) to the `setupViewModel` method of [`EcosystemFragment`](api/edu/cnm/deepdive/rps/controller/EcosystemFragment.html). Since this method is `private` (it's a helper method invoked by the [`onViewCreated`](api/edu/cnm/deepdive/rps/controller/EcosystemFragment.html#onViewCreated(android.view.View,android.os.Bundle)) method), you won't be able to get any details from it in the usual Javadoc pages; however, since the Javadoc documentation was generated with the `-linksource` option, you can examine the generated [source code listing of the `setupViewModel` method](api/src-html/edu/cnm/deepdive/rps/controller/EcosystemFragment.html#line-107). This source code is extensively commented: in it, you can read comments for the existing code, as well as the [`TODO` comments](api/src-html/edu/cnm/deepdive/rps/controller/EcosystemFragment.html#line-147) for detailed instructions and specifications for the changes you must make. 

