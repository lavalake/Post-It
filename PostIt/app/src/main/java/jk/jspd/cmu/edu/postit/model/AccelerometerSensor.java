package jk.jspd.cmu.edu.postit.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class AccelerometerSensor implements SensorEventListener {
    private static final int SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION;
    private static final int DEFAULT_SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;
    private SensorManager sensorManager;
    private int sensorDelay;
    private float[] vector;

    private long timestamp;
    private int accuracy;

    private List<Callbacks> listeners;



    private  boolean move;
    private  double value;

    public AccelerometerSensor(Context context) {
        this(context, DEFAULT_SENSOR_DELAY);
    }

    public AccelerometerSensor(Context context, int sensorDelay) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorDelay = sensorDelay;
        this.vector = new float[3];
        this.move = false;
        this.timestamp = 0;
        this.accuracy = 0;
        this.listeners = new LinkedList<>();
    }

    public void startSensing() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE),
                sensorDelay);
    }

    public void stopSensing() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == SENSOR_TYPE) {



            }


    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        if (sensor.getType() == SENSOR_TYPE && !listeners.isEmpty()) {
            for (Callbacks listener : listeners) {
                listener.onMoveAccuracyChanged(accuracy);
            }
        }
    }



    public void addListener(Callbacks listener) {
        listeners.add(listener);
    }

    public void removeListener(Callbacks listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public interface Callbacks {
        void onMoveChanged(boolean move, double value);

        void onMoveAccuracyChanged(int accuracy);
    }
    public boolean isMove() {
        return move;
    }
}
