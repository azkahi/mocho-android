package com.araara.mocho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private EditText editEmail;
    private EditText editPassword;
    private EditText editUsername;
    private TextView hasAccount;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editUsername = (EditText) findViewById(R.id.editUsername);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFieldValid()) {
                    SignUpTask signUpTask = new SignUpTask();
                    signUpTask.execute("http://10.0.2.2:3000/api/users");
                }
            }
        });

        hasAccount = (TextView) findViewById(R.id.hasAccount);
        hasAccount.setClickable(true);
        hasAccount.setMovementMethod(LinkMovementMethod.getInstance());
        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String createPostBody() {
        String ans = "email=" + editEmail.getText().toString();
        ans += "&username=" + editUsername.getText().toString();
        return ans;
    }

    private boolean isFieldValid() {
        boolean ans = false;
        if (editEmail.getText().length() == 0) {
            ans = true;
            editEmail.setError("This field is required.");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            ans = true;
            editEmail.setError("Email format is invalid.");
        }
        if (editUsername.getText().length() == 0) {
            ans = true;
            editUsername.setError("This field is required.");
        }
        if (editPassword.getText().length() == 0) {
            ans = true;
            editPassword.setError("This field is required.");
        }
        return ans;
    }

    private class SignUpTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject res = new JSONObject(s);
                if (res.getString("status").equals("OK")) {
                    Log.d(TAG, "onPostExecute: Successful register");
                } else {
                    if (res.has("email")) {
                        editEmail.setError(res.getString("email"));
                    }
                    if (res.has("username")) {
                        editUsername.setError(res.getString("username"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.hide();
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
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(createPostBody());
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
                Log.d(TAG, "doInBackground: response: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
