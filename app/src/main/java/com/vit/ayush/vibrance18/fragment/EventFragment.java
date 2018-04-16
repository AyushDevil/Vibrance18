package com.vit.ayush.vibrance18.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.Payment_Activity;
import com.vit.ayush.vibrance18.activity.ProfileActivity;
import com.vit.ayush.vibrance18.activity.server;
import com.vit.ayush.vibrance18.other.EventAdapter;
import com.vit.ayush.vibrance18.other.Events;
import com.vit.ayush.vibrance18.other.LoadImg;
import com.vit.ayush.vibrance18.other.NotificationPublisher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;

import static com.vit.ayush.vibrance18.other.NotificationPublisher.NOTIFICATION_ID;

public class EventFragment extends Fragment implements EventAdapter.onItemClickListener
{
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private EventAdapter adapter;
    private List<Events> data_list;
    public HashMap<String, String> club_data;
    private Boolean mShowingBack;

    private LinearLayout l1, l2, l3, l4, l5,lay,allclubs;
    private AlertDialog alert, b;
    private int eventId, check;
    private TextView team;
    String msg;
    GifImageView gif_success;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String myprefs = "login.conf";
    ImageView img;
    List<String> Filter_id;
    //private View root;
    public static final String key = "PASS_DATA";
    View space;
    int search_mode;
    String search_type="";
    String search_data="";
    Boolean fab_check=false;
    FabSpeedDial f;
    View shadow;
    ArrayList<String> Clubs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        return root;
    }

    @Override
    public void onStart() {

        super.onStart();
        gif_success=getView().findViewById(R.id.success_gif);
        f =  getView().findViewById(R.id.filter_button);
        shadow = getView().findViewById(R.id.shadowView);
        allclubs = getView().findViewById(R.id.filter_menu1);
        recyclerView = getView().findViewById(R.id.event_recycler_view);
        data_list = new ArrayList<>();
        club_data = new HashMap<String, String>();
        prefs = this.getActivity().getSharedPreferences(myprefs, Context.MODE_PRIVATE);
        editor = prefs.edit();
        msg = "";
        editor.putString("events", "0");
        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shadow.setVisibility(View.GONE);
                if (allclubs.getVisibility() == View.VISIBLE) {
                    allclubs.setVisibility(View.GONE);
                }


            }
        });

        // countEvent = Integer.parseInt(prefs.getString("events","0"));
        Clubs = new ArrayList<>();
        Clubs.clear();
        Clubs.add("Select Club -");
        Clubs.add("All");
        club_data.put("-99","All");
        getClub();
        //show_filter();
        search_mode=0;
        get_data(0, 0, null);
        gridCreate(1);

        f.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                allclubs.setVisibility(View.GONE);
                shadow.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Toast.makeText(getContext(), "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                if (menuItem.getTitle().equals("Dance")){
                    String sorting_key="-99";
                    Iterator<Map.Entry<String,String>> iter = club_data.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String,String> entry = iter.next();
                        if (entry.getValue().equals("Dance")) {
                            sorting_key = entry.getKey();
                        }
                    }
                    if (sorting_key.equals("-99"))
                        search_mode=0;
                    else
                        search_mode=1;
                    search_type="club";
                    search_data=sorting_key;

                    get_data(0,0,null);
                    shadow.setVisibility(View.GONE);
                }

                if (menuItem.getTitle().equals("Music")){
                    String sorting_key="-99";
                    Iterator<Map.Entry<String,String>> iter = club_data.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String,String> entry = iter.next();
                        if (entry.getValue().equals("Music")) {
                            sorting_key = entry.getKey();
                        }
                    }
                    if (sorting_key.equals("-99"))
                        search_mode=0;
                    else
                        search_mode=1;
                    search_type="club";
                    search_data=sorting_key;

                    get_data(0,0,null);
                    shadow.setVisibility(View.GONE);
                }

                if (menuItem.getTitle().equals("Sports")){
                    String sorting_key="-99";
                    Iterator<Map.Entry<String,String>> iter = club_data.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String,String> entry = iter.next();
                        if (entry.getValue().equals("Sports")) {
                            sorting_key = entry.getKey();
                        }
                    }
                    if (sorting_key.equals("-99"))
                        search_mode=0;
                    else
                        search_mode=1;
                    search_type="club";
                    search_data=sorting_key;

                    get_data(0,0,null);
                    shadow.setVisibility(View.GONE);
                }

                if(menuItem.getTitle().equals("All Clubs")){
                    //filter_menu.setVisibility(View.VISIBLE);
                    allclubs.setVisibility(View.VISIBLE);
                    //Clubs={"Select Club -","Music","Sports","Dance","Code","Gaming"};
                    final Spinner spinner=new Spinner(getActivity());
                    spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,Clubs));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int n = spinner.getSelectedItemPosition();
                            if(n > 0){
                                allclubs.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Club selected - " + Clubs.get(n), Toast.LENGTH_SHORT).show();
                                String sorting_key="-99";
                                Iterator<Map.Entry<String,String>> iter = club_data.entrySet().iterator();
                                while (iter.hasNext()) {
                                    Map.Entry<String,String> entry = iter.next();
                                    if (entry.getValue().equals(Clubs.get(n))) {
                                        sorting_key = entry.getKey();
                                    }
                                }
                                if (sorting_key.equals("-99")) {
                                    search_mode = 0;
                                }
                                else
                                    search_mode=1;
                                search_type="club";
                                search_data=sorting_key;

                                get_data(0,0,null);
                                shadow.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {  }
                    });
                    allclubs.removeAllViews();
                    allclubs.addView(spinner);
                } else
                    shadow.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onMenuClosed() {
                shadow.setVisibility(View.GONE);
            }
        });
    }



    private void getClub() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

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
                        Clubs.add(object.getString("clubname"));
                    }
                    //Log.d("...Club Data", Arrays.asList(club_data).toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) { }
        };
        task.execute();
    }

    public void gridCreate(final int span) {
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
                l5 = view.findViewById(R.id.waitprocess);
                final TextView name, desc, category, venue, date, amount, team,cancel;
                final Button payLater, payNow; //paylater is online and payNow is cash.
                name = view.findViewById(R.id.eve_name);
                desc = view.findViewById(R.id.eve_desc);
                team = view.findViewById(R.id.eve_team);
                venue = view.findViewById(R.id.eve_venue);
                category = view.findViewById(R.id.eve_category);
                amount = view.findViewById(R.id.eve_amount);
                date = view.findViewById(R.id.eve_date);
                payLater = view.findViewById(R.id.payLater);
                payNow = view.findViewById(R.id.payNow);
                cancel = view.findViewById(R.id.cancel);
                space=view.findViewById(R.id.space_view);
                name.setText(clickedItem.getEventname());
                desc.setText(clickedItem.getEventdesc());
                team.setText(clickedItem.getTeamsize());
                venue.setText(clickedItem.getVenue());
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
                                       // Log.d("...submission", "....ccdsc");
                                    }
                                });
                                teams.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Registration Cancelled ", Toast.LENGTH_SHORT).show();
                                        //Log.d(".. Register Cancel ", "csa");
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
                    {   payNow.setText("You Missed Your Chance !");
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
                        alertDialog1.setMessage(desc.getText().toString());
                        alertDialog1.setCancelable(false);
                        final AlertDialog a = alertDialog1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
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
                       // Log.d("...Before asynk", "asc");
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
                //alert.getWindow().setLayout(1000, 900);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.show();
                alert.getWindow().setLayout(1000, 1100);
                //alertDialog.show().getWindow().setLayout(1000, 900); //Controlling width and height
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(!data_list.isEmpty()){
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    get_data(data_list.get(data_list.size() - 1).getSln() + 1, 0, null);
                }
            }
            }
        });
    }

    private void get_data(final int id, final int x, final StringBuffer user_id) {

        final int mode=search_mode;
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Integer> task = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {

                OkHttpClient client = new OkHttpClient();
                if (x == 0) {
                    Request request;
                    if (mode==1)
                    {   data_list.clear();
                        request = new Request.Builder().url(server.server_ip()+"AppEventByClub.php?id=" + search_data.trim()).build();
                    }else
                     request= new Request.Builder().url(server.server_ip() + "AppAllEvent.php?id=" + id).build();
                        //Log.d("..link ",request.toString());
                    try {
                        Response response = client.newCall(request).execute();
                        JSONArray array = new JSONArray(response.body().string());
                        int s = id;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            if (mode==1)
                            {
                                if(search_type.equals("club")) {
                                    if (!(object.getString("clubid").equals(search_data)))
                                        continue;

                                }else if(search_type.equals("text"))
                                {
                                    if (!(object.getString("eventname").contains(search_data)))
                                        continue;
                                }


                            }
                            Events data = new Events(s++, object.getString("idevent"), object.getString("eventname"), object.getString("eventdesc"),
                                    object.getString("eventtime"), object.getString("eventdate"), object.getString("venue"), object.getString("fee"),
                                    object.getString("schoolid"), object.getString("clubid"), object.getString("teamsize"), object.getString("istrending")
                            ,object.getString("status"));

                            data_list.add(data);
                            //Log.d("id", Integer.toString(array.length()));
                           // Log.d("My List  ", array.toString());
                           // Log.d("......times....", Integer.toString(i));
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
                    //String senddata = "pid=" + user_id.toString();
                    //Log.d(".....url..|||---",server.server_ip()+"AppRegisterEvent.php?eventid="+id+"&pid=" + user_id.toString());
                    Request request = new Request.Builder().url(server.server_ip()+"AppRegisterEvent.php?eventid=" + id + "&pid=" + u).build();
                   // Log.d("sada....", request.toString());

                    try {
                        Response response = client.newCall(request).execute();
                        msg = response.body().string().trim();
                       // Log.d(".....DATA||",msg);
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
                    scheduleNotification(getNotification("Registration Successful for event","Event Registered"), 0,0);
                    l5.setVisibility(View.GONE);
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

    @Override
    public void onclick(int position) {}

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
    /* private void success(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                l4.setVisibility(View.GONE);
                alert.dismiss();
            }
        }, 5000);
    }*/

    public HashMap<String,String > SendClub()
    {
        return club_data;
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
