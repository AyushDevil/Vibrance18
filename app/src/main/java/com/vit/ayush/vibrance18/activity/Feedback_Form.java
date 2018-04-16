package com.vit.ayush.vibrance18.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vit.ayush.vibrance18.R;

public class Feedback_Form extends AppCompatActivity {

    WebView webView;
    WebSettings settings;
    Button givefeed;
    LinearLayout layout1,load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback__form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarfeedback);
        setSupportActionBar(toolbar);
        load = findViewById(R.id.loadfeedback);
        givefeed = (Button)findViewById(R.id.givefeed);
        webView = (WebView) findViewById(R.id.feedbackform);
        layout1 = findViewById(R.id.detailsfeedback);
        settings = webView.getSettings();

        givefeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                load.setVisibility(View.VISIBLE);
                settings.setJavaScriptEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                settings.setAppCacheEnabled(true);
                settings.setEnableSmoothTransition(true);
                settings.setDomStorageEnabled(true);
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                settings.setBuiltInZoomControls(true);
                settings.setSaveFormData(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSddsZsmSQ2htAEMNBI6uaX3S2d8pFvIXCW6Zms0JQCY4LllgA/viewform?c=0&w=1");
                webView.setWebViewClient(new MyWebViewClient());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabfeedback);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchNextActivity;
                launchNextActivity = new Intent(Feedback_Form.this, MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(launchNextActivity);
            }
        });

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            load.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.ACTION_DOWN:
                    if(webView.canGoBack()){
                        webView.goBack();
                    }
                    else{
                        finish();
                    }
                    return true;
            }
        }
        return  super.onKeyDown(keyCode, event);
    }
}
