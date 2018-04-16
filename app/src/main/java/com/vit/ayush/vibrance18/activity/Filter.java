package com.vit.ayush.vibrance18.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.other.EventAdapter;
import com.vit.ayush.vibrance18.other.Events;
import com.vit.ayush.vibrance18.other.LoadImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Filter extends AppCompatActivity {

    String search,id;
    List<Events> event_data;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        prefs = this.getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        editor = prefs.edit();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        search = intent.getStringExtra("search");
    }}
    /*



        if (id.isEmpty()) {
            Log.d("...search ", search);
            set_data(0,0);

        }
        if (search.isEmpty()) {
            Log.d("...mesage ", id);

            set_data(1,0);
        }


    }


    public void gridCreate(int span) {
        gridLayoutManager = new GridLayoutManager(getActivity(), span);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new EventAdapter(getActivity(), data_list);
        recyclerView.setAdapter(adapter);


        adapter.setOnClick(new EventAdapter.onItemClickListener() {
            @Override
            public void onclick(int position) throws ParseException {
                final Events clickedItem = data_list.get(position);
                //Toast.makeText(getContext(),"Event - " + clickedItem.getEventname(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View view = factory.inflate(R.layout.eventcarddetail, null);
                alertDialog.setView(view);
                //showing using animations -
                l1 = view.findViewById(R.id.tentlayout);
                l2 = view.findViewById(R.id.upcominglayout);
                l3 = view.findViewById(R.id.event_details);
                l4 = view.findViewById(R.id.success);
                img = view.findViewById(R.id.eventImg);
                final TextView name, desc, category, venue, date, amount, team;
                final Button payLater, register;
                name = view.findViewById(R.id.eve_name);
                desc = view.findViewById(R.id.eve_desc);
                team = view.findViewById(R.id.eve_team);
                venue = view.findViewById(R.id.eve_venue);
                category = view.findViewById(R.id.eve_category);
                amount = view.findViewById(R.id.eve_amount);
                date = view.findViewById(R.id.eve_date);
                payLater = view.findViewById(R.id.payLater);

                name.setText(clickedItem.getEventname());
                desc.setText(clickedItem.getEventdesc());
                team.setText(clickedItem.getTeamsize());
                venue.setText(clickedItem.getVenue());
                category.setText(club_data.get(clickedItem.getClubid()));
                amount.setText(clickedItem.getFee());
                date.setText(clickedItem.getEventdate());

                if (clickedItem.getStatus().equals("0")) {
                    payLater.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            //                        Intent i = new Intent(getActivity(), Registration.class);
                            //                        startActivity(i);
                            final StringBuffer user_id = new StringBuffer();

                            final List<EditText> edid = new ArrayList<>();

                            final int no = Integer.parseInt(team.getText().toString());
                            if (no > 1) {
                                lay = new LinearLayout(view.getContext());
                                lay.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                                lay.setOrientation(LinearLayout.VERTICAL);
                                for (int j = 1; j < no; j++) {
                                    EditText edttext = new EditText(getContext());
                                    //String temp_id="participant"+(j+1);
                                    edid.add(edttext);
                                    edttext.setId(j);
                                    edttext.setHint("Participant " + (j + 1) + " id");
                                    edttext.setLayoutParams(lay.getLayoutParams());
                                    edttext.getBackground().setColorFilter(getResources().getColor(R.color.dot_dark_screen1), PorterDuff.Mode.SRC_ATOP);
                                    lay.addView(edttext);
                                }
                            }
                            eventId = Integer.parseInt(clickedItem.getIdevent());
                            if (no == 1) {
                                l3.setVisibility(View.GONE);
                                l4.setVisibility(View.VISIBLE);

                                String userx = prefs.getString("username", "");
                                user_id.append(userx);
                                get_data(eventId, 1, user_id);

                            } else if (no > 1) {
                                final AlertDialog.Builder teams = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                                teams.setView(lay);
                                teams.setTitle("Team Members");
                                teams.setCancelable(true);
                                teams.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.d("...submission", "....ccdsc");

                                    }
                                });
                                teams.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Registration Cancelled ", Toast.LENGTH_LONG).show();
                                        Log.d(".. Register Cancel ", "csa");

                                    }
                                });
                                b = teams.create();
                                b.show();

                                b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        l3.setVisibility(View.GONE);

                                        int c = 0;
                                        for (int i = 0; i < edid.size(); i++) {
                                            if (!(edid.get(i).getText().toString().equals(""))) {
                                                c++;
                                                user_id.append(edid.get(i).getText().toString().trim() + ",");
                                            }
                                        }
                                        if (c == Integer.parseInt(clickedItem.getTeamsize()) - 1) {
                                            Log.d("..users reg ", user_id.toString());
                                            //Toast.makeText(getContext(), "ITS ok", Toast.LENGTH_LONG).show();
                                            b.dismiss();
                                            String userx = prefs.getString("username", "");
                                            user_id.append(userx);
                                            get_data(eventId, 1, user_id);


                                        } else {
                                            Toast.makeText(getContext(), "Please Fill All Fields ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }


                        }
                    });
                }
                else
                {
                    payLater.setText("You Missed Your Chance !");
                    payLater.setClickable(false);
                }

                desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog);
                        alertDialog1.setTitle("Event Description");
                        alertDialog1.setMessage(desc.getText().toString());
                        alertDialog1.setCancelable(false);
                        final AlertDialog a = alertDialog1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();

                        a.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                a.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.dot_dark_screen2));
                            }
                        });
                        a.show();
                    }
                });


                final Animation downtotop = AnimationUtils.loadAnimation(getActivity(), R.anim.downtoup);
                final Animation uptodown = AnimationUtils.loadAnimation(getActivity(), R.anim.uptodown);
                final Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
                final Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);

                downtotop.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        l2.setAnimation(slideUp);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                uptodown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        l1.setAnimation(slideDown);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {


                        Log.d("...Before asynk", "asc");
                        LoadImg obj = new LoadImg(getActivity().getApplicationContext(), img, clickedItem.getIdevent().trim(), 0);
                        obj.execute();

                        l2.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        startAnim();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                slideDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        l1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.VISIBLE);
                l2.setAnimation(downtotop);
                l1.setAnimation(uptodown);
                alert = alertDialog.create();
                //alert.getWindow().setLayout(1000, 900);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.show();
                alert.getWindow().setLayout(1000, 900);
                //alertDialog.show().getWindow().setLayout(1000, 900); //Controlling width and height
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    get_data(data_list.get(data_list.size() - 1).getSln() + 1, 0, null);
                }
            }
        });
    }


    private void set_data(int type,final int x)
    {
        final int id=800;
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Integer> task = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();
                if (x == 0) {
                    Request request = new Request.Builder().url(server.server_ip() + "AppAllEvent.php?id=" + id).build();

                    try {
                        Response response = client.newCall(request).execute();
                        JSONArray array = new JSONArray(response.body().string());
                        int s = id;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            Events data = new Events(s++, object.getString("idevent"), object.getString("eventname"), object.getString("eventdesc"),
                                    object.getString("eventtime"), object.getString("eventdate"), object.getString("venue"), object.getString("fee"),
                                    object.getString("schoolid"), object.getString("clubid"), object.getString("teamsize"), object.getString("istrending")
                                    ,object.getString("status"));

                            event_data.add(data);

                            Log.d("......times....", Integer.toString(i));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {

                        System.out.print("End of Contents");
                    }
                } else if (x == 1) {

                    //user_id.append(")");
                    //String x=user_id.toString();
                    String u = "";
                    try {
                        u = URLEncoder.encode(user_id.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String senddata = "pid=" + user_id.toString();
                    //Log.d(".....url..|||---",server.server_ip()+"AppRegisterEvent.php?eventid="+id+"&pid=" + user_id.toString());
                    Request request = new Request.Builder().url(server.server_ip()+"AppRegisterEvent.php?eventid=" + id + "&pid=" + u).build();
                    Log.d("sada....", request.toString());

                    try {
                        Response response = client.newCall(request).execute();
                        msg = response.body().string().trim();
                        Log.d(".....DATA||",msg);
                        if (msg.equals("1")) {

                            return 1;
                        }else if(msg.equals("-1")){
                            return -1;
                        }
                        else {
                            return 0;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return 0;
            }



            @Override
            protected void onPostExecute(Integer i) {
                adapter.notifyDataSetChanged();

                check = i;
                if (check == 1 && x == 1) {

                    Toast.makeText(getContext(), "Registered Successfully ", Toast.LENGTH_LONG).show();
                    scheduleNotification(getNotification("Registration Successfull for event","Event Registered"), 0,0);

                    l4.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            alert.dismiss();
                        }
                    }, 2000);
                } else if (check == -1 && x == 1) {
                    alert.dismiss();
                    Toast.makeText(getContext(), "Already Registered, Check profile ", Toast.LENGTH_LONG).show();
                }
                else if(check==0 && x==1) {
                    alert.dismiss();
                    Toast.makeText(getContext(), "Please try again ", Toast.LENGTH_LONG).show();
                }
            }
        };

        task.execute(id);
    }


}*/
