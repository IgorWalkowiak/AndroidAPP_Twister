package com.example.twister;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment implements SensorEventListener {

    private TextView mCounterText;
    private TextView mResultText;
    private CountDownTimer mCountDownTimer;
    private SensorManager sensorManager;
    private boolean firstQuarter = false;
    private boolean secondQuarter = false;
    private boolean thirdQuarter = false;
    private boolean fourthQuarter = false;

    private int laps = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.i("DEEEBUG", "AHOJ ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCounterText = view.findViewById(R.id.timeText);
        mResultText = view.findViewById(R.id.textResult);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_FASTEST);

        mCountDownTimer = new CountDownTimer(10000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                long milies = millisUntilFinished%1000;
                milies = milies/10;
                String text = String.format("%02d:%02d",seconds,milies);
                mCounterText.setText(text);
            }

            @Override
            public void onFinish() {
                Log.i("DEEEBUG", "onFinish");
                mCounterText.setText("00:00");
                mResultText.setText("Your result: "+laps);
            }
        };

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DEEEBUG", "CLICK ");
                startTimer();
            }
        });
    }

    private void startTimer(){
        Log.i("DEEEBUG", "startTimer() ");
        laps = 0;
        mCountDownTimer.cancel();
        mCountDownTimer.start();
    };

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_GRAVITY){
            float currentGonX = event.values[1];
            float currentGonY = event.values[2];
            if(!firstQuarter && !fourthQuarter && currentGonX > 6.0 )
            {
                firstQuarter = true;
                Log.i("LAP", "firstQuarter");
            }
            else if(firstQuarter && !secondQuarter && currentGonX > -3.5 && currentGonX < 3.5 && currentGonY > 0)
            {
                secondQuarter = true;
                Log.i("LAP", "secondQuarter");
            }
            else if(secondQuarter && !thirdQuarter && currentGonX < -6.0 )
            {
                thirdQuarter = true;
                Log.i("LAP", "thirdQuarter");
            }
            else if(thirdQuarter && !fourthQuarter && currentGonX > -3.5 && currentGonX < 3.5 && currentGonY < 0)
            {
                fourthQuarter = true;
                Log.i("LAP", "fourthQuarter");
            }

            if(firstQuarter && secondQuarter && thirdQuarter && fourthQuarter && currentGonX > 8.0 && currentGonY > 0)
            {
                firstQuarter = secondQuarter = thirdQuarter = fourthQuarter = false;
                Log.i("LAP", "new lap");
                laps++;
            }
        }
    }
}