# Live Wallpaper Template

Use this template to help create live wallpapers for Android.
Note this for live wallpapers that use the `Canvas` and not OpenGL.

Using the canvas limits your wallpaper to about 30 FPS

## Files you need to edit

* `ExampleWallpaperDrawer`
This is the class that draws the wallpaper

* `ExampleWallpaperService`
This class provides the name of the drawer class

* `SettingsActivity` and `activity_settings.xml`
This activity allows the user to change settings for the wallpaper

* `wallpaper.xml`
This xml file is read by other apps to provide the user access to the live wallpaper

* `wallpaper_thumbnail.png`
This is preview of the wallpaper used by wallpaper listing apps

* `release.jks`, `release.properties`, `strings.xml`, `ic_launcher*`, `build.gradle`, and the package name
These are the usual Android files and should be updated to match your app

## Template files

The files in the `template` package probably don't need to be edited and are mostly used to provided the drawer to the settings activity and the launcher. 
They have javadoc explaining their purpose in more detail in each file. 

## License

```
Copyright 2018 Ray Britton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
