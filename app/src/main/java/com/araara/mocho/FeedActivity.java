package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FeedActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;


    final UpdateHunger updateHunger = new UpdateHunger();
    int meat, rice;
    int idx;
    ImageButton meatButton;
    ImageButton riceButton;
    TextView meatQty;
    TextView riceQty;
    Monster[] tempMonster;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateHunger.execute("TES");
        String res = sharedPreferences.getString("OwnedMonster", "NONE");
        Log.d("BEFORE:", res);
        String temp = DataModel.updateJSONdata(res, tempMonster[idx], idx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OwnedMonster", temp);
        editor.apply();
        res = sharedPreferences.getString("OwnedMonster", "NONE");
        Log.d("AFTER:", res);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        progressDialog = new ProgressDialog(FeedActivity.this);
        meatButton = (ImageButton) findViewById(R.id.btnMeat);
        riceButton = (ImageButton) findViewById(R.id.btnRice);
        meatQty = (TextView) findViewById(R.id.meatQty);
        riceQty = (TextView) findViewById(R.id.riceQty);

        sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);

        progressDialog.setMessage("Please wait, retrieving food data...");
        progressDialog.show();

        Intent intent = getIntent();
        idx = intent.getIntExtra("idxmonster", -1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RetrieveFoodData retrieveFoodData = new RetrieveFoodData();
        retrieveFoodData.execute("http://ranggarmaste.cleverapps.io/api/users/" + user.getDisplayName() + "/foods");

        String res = sharedPreferences.getString("OwnedMonster", "NONE");

        tempMonster = DataModel.parseMonster(res);


        meatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meat--;
                if (meat < 0) {
                    meat = 0;
                } else {
                    if (tempMonster.length >= 0) {
                        tempMonster[idx].setHunger(tempMonster[idx].getHunger() + 100);
                        Log.d("MEAT:", tempMonster[idx].getName() + ": " + tempMonster[idx].getHunger());
                    }
                    meatQty.setText("" + meat);
                }
            }
        });
        riceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rice--;
                if (rice < 0) {
                    rice = 0;
                } else {
                    if (tempMonster.length >= 0) {
                        tempMonster[idx].setHunger(tempMonster[idx].getHunger() + 50);
                    }
                    riceQty.setText("" + rice);
                }
            }
        });


    }

    private class RetrieveFoodData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("FOOD", s);
                JSONObject res = new JSONObject(s);
                String ownedFood = res.getString("OwnedFoods");
                JSONArray foodArray = new JSONArray(ownedFood);
                for (int i = 0; i < foodArray.length(); i++) {
                    if (foodArray.getJSONObject(i).getString("name").equals("Meat")) {
                        meat = foodArray.getJSONObject(i).getInt("quantity");
                    } else {
                        rice = foodArray.getJSONObject(i).getInt("quantity");
                    }
                }
                meatQty.setText("" + meat);
                riceQty.setText("" + rice);
                progressDialog.hide();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL(params[0]);

                Log.d("Connect", "start");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Log.d("Connect", "success?");
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
                Log.d("Retrieved", response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class UpdateHunger extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TES", "STARTINGTEST");
                URL url = new URL("http://ranggarmaste.cleverapps.io/api/users/" + user + "/monsters/" + tempMonster[idx].getId());

                Log.d("Connect", "start");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write("hunger=" + tempMonster[idx].getHunger());
                Log.d("RANGGARMASTE", "hunger=" + tempMonster[idx].getHunger());
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
                Log.d("Tes", "doInBackground: response: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
