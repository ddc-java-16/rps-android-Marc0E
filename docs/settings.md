---
title: "<code>settings.xml</code>"
description: "Settings definitions for user preferences"
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
<PreferenceScreen
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto">

  <SeekBarPreference
    android:key="@string/num_breeds_key"
    android:title="@string/num_breeds_title"
    android:icon="@drawable/number"
    app:min="@integer/num_breeds_min"
    android:max="@integer/num_breeds_max"
    android:defaultValue="@integer/num_breeds_default"
    app:seekBarIncrement="@integer/num_breeds_increment"
    app:showSeekBarValue="true"
    app:updatesContinuously="false"/>

  <SeekBarPreference
    android:key="@string/terrain_size_key"
    android:title="@string/terrain_size_title"
    android:icon="@drawable/height"
    app:min="@integer/terrain_size_min"
    android:max="@integer/terrain_size_max"
    android:defaultValue="@integer/terrain_size_default"
    app:showSeekBarValue="true"/>

  <SwitchPreference
    android:key="@string/toroidal_topology_key"
    android:title="@string/toroidal_topology_title"
    android:icon="@drawable/torus"
    android:summaryOn="@string/toroidal_topology_summary_on"
    android:summaryOff="@string/toroidal_topology_summary_off"
    android:defaultValue="@bool/toroidal_topology_default"/>

  <SeekBarPreference
    android:key="@string/swap_likelihood_key"
    android:title="@string/swap_likelihood_title"
    android:summary="@string/swap_likelihood_summary"
    android:icon="@drawable/swap"
    app:min="@integer/swap_likelihood_min"
    android:max="@integer/swap_likelihood_max"
    android:defaultValue="@integer/swap_likelihood_default"
    app:showSeekBarValue="true"/>

  <SeekBarPreference
    android:key="@string/run_speed_key"
    android:title="@string/run_speed_title"
    android:icon="@drawable/speed"
    app:min="@integer/run_speed_min"
    android:max="@integer/run_speed_max"
    android:defaultValue="@integer/run_speed_default"
    app:showSeekBarValue="true"/>

</PreferenceScreen>
```