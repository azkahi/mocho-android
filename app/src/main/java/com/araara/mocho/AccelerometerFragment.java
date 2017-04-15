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
    private int idx;
    private ProgressDialog progressDialog;

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
        idx = getArguments().getInt("idxmonster");
        Log.d(TAG, "onCreate: IDX MONSTER: " + idx);
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
                new CountDownTimer(15000, 100) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText(millisUntilFinished / 1000 + "." + (millisUntilFinished/100) % 10 + " s");
                        tvTime2.setText(millisUntilFinished / 1000 + "." + (millisUntilFinished/100) % 10 + " s");
                    }

                    public void onFinish() {
                        tvTime.setText("0.0 s");
                        tvTime2.setText("0.0 s");
                        mSensorManager.unregisterListener(AccelerometerFragment.this);
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        UpdateDEFTask updateDEFTask = new UpdateDEFTask();
                        updateDEFTask.execute(accValue);
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

    private class UpdateDEFTask extends AsyncTask<Double, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.hide();
            SuccessDialogFragment successDialogFragment = new SuccessDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Monster's DEF +" + integer + "!");
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

            int defVal = calculate(doubles[0]);
            int newDef = defVal + monsters[idx].getDefense();
            Log.d(TAG, "doInBackground: newDef: " + newDef);
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
                writer.write("addedDef=" + newDef + "&hunger=" + newHunger);
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
            return defVal;
        }

        private int calculate(Double rawVal) {
            int ans;
            if (rawVal <= 5) {
                ans = 5;
            } else if (rawVal <= 10) {
                ans = 3;
            } else if (rawVal <= 20) {
                ans = 2;
            } else {
                ans = 1;
            }
            return ans;
        }
    }
}

