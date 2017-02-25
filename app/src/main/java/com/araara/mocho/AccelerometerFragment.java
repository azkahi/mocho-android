package com.araara.mocho;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ranggarmaste on 2/24/17.
 */

public class AccelerometerFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "Accelerometer";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView tvDefSensor;
    private TextView tvDefSensor2;
    private TextView tvTime;
    private TextView tvTime2;
    private Button btnStartAcc;
    private Button btnInfoAcc;

    private double[] target;
    private double accValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        target = new double[3];
        for (int i = 0; i < target.length; i++) {
            target[i] = Math.random() * 10;
            target[i] *= Math.random() > 0.5 ? 1 : -1;
        }
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        tvDefSensor = (TextView) view.findViewById(R.id.tvATKSensor);
        tvDefSensor2 = (TextView) view.findViewById(R.id.tvDEFSensor2);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvTime2 = (TextView) view.findViewById(R.id.tvTime2);
        btnStartAcc = (Button) view.findViewById(R.id.btnStartAcc);
        btnInfoAcc = (Button) view.findViewById(R.id.btnInfoAcc);

        btnStartAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(AccelerometerFragment.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                new CountDownTimer(15000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText(millisUntilFinished / 1000 + " s");
                        tvTime2.setText(millisUntilFinished / 1000 + " s");
                    }

                    public void onFinish() {
                        tvTime.setText("0 s");
                        tvTime2.setText("0 s");
                        mSensorManager.unregisterListener(AccelerometerFragment.this);
                    }
                }.start();
            }
        });

        btnInfoAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccDialogFragment accDialogFragment = new AccDialogFragment();
                accDialogFragment.show(getFragmentManager(), "acc_info");
            }
        });
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] res = event.values;
        accValue = calculateVal(res);
        tvDefSensor.setText(String.format("%.2f", accValue));
        tvDefSensor2.setText(String.format("%.2f", accValue));
    }

    private double calculateVal(float[] res) {
        double ans = 0;
        for (int i = 0; i < res.length; i++) {
            ans += Math.abs(res[i] - target[i]);
        }
        Log.d(TAG, "calculateVal: " + ans);
        return ans;
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

