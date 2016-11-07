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

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SensorManager sensorManager;
    final Handler h = new Handler();
    Runnable r;

    float a_y;
    float a_x;
    float a_z;
    float p;
    Location mLastLocation;
    Sensor accelerometer;
    Sensor pressure;
    SensorEventListener accelerometerListener;
    SensorEventListener pressureListener;
    ArrayList<DataPoint> dataPointArrayList;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private final String TAG = "SENSORS:";

    /**Once we have an Arduino
     * BluetoothAndroid mRobot = BluetoothArduino.getInstance("ExtSensorsRobot");
     */

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    250);

        }
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        accelerometerListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                a_x=sensorEvent.values[0];
                a_y=sensorEvent.values[0];
                a_z=sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        pressureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                p = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        dataPointArrayList = new ArrayList<DataPoint>();
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
        /**
         * mRobot.Connect();
         */
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    public void record(View view){
        final int delay = 1000; //milliseconds
        r = new Runnable(){
            public void run(){
                Log.d(TAG, "RUN!");
                DataPoint point = new DataPoint(a_x, a_y, a_z, p, new Date());
                point.lat=mLastLocation.getLatitude();
                point.alt=mLastLocation.getAltitude();
                point.lon=mLastLocation.getLongitude();
                /**
                 * String msg = mRobot.getLastMessage();
                 * String[] parts = msg.split("-");
                 * point.ext_p=Float.parseFloat(parts[0]);
                 * point.ext_t=Float.parseFloat(parts[1]);
                 */
                dataPointArrayList.add(point);
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

                h.postDelayed(this, delay);
            }
        };
        h.postDelayed(r, delay);
    }

    public void stopRecord(View view){
        h.removeCallbacks(r);
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(pressureListener);
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

}
