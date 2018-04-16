package com.vit.ayush.vibrance18.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vit.ayush.vibrance18.R;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import java.util.HashMap;

public class ForgotPassActivity extends AppCompatActivity {

    EditText emailid;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        emailid = findViewById(R.id.emailforgot);
        submit = findViewById(R.id.forgotpasssubmit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap data=new HashMap();
                data.put("email",emailid.getText().toString());

                PostResponseAsyncTask task = new PostResponseAsyncTask(ForgotPassActivity.this, data, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(!s.contains("failed")){
                            Toast.makeText(ForgotPassActivity.this,"Account details sent to your mail::"+s,Toast.LENGTH_LONG).show();
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(ForgotPassActivity.this, LoginActivity.class);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchNextActivity);
                        }
                        else{
                            Toast.makeText(ForgotPassActivity.this, "User with the email does not exist::"+s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                task.execute(server.server_ip_only()+"register/DB_Service/Appforgotpass.php");
            }
        });


    }

}
