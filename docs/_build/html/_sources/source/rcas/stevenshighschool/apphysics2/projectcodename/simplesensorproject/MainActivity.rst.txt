.. java:import:: android Manifest

.. java:import:: android.app AlarmManager

.. java:import:: android.app PendingIntent

.. java:import:: android.content Context

.. java:import:: android.content Intent

.. java:import:: android.content IntentFilter

.. java:import:: android.content.pm PackageManager

.. java:import:: android.graphics SurfaceTexture

.. java:import:: android.hardware Camera

.. java:import:: android.hardware Sensor

.. java:import:: android.hardware SensorEvent

.. java:import:: android.hardware SensorEventListener

.. java:import:: android.hardware SensorManager

.. java:import:: android.hardware.usb UsbDevice

.. java:import:: android.hardware.usb UsbDeviceConnection

.. java:import:: android.hardware.usb UsbManager

.. java:import:: android.media CamcorderProfile

.. java:import:: android.media MediaRecorder

.. java:import:: android.os BatteryManager

.. java:import:: android.os Bundle

.. java:import:: android.os Environment

.. java:import:: android.os Handler

.. java:import:: android.os SystemClock

.. java:import:: android.support.v4.app ActivityCompat

.. java:import:: android.support.v4.content ContextCompat

.. java:import:: android.support.v7.app AppCompatActivity

.. java:import:: android.util Log

.. java:import:: android.view Surface

.. java:import:: android.view View

.. java:import:: android.view WindowManager

.. java:import:: android.widget Button

.. java:import:: android.widget EditText

.. java:import:: android.widget TextView

.. java:import:: android.widget Toast

.. java:import:: com.felhr.usbserial UsbSerialDevice

.. java:import:: eu.chainfire.libsuperuser Shell

.. java:import:: java.io File

.. java:import:: java.io FileOutputStream

.. java:import:: java.io IOException

.. java:import:: java.io ObjectOutputStream

.. java:import:: java.io OutputStream

.. java:import:: java.text SimpleDateFormat

.. java:import:: java.util ArrayList

.. java:import:: java.util Date

MainActivity
============

.. java:package:: rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject
   :noindex:

.. java:type:: @SuppressWarnings public class MainActivity extends AppCompatActivity

   The MainActivity class contains, you guessed it, the main part of the code. This does basically EVERYTHING! It provides a UI, but that isn't really functional anymore (I mean, you're not going up with your high altitude balloon). It's better explained in the chunks of code down below, because this whole thing is sort of an amalgamation of various bits of functionality and strokes of genius (maybe just strokes, idk).

   :author: Alan Zhu, Joshua Morin-Baxter

Fields
------
ACTION_USB_PERMISSION
^^^^^^^^^^^^^^^^^^^^^

.. java:field:: public static final String ACTION_USB_PERMISSION
   :outertype: MainActivity

aLin_x
^^^^^^

.. java:field::  float aLin_x
   :outertype: MainActivity

aLin_y
^^^^^^

.. java:field::  float aLin_y
   :outertype: MainActivity

aLin_z
^^^^^^

.. java:field::  float aLin_z
   :outertype: MainActivity

accelerometer
^^^^^^^^^^^^^

.. java:field::  Sensor accelerometer
   :outertype: MainActivity

accelerometer2
^^^^^^^^^^^^^^

.. java:field::  Sensor accelerometer2
   :outertype: MainActivity

accelerometer2Listener
^^^^^^^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener accelerometer2Listener
   :outertype: MainActivity

accelerometerListener
^^^^^^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener accelerometerListener
   :outertype: MainActivity

actualA_x
^^^^^^^^^

.. java:field::  float actualA_x
   :outertype: MainActivity

actualA_y
^^^^^^^^^

.. java:field::  float actualA_y
   :outertype: MainActivity

actualA_z
^^^^^^^^^

.. java:field::  float actualA_z
   :outertype: MainActivity

arduinoInRecent
^^^^^^^^^^^^^^^

.. java:field::  String arduinoInRecent
   :outertype: MainActivity

captureCallback
^^^^^^^^^^^^^^^

.. java:field::  Camera.PictureCallback captureCallback
   :outertype: MainActivity

ch
^^

.. java:field::  byte ch
   :outertype: MainActivity

connection
^^^^^^^^^^

.. java:field::  UsbDeviceConnection connection
   :outertype: MainActivity

course
^^^^^^

.. java:field::  float course
   :outertype: MainActivity

dataPointArrayList
^^^^^^^^^^^^^^^^^^

.. java:field::  ArrayList<DataPoint> dataPointArrayList
   :outertype: MainActivity

device
^^^^^^

.. java:field::  UsbDevice device
   :outertype: MainActivity

editText
^^^^^^^^

.. java:field::  EditText editText
   :outertype: MainActivity

ext_alt
^^^^^^^

.. java:field::  float ext_alt
   :outertype: MainActivity

ext_altEST
^^^^^^^^^^

.. java:field::  float ext_altEST
   :outertype: MainActivity

