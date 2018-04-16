package com.vit.ayush.vibrance18.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.intro.intro_main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifTextView;

public class WelcomeActivity extends AppCompatActivity {
    LinearLayout l1,l2;
    SharedPreferences prefs;
    Context context;

    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";
    boolean connected;
    Calendar c;
    long date;
    long da1,da2;
    TextView message;
    GifTextView gifload;
    Button butback;


    Animation uptodown,downtoup;
    private static int TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        prefs=getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        c=Calendar.getInstance();
        date=System.currentTimeMillis();
        da1=date+5000;
        da2=date+1000;
        butback=findViewById(R.id.appclosebut);
        gifload=findViewById(R.id.waitload);
        message=findViewById(R.id.connectservertext);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        butback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        connected = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
            message.setText("Please Check your internet connection and try again....");
            gifload.setVisibility(View.GONE);
            butback.setVisibility(View.VISIBLE);
        }




                final String username = prefs.getString("username","");
                final String password = prefs.getString("password","");

                if(connected) {
                    if (!(username == "" && password == "")) {



                        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

                            @Override
                            protected String doInBackground(String... urlW) {


                                try {
                                    HttpURLConnection.setFollowRedirects(false);
                                    HttpURLConnection con = (HttpURLConnection) new URL(urlW[0]).openConnection();
                                    con.setRequestMethod("HEAD");
                                    //Log.d("...response : ","456");
                                    con.setConnectTimeout(TIME_OUT);
                                    System.out.println(con.getResponseCode());
                                    //Log.d("...response : ",Integer.toString(con.getResponseCode()));
                                    if (!(con.getResponseCode() == HttpURLConnection.HTTP_OK)) {
                                        connected = false;

                                    }
                                } catch (Exception e) {
                                    connected=false;
                                    //System.out.print("Error");
                                }

                                if (connected) {
                                    OkHttpClient client = new OkHttpClient();

                                    RequestBody formBody = new FormBody.Builder()
                                            .add("username", username)
                                            .add("password", password)
                                            .build();

                                    Request requestc = new Request.Builder()
                                            .url(urlW[0])
                                            .post(formBody)
                                            .build();
                                    //Request requestc = new Request.Builder().url(url[0].trim() + "?username=" + username.trim() + "&password=" + password.trim()).build();
                                    //Log.d("....Wellcome Web...", requestc.toString());
                                    try {
                                        Response response = client.newCall(requestc).execute();
                                        String feed = (response.body().string());
                                        //Log.d("...opening", feed);
                                        return feed;

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                }
                                else
                                {
                                    return "not";
                                }
                            }



                            @Override
                            protected void onPostExecute(String s) {

                                //Log.d("..Server :",s);
                                if(s.equals("not") ){
                                    message.setText("Server not responding. Try again in some time !");
                                    gifload.setVisibility(View.GONE);
                                    butback.setVisibility(View.VISIBLE);

                                }
                                else if (s.contains("failed")) {
                                    Toast.makeText(WelcomeActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                    Intent launchNextActivity;
                                    launchNextActivity = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(launchNextActivity);



                                }
                                else {

                                    Intent launchNextActivity;
                                    launchNextActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(launchNextActivity);
                                }
                            }
                        };

                        task.execute(server.server_ip() + "AppLogin.php");

                    } else {
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(WelcomeActivity.this, intro_main.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(launchNextActivity);
                    }
                }
                else{
                   // Log.d("Connection Status","FALSE");
                    message.setText("Please Check your internet connection and try again....");
                    gifload.setVisibility(View.GONE);
                    butback.setVisibility(View.VISIBLE);
                }
    }
}