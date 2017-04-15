package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.LocationData;
import com.araara.mocho.game.Monster;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubtypeActivity extends AppCompatActivity {
    private static final String TAG = "SubtypeActivity";
    private TextView tvDesc;
    private TextView tvTitle;
    private TextView tvBonus;
    private Button btnUnlock;
    private ImageView imgLocation;
    private int idx;
    private int val;
    private String query;
    private String attr;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtype);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBonus = (TextView) findViewById(R.id.tvBonus);
        btnUnlock = (Button) findViewById(R.id.btnUnlock);
        imgLocation = (ImageView) findViewById(R.id.imgLocation);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String location = bundle.getString("loc");
        idx = bundle.getInt("idxmonster");
        Log.d(TAG, "onCreate: location: " + location);
        Log.d(TAG, "onCreate: idxmonster: " + idx);
        for (int i = 0; i < LocationData.length; i++) {
            if (LocationData.names[i].equals(location)) {
                tvDesc.setText(LocationData.descs[i]);
                tvTitle.setText(LocationData.names[i]);
                tvBonus.setText(LocationData.attrs[i] + " +" + LocationData.amounts[i]);
                imgLocation.setImageResource(LocationData.images[i]);
                attr = LocationData.attrs[i];
                val = LocationData.amounts[i];
                if (LocationData.attrs[i].equals("ATK")) {
                    query = "addedAtk";
                } else if (LocationData.attrs[i].equals("DEF")) {
                    query = "addedDef";
                } else if (LocationData.attrs[i].equals("REC")) {
                    query = "addedRec";
                } else if (LocationData.attrs[i].equals("HP")) {
                    query = "addedHP";
                } else {
                    query = "addedSP";
                }
            }
        }

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SubtypeActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                UpdateTask updateTask = new UpdateTask();
                updateTask.execute();
            }
        });
    }

    private class UpdateTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.hide();
            SuccessDialogFragment successDialogFragment = new SuccessDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message", "Monster's " + attr + " +" + integer + "!");
            successDialogFragment.setArguments(bundle);
            successDialogFragment.show(getSupportFragmentManager(), "message");
            super.onPostExecute(integer);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            Monster[] monsters = null;
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            SharedPreferences sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);
            String parseMonsterString = sharedPreferences.getString("OwnedMonster", "NONE");
            if (!parseMonsterString.equals("NONE"))
                monsters = DataModel.parseMonster(parseMonsterString);

            String response = "";
            try {
                URL url = new URL("http://ranggarmaste.cleverapps.io/api/users/" + username + "/monsters/" + monsters[idx].getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(query + "=" + val + "&subtype=1");
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
            return val;
        }
    }
}