ext_lat
^^^^^^^

.. java:field::  float ext_lat
   :outertype: MainActivity

ext_lon
^^^^^^^

.. java:field::  float ext_lon
   :outertype: MainActivity

ext_p
^^^^^

.. java:field::  float ext_p
   :outertype: MainActivity

ext_rh
^^^^^^

.. java:field::  float ext_rh
   :outertype: MainActivity

ext_temp
^^^^^^^^

.. java:field::  float ext_temp
   :outertype: MainActivity

flash
^^^^^

.. java:field::  Camera flash
   :outertype: MainActivity

gps_speed
^^^^^^^^^

.. java:field::  float gps_speed
   :outertype: MainActivity

gravity
^^^^^^^

.. java:field::  Sensor gravity
   :outertype: MainActivity

gravityListener
^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener gravityListener
   :outertype: MainActivity

gravity_x
^^^^^^^^^

.. java:field::  float gravity_x
   :outertype: MainActivity

gravity_y
^^^^^^^^^

.. java:field::  float gravity_y
   :outertype: MainActivity

gravity_z
^^^^^^^^^

.. java:field::  float gravity_z
   :outertype: MainActivity

humidity
^^^^^^^^

.. java:field::  Sensor humidity
   :outertype: MainActivity

humidityListener
^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener humidityListener
   :outertype: MainActivity

iterReading
^^^^^^^^^^^

.. java:field::  int iterReading
   :outertype: MainActivity

mMediaRecorder
^^^^^^^^^^^^^^

.. java:field::  MediaRecorder mMediaRecorder
   :outertype: MainActivity

mVideoCamera
^^^^^^^^^^^^

.. java:field::  Camera mVideoCamera
   :outertype: MainActivity

magnet
^^^^^^

.. java:field::  Sensor magnet
   :outertype: MainActivity

magnetListener
^^^^^^^^^^^^^^

.. java:field::  SensorEventListener magnetListener
   :outertype: MainActivity

magnetic_x
^^^^^^^^^^

.. java:field::  float magnetic_x
   :outertype: MainActivity

magnetic_y
^^^^^^^^^^

.. java:field::  float magnetic_y
   :outertype: MainActivity

magnetic_z
^^^^^^^^^^

.. java:field::  float magnetic_z
   :outertype: MainActivity

mainCamera
^^^^^^^^^^

.. java:field::  Camera mainCamera
   :outertype: MainActivity

nTexture
^^^^^^^^

.. java:field::  int nTexture
   :outertype: MainActivity

pAsInPressure
^^^^^^^^^^^^^

.. java:field::  float pAsInPressure
   :outertype: MainActivity

pressure
^^^^^^^^

.. java:field::  Sensor pressure
   :outertype: MainActivity

pressureListener
^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener pressureListener
   :outertype: MainActivity

rCamera
^^^^^^^

.. java:field::  Runnable rCamera
   :outertype: MainActivity

rSensors
^^^^^^^^

.. java:field::  Runnable rSensors
   :outertype: MainActivity

relativeHumidity
^^^^^^^^^^^^^^^^

.. java:field::  float relativeHumidity
   :outertype: MainActivity

rootTest
^^^^^^^^

.. java:field::  Button rootTest
   :outertype: MainActivity

rot_x
^^^^^

.. java:field::  float rot_x
   :outertype: MainActivity

rot_y
^^^^^

.. java:field::  float rot_y
   :outertype: MainActivity

rot_z
^^^^^

.. java:field::  float rot_z
   :outertype: MainActivity

rotation
^^^^^^^^

.. java:field::  Sensor rotation
   :outertype: MainActivity

rotationListener
^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener rotationListener
   :outertype: MainActivity

runnableManager
^^^^^^^^^^^^^^^

.. java:field:: final Handler runnableManager
   :outertype: MainActivity

sensorFileName
^^^^^^^^^^^^^^

.. java:field::  Date sensorFileName
   :outertype: MainActivity

serialPort
^^^^^^^^^^

.. java:field::  UsbSerialDevice serialPort
   :outertype: MainActivity

startButton
^^^^^^^^^^^

.. java:field::  Button startButton
   :outertype: MainActivity

stoppedN
^^^^^^^^

.. java:field::  int stoppedN
   :outertype: MainActivity

tAsInTemperature
^^^^^^^^^^^^^^^^

.. java:field::  float tAsInTemperature
   :outertype: MainActivity

tMinusBackup
^^^^^^^^^^^^

.. java:field:: public long tMinusBackup
   :outertype: MainActivity

temperature
^^^^^^^^^^^

.. java:field::  Sensor temperature
   :outertype: MainActivity

temperatureListener
^^^^^^^^^^^^^^^^^^^

.. java:field::  SensorEventListener temperatureListener
   :outertype: MainActivity

textView
^^^^^^^^

.. java:field::  TextView textView
   :outertype: MainActivity

usbManager
^^^^^^^^^^

