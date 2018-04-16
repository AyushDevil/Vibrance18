package com.vit.ayush.vibrance18.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.LoginActivity;
import com.vit.ayush.vibrance18.activity.server;
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

public class ProfileFragment extends Fragment{


    private RecyclerView R_e_recyclerView;
    private GridLayoutManager R_e_gridLayoutManager;
    private RegisteredEventAdapter R_e_adapter;
    private RecyclerView R_t_recyclerView;
    private GridLayoutManager R_t_gridLayoutManager;
    private RegisteredTeesAdapter R_t_adapter;

    TextView name,eventNT,profileT,teesT;
    TextView email;
    TextView phn;
    TextView eventscount;
    TextView teescount;
    Button logout;
    CardView cardView;
    SharedPreferences prefs;

    private List<RegisteredEventClass> data_Reg_E;
    private List<RegisteredTeesClass> data_Reg_T;
    public  String user;
    public  String mail;
    public  String phone;
    public  int countevents;
    public  int counttees;
    LinearLayout eventN,teesN,profile,userdet,eventdet,teesdet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();



        eventNT=getActivity().findViewById(R.id.eventN_txt);
        profileT=getActivity().findViewById(R.id.profile_txt);
        teesT=getActivity().findViewById(R.id.teesN_txt);

        eventN=getActivity().findViewById(R.id.eventNum);
        teesN=getActivity().findViewById(R.id.teesNum);
        profile=getActivity().findViewById(R.id.profileNum);
        userdet=getActivity().findViewById(R.id.userDetails);
        eventdet=getActivity().findViewById(R.id.userEventDetails);
        teesdet=getActivity().findViewById(R.id.userTeesDetails);

        email=getView().findViewById(R.id.dispmail);
        teescount=getView().findViewById(R.id.teescount);
        phn=getView().findViewById(R.id.dispphn);
        logout = getView().findViewById(R.id.logoutprofile);
        eventscount=getView().findViewById(R.id.eventscount);
        cardView=getView().findViewById(R.id.cardViewprofile);
        prefs = this.getActivity().getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        name=getView().findViewById(R.id.dispuser);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().clear().commit();
                Intent launchNextActivity;
                launchNextActivity = new Intent(getActivity(), LoginActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getActivity(), "You have been logged out", Toast.LENGTH_LONG).show();
                startActivity(launchNextActivity);
            }
        });

        data_Reg_E=new ArrayList<RegisteredEventClass>();
        data_Reg_T=new ArrayList<RegisteredTeesClass>();
        get_data(prefs.getString("username",""));

        R_e_gridLayoutManager = new GridLayoutManager(getActivity(),1);
        R_t_gridLayoutManager = new GridLayoutManager(getActivity(),1);

        R_e_recyclerView = getView().findViewById(R.id.EventsRegisteredRecycler);
        R_e_recyclerView.setLayoutManager(R_e_gridLayoutManager);
        R_e_recyclerView.setHasFixedSize(true);
        R_e_adapter = new RegisteredEventAdapter(getActivity(),data_Reg_E);

        R_t_recyclerView = getView().findViewById(R.id.TeesBookedRecycler);
        R_t_recyclerView.setLayoutManager(R_t_gridLayoutManager);
        R_t_recyclerView.setHasFixedSize(true);
        R_t_adapter = new RegisteredTeesAdapter(getActivity(),data_Reg_T);

        R_e_recyclerView.destroyDrawingCache();
        R_t_recyclerView.destroyDrawingCache();

        eventN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setGoneFrame();
                eventN.setBackgroundDrawable(getResources().getDrawable(R.drawable.live_tab));
                eventNT.setTextColor(getResources().getColor(R.color.font_card_col));
                R_e_recyclerView.setAdapter(R_e_adapter);
                eventdet.setVisibility(View.VISIBLE);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGoneFrame();
                profile.setBackgroundDrawable(getResources().getDrawable(R.drawable.live_tab));
                profileT.setTextColor(getResources().getColor(R.color.font_card_col));
                logout.setVisibility(View.VISIBLE);
                userdet.setVisibility(View.VISIBLE);
            }
        });
        teesN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGoneFrame();
                teesN.setBackgroundDrawable(getResources().getDrawable(R.drawable.live_tab));
                teesT.setTextColor(getResources().getColor(R.color.font_card_col));
                R_t_recyclerView.setAdapter(R_t_adapter);
                teesdet.setVisibility(View.VISIBLE);
            }
        });

    }


    private void setGoneFrame()
    {
        logout.setVisibility(View.GONE);
        eventN.setBackgroundColor(getResources().getColor(R.color.card_back));
        profile.setBackgroundColor(getResources().getColor(R.color.card_back));
        teesN.setBackgroundColor(getResources().getColor(R.color.card_back));
        profileT.setTextColor(getResources().getColor(R.color.txt_default));
        teesT.setTextColor(getResources().getColor(R.color.txt_default));
        eventNT.setTextColor(getResources().getColor(R.color.txt_default));
        userdet.setVisibility(View.GONE);
        eventdet.setVisibility(View.GONE);
        teesdet.setVisibility(View.GONE);
    }





    private void get_data(final String id) {

        @SuppressLint("StaticFieldLeak") AsyncTask<String,Void,Void> task = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(server.server_ip() + "Appdetails.php?id=" + id).build();
                Request request2 = new Request.Builder().url(server.server_ip() + "Appdetailstee.php?id=" + id).build();

                try {
                    Response response = client.newCall(request).execute();
                    Response response2 = client.newCall(request2).execute();
                    JSONArray array = new JSONArray(response.body().string());
                    JSONArray arraytee = new JSONArray(response2.body().string());
                    countevents=array.length()-1;
                    counttees = arraytee.length();

                    int i=0;
                    for ( i = 0; i < 1; i++) {
                        JSONObject object = array.getJSONObject(i);

                        user= object.getString("name");
                        mail = object.getString("email");
                        phone = object.getString("phno");
                        //Log.d("id", Integer.toString(array.length()));
                        //Log.d("My List  ", array.toString());
                        //Log.d("......times....", Integer.toString(i));
                    }
                    for (i=1;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        //Log.d("....check data..",object.getString("idevent"));
                        RegisteredEventClass data = new RegisteredEventClass(object.getString("eventname"),object.getString("venue"),
                                object.getString("eventdate"),object.getString("eventtime"),"0");

                        data_Reg_E.add(data);
                    }
                    //Log.d("My Registered E List  ", Arrays.asList(data_Reg_E).toString());

                    for (i=0;i<arraytee.length();i++)
                    {
                        JSONObject object = arraytee.getJSONObject(i);
                        //Log.d("....check data..",object.getString("idevent"));
                        RegisteredTeesClass data = new RegisteredTeesClass(object.getString("name"),object.getString("front"),
                                object.getString("size"),object.getString("qty"),object.getString("status"));

                        data_Reg_T.add(data);
                    }
                    //Log.d("My Registered t List  ", arraytee.toString());



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    System.out.print("End of Contents");
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                R_e_adapter.notifyDataSetChanged();
                R_t_adapter.notifyDataSetChanged();
                name.setText(user);
                email.setText(mail);
                phn.setText(phone);
                eventscount.setText(Integer.toString(countevents));
                teescount.setText(Integer.toString(counttees));
                super.onPostExecute(aVoid);
            }
        };
        task.execute(id);

    }


}
