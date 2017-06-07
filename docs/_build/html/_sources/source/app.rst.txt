Android App
=================

The CODENAME app was build within Android Studio. The
folder "Project CODENAME" should be opened with Android
Studio (the app was built with 2.2-2.3, the Gradle plugin
may have to be updated depending on which version of Studio
you are using). To deploy the app, simply run the code from Android
Studio. For our launch, we used adb over tcpip on a mobile hotspot
to deploy our app and run our final checks while the phone was
sealed inside the payload.

Notes:
-------
* The code was specifically designed for a Samsung Galaxy S4. As
  the devices used diversify, we will fix bugs to solve problems
  for each case.
* The status of the phone can be monitored over adb. If the app
  is generating logs that say "RUN!" or "PHOTO!", then the app is
  running.
* The phone itself does not record any GPS data as phone GPSs fail
  over certain heights. Instead, use the Tracksoar for reliable data.
  Various data recorded are thus -1 as they were from previous versions
  of the code.
* The app is incredibly persistent, in fact, the only case we found where
  the app died was due to overheat. As such, do **not** install this app
  on your personal phone as it will prevent you from doing anything except
  take data with it. Be careful.
* Data and photos is saved within the public app data folder on the SD Card.
  Ensure that the app has the permissions necessary if you are running
  above 6.0.
