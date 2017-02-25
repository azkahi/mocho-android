package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.araara.mocho.MESSAGE";
    private static final String TAG = "HomeActivity";
    private ProgressDialog progressDialog;
    private TextView tvWelcome;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            tvWelcome.append(user.getDisplayName());
            Toast.makeText(this, "Welcome, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        }


        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please wait, retrieving your monsters...");
        progressDialog.show();

        RetrieveMonsterData retrieveMonsterData = new RetrieveMonsterData();
        retrieveMonsterData.execute("http://ranggarmaste.cleverapps.io/api/users/" + user.getDisplayName() + "/monsters");

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class RetrieveMonsterData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);
                JSONObject res = new JSONObject(s);
                String ownedMonsters = res.getString("OwnedMonsters");
                progressDialog.hide();
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                intent.putExtra(EXTRA_MESSAGE, ownedMonsters);
                startActivity(intent);
                Log.d(TAG, res.getString("email"));
                Log.d(TAG, "onPostExecute: Successful retrieval");
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
                Log.d(TAG, "tes");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                Log.d(TAG, params[0]);

                int responseCode = conn.getResponseCode();
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
            return response;
        }
    }
}
