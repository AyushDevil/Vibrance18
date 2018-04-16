package com.vit.ayush.vibrance18.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.fragment.EventFragment;
import com.vit.ayush.vibrance18.fragment.HomeFragment;
import com.vit.ayush.vibrance18.fragment.TeesFragment;
import com.vit.ayush.vibrance18.fragment.ProfileFragment;
import com.vit.ayush.vibrance18.fragment.ProshowFragment;
import com.vit.ayush.vibrance18.fragment.SettingsFragment;
import com.vit.ayush.vibrance18.other.RegisteredEventClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,ProshowFragment.OnFragmentInteractionListener,TeesFragment.OnFragmentInteractionListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private List<RegisteredEventClass> data_Reg_Et;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    SharedPreferences prefs;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://www.photohdx.com/images/2016/03/red-orange-soft-carpet-texture-background.jpg";
    private static final String urlProfileImg = "http://www.vitvibrance.com/register/img/vibrance_logo_dark_small.png";
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_MERCHANDISE = "merchandise";
    private static final String TAG_NOTIFICATIONS = "notification";
    private static final String TAG_PROS = "proshows";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_PROFILE = "profile";
    public static String CURRENT_TAG = TAG_HOME;
    boolean doubleBackToExitPressedOnce = false;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        data_Reg_Et=new ArrayList<RegisteredEventClass>();

        prefs = getSharedPreferences("login.conf",MODE_PRIVATE);

        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.namenav);
        txtWebsite = (TextView) navHeader.findViewById(R.id.websitenav);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(prefs.getString("Name",""));
        txtWebsite.setText("Welcome !");

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:

                EventFragment eventFragment = new EventFragment();
                return eventFragment;
            case 2:
               TeesFragment teesFragment = new TeesFragment();
                return teesFragment;
            case 3:
                // proshows fragment
                ProshowFragment proshowFragment = new ProshowFragment();
                return proshowFragment;
            case 4:
                // profiles fragment
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 5:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    if(menuItem.getItemId()==R.id.nav_home) {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;

                    }
                    else if(menuItem.getItemId()==R.id.nav_events){
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_EVENTS;}

                    else if(menuItem.getItemId()==R.id.nav_product){
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MERCHANDISE;}
                    else if(menuItem.getItemId()==R.id.nav_proshow){
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PROS;}
                    else if(menuItem.getItemId()==R.id.nav_profile) {
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_PROFILE;
                    }
                    else if(menuItem.getItemId()==R.id.nav_settings) {
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SETTINGS;
                    }
                    else if(menuItem.getItemId()==R.id.nav_about_us){
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;}
                    else if(menuItem.getItemId()==R.id.nav_gallery){
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                        drawer.closeDrawers();
                        return true;}
                    else if(menuItem.getItemId()==R.id.nav_feedback){
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Feedback_Form.class));
                        drawer.closeDrawers();
                        return true;}
                    else {
                        navItemIndex = 0;

                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
            else{
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 800);
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 4) {
            getMenuInflater().inflate(R.menu.profile, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // user is in notifications fragment
        // and selected 'Mark all as Read'


        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_logout) {
            prefs = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
            prefs.edit().clear().commit();
            Intent launchNextActivity;
            launchNextActivity = new Intent(MainActivity.this, LoginActivity.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(getApplicationContext(), "You have been logged out", Toast.LENGTH_LONG).show();
            startActivity(launchNextActivity);

        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
}