package com.vit.ayush.vibrance18.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.fragment.SettingsFragment;
import com.vit.ayush.vibrance18.other.NotificationPublisher;

import java.net.URL;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.vit.ayush.vibrance18.other.NotificationPublisher.NOTIFICATION_ID;

public class Payment_Activity extends AppCompatActivity {

    Button backhome, proceedpayment;
    LinearLayout web;
    SharedPreferences prefs;
    public static final String myprefs = "login.conf";
    String eventId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        web = findViewById(R.id.weblinear);
        backhome = findViewById(R.id.gobackhome);
        proceedpayment = findViewById(R.id.proceedpay);
        prefs = getSharedPreferences(myprefs, Context.MODE_PRIVATE);

        eventId = getIntent().getStringExtra("EventId");


        final String uid = prefs.getString("username", "");


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNextActivity;
                launchNextActivity = new Intent(Payment_Activity.this, MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(launchNextActivity);
            }
        });
        proceedpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlString= server.server_ip() + "AppPayment.php?username=" + uid.trim() + "&event=" + eventId.trim();
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    Payment_Activity.this.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    Payment_Activity.this.startActivity(intent);
                }
            }
        });

    }

}
