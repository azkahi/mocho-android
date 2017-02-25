package com.araara.mocho;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ranggarmaste on 2/23/17.
 */

public class GyroFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "GyroFragment";
    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private TextView tvATKSensor;
    private TextView tvTime;
    private Button btnStartGyro;
    private Button btnInfoGyro;
    private double accValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gyro, container, false);
        tvATKSensor = (TextView) view.findViewById(R.id.tvATKSensor);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        btnStartGyro = (Button) view.findViewById(R.id.btnStartGyro);
        btnInfoGyro = (Button) view.findViewById(R.id.btnInfoGyro);

        btnStartGyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(GyroFragment.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText(millisUntilFinished / 1000 + " s");
                    }

                    public void onFinish() {
                        tvTime.setText("0 s");
                        mSensorManager.unregisterListener(GyroFragment.this);
                    }
                }.start();
            }
        });
        btnInfoGyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GyroDialogFragment gyroDialogFragment = new GyroDialogFragment();
                gyroDialogFragment.show(getFragmentManager(), "gyro_info");
            }
        });
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] res = event.values;
        boolean shouldPrint = false;
        for (int i = 0; i < res.length; i++) {
            if (res[i] > 1) {
                shouldPrint = true;
            }
        }
        if (shouldPrint) {
            accValue += calculateVal(res);
        }
        tvATKSensor.setText(String.format("%.2f", accValue));
    }

    private double calculateVal(float[] res) {
        double val = 0;
        for (int i = 0; i < res.length; i++) {
            val += Math.pow(res[i], 2);
        }
        return Math.sqrt(val);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
