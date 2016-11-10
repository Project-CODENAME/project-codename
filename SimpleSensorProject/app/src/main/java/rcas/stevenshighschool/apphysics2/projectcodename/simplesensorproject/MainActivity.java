package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/** For Root Access*/
//import android.widget.Button;
//import android.app.Activity;
//import android.widget.Button;
//import android.widget.Toast;
//import android.net.Uri;
//import android.os.AsyncTask;


import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
//import java.util.List;
import java.util.Date;

import eu.chainfire.libsuperuser.Shell;

//import eu.chainfire.libsuperuser.Shell;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /** Sensor manager */
    private SensorManager sensorManager;

    /** Main loops for the sensors */
    final Handler h = new Handler();
    Runnable r;

    /** Data values */
    float t;
    float a_y;
    float a_x;
    float a_z;
    float p;
    float m_x;
    float m_y;
    float m_z;
    float rh;
    float rot_x;
    float rot_y;
    float rot_z;
    float g_x;
    float g_y;
    float g_z;
    //float ext_t;
    //float ext_p;
    Location mLastLocation;

    /**
     * Sensors and their listeners
     */
    Sensor accelerometer;
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


    Button reboot,recv,shut,sysui;


    /**
     * Array list of datapoints
     */
    ArrayList<DataPoint> dataPointArrayList;

    /**
     * Google Location things
     */
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    /**
     * Logging TAG
     */
    private final String TAG = "SENSORS:";

    /**
     * Arduino connection code will be inserted below
     *
     * NOTE: SENSOR DATA COLLECTED BY THE ARDUINO WILL BE WRITTEN TO ext_t, ext_p, and ard_alt
     *
     * BluetoothAndroid mRobot = BluetoothArduino.getInstance("ExtSensorsRobot");
     * */


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Starts by getting the location and requesting location updates
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            startLocationUpdates();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // In case the connection fails
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    //Initialization of the activity


    public class StartUp extends AsyncTask<String,Void,Void> {
        public Context context = null;
        boolean suAvailable = false;
        public MainActivity.StartUp setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        protected Void doInBackground(String... params) {
            suAvailable = Shell.SU.available();
            if (suAvailable) {

                // suResult = Shell.SU.run(new String[] {"cd data; ls"}); Shell.SU.run("reboot");
                switch (params[0]){
                    case "reboot"  : Shell.SU.run("reboot");break;
                    case "recov"   : Shell.SU.run("reboot recovery");break;
                    case "shutdown": Shell.SU.run("reboot -p");break;
                    //case "sysui"   : Shell.SU.run("am startservice -n com.android.systemui/.SystemUIService");break;
                    case "sysui"   : Shell.SU.run("pkill com.android.systemui");break;
                }
            }
            else{
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(),"Phone not Rooted",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //starts things
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reboot = (Button) findViewById(R.id.btn_reb);
        recv = (Button) findViewById(R.id.btn_rec);
        shut = (Button) findViewById(R.id.shut);
        sysui = (Button) findViewById(R.id.SysUi);
        reboot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                (new MainActivity.StartUp()).setContext(v.getContext()).execute("reboot");
            }
        });
        recv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new MainActivity.StartUp()).setContext(v.getContext()).execute("recov");
            }
        });
        shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new MainActivity.StartUp()).setContext(v.getContext()).execute("shutdown");
            }
        });
        sysui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (new MainActivity.StartUp()).setContext(v.getContext()).execute("sysui");

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

        /** Initializes sensors and their listeners */
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        /** TODO incorporate low-power mode for payload in emergency low-power situation */


        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes a to most recent value
                a_x = sensorEvent.values[0];
                a_y = sensorEvent.values[1];
                a_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        pressureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes p to most recent value
                p = sensorEvent.values[0];
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
                m_x = sensorEvent.values[0];
                m_y = sensorEvent.values[1];
                m_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        humidityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes rh to most recent value
                rh = sensorEvent.values[0];
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
                g_x = sensorEvent.values[0];
                g_y = sensorEvent.values[1];
                g_z = sensorEvent.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };

        temperatureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes t to most recent value
                t = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //do nothing
            }
        };


        /** Initializes Array */
        dataPointArrayList = new ArrayList<DataPoint>();

        /** Initializes Google Location Things */
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /**
         * mRobot.Connect();
         */
    }

    protected void startLocationUpdates() {
        //Starts location updates
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    /**
     * Recording function that starts running things
     */
    public void record(View view) {
        final int delay = 1000; //milliseconds

        /** Initializes and starts Runnable */
        r = new Runnable() {
            public void run() {
                Log.d(TAG, "RUN!");

                /** Initializes the data point class */
                /** TODO decide on preferred order order of variables - not hugely important but deserves some consideration */
                DataPoint point = new DataPoint(t, g_x, g_y, g_z, rot_x, rot_y, rot_z, rh, m_x, m_y, m_z, a_x, a_y, a_z, p, new Date());
                if (mLastLocation != null) {
                    point.lat = mLastLocation.getLatitude();
                    point.alt = mLastLocation.getAltitude();
                    point.lon = mLastLocation.getLongitude();
                }

                /**
                 * String msg = mRobot.getLastMessage();
                 * String[] parts = msg.split("-");
                 * point.ext_p=Float.parseFloat(parts[0]);
                 * point.ext_t=Float.parseFloat(parts[1]);
                 * point.ard_alt=Float.parseFloat(parts[2]);
                 */

                dataPointArrayList.add(point);

                /** Object is serialized here, and the datafile is saved to the documents folder */
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(path, "SENSORDATA.txt");
                try {
                    path.mkdirs();
                    OutputStream os = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(os);
                    out.writeObject(dataPointArrayList);
                    out.close();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, mGoogleApiClient.isConnected() + "");
                //schedules the next job
                h.postDelayed(this, delay);
            }
        };

        //schedules the first job
        h.postDelayed(r, delay);
    }

    public void stopRecord(View view) {
        h.removeCallbacks(r);
    }

    /**
     * Initializes sensors that should not be done in onCreate
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magnetListener, magnet, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(humidityListener, humidity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(rotationListener, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gravityListener, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(temperatureListener, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /**
     * de-initializes sensors that should be destroyed before onDestroyed
     */
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(pressureListener);
        sensorManager.unregisterListener(magnetListener);
        sensorManager.unregisterListener(humidityListener);
        sensorManager.unregisterListener(rotationListener);
        sensorManager.unregisterListener(gravityListener);
        sensorManager.unregisterListener(temperatureListener);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 251: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient.isConnected()) {
                        /** TODO fix permission issues...several possible solutions to entertain */
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        startLocationUpdates();
                    }
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }







}

