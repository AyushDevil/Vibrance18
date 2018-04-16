package com.vit.ayush.vibrance18.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    Button login,register;
    TextView forgotpass;
    EditText user,pass;
    SharedPreferences prefs;
    boolean connected;


    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder1.setMessage("Please Login with Your Vibrance Account if you have any or create a new one by clicking on SignUp");
        builder1.setCancelable(true);
        builder1.setTitle("Notice !");

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        login=findViewById(R.id.loginmain);
        register=findViewById(R.id.registermain);
        user=findViewById(R.id.usernamelogin);
        pass=findViewById(R.id.passwordlogin);
        forgotpass = findViewById(R.id.forgotbutton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        connected = false;
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else {
                    connected = false;
                }
                if(connected) {
                    if (!user.getText().toString().equals("") && !pass.getText().toString().equals("")) {
                        HashMap data = new HashMap();
                        data.put("username", user.getText().toString());
                        data.put("password", pass.getText().toString());
                        prefs = getSharedPreferences(myprefs, Context.MODE_PRIVATE);
                        editor = prefs.edit();
                        PostResponseAsyncTask task = new PostResponseAsyncTask(LoginActivity.this, data, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (!s.contains("failed")) {
                                    editor.putString("username", user.getText().toString());
                                    editor.putString("password", pass.getText().toString());
                                    editor.putString("Name", s);
                                    editor.apply();
                                    Intent launchNextActivity;
                                    launchNextActivity = new Intent(LoginActivity.this, MainActivity.class);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(launchNextActivity);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        task.execute(server.server_ip() + "AppLogin.php");
                    } else {
                        Toast.makeText(LoginActivity.this, "Please Fill all the fields !", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Connect to the intrnet and try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this,ForgotPassActivity.class);
                        startActivity(intent);
            }
        });

    }

}
