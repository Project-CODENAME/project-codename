Decoder
========
The decoder deserializes the data file into a .csv and a .kml file.
The .csv file is used throughout the rest of the code and is
extremely useful for raw data analysis. On the other hand, the .kml
file is deprecated and no longer contains any useful information
because the phone no longer records GPS data. If it becomes configurable
to allow for the phone to record GPS, then the kml file contains the
appropriate data.

Notes:
-------
* This code was written for Java 1.8.0 using IntelliJ IDEA Professional.
  To edit, open the folder ``Decoder\rcas\stevenshighschool\apphysics2\projectcodename\simplesensorproject`` as a project in IntelliJ.
  To compile the code, run the "Build Project" action in IntelliJ.
* The DataPoint class should be identical to the class in the Android
  source code. Any edits to one should be copied to the other.
* To run the code, run java from the Decoder/production/simplesensor folder.
  The Decoder requires jdom (for the .kml) which is included, the jar should
  be added to the cp in the command. An example command (for Windows) is:

    ``java -cp .;jdom2-2.0.3.jar rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject.Main DATA.txt``
