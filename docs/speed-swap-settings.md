---
title: "Additional settings"
description: "Incorporating all user settings in simulation execution"
menu: "Settings"
order: 30
---

{% include ddc-abbreviations.md %}

## Overview

The application presents a settings screen to the user, available under the overflow menu. 3 of the settings---controlling initial number of breeds, terrain size, and terrain topology---are already incorporated in the simulation code, when a new ecosystem is created. 

However, there are two user-controllable settings that are currently being ignored by the app: simulation run speed and swap probability. In this task, you must add code to the [`EcosystemViewModel`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html) class to consume these settings when starting or resuming simulation execution.

### Files to add

_(none)_

### Existing files to change

* `EcosystemViewModel.java`

## Fields

When reading settings from a `SharedPreferences` instance (which is where settings are automatically stored after editing them via a `PreferenceFragment` subclass instance, such as this app's [`SettingsFragment`](api/edu/cnm/deepdive/rps/controller/SettingsFragment.html)), we need to provide a `String` key, along with a default value (of the appropriate type for the setting) to use in the event that a value for the given setting has not yet been set by the user. The best place to store the key and default value is in the value resources of the app; in fact, the necessary resources are already included (along with resources specifying the minimum and maximum values) in this app: you can see these resource references in the attribute values of the [`res/xml/settings.xml`]() file. 

In the [`EcosystemViewModel`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html) class, there are already key and default value fields declared for the user settings that are currently being used; you must add 4 more fields: 2 for the simulation speed key and default value, and 2 more for the swap probability key and default value. See the [`TODO` comments in the field declarations](api/src-html/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#line-71) for detailed specifications and instructions.

## Initialization

The fields created in the previous step must be declared `final` (according to the instructions). Thus, the fields must be assigned values during initialization---e.g., in the constructor. See the [`TODO` comments in the constructor](api/src-html/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#line-79) for detailed specifications and instructions.

## Use in simulation execution

The settings already being used in the code are needed when the ecosystem is created; thus, they must be read in the [`create()`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#create()) method. The settings that you must now use are needed when model execution is started or resumed; thus, they must be read in the [`run()`](api/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#run()) method. See the [`TODO` comments in the source code of the `run()` method](api/src-html/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#line-134) for detailed specifications and instructions.

**Important**: As detailed in the `TODO` comments, neither the run speed nor swap probability setting should be used as-is; instead, both must be used in computations, to produce the values passed in the [invocation of the `EcosystemRepository.run` method](api/src-html/edu/cnm/deepdive/rps/viewmodel/EcosystemViewModel.html#line-181). The computations involved are simple, as long as you pay close attention to the instructions.