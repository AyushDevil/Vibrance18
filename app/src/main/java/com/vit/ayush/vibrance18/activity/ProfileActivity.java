package com.vit.ayush.vibrance18.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.other.RegisteredEventAdapter;
import com.vit.ayush.vibrance18.other.RegisteredEventClass;
import com.vit.ayush.vibrance18.other.RegisteredTeesAdapter;
import com.vit.ayush.vibrance18.other.RegisteredTeesClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private List<RegisteredEventClass> data_Reg_E;
    private List<RegisteredTeesClass> data_Reg_T;
    private RecyclerView R_e_recyclerView;
    private GridLayoutManager R_e_gridLayoutManager;
    private RegisteredEventAdapter R_e_adapter;
    private RecyclerView R_t_recyclerView;
    private GridLayoutManager R_t_gridLayoutManager;
    private RegisteredTeesAdapter R_t_adapter;
    LinearLayout gifload;
    FloatingActionButton fabback;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        data_Reg_E=new ArrayList<RegisteredEventClass>();
        data_Reg_T=new ArrayList<RegisteredTeesClass>();
        R_e_gridLayoutManager = new GridLayoutManager(this,1);
        //R_t_gridLayoutManager = new GridLayoutManager(this,1);
        gifload = findViewById(R.id.loadgif);
        fabback = findViewById(R.id.fabhomeback);


        error=findViewById(R.id.error_msg);
        R_e_recyclerView = findViewById(R.id.EventsRegisteredRecycler_n);
        R_e_recyclerView.setLayoutManager(R_e_gridLayoutManager);
        R_e_recyclerView.setHasFixedSize(true);
        R_e_adapter = new RegisteredEventAdapter(this,data_Reg_E);

        R_e_recyclerView.setAdapter(R_e_adapter);

        R_e_recyclerView.destroyDrawingCache();
        //R_t_recyclerView.destroyDrawingCache();
        SharedPreferences prefs=getSharedPreferences("login.conf",MODE_PRIVATE);
//Log.d("....notification","done");

fabback.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent launchNextActivity;
        launchNextActivity = new Intent(ProfileActivity.this, MainActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
    }
});


        set_data(prefs.getString("username",""));

    }

    private void set_data(final String id) {

        @SuppressLint("StaticFieldLeak") AsyncTask<String,Void,Integer> task = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... strings) {
               // Log.d("....notification  1","done");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(server.server_ip() + "Appdetails.php?id=" + id).build();


                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    int i=0;
                    for (i=1;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        //Log.d("....check data..",object.getString("idevent"));
                        RegisteredEventClass data = new RegisteredEventClass(object.getString("eventname"),object.getString("venue"),
                                object.getString("eventdate"),object.getString("eventtime"),"0");

                        data_Reg_E.add(data);
                    }
                    //Log.d("My Registered E List  ", Arrays.asList(data_Reg_E).toString());


                    if (array.length()==0){
                        return 1;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    System.out.print("End of Contents");
                }
                return 0;

            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                if (aVoid==1){
                   error.setVisibility(View.VISIBLE);
                }
                gifload.setVisibility(View.GONE);
                R_e_adapter.notifyDataSetChanged();
                //R_t_adapter.notifyDataSetChanged();
                super.onPostExecute(aVoid);

            }
        };
        task.execute(id);


    }
}
