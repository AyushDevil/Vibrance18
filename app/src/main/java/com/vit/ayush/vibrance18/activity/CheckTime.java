package com.vit.ayush.vibrance18.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckTime extends AppCompatActivity {

    long tmili,tmili1,cktime;
    TextView tc;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_time);
        tc = findViewById(R.id.timecheck);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");


        String dateString = sdf.format(date);
        try {
            Date dt = sdf.parse(dateString);
            tmili1 = dt.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        get_data("SURAJ");

        //Date date = new Date();
        //tc.setText((CharSequence) date);
        /*String date="Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf=new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd MMM yyyy");
        date = spf.format(newDate);
        tc.setText(date);*/
    }

    private void get_data(final String id) {

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(server.server_ip() + "Appdetails.php?id=" + id).build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    JSONObject ob = array.getJSONObject(1);
                    a = ob.getString("eventdate") +" "+ ob.getString("eventtime");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm a");
                    Date dt = sdf.parse(a);
                    tmili = dt.getTime();
                    cktime=tmili-tmili1;

                    int i = 0;

                    /*for (i=1;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        //Log.d("....check data..",object.getString("idevent"));
                        RegisteredEventClass data = new RegisteredEventClass(object.getString("eventname"),object.getString("venue"),
                                object.getString("eventdate"),object.getString("eventtime"),"0");

                       // data_Reg_E.add(data);
                    }*/



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    System.out.print("End of Contents");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(cktime>0)
                tc.setText(Long.toString(tmili));
                else
                    tc.setText("Time Up");
            }
        };
        task.execute(id);
    }
}
