package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the main activity from which everything is run.
 * Extends GoogleApiClient things so we can get the location
 */
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Sensor manager
    private SensorManager sensorManager;

    //Main loops for the sensors
    final Handler h = new Handler();
    Runnable r;

    //Data values
    float a_y;
    float a_x;
    float a_z;
    float p;
    Location mLastLocation;

    //Sensors and their listeners
    Sensor accelerometer;
    Sensor pressure;
    SensorEventListener accelerometerListener;
    SensorEventListener pressureListener;

    //Array list of datapoints
    ArrayList<DataPoint> dataPointArrayList;

    //Google Location things
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    //Logging TAG
    private final String TAG = "SENSORS:";

    /**Once we have an Arduino
     * BluetoothAndroid mRobot = BluetoothArduino.getInstance("ExtSensorsRobot");
     */

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //starts things
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gets sensors and checks for permissions
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
         if (ContextCompat.checkSelfPermission(this,
                         Manifest.permission.WRITE_EXTERNAL_STORAGE)
                         != PackageManager.PERMISSION_GRANTED) {
             // No explanation needed, we can request the permission.
             ActivityCompat.requestPermissions(this,
                     new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                     250);

         }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    251);

        }

        //initializes sensors and their listeners
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        //TODO extra sensors--magnetic and orientation--commented out relative humidity, temperature
        /**
         * TODO note that we have low power sensors as in https://source.android.com/devices/sensors/sensor-types.html
         * that is - maybe look at geomagnetic rotation vector, as well as rotation vector
         */
        accelerometerListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //changes a to most recent value
                a_x=sensorEvent.values[0];
                a_y=sensorEvent.values[1];
                a_z=sensorEvent.values[2];
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

        //initializes array
        dataPointArrayList = new ArrayList<DataPoint>();

        //intiializes google location things
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

    //recording function that starts running things
    public void record(View view){
        final int delay = 1000; //milliseconds

        //initializes and starts Runnable
        r = new Runnable(){
            public void run(){
                Log.d(TAG, "RUN!");

                //initializes data points and its values
                DataPoint point = new DataPoint(a_x, a_y, a_z, p, new Date());
                if(mLastLocation!=null) {
                    point.lat = mLastLocation.getLatitude();
                    point.alt = mLastLocation.getAltitude();
                    point.lon = mLastLocation.getLongitude();
                }
                /**
                 * String msg = mRobot.getLastMessage();
                 * String[] parts = msg.split("-");
                 * point.ext_p=Float.parseFloat(parts[0]);
                 * point.ext_t=Float.parseFloat(parts[1]);
                 */
                dataPointArrayList.add(point);

                //saves to a folder(pictures for some vague reason), the sensor data file--and the object is serialized here
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path,"SENSORDATA.txt");
                try {
                    path.mkdirs();
                    OutputStream os = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(os);
                    out.writeObject(dataPointArrayList);
                    out.close();
                    os.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(TAG, mGoogleApiClient.isConnected()+"");
                //schedules the next job
                h.postDelayed(this, delay);
            }
        };

        //schedules the first job
        h.postDelayed(r, delay);
    }

    public void stopRecord(View view){
        h.removeCallbacks(r);
    }

    //initializes things sensors that should not be done in onCreate
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        if(mGoogleApiClient.isConnected()){
            startLocationUpdates();
        }
    }

    //de-initializes sensors that should be destroyed before onDestroyed
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(pressureListener);
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
        //TODO WRITE WAKELOCK
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 251: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mGoogleApiClient.isConnected()){
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