.. java:field::  UsbManager usbManager
   :outertype: MainActivity

useThisOne
^^^^^^^^^^

.. java:field::  SurfaceTexture useThisOne
   :outertype: MainActivity

Methods
-------
flashLightOff
^^^^^^^^^^^^^

.. java:method:: public void flashLightOff(View view)
   :outertype: MainActivity

   This one turns the flashlight off if the flashlight is on. It's not called because \ :java:ref:`flashLightOn(View)`\  doesn't work either and isn't called.

   :param view: Just in case you wanted to open call this method with a UI button, but passing in null is fine.

flashLightOn
^^^^^^^^^^^^

.. java:method:: public void flashLightOn(View view)
   :outertype: MainActivity

   A piece of code that wrestles control of the camera from the main photo-taking thread before turning on the camera flashlight on the back, in order to make the payload easier to find at night. It is not used primarily because it's so difficult to implement interlacing between the main camera and this flashlight camera, but hopefully it can be fixed at some point.

   :param view: Just in case you wanted to open call this method with a UI button, but passing in null is fine.

getBatteryPercentage
^^^^^^^^^^^^^^^^^^^^

.. java:method:: public static int getBatteryPercentage(Context context)
   :outertype: MainActivity

   Gets the current battery to be used for \ :java:ref:`rCamera`\  and \ :java:ref:`rSensors`\  in determining battery saving and to be saved for data analysis

   :param context: the context of the application from which battery stats can be pulled
   :return: the current percentage of the battery

getBatteryTemp
^^^^^^^^^^^^^^

.. java:method:: public static int getBatteryTemp(Context context)
   :outertype: MainActivity

   Gets battery temperature to determine delay of \ :java:ref:`rCamera`\  and \ :java:ref:`rSensors`\  and to be recorded for data analysis

   :param context: the context of the application from which battery stats are to be pulled
   :return: battery temperature (deciCelsius?)

giveCameraBackToOtherThread
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void giveCameraBackToOtherThread()
   :outertype: MainActivity

   In life, sometimes you've got to return things you've taken forcefully. Control of the camera is no exception. This is like the yin to the \ :java:ref:`takeCameraFromOtherThread()`\ 's yang. It makes the main camera active again and starts the rCamera runnable again.

hackInt
^^^^^^^

.. java:method:: public int hackInt()
   :outertype: MainActivity

   At some point, this was used in attempt to solve a problem with the camera of the Samsung Galaxy S4 in order to create SurfaceTextures from within a Runnable. It's still used but not in the way it was originally intended.

   :return: an id to be used to initialize SurfaceTextures from within a runnable

onClickClear
^^^^^^^^^^^^

.. java:method:: public void onClickClear(View view)
   :outertype: MainActivity

   Clears both the text of the incoming messages and outgoing messages for Arduino connection, meaning that it is no longer used but can be revived along with everything else.

   :param view: can be called with null, used to support button clicks

onCreate
^^^^^^^^

.. java:method:: @Override protected void onCreate(Bundle savedInstanceState)
   :outertype: MainActivity

   Creates the activity and all of its parts. Better to read it than write a description.

   :param savedInstanceState: past to the super function, maintaining the saved instance state

onStart
^^^^^^^

.. java:method:: @Override protected void onStart()
   :outertype: MainActivity

   Initializes sensors and other things that should not be done in onCreate

onStop
^^^^^^

.. java:method:: @Override protected void onStop()
   :outertype: MainActivity

   de-initializes sensors and things that should be destroyed before onDestroyed

record
^^^^^^

.. java:method:: @SuppressWarnings public void record(View view)
   :outertype: MainActivity

   Starts recording data and taking photos at the start of the activity and in testing with the UI. It changes rates depending on heat to avoid overheating the phone at altitude. Some of this is better to read than to describe.

   :param view: Used to allow for UI interaction to start the recording

recordButton
^^^^^^^^^^^^

.. java:method:: public void recordButton()
   :outertype: MainActivity

   starts a recording - but isn't called at all right now

rootTest
^^^^^^^^

.. java:method:: public boolean rootTest()
   :outertype: MainActivity

   Tests if the phone is rooted, although that is unimportant to the app as of now. This could be used to further exploit the abilities of the phone being used.

   :return: True if root is available on the phone, False if not

setUiEnabled
^^^^^^^^^^^^

.. java:method:: public void setUiEnabled(boolean bool)
   :outertype: MainActivity

   Code that enables/disables the Arduino connection UI. It is no longer used by the app but should be revived with the Arduino code when the time comes.

   :param bool: state of the variables - if connected, it should be true

stopRecord
^^^^^^^^^^

.. java:method:: public void stopRecord(View view)
   :outertype: MainActivity

   Stops all recording of sensors and photos.

   :param view: to be used from UI

takeCameraFromOtherThread
^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void takeCameraFromOtherThread()
   :outertype: MainActivity

   This is the code that does the actual wrestling for \ :java:ref:`flashLightOn(View)`\ . It kills the camera and opens it up to domination by another thread.

