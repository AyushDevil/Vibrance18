package com.vit.ayush.vibrance18.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.MainActivity;
import com.vit.ayush.vibrance18.activity.server;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class SettingsFragment extends Fragment{
    Button changepass,changefinal;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    EditText pass;
    TextView usr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        changepass=getView().findViewById(R.id.changebtn);
        changefinal=getView().findViewById(R.id.submitnewbtn);
        usr=getView().findViewById(R.id.idchange);
        prefs=this.getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        pass=getView().findViewById(R.id.passchange);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepass.setVisibility(View.GONE);
                usr.setVisibility(View.VISIBLE);
                usr.setText(prefs.getString("Name",""));
                pass.setVisibility(View.VISIBLE);
                changefinal.setVisibility(View.VISIBLE);
            }
        });
        changefinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap data=new HashMap();
                data.put("id",prefs.getString("username",""));
                data.put("password",pass.getText().toString());

                editor=prefs.edit();
                PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), data, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(!s.contains("failed")){
                            editor.putString("password",pass.getText().toString());
                            editor.apply();
                            Toast.makeText(getActivity(),"Password Update successful",Toast.LENGTH_SHORT).show();
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(getActivity(), MainActivity.class);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchNextActivity);
                        }
                        else{
                            Toast.makeText(getActivity(), "Problem updating password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                task.execute(server.server_ip()+"Apppasswordchange.php");
            }
        });





    }


}
