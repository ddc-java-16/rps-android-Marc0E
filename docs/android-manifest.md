---
title: "<code>AndroidManifest.xml</code>"
description: "Manifest declaring icons, activities, permissions, and features of the app to the Android system"
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
<manifest
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:tools="http://schemas.android.com/tools">

  <!--
      TODO Create a new launcher icon suitable for this app. Then, change the android:icon and 
       android:roundIcon attribute values (below) to refer to the new icon.
  -->

  <application
    android:allowBackup="false"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:name="edu.cnm.deepdive.rps.RpsApplication"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme"
    tools:ignore="AllowBackup,DataExtractionRules">

    <activity
      android:name="edu.cnm.deepdive.rps.controller.MainActivity"
      android:exported="true">

      <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
      </intent-filter>

    </activity>

  </application>


</manifest>
```