Animations
===============

The animations folder contains three important files:

* index.html
* main.html
* mainvr.html

Both index and mainvr are focused upon VR animations (I'm not sure if both work) while main is the simple animation.

Notes:
--------
* data.csv (from the Decoder) must be present in the Animations folder.
* This program **must** be deployed as a hosted server. This can be
  done out of WebStorm (which is what this project was built in) or
  by running ``python -m http.server`` in the Animations folder. Then
  visit localhost:8000. Choose the file you want to view.
* Animations **DOES NOT WORK ON CHROME.** Instead, load the server in Firefix.
* This code depends on various external programs that should be loaded
  remotely but may not be, check the Console if it isn't working.
