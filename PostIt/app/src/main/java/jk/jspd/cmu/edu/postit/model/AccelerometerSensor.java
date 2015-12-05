package jk.jspd.cmu.edu.postit.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
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
    float x;
    float y;
    float z;


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
        this.x=0;
        this.y=0;
        this.z=0;
    }

    public void start() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE),
                sensorDelay);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == SENSOR_TYPE) {
            if(x == 0 && y == 0 && z == 0){
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                timestamp = event.timestamp;
            }else{
                double delx = x - event.values[0];
                double dely = y - event.values[1];
                double delz = z - event.values[2];
                double delta = Math.sqrt(delx*delx + dely*dely + delz*delz);
                boolean hasListeners = !listeners.isEmpty();
                if(delta > 30 && hasListeners &&(event.timestamp-timestamp>500000000)) {
                    Log.e("sensor","timestamp: "+event.timestamp);
                    for (Callbacks listener : listeners) {
                        listener.onMoveChanged(true, delta);
                    }
                    timestamp = event.timestamp;
                }

            }


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
