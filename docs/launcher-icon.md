---
title: "Launcher icon"
description: "Creating a custom launcher icon for the application"
menu: "Icon"
order: 40
---

{% include ddc-abbreviations.md %}

## Overview

While many simple assignments in this bootcamp don't require you to create a custom launcher icon for an Android app, no such app is truly complete without one.

For this task, you must create a custom launcher icon, suitable for the purpose. Minimally, it should convey the concept of an ecosystem (difficult to do), convey the concept of Rock-Paper-Scissors (easier, but still challenging), or simply present a scaled-down "chunk" of the animation display (e.g., a 3 X 3 or 4 X 4 terrain with colored circles, as seen below).

### Files to add

* Launcher icon assets (foreground layer image, generated mipmap resources)

### Existing files to change

* `AndroidManifest.xml`

## Create the assets
{: menu="Create"}

Creating these and adding them to the project usually comprises at least 3 main steps:

1. Find or create an SVG file with the content you want to use for the icon foreground, e.g.,

    ![rps-icon.svg](img/rps-icon.svg){: style="width: 25%; min-width="5em;"}

    (Optionally, you can create another SVG for the background; you can also simply specify a solid color for the background in step 3.)

2. With the `app/res` or `app/res/drawable` directory selected in the **Android** view of the **Project** tool window (or the `app/src/main/res` directory in the **Project** view of the **Project** tool window), use the **New/Vector Asset** menu command to import the SVG and convert it to an XML drawable. (Remember that this drawable resource must use `lower_snake_case` casing for the base filename---e.g., `rps_icon.xml`.)

    (If you created or found another SVG for the background layer, import it as well.)

3. With the `app/res` or `app/res/mipmap` directory selected in the **Android** view of the **Project** tool window (or the `app/src/main/res` directory in the **Project** view of the **Project** tool window), use the **New/Image Asset** menu command create a mipmap resource for your launcher icon:

    1. Specify a name for the launcher icon. The default is usually `ic_launcher`, but it may be less confusing to give it a name related to the project---e.g., `ic_rps_launcher`.

    2. Specify the XML drawable imported in step 2 as the foreground layer. (Make sure to use the **Trim** and **Resize** controls to fit all---or almost all---of your foreground image inside the safe zone outline).
   
    3. For the background layer, specify a solid color (or, optionally, an XML drawable).

    4. In the **Options** tab, select **Yes** for the **Legacy Icon**, **Round Icon**, and **Google Play Store Icon** options, select "Square" in the **Shape** selector for the **Legacy Icon**, and **WebP** for the **Icon Format** option.

    5. Click the **Finish** button to create the vector and mipmap resources for the launcher icon.

## Configure the app to use the new icon
{: menu="Configure"}

Unless it has the same name as the previously configured (default) launcher icon, you will need to modify 2 attributes of the `application` element in the `AndroidManifest.xml` file to get Android to use your new launcher icon. See the [`TODO` comments in the `AndroidManifest.xml` file](android-manifest.md) for more details.