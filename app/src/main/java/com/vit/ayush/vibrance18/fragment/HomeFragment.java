package com.vit.ayush.vibrance18.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.MainActivity;
import com.vit.ayush.vibrance18.activity.Payment_Activity;
import com.vit.ayush.vibrance18.activity.ProfileActivity;
import com.vit.ayush.vibrance18.activity.server;
import com.vit.ayush.vibrance18.other.Events;
import com.vit.ayush.vibrance18.other.HomeAdapter;
import com.vit.ayush.vibrance18.other.ImageAdapter;
import com.vit.ayush.vibrance18.other.LoadImg;
import com.vit.ayush.vibrance18.other.NotificationPublisher;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.droidsonroids.gif.GifTextView;

import static com.vit.ayush.vibrance18.other.NotificationPublisher.NOTIFICATION_ID;

public class HomeFragment extends Fragment implements ImageAdapter.onItemClickListener {


    ImageAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    HashMap<String, String> club_data;
    long timesystem,tmevent,difftime,date;
    String datetime,datestring;
    int i;
    Dialog updatedialog;
    GifTextView updatecheckwait;



    List<String> mResources;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private HomeAdapter adapter;
    private List<Events> data_list;
    public static final String key = "PASS_DATA";
    String msg;



    private Boolean mShowingBack;
    private LinearLayout l1, l2, l3, l4, l5,lay;
    private AlertDialog alert, b;
    private int eventId, check;
    View space;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";
    ImageView img;
    SimpleDateFormat sdf,sdf1;
    Date dt,dt1;
    String currentVersion;
    TextView txtclose;
    Button updateapp;


    Timer imagetimer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the intro_frag3 for this fragment
        //Log.d("..........open", ",,msg,,,");
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        prefs = this.getActivity().getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        mResources = new ArrayList<>();
        updatecheckwait = getView().findViewById(R.id.updatecheckwait);
        Toast.makeText(getContext(),"Dont forget to give feedback for the Events !  Goto Feedback in the Navigation Menu",Toast.LENGTH_LONG).show();
        updatedialog = new Dialog(getActivity());
        updatedialog.setContentView(R.layout.update_dialog);
        txtclose = updatedialog.findViewById(R.id.txtclose);
        updateapp = updatedialog.findViewById(R.id.btnupdate);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNextActivity;
                launchNextActivity = new Intent(getActivity(), MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(launchNextActivity);
            }
        });
        updateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.vit.ayush.vibrance18"));
                startActivity(intent);
            }
        });
        try {
            currentVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            check_update();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        date=System.currentTimeMillis();
        sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        sdf1 = new SimpleDateFormat("yyyy-mm-dd hh:mm a");
        datestring = sdf.format(date);
        try {
            dt = sdf.parse(datestring);
            timesystem = dt.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        club_data = new HashMap<String, String>();
        getClub();
        get_data(prefs.getString("username",""));


        recyclerView = getView().findViewById(R.id.home_recycler);
        data_list = new ArrayList<>();


        editor = prefs.edit();
        editor.putString("events", "0");
        // countEvent = Integer.parseInt(prefs.getString("events","0"));
        get_data(0, 0, null);
        asynk_proshow();

        gridCreate(1);


    }
    private void check_update(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, String, String> checkupdate = new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {
                updatecheckwait.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String newVersion = null;
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getContext().getPackageName() + "&hl=it")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select("div[itemprop=softwareVersion]")
                            .first()
                            .ownText();
                    return newVersion;
                } catch (Exception e) {
                    return newVersion;
                }
            }

            @Override
            protected void onPostExecute(String onlineVersion) {
                updatecheckwait.setVisibility(View.GONE);
                super.onPostExecute(onlineVersion);
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (currentVersion.compareToIgnoreCase(onlineVersion)<0) {
                        updatedialog.show();
                    }
                    else{
                        updatedialog.dismiss();
                    }
                }
            }
        };
        checkupdate.execute();

    }

    private void asynk_proshow() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> getProshow = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final OkHttpClient Pclient = new OkHttpClient();
                Request proshow = new Request.Builder().url(server.server_ip() + "AppProshow.php").build();
               // Log.d("...prourl", proshow.toString());

                try {
                    Response pro = Pclient.newCall(proshow).execute();
                    JSONArray proArray = new JSONArray(pro.body().string());
                    JSONObject obj = proArray.getJSONObject(0);
                    for (int k = 0; k < obj.length(); k++) {
                        mResources.add(obj.getString(Integer.toString(k + 1)));
                       // Log.d("...prodata", obj.getString(Integer.toString(k + 1)));
                    }
                    //Log.d("....proshow...", mResources.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                proshow();

                if(mCustomPagerAdapter!=null)
                    mCustomPagerAdapter.notifyDataSetChanged();

            }
        };

        getProshow.execute();
    }

    private void proshow() {
        if ((mResources.size() > 0) && (!(getActivity() == null))) {
            mCustomPagerAdapter = new ImageAdapter(getContext(), mResources);
            mCustomPagerAdapter.setOnClick(this);

            mViewPager = getView().findViewById(R.id.pager);
            mViewPager.setAdapter(mCustomPagerAdapter);

            //Timer

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mViewPager.post(new Runnable() {

                        @Override
                        public void run() {
                            if (mResources.size() != 0)
                                mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % mResources.size());
                        }
                    });
                }
            };
            imagetimer = new Timer();
            imagetimer.schedule(timerTask, 3000, 3000);

        }
    }

    private void getClub() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Request requestc = new Request.Builder().url(server.server_ip() + "AppClub.php").build();
               // Log.d("....webLink...", requestc.toString());
                try {
                    Response response = client.newCall(requestc).execute();
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        //datatt=object.getString("clubname");
                        club_data.put(object.getString("idclub"), object.getString("clubname"));

                    }
                  //  Log.d("...Club Data", Arrays.asList(club_data).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        };
        task.execute();
    }


    public void gridCreate(int span) {
        gridLayoutManager = new GridLayoutManager(getActivity(), span, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new HomeAdapter(getActivity(), data_list);
        recyclerView.setAdapter(adapter);

        //adapter.setOnClick(HomeFrl4agment.this);


        adapter.setOnClick(new HomeAdapter.onItemClickListener() {
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
                l5 = view.findViewById(R.id.waitprocess);
                img = view.findViewById(R.id.eventImg);
                final TextView name, desc, category, venue, date, amount, team,cancel;
                final Button payLater, payNow;
                name = view.findViewById(R.id.eve_name);
                desc = view.findViewById(R.id.eve_desc);
                team = view.findViewById(R.id.eve_team);
                venue = view.findViewById(R.id.eve_venue);
                category = view.findViewById(R.id.eve_category);
                amount = view.findViewById(R.id.eve_amount);
                date = view.findViewById(R.id.eve_date);
                payLater = view.findViewById(R.id.payLater);
                payNow = view.findViewById(R.id.payNow);
                space=view.findViewById(R.id.space_view);
                cancel = view.findViewById(R.id.cancel);
                name.setText(clickedItem.getEventname());


                desc.setText(Html.fromHtml(clickedItem.getEventdesc()));

                team.setText(clickedItem.getTeamsize());
                venue.setText(clickedItem.getVenue());


                //System.out.print("caskjcbsakjcsakjc skjc");
                category.setText(club_data.get(clickedItem.getClubid()));
                amount.setText(clickedItem.getFee());
                date.setText(clickedItem.getEventdate());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                if (clickedItem.getStatus().equals("0") || clickedItem.getStatus().equals("10")) {

                    if (clickedItem.getStatus().equals("10")) {
                        payNow.setVisibility(View.GONE);
                        space.setVisibility(View.GONE);
                    }

                    payLater.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {

                            final StringBuffer user_id = new StringBuffer();
                            final List<EditText> edid = new ArrayList<>();

                            final int no = Integer.parseInt(team.getText().toString());
                            if (no > 1) {
                                lay = new LinearLayout(view.getContext());
                                lay.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                                lay.setOrientation(LinearLayout.VERTICAL);
                                for (int j = 1; j < no; j++) {
                                    EditText edttext = new EditText(getContext());
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
                                //create a confirmation dialog-
                                final AlertDialog.Builder confirm = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog);
                                confirm.setTitle("Confirmation");
                                confirm.setMessage("Are you sure to register for this event?");
                                confirm.setCancelable(false);
                                final AlertDialog a = confirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        l3.setVisibility(View.GONE);
                                        l5.setVisibility(View.VISIBLE);

                                        String userx = prefs.getString("username", "");
                                        user_id.append(userx);
                                        get_data(eventId, 1, user_id);

                                        //Toast.makeText(getContext(), "YES clicked for 1 member", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Registration Cancelled", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                }).create();
                                a.show();

                            } else if (no > 1) {
                                final AlertDialog.Builder teams = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                                teams.setView(lay);
                                teams.setTitle("Team Members");
                                teams.setCancelable(false);
                                teams.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Log.d("...submission", "....ccdsc");
                                    }
                                });
                                teams.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Registration Cancelled ", Toast.LENGTH_SHORT).show();
                                       // Log.d(".. Register Cancel ", "csa");
                                    }
                                });
                                b = teams.create();
                                b.show();

                                b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int c = 0;
                                        for (int i = 0; i < edid.size(); i++) {
                                            if (!(edid.get(i).getText().toString().equals(""))) {
                                                c++;
                                                user_id.append(edid.get(i).getText().toString().trim() + ",");
                                            }
                                        }
                                        if (c == Integer.parseInt(clickedItem.getTeamsize()) - 1) {
                                                /*Log.d("..users reg ", user_id.toString());*/
                                            //Toast.makeText(getContext(), "userids " + user_id, Toast.LENGTH_SHORT).show();
                                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputManager.hideSoftInputFromWindow(lay.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                            //create a confirmation dialog-
                                            final AlertDialog.Builder confirm = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog);
                                            confirm.setTitle("Confirmation");
                                            confirm.setMessage("Are you sure to register for this event?");
                                            confirm.setCancelable(false);
                                            final AlertDialog a = confirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    l3.setVisibility(View.GONE);
                                                    l5.setVisibility(View.VISIBLE);


                                                    String userx = prefs.getString("username", "");
                                                    user_id.append(userx);
                                                    get_data(eventId, 1, user_id);

                                                    //Toast.makeText(getContext(), "YES clicked for team members", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    b.dismiss();
                                                }
                                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(getContext(), "Registration Cancelled", Toast.LENGTH_SHORT).show();
                                                    b.dismiss();
                                                    dialogInterface.dismiss();
                                                }
                                            }).create();
                                            a.show();
                                        } else {
                                            Toast.makeText(getContext(), "Please Fill All Fields ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    payNow.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            final StringBuffer user_id = new StringBuffer();

                            final List<EditText> edid = new ArrayList<>();

                            final int no = Integer.parseInt(team.getText().toString());
                            eventId = Integer.parseInt(clickedItem.getIdevent());
                            //if (no == 1) {
                            //create a confirmation dialog-
                            final AlertDialog.Builder confirm = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog);
                            confirm.setTitle("Confirmation");
                            final String[] items = {"I accept Terms and Conditions"};
                            final int[] i = {0};
                            confirm.setMultiChoiceItems(items, null,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int selectedItemId, boolean isSelected) {
                                            if (isSelected) {
                                                i[0] = 1;
                                            } else {
                                                i[0] = -1;
                                            }
                                        }
                                    });

                            confirm.setCancelable(false);
                            final AlertDialog a = confirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), "Registration Cancelled", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            }).setNeutralButton("Read T & C", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create();
                            a.show();
                            a.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Boolean wantToCloseDialog = false;
                                    if (i[0] == 1) {
                                        l3.setVisibility(View.GONE);


                                        //Toast.makeText(getContext(), "YES clicked for 1 member", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), Payment_Activity.class);
                                        intent.putExtra("EventId", clickedItem.getIdevent());
                                        startActivity(intent);
                                        wantToCloseDialog = true;
                                    } else
                                        Toast.makeText(getContext(), "Please select the checkbox to proceed", Toast.LENGTH_SHORT).show();
                                    if (wantToCloseDialog)
                                        a.dismiss();
                                }
                            });
                            a.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Boolean wantToCloseDialog = false;
                                    final AlertDialog.Builder terms = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
                                    terms.setTitle("Terms and Conditions");
                                    //Terms and condition webpage
                                    WebView wv = new WebView(getContext());
                                    wv.setVerticalScrollBarEnabled(true);
                                    wv.setHorizontalScrollBarEnabled(false);
                                    wv.loadUrl("file:///android_asset/TandC.html");
                                    terms.setView(wv);
                                    terms.setCancelable(false);
                                    final AlertDialog tcalert = terms.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    }).create();
                                    tcalert.show();

                                    if (wantToCloseDialog)
                                        a.dismiss();
                                }
                            });
                        }
                    });
                }  else
                {
                    payNow.setText("You Missed Your Chance !");
                    payNow.setClickable(false);
                    space.setVisibility(View.GONE);
                    payNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    payLater.setVisibility(View.GONE);
                }



                desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog);
                        alertDialog1.setTitle("Event Description");
                        alertDialog1.setMessage(Html.fromHtml(desc.getText().toString()));
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


                        //Log.d("...Before asynk", "asc");
                        Picasso.with(getContext()).load(R.drawable.loading_ani).placeholder(R.drawable.loading_ani).resize(50, 50).into(img);
                        LoadImg obj = new LoadImg(getActivity().getApplicationContext(), img, clickedItem.getIdevent().trim(), 0);
                        obj.setImage();

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
                alertDialog.setCancelable(false);
                alert = alertDialog.create();
                alert.getWindow().setLayout(1000, 900);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.show();
                //alertDialog.show().getWindow().setLayout(1000, 900); //Controlling width and height
            }
        });


    }


    private void get_data(final int id, final int x, final StringBuffer user_id) {
        System.out.print("da");



        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Integer> task = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {

                final OkHttpClient client = new OkHttpClient();
                if (x == 0) {
                    Request request = new Request.Builder().url(server.server_ip() + "AppAllTrendingEvents.php").build();


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

                            data_list.add(data);
                           // Log.d("id", Integer.toString(array.length()));
                           // Log.d("My List Event  ", array.toString());
                            //Log.d("length ...",Integer.toString(data_club.size()));
                           // Log.d("......times....", Integer.toString(i));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {

                        System.out.print("End of Contents");
                    }
                } else if (x == 1) {
                    String u = "";
                    try {
                        u = URLEncoder.encode(user_id.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String senddata = "pid=" + user_id.toString();
                    //Log.d(".....url..|||---",server.server_ip()+"AppRegisterEvent.php?eventid="+id+"&pid=" + user_id.toString());
                    Request request = new Request.Builder().url(server.server_ip() + "AppRegisterEvent.php?eventid=" + id + "&pid=" + u).build();
                   // Log.d("sada....", request.toString());

                    try {
                        Response response = client.newCall(request).execute();
                        msg = response.body().string();
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
                    Toast.makeText(getContext(),"Dont forget to give feedback for the events you attended in the Feedback menu in Navigation bar",Toast.LENGTH_SHORT).show();
                    scheduleNotification(getNotification("Registration Successfull for event","Event Registered"), 0,0);
                    l5.setVisibility(View.GONE);
                    l4.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            alert.dismiss();
                        }
                    }, 4000);
                } else if (check == -1 && x == 1) {
                    alert.dismiss();
                    Toast.makeText(getContext(), "Already Registered, Check profile ", Toast.LENGTH_LONG).show();
                }
                else if (check == 0 && x == 1) {
                    alert.dismiss();
                    Toast.makeText(getContext(), "Please try again ", Toast.LENGTH_LONG).show();
                }
            }
        };

        task.execute(id);
    }


    @Override
    public void onImageclick(int position) {
        Toast.makeText(getActivity(), "Open Proshows from Navigation Bar to register", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
    }

    private void startAnim() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img.animate()
                        .translationY(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                img.setVisibility(View.GONE);
                                l3.setVisibility(View.VISIBLE);
                                l3.animate().setDuration(800)
                                        .translationY(0)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                            }
                                        });
                            }
                        });
            }
        }, 3000);

    }

    private void scheduleNotification(Notification notification, long delay,int num) {
        //Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, num);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), num, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       // Log.d(".......Id:::",Integer.toString(num));
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
    private void get_data(final String id) {

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(server.server_ip() + "Appdetails.php?id=" + id).build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());



                    i = 0;

                    for (i=1;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        //Log.d("....check data..",object.getString("idevent"));
                       /* RegisteredEventClass data = new RegisteredEventClass(object.getString("eventname"),object.getString("venue"),
                                object.getString("eventdate"),object.getString("eventtime"),"0");

                       // data_Reg_E.add(data);*/
                        datetime = object.getString("eventdate") +" "+ object.getString("eventtime");
                        dt1 = sdf1.parse(datetime);
                        tmevent = dt1.getTime();
                        tmevent =tmevent-3600000;
                        difftime=tmevent-timesystem;
                        if(difftime>0)
                            scheduleNotification(getNotification(object.getString("eventname")+"  Venue: "+object.getString("venue")+"  Time: "+datetime,"Upcoming Event"), difftime,i);


                    }



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

            }
        };
        task.execute(id);
    }
}


