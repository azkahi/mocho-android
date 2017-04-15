package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.araara.mocho.game.MonsterAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private SharedPreferences sharedPreferences;
    private Monster[] monsterList;
    private ProgressDialog progressDialog;
    private ListView lv;
    private FirebaseUser user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.signOut:
                SignOutTask signOutTask = new SignOutTask();
                signOutTask.execute(user.getDisplayName());
                break;
            case R.id.refreshMonsters:
                progressDialog.setMessage("Please wait, retrieving your monsters...");
                progressDialog.show();
                RetrieveMonsterData retrieveMonsterData = new RetrieveMonsterData();
                retrieveMonsterData.execute("http://ranggarmaste.cleverapps.io/api/users/" + user.getDisplayName() + "/monsters");
                break;
            case R.id.itemGPS:
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        return true;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        lv = (ListView) findViewById(R.id.listView);
        progressDialog = new ProgressDialog(GameActivity.this);
        progressDialog.setMessage("Please wait, retrieving your monsters...");
        progressDialog.show();
        
        user = FirebaseAuth.getInstance().getCurrentUser();
        RetrieveMonsterData retrieveMonsterData = new RetrieveMonsterData();
        retrieveMonsterData.execute("http://ranggarmaste.cleverapps.io/api/users/" + user.getDisplayName() + "/monsters");
    }

    private class RetrieveMonsterData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject res = new JSONObject(s);
                String ownedMonsters = res.getString("OwnedMonsters");
                progressDialog.hide();

                sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("OwnedMonster", ownedMonsters);
                editor.apply();

                monsterList = DataModel.parseMonster(ownedMonsters);
                MonsterAdapter adapter = new MonsterAdapter(GameActivity.this, monsterList);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(GameActivity.this, DetailGameActivity.class);
                        intent.putExtra("idxmonster", i);
                        startActivity(intent);
                    }
                });
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

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class SignOutTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Signing out...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String username = strings[0];
            if (deleteToken(username)) {
                return "OK";
            } else {
                return "ERROR";
            }
        }

        private boolean deleteToken(String username) {
            try {
                URL url = new URL("http://ranggarmaste.cleverapps.io/api/user_keys/" + username);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            if (s.equals("OK")) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(GameActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
