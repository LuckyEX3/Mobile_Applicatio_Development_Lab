package com.s22010040.test5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private MediaPlayer mediaPlayer;
    private TextView textView;
    private boolean isAvailable = false;
    private final float THRESHOLD = 40 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor);

        textView = findViewById(R.id.textView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alert);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tempSensor != null)
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float temp = event.values[0];
        textView.setText("Temperature: " + temp + " °C");

        if (temp > THRESHOLD && !isAvailable) {
            mediaPlayer.start();
            isAvailable = true;
            Toast.makeText(this, "Alert! Temperature exceeded threshold - Audio playing", Toast.LENGTH_SHORT).show();
        } else if (temp <= THRESHOLD) {
            isAvailable = false; // Reset if temp drops below threshold
            Toast.makeText(this, "Temperature normal - Audio stopped", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
