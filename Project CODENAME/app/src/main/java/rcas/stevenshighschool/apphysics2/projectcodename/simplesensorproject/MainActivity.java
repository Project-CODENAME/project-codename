package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;

import eu.chainfire.libsuperuser.Shell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import java.util.List;

/**
 * The MainActivity class contains, you guessed it, the main part of the code.
 * This does basically EVERYTHING! It provides a UI, but that isn't really functional
 * anymore (I mean, you're not going up with your high altitude balloon). It's better
 * explained in the chunks of code down below, because this whole thing is sort of an
 * amalgamation of various bits of functionality and strokes of genius (maybe just strokes, idk).
 *
 * @author Alan Zhu
 * @author Joshua Morin-Baxter
 * @version honestly who knows?
 * @since Fall 2016
 */
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    //the manager of the sensors (isn't that kinda obvious?)
    private SensorManager sensorManager;

    //loops for the phone to run and their manager
    final Handler runnableManager = new Handler();
    Runnable rSensors;
    Runnable rCamera;

    //initialize all the data values
    float tAsInTemperature;
    float aLin_x;
    float aLin_y;
    float aLin_z;
    float pAsInPressure;
    float magnetic_x;
    float magnetic_y;
    float magnetic_z;
    float relativeHumidity;
    float rot_x;
    float rot_y;
    float rot_z;
    float gravity_x;
    float gravity_y;
    float gravity_z;
    float actualA_x;
    float actualA_y;
    float actualA_z;

    //These ones are disabled by default
    float ext_lat = -1;
    float ext_lon = -1;
    float ext_alt = -1;
    float ext_p = -1;
    float ext_temp = -1;
    float ext_altEST = -1;
    float ext_rh = -1;
    float course = -1;
    float gps_speed = -1;

    // a useful variable no longer used
    int stoppedN = 0;

    //Sensors and their listeners
    Sensor accelerometer;
    Sensor accelerometer2;
    Sensor pressure;
    Sensor magnet;
    Sensor humidity;
    Sensor rotation;
    Sensor gravity;
    Sensor temperature;
    SensorEventListener accelerometerListener;
    SensorEventListener pressureListener;
    SensorEventListener magnetListener;
    SensorEventListener humidityListener;
    SensorEventListener rotationListener;
    SensorEventListener gravityListener;
    SensorEventListener temperatureListener;
    SensorEventListener accelerometer2Listener;


    // Root variables, probably not really used anymore either
    Button rootTest, reboot, sysui;

    // Arduino connection variables, from a more hopeful time
    UsbDevice device;
    public static final String ACTION_USB_PERMISSION = "rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject.USB_PERMISSION";
    Button startButton, sendButton, clearButton, stopButton;
    TextView textView;
    EditText editText;
    UsbManager usbManager;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    byte ch, buffer[] = new byte[1024];
    int iterReading = 0;
    String arduinoInRecent;

    // All of the cameras that could be used (one is!)
    Camera mVideoCamera;
    Camera flash;
    Camera mainCamera;
    MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private boolean cameraTaken = false;

    Date sensorFileName;

    //Array of data points that will be put into the file
    ArrayList<DataPoint> dataPointArrayList;

    // Tag for Log.d
    private final String TAG = "SENSORS v0: ";

    /**
     * A piece of code that wrestles control of the camera from the main photo-taking thread
     * before turning on the camera flashlight on the back, in order to make the payload easier
     * to find at night. It is not used primarily because it's so difficult to implement
     * interlacing between the main camera and this flashlight camera, but hopefully it can be fixed
     * at some point.
     *
     * @param view Just in case you wanted to open call this method with a UI button, but
     *             passing in null is fine.
     */
    public void flashLightOn(View view) {
        Log.d(TAG, "flashLightOn");

        // if the camera's already taken, then we can't do anything about it
        if (cameraTaken) {
            return;
        }

        // everything is in a try block because it will inevitably break
        // TODO: 5/18/2017 maybe we should try doing something in the finally block to solve interlacing problems
        try {
            // this if statement is just in case your phone is terrible and doesn't even have a camera flash
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                //wrestles control
                takeCameraFromOtherThread();

                // finds the back camera and takes it (don't mind the weird logging)
                int cameraId = 0;
                int numberOfCameras = Camera.getNumberOfCameras();
                Log.d("Hey", "" + numberOfCameras);
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        cameraId = i;
                        break;
                    }
                }
                flash = Camera.open(cameraId);

                // sets the camera up to do the flash thing
                Camera.Parameters p = flash.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                flash.setParameters(p);

                // you'll be blinded if you're not careful
                flash.startPreview();

                // don't let anyone else take it
                cameraTaken = true;
            }
        } catch (Exception e) {
            // well something went wrong now debug it
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOn()",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is the code that does the actual wrestling for {@link #flashLightOn(View)}.
     * It kills the camera and opens it up to domination by another thread.
     */
    public void takeCameraFromOtherThread() {
        if (mainCamera != null) {
            runnableManager.removeCallbacks(rCamera);
            mainCamera.stopPreview();
            mainCamera.release();
            mainCamera = null;
            Log.d(TAG, "cameraCLOSED");
            cameraTaken = false;
        }
    }

    /**
     * In life, sometimes you've got to return things you've taken forcefully. Control of the camera
     * is no exception. This is like the yin to the {@link #takeCameraFromOtherThread()}'s yang. It
     * makes the main camera active again and starts the rCamera runnable again.
     */
    public void giveCameraBackToOtherThread() {
        // if the other camera is dead, we can safely start the old thread back up
        if (flash == null && !cameraTaken) {
            // open the camera but very carefully
            mainCamera = openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            cameraTaken = true;
            if (mainCamera == null) {
                // if it doesn't work, try, try again
                runnableManager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "no Camera for now");
                        giveCameraBackToOtherThread();
                    }
                }, 10000);
            }
            // this code used to act weird on a Samsung Galaxy S4, so this was part of the solution. Figures.
            useThisOne = new SurfaceTexture(hackInt());
            try {
                mainCamera.setPreviewTexture(useThisOne);
            } catch (IOException e) {
                // well...
                e.printStackTrace();
            }
        }
        // and then we start running the thread
        runnableManager.post(rCamera);
    }

    /**
     * This one turns the flashlight off if the flashlight is on. It's not called because
     * {@link #flashLightOn(View)} doesn't work either and isn't called.
     *
     * @param view Just in case you wanted to open call this method with a UI button, but
     *             passing in null is fine.
     */
    public void flashLightOff(View view) {
        Log.d(TAG, "flashLightOff");
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH) && flash != null) {
                //kill our camera
                flash.stopPreview();
                flash.release();
                flash = null;
                Log.d(TAG, "cameraCLOSED");
                cameraTaken = false;

                //give it back
                giveCameraBackToOtherThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOff",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Arduino Connection Code - kept to be revived at some point possibly
    //TODO variables taken from TrackSoar be written to ext_p, ext_t, lat, long, and alt

    /*UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            Log.d(TAG, "receive");
            ByteArrayInputStream mIn = new ByteArrayInputStream(arg0);
            try {
                while((ch=(byte)mIn.read())!=-1){
                    if (ch != '#') {
                        buffer[iterReading++] = ch;
                    } else {
                        buffer[iterReading] = '\0';
                        arduinoInRecent = new String(buffer);
                        buffer = new byte[1024];
                        iterReading = 0;
                        processMessage();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


       }
    };

    private void processMessage() {
        if(arduinoInRecent != null) {
            String[] parts = arduinoInRecent.split("-");
            int i=0;
            while(parts[i].equals("")){
                i++;
            }
            ext_lat = Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_lon=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_alt=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_p=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_temp=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_altEST=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            ext_rh = Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            course=Float.parseFloat(parts[i]);
            i++;
            while(parts[i].equals("")){
                i++;
            }
            gps_speed=Float.parseFloat(parts[i]);
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                Log.d(TAG, "TEST!");
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            setUiEnabled(true);
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            tvAppend(textView, "Serial Connection Opened!\n");
                            serialPort.write("hi".getBytes());
                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                onClickStart(startButton);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                onClickStop(stopButton);

            }
        }
    }; */


    /**
     * Code that enables/disables the Arduino connection UI. It is no longer used by the app but
     * should be revived with the Arduino code when the time comes.
     *
     * @param bool state of the variables - if connected, it should be true
     */
    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);
    }

    /*
    public void onClickStart(View view) {
        Log.d(TAG, "clickstart1");
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            Log.d(TAG, "clickstart2");
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                Log.d(TAG, deviceVID+"");
                if (deviceVID == 9025)//Arduino Vendor ID
                {

                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }


    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }

    public void onClickStop(View view) {
        setUiEnabled(false);
        serialPort.close();
        tvAppend(textView, "\nSerial Connection Closed! \n");

    } */

    /**
     * Clears both the text of the incoming messages and outgoing messages for Arduino connection,
     * meaning that it is no longer used but can be revived along with everything else.
     *
     * @param view can be called with null, used to support button clicks
     */
    public void onClickClear(View view) {
        textView.setText(" ");
        editText.setText(" ");
    }

    /**
     * adds text to a TextView, used to add incoming messages from the defunct Arduino connection
     * code to the textview displaying those messages. It is defunct currently.
     *
     * @param tv TextView to add text to
     * @param text text to add to the TextView
     */
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ftext != null) {
                    ftv.append(ftext);
                }
            }
        });
    }


    /**
     * ------------------------------------- END ARDUINO CONNECTION CODE
     * -------------------------------------
     */

    public boolean rootTest() {
        boolean suAvailable = Shell.SU.available();
        if (suAvailable) {
            Toast.makeText(getApplicationContext(), "Your Phone Is Rooted, Begin The Asian Invasion", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "The Asian Invasion Cannot Begin, Your Phone Is Not Rooted", Toast.LENGTH_SHORT).show();
        }
        return suAvailable;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CREATE");
        //starts things
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PersistenceService.class);
        startService(intent);

        // Arduino Connection Stuff

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //registerReceiver(broadcastReceiver, filter);


        // Root Actions
        rootTest = (Button) findViewById(R.id.rootTest);

        rootTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootTest();
            }
        });

        //gets sensors and checks for permissions
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    250);

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    251);

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    23);

        }

        //Initializes sensors and their listeners
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometer2 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // TODO incorporate low-power mode for payload in emergency low-power situation


        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes a to most recent value
                aLin_x = sensorEvent.values[0];
                aLin_y = sensorEvent.values[1];
                aLin_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        accelerometer2Listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes a to most recent value
                actualA_x = sensorEvent.values[0];
                actualA_y = sensorEvent.values[1];
                actualA_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        pressureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes pAsInPressure to most recent value
                pAsInPressure = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        magnetListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes m to most recent value
                magnetic_x = sensorEvent.values[0];
                magnetic_y = sensorEvent.values[1];
                magnetic_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };
        humidityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes relativeHumidity to most recent value
                relativeHumidity = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };
        rotationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes rot to most recent value
                rot_x = sensorEvent.values[0];
                rot_y = sensorEvent.values[1];
                rot_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };
        gravityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes g to most recent value
                gravity_x = sensorEvent.values[0];
                gravity_y = sensorEvent.values[1];
                gravity_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };
        temperatureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes tAsInTemperature to most recent value
                tAsInTemperature = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };


        //Initializes Array
        dataPointArrayList = new ArrayList<DataPoint>();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        Intent intent2 = new Intent(this, PersistenceService.class);
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60 * 1000 * 5,
                60 * 1000 * 5, alarmIntent);
    }

    Camera.PictureCallback captureCallback;
    SurfaceTexture useThisOne;

    int nTexture = 0;

    public int hackInt() {
        nTexture++;
        if (nTexture > 100) {
            nTexture = 0;
        }
        return nTexture;
    }

    /**
     * Recording function that starts running things
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public long tMinusBackup = 60 * 20;

    public void record(View view) {
        final int delay = 1000 * 20; //milliseconds
        final int delayHOT = 1000 * 60;
        final int delayREALLYHOT = 1000 * 60 * 3;
        final int delayCameraCold = 1000 * 30;
        final int delayCamera = 1000 * 60; //milliseconds
        final int delayCameraWarm = 1000 * 60 * 2;
        final int delayCameraHOT = 1000 * 60 * 5;
        final int delayCameraREALLYHOT = 1000 * 60 * 10;

        File[] aDirArray = ContextCompat.getExternalFilesDirs(this, null);
        File aExtDcimDir = new File(aDirArray[1], Environment.DIRECTORY_DCIM);
        File aExtDocsDir = new File(aDirArray[1], Environment.DIRECTORY_DOCUMENTS);

        final String docsPath = aExtDocsDir.getAbsolutePath() + "/High Altitude DATA";
        new File(docsPath).mkdirs();
        //Initializes and starts Runnable
        final Context context = this;
        rSensors = new Runnable() {
            public void run() {
                Log.d(TAG, "RUN!");

                // Initializes the data point class
                // TODO decide on preferred order order of variables - not hugely important but deserves some consideration
                DataPoint point = new DataPoint(tAsInTemperature, gravity_x, gravity_y, gravity_z, rot_x, rot_y, rot_z, relativeHumidity, magnetic_x, magnetic_y, magnetic_z, aLin_x, aLin_y, aLin_z, pAsInPressure, new Date());

                point.ext_alt = ext_alt;
                point.ext_lon = ext_lon;
                point.ext_lat = ext_lat;
                point.ext_altEST = ext_altEST;
                point.ext_temp = ext_temp;
                point.ext_p = ext_p;
                point.ext_rh = ext_rh;
                point.course = course;
                point.gps_speed = gps_speed;
                point.a_actual_x = actualA_x;
                point.a_actual_y = actualA_y;
                point.a_actual_z = actualA_z;
                point.battery_percent = getBatteryPercentage(context);
                point.battery_temp = getBatteryTemp(context);
                Log.d(TAG, "" + point.battery_temp);
                /**if (Math.abs(Math.sqrt(Math.pow(actualA_x, 2) + Math.pow(actualA_y, 2) + Math.pow(actualA_z, 2)) - 9.81) < 0.4) {
                    Log.d("HEY!", "stopped: " + stoppedN);
                    stoppedN++;
                } else {
                    stoppedN = 0;
                }*/

                /**if(stoppedN > 1000){
                 flashLightOn(null);
                 } else if(flash != null) {
                 flashLightOff(null);
                 }*/

                dataPointArrayList.add(point);

                // Object is serialized here, and the datafile is saved to the documents folder */
                SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
                String strDate = sdfDate.format(sensorFileName);
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(path, "SENSORDATA" + strDate + ".txt");
                File file2 = new File(docsPath, "SENSORDATA" + strDate + ".txt");
                try {
                    //noinspection ResultOfMethodCallIgnored
                    path.mkdirs();
                    OutputStream os = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(os);
                    out.writeObject(dataPointArrayList);
                    OutputStream os2 = new FileOutputStream(file2);
                    ObjectOutputStream out2 = new ObjectOutputStream(os2);
                    out2.writeObject(dataPointArrayList);
                    out.close();
                    out2.close();
                    os.close();
                    os2.close();
                    tMinusBackup--;
                    if (tMinusBackup == 0) {
                        OutputStream os1 = new FileOutputStream(new File(docsPath, "SENSORBACKUP.txt"));
                        ObjectOutputStream out1 = new ObjectOutputStream(os1);
                        out1.writeObject(dataPointArrayList);
                        out1.close();
                        os1.close();
                        tMinusBackup = 60 * 20;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //schedules the next job
                if(getBatteryTemp(context) > 250){
                    runnableManager.postDelayed(this, delayREALLYHOT);
                } else if (getBatteryTemp(context) > 200) {
                    runnableManager.postDelayed(this, delayHOT);
                } else {
                    runnableManager.postDelayed(this, delay);
                }
            }
        };

        final String path = aExtDcimDir.getAbsolutePath() + "/High Altitude Photos";
        new File(path).mkdirs();
        captureCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                mainCamera.stopPreview();
                FileOutputStream outStream;
                Log.d(TAG, "picDONE");
                try {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    File output = new File(path, strDate + ".jpg");
                    outStream = new FileOutputStream(output);
                    outStream.write(data);
                    outStream.close();
                    Log.d(TAG, "saveINT");
                    String removableStoragePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath();
                    File fileList[] = new File("/storage/").listFiles();
                    for (File file : fileList) {
                        if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file.isDirectory() && file.canRead() && file.getAbsolutePath().length() == 18)
                            removableStoragePath = file.getAbsolutePath();
                    }
                    String finalPath2 = removableStoragePath + "/High Altitude Photos/Back";// set your directory path here
                    final File file = new File(finalPath2);
                    //noinspection ResultOfMethodCallIgnored
                    Log.d(TAG, file.mkdirs() + "");
                    finalPath2 += new Date() + ".jpg";
                    //outStream = new FileOutputStream(finalPath2);
                    //outStream.write(data);
                    //outStream.close();
                    //Log.d(TAG,"suSTART " + finalPath);
                    //Shell.SU.run("cp \""+finalPath+"\" \""+removableStoragePath+"/High Altitude Photos/Back/"+new Date() + ".jpg\"");
                    //Log.d(TAG,"suEND");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainCamera.startPreview();
            }
        };
        rCamera = new Runnable() {
            @Override
            public void run() {
                // start the camera
                mainCamera.startPreview();
                if (getBatteryTemp(context) > 250) {
                    runnableManager.postDelayed(rCamera, delayCameraREALLYHOT);
                } else if (getBatteryTemp(context) > 200) {
                    runnableManager.postDelayed(rCamera, delayCameraHOT);
                } else if (getBatteryTemp(context) > 150) {
                    runnableManager.postDelayed(rCamera, delayCameraWarm);
                } else if (getBatteryTemp(context) < 0) {
                    runnableManager.postDelayed(rCamera, delayCameraCold);
                } else {
                    runnableManager.postDelayed(rCamera, delayCamera);
                }
                mainCamera.takePicture(null, null, captureCallback);
                Log.d(TAG, "PHOTO!");
               /* Camera camera2 = openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                SurfaceView surface2 = new SurfaceView(getBaseContext());
                try {
                    camera2.setPreviewTexture(new SurfaceTexture(0));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                camera2.startPreview();
                final String path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/High Altitude Photos/Front/";
                final File file2 = new File(path);
                file2.mkdirs();
                Camera.PictureCallback jpegCallback2 = new Camera.PictureCallback() {
                    public void onPictureTaken(byte[] data, Camera camera) {
                        FileOutputStream outStream = null;
                        try {
                            String finalPath = path2 + new Date() + ".jpg";// set your directory path here
                            outStream = new FileOutputStream(finalPath);
                            outStream.write(data);
                            outStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            camera.stopPreview();
                            camera.release();
                            camera = null;
                        }
                    }
                };
                camera2.takePicture(null, null, jpegCallback2);*/
            }
        };
        //schedules the first job
        runnableManager.postDelayed(rSensors, delay);
        giveCameraBackToOtherThread();
        //flashLightOn(null);
        //Runnable once = new Runnable() {
        //  @Override
        //    public void run() {
        //        flashLightOff(null);
        //    }
        //};
        //runnableManager.postDelayed(once, 1000 * 10);
    }

    public void stopRecord(View view) {
        runnableManager.removeCallbacks(rSensors);
        runnableManager.removeCallbacks(rCamera);
    }

    /**
     * public void batterysave() { runnableManager.removeCallbacks(rSensors); runnableManager.removeCallbacks(rCamera); final int delay
     * = 1000 * 10; //milliseconds final int delayCamera = 1000 * 120; //milliseconds
     *
     * //Initializes and starts Runnable rSensors = new Runnable() { public void run() { Log.d(TAG,
     * "RUN!");
     *
     * // Initializes the data point class // TODO decide on preferred order order of variables -
     * not hugely important but deserves some consideration DataPoint point = new DataPoint(tAsInTemperature, gravity_x,
     * gravity_y, gravity_z, rot_x, rot_y, rot_z, relativeHumidity, magnetic_x, magnetic_y, magnetic_z, aLin_x, aLin_y, aLin_z, pAsInPressure, new Date());
     *
     * point.ext_alt = ext_alt; point.ext_lon = ext_lon; point.ext_lat = ext_lat; point.ext_altEST =
     * ext_altEST; point.ext_temp = ext_temp; point.ext_p = ext_p; point.ext_rh = ext_rh;
     * point.course = course; point.gps_speed = gps_speed;
     *
     * dataPointArrayList.add(point);
     *
     * // Object is serialized here, and the datafile is saved to the documents folder File path =
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); File file =
     * new File(path, "SENSORDATA.txt"); try { //noinspection ResultOfMethodCallIgnored
     * path.mkdirs(); OutputStream os = new FileOutputStream(file); ObjectOutputStream out = new
     * ObjectOutputStream(os); out.writeObject(dataPointArrayList); out.close(); os.close(); } catch
     * (Exception e) { e.printStackTrace(); } //schedules the next job runnableManager.postDelayed(this, delay); }
     * }; rCamera = new Runnable() {
     *
     * @Override public void run() { Log.d(TAG, "PHOTO!"); Camera camera =
     * openCamera(Camera.CameraInfo.CAMERA_FACING_BACK); try { camera.setPreviewTexture(new
     * SurfaceTexture(0)); } catch (IOException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } camera.startPreview(); final String path =
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/High
     * Altitude Photos/Back"; final File file = new File(path); //noinspection
     * ResultOfMethodCallIgnored file.mkdirs(); Camera.PictureCallback jpegCallback = new
     * Camera.PictureCallback() { public void onPictureTaken(byte[] data, Camera camera) {
     * FileOutputStream outStream; try { String finalPath = path + new Date() + ".jpg";// set your
     * directory path here outStream = new FileOutputStream(finalPath); outStream.write(data);
     * outStream.close(); } catch (Exception e) { e.printStackTrace(); } finally {
     * camera.stopPreview(); camera.release(); camera = null; } } }; camera.takePicture(null, null,
     * jpegCallback);
     *
     * Camera camera2 = openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); SurfaceView surface2 =
     * new SurfaceView(getBaseContext()); try { camera2.setPreviewTexture(new SurfaceTexture(0)); }
     * catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
     * camera2.startPreview(); final String path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
     * + "/High Altitude Photos/Front/"; final File file2 = new File(path); file2.mkdirs();
     * Camera.PictureCallback jpegCallback2 = new Camera.PictureCallback() { public void
     * onPictureTaken(byte[] data, Camera camera) { FileOutputStream outStream = null; try { String
     * finalPath = path2 + new Date() + ".jpg";// set your directory path here outStream = new
     * FileOutputStream(finalPath); outStream.write(data); outStream.close(); } catch (Exception e)
     * { e.printStackTrace(); } finally { camera.stopPreview(); camera.release(); camera = null; } }
     * }; camera2.takePicture(null, null, jpegCallback2); runnableManager.postDelayed(this, delayCamera); } };
     * //schedules the first job runnableManager.postDelayed(rSensors, delay); runnableManager.postDelayed(rCamera, delayCamera); }
     */

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    public static int getBatteryTemp(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) : -1;

        return level;
    }

    /**
     * Initializes sensors that should not be done in onCreate
     */
    @Override
    protected void onStart() {
        super.onStart();
        sensorFileName = new Date();
        //TODO add null checks to allow testing on certain phone models
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelerometer2Listener, accelerometer2, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magnetListener, magnet, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(humidityListener, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(rotationListener, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gravityListener, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(temperatureListener, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        if (!isRecording) {
            isRecording = true;
            record(null);
        }
    }

    /**
     * de-initializes sensors that should be destroyed before onDestroyed
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "STOP");
        super.onStop();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(pressureListener);
        sensorManager.unregisterListener(magnetListener);
        sensorManager.unregisterListener(humidityListener);
        sensorManager.unregisterListener(rotationListener);
        sensorManager.unregisterListener(gravityListener);
        sensorManager.unregisterListener(temperatureListener);
        stopRecord(null);
        isRecording = false;
        takeCameraFromOtherThread();
    }

    /**
     * Code that runs camera and takes a photograph every 15 seconds
     */
    //TODO add video capabilities, even if they are commented out
    private Camera openCamera(int n) {
        Log.d(TAG, "cameraOPENED");
        int cameraCount;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == n) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return cam;
    }

    private boolean prepareVideoRecorder() {

        mVideoCamera = openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mVideoCamera.unlock();
        mMediaRecorder.setCamera(mVideoCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/High Altitude Videos/";
        final File file = new File(path);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        mMediaRecorder.setOutputFile(path + new Date() + ".mp4");

        // Step 5: Set the preview output - nothing in this case
        Surface surface = new Surface(new SurfaceTexture(0));
        mMediaRecorder.setPreviewDisplay(surface);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mVideoCamera.release();
        }
    }

    //this can be called to start and stop recording - it currently isn't at all
    public void recordButton() {
        if (isRecording) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
            isRecording = false;
        } else {
            if (prepareVideoRecorder()) {
                mMediaRecorder.start();
                isRecording = true;
            } else {
                releaseMediaRecorder();
            }
        }
    }

}
