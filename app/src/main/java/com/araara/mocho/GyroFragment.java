package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private int idx;
    private ProgressDialog progressDialog;
    private double accValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        idx = getArguments().getInt("idxmonster");
        Log.d(TAG, "onCreate: IDX MONSTER: " + idx);
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
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        UpdateATKTask updateATKTask = new UpdateATKTask();
                        updateATKTask.execute(accValue);
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

    private class UpdateATKTask extends AsyncTask<Double, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.hide();
            SuccessDialogFragment successDialogFragment = new SuccessDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Monster's ATK +" + integer + "!");
            successDialogFragment.setArguments(bundle);
            successDialogFragment.show(getFragmentManager(), "message");
            super.onPostExecute(integer);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Double... doubles) {
            Monster[] monsters = null;
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);
            String parseMonsterString = sharedPreferences.getString("OwnedMonster", "NONE");
            if (!parseMonsterString.equals("NONE"))
                monsters = DataModel.parseMonster(parseMonsterString);

            int atkVal = calculate(doubles[0]);
            int newAtk = atkVal + monsters[idx].getAttack();
            Log.d(TAG, "doInBackground: newAtk: " + newAtk);
            int newHunger = monsters[idx].getHunger() - 50;
            int idMonster = idx == 0 ? 2 : 4;

            String response = "";
            try {
                URL url = new URL("http://ranggarmaste.cleverapps.io/api/users/" + username + "/monsters/" + idMonster);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write("addedAtk=" + newAtk + "&hunger=" + newHunger);
                writer.flush();
                writer.close();

                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "doInBackground: responseCode: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
                Log.d(TAG, "doInBackground: response: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return atkVal;
        }

        private int calculate(Double rawVal) {
            int ans;
            if (rawVal >= 60) {
                ans = 5;
            } else if (rawVal >= 40) {
                ans = 3;
            } else if (rawVal >= 20) {
                ans = 2;
            } else {
                ans = 1;
            }
            return ans;
        }
    }
}
