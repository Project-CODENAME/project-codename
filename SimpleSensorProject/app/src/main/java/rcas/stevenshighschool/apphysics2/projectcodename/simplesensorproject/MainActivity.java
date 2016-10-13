package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    final Handler h = new Handler();
    Runnable r;

    float a_y;
    float a_x;
    float a_z;
    float p;
    Sensor accelerometer;
    Sensor pressure;
    SensorEventListener accelerometerListener;
    SensorEventListener pressureListener;
    ArrayList<DataPoint> dataPointArrayList;

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
    }


    public void record(View view){
        final int delay = 1000; //milliseconds
        r = new Runnable(){
            public void run(){
                DataPoint point = new DataPoint(a_x, a_y, a_z, p, new Date());
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
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(pressureListener);
    }

}
