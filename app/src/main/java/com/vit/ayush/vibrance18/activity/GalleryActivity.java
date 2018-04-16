package com.vit.ayush.vibrance18.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.other.GalleryAdapter;
import com.vit.ayush.vibrance18.other.Image;
import java.util.ArrayList;

import static com.vit.ayush.vibrance18.activity.server.server_ip;
import static com.vit.ayush.vibrance18.activity.server.server_ip_only;
public class GalleryActivity extends AppCompatActivity {
    FloatingActionButton backhome;
    private String TAG = GalleryActivity.class.getSimpleName();
    private static final String endpoint = "https://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    public static String[] mThumbIds= {
            server_ip()+"AppGallery/img2.jpg",
            server_ip()+"AppGallery/img3.jpg",
            server_ip()+"AppGallery/img99.jpg",
            server_ip()+"AppGallery/img6.jpg",
            server_ip()+"AppGallery/img21.jpg",
            server_ip()+"AppGallery/img33.jpg",
            server_ip()+"AppGallery/img37.jpg",
            server_ip()+"AppGallery/img62.jpg",
            server_ip()+"AppGallery/img72.jpg",
            server_ip()+"AppGallery/img73.jpg",
            server_ip()+"AppGallery/img78.jpg",
            server_ip()+"AppGallery/img81.jpg",
            server_ip()+"AppGallery/img96.jpg",
            server_ip()+"AppGallery/img19.jpg",
            server_ip()+"AppGallery/img103.jpg",
            server_ip()+"AppGallery/img102.jpg",
            server_ip()+"AppGallery/img117.jpg",
            server_ip()+"AppGallery/img122.jpg",
            server_ip()+"AppGallery/img125.jpg",
            server_ip()+"AppGallery/img126.jpg",
            server_ip()+"AppGallery/img132.jpg",
            server_ip()+"AppGallery/img134.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        backhome=findViewById(R.id.homegall);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNextActivity;
                launchNextActivity = new Intent(GalleryActivity.this, MainActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(launchNextActivity);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        images = new ArrayList<>();
        fetchImages();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_gallery);

        pDialog = new ProgressDialog(this);


        //Log.d("....images",images.toString());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

         recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        recyclerView.setAdapter(mAdapter);
    }

    private void fetchImages() {


        for (int i = 0; i < mThumbIds.length; i++) {
            Image image = new Image();
            image.setUrl(mThumbIds[i]);

            images.add(image);


        }

       // mAdapter.notifyDataSetChanged();
    }



}
