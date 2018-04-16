package com.vit.ayush.vibrance18.fragment;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.ProfileActivity;
import com.vit.ayush.vibrance18.activity.server;
import com.vit.ayush.vibrance18.other.LoadImg;
import com.vit.ayush.vibrance18.other.NotificationPublisher;
import com.vit.ayush.vibrance18.other.Tees;
import com.vit.ayush.vibrance18.other.TeesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.vit.ayush.vibrance18.other.NotificationPublisher.NOTIFICATION_ID;

public class TeesFragment extends Fragment implements TeesAdapter.onItemClickListener{



    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private TeesAdapter adapter;
    private List<Tees> data_list;
    public static final String key = "PASS_DATA";
    int sizeidselect;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";
    Spinner spinner;
    String quan,size;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tees, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        prefs = this.getActivity().getSharedPreferences(myprefs, Context.MODE_PRIVATE);


        recyclerView = getView().findViewById(R.id.tees_recycler_view);
        data_list = new ArrayList<>();

        get_data(0,"");

        gridCreate(1);



    }

    public void gridCreate(int span)
    {
        gridLayoutManager = new GridLayoutManager(getActivity(),span);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new TeesAdapter(getActivity(),data_list);
        recyclerView.setAdapter(adapter);

        adapter.setOnClick(TeesFragment.this);

    }




    private void get_data(final int x, final String id) {

        @SuppressLint("StaticFieldLeak") final AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... integers) {

                OkHttpClient client = new OkHttpClient();
                if (x==0) {
                    Request request = new Request.Builder().url(server.server_ip()+"AppAllTees.php").build();
                    //Log.d("..Tees Link",request.toString());
                    try {
                        Response response = client.newCall(request).execute();
                        JSONArray array = new JSONArray(response.body().string());


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            Tees data = new Tees(object.getString("id"), object.getString("name"), object.getString("front"),
                                    object.getString("back"), object.getString("side"), object.getString("description"), object.getString("price"));

                            data_list.add(data);

                            //Log.d("......times....", Integer.toString(i));
                        }
                        //Log.d("Tees List  ", array.toString());

                    } catch (Exception e) {

                        System.out.print("Error in Tees");
                    }
                }else {
                    String userx = prefs.getString("username", "");
                    Request request = new Request.Builder().url(server.server_ip()+"AppTeesRegister.php?tid="+id+"&uid="+userx.trim()
                            +"&size="+size+"&quan="+quan).build();
                    //Log.d("...tees Book :",request.toString());
                    try {
                        Response response = client.newCall(request).execute();
                        String result =response.body().string();

                        return result.trim();
                    } catch (Exception e)
                    {
                        System.out.print(e.toString());
                    }
                }

                return "0";
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.equals("0"))
                    adapter.notifyDataSetChanged();
                else if (result.equals("success"))
                {
                    scheduleNotification(getNotification("Booking Successful for Tees","Tees Booked"), 0,0);
                    Toast.makeText(getContext(),"Tees Booking Succesfull !",Toast.LENGTH_LONG).show();

                }
                else
                    Toast.makeText(getContext(),"Tees Booking UnSuccesfull !",Toast.LENGTH_LONG).show();
                //Log.d("..tees","comlete");

            }
        };

        task.execute();
    }

    @Override
    public void onclick(final int position) {

        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.teesbookingdialog, null);
        alertDialog.setContentView(R.layout.teesbookingdialog);
        Button payLater;
        ImageView reduce,add,teeimg;
        final TextView name, quantity, amount, cancel;
        String[] sizetees = {"S","M","L","XL","XXL"};
        spinner = alertDialog.findViewById(R.id.teessizespin);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,sizetees));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeidselect = spinner.getSelectedItemPosition()+1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sizeidselect = 1;
            }
        });
        name= alertDialog.findViewById(R.id.teename);
        quantity =  alertDialog.findViewById(R.id.diateequantity);
        amount=  alertDialog.findViewById(R.id.diateeprice);
        teeimg= alertDialog.findViewById(R.id.teesbookimg);
        payLater =  alertDialog.findViewById(R.id.btnbooktee);
        cancel =  alertDialog.findViewById(R.id.teebookclose);
        reduce =  alertDialog.findViewById(R.id.minusbut);
        add =  alertDialog.findViewById(R.id.plusbut);
        name.setText(data_list.get(position).getName());
        //Log.d("...Price::",data_list.get(position).getPrice());
        amount.setText(data_list.get(position).getPrice().trim());
        //Log.d("...Price::",amount.getText().toString());
        String F=server.server_ip()+"shirt/upload/"+data_list.get(position).getFront();


        LoadImg objF=new LoadImg(getContext(),teeimg,null,1);
        objF.setUrl(F);
        objF.setImage();
        alertDialog.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(quantity.getText().toString());
                quantity.setText(Integer.toString(n+1));
            }
        });
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(quantity.getText().toString());
                if(n>1)
                quantity.setText(Integer.toString(n-1));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        payLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                quan=quantity.getText().toString();
                size=Integer.toString(sizeidselect);
                get_data(1,data_list.get(position).getId().trim());
                alertDialog.dismiss();
                Toast.makeText(getContext(),"Processing, Please Wait..", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void imgClick(int p) {
        final Dialog nagDialog = new Dialog(getActivity(),R.style.theme_tees_dialog);
        nagDialog.setCancelable(false);
        nagDialog.setCanceledOnTouchOutside(true);
        nagDialog.setContentView(R.layout.preview_image);
        nagDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button close=nagDialog.findViewById(R.id.btnIvClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nagDialog.dismiss();
            }
        });
        ImageView imgF,imgS,imgB;
        imgF= nagDialog.findViewById(R.id.teeimgF);
        imgB= nagDialog.findViewById(R.id.teeimgB);
        imgS= nagDialog.findViewById(R.id.teeimgS);

        String F,S,B;
        F=server.server_ip()+"shirt/upload/"+data_list.get(p).getFront();
        S=server.server_ip()+"shirt/upload/"+data_list.get(p).getSide();
        B=server.server_ip()+"shirt/upload/"+data_list.get(p).getBack();

        LoadImg objF=new LoadImg(getContext(),imgF,null,1);
        objF.setUrl(F);
        objF.setImage();

        LoadImg objS=new LoadImg(getContext(), imgS,null,1);
        objS.setUrl(S);
        objS.setImage();

        LoadImg objB=new LoadImg(getContext(),imgB,null,1);
        objB.setUrl(B);
        objB.setImage();

        nagDialog.show();
    }

    public interface OnFragmentInteractionListener {
    }

    private void scheduleNotification(Notification notification, long delay, int num) {
        //Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, num);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), num, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content,String title) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getActivity());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setContentTitle(title);
        builder.setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 });
        builder.setLights(Color.GREEN, 3000, 3000);
        builder.setContentText(content);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setContentIntent(pi);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }

}
