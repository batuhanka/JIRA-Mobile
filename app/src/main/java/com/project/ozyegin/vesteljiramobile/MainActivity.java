package com.project.ozyegin.vesteljiramobile;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity  {

    public EditText mUsernameView;
    public EditText mPasswordView;
    public Button mSignInButton;
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = (Button)    findViewById(R.id.signin_btn);
        mUsernameView = (EditText)  findViewById(R.id.username_field);
        mPasswordView = (EditText)  findViewById(R.id.password_field);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });
    }

    public void attemptLogin() {

        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        Log.e("BATU", username + " : " + password);

        mAuthTask = new UserLoginTask(username, password);
        mAuthTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            checkAccount(mUsername, mPassword);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        private void checkAccount(String username, String password){
            String json = "";
            InputStream is;

           try{
                HttpClient client 			= new DefaultHttpClient();
                CookieStore cookieStore 	= new BasicCookieStore();
                HttpContext httpContext 	= new BasicHttpContext();
                HttpPost post 				= new HttpPost("http://10.108.95.25/jira/rest/auth/1/session");
                httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                post.setHeader("Content-type", "application/json");

                JSONObject obj = new JSONObject();
                obj.put("username", username);
                obj.put("password", password);

                post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
                HttpResponse response = client.execute(post, httpContext);

                is = response.getEntity().getContent();
                try {
                    BufferedReader reader   = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb        = new StringBuilder();
                    String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                    is.close();
                    json = sb.toString();
                } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.get("session") != null){
                Log.e("BATU", "Login Successful");
                Intent intent   = new Intent(getBaseContext(), DashboardActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                MainActivity.this.finish();
            }
            else{
                Log.e("BATU", "Login Failed");
            }

        }catch(Exception ignored){	Log.e("BATU", ignored.toString()); }
       }
    }
}
