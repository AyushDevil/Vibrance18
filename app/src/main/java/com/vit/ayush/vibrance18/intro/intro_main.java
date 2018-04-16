package com.vit.ayush.vibrance18.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.LoginActivity;

public class intro_main extends AppCompatActivity {

    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private intro_adapter adapter;

    private TextView[] dots;
    TextView nxt,skip;
    int currpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.intro_main);

        mViewPager =findViewById(R.id.viewpager);
        dotsLayout = (LinearLayout) findViewById(R.id.dots);
        nxt=findViewById(R.id.nextBtn);
        skip=findViewById(R.id.Skip);
        skip.setText("skip");
        nxt.setText("NEXT");


        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new intro_adapter(this));
        setDots(0);

        mViewPager.addOnPageChangeListener(viewListener);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchNextActivity;
                launchNextActivity = new Intent(intro_main.this, LoginActivity.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(launchNextActivity);
            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currpos<(dots.length-1))
                mViewPager.setCurrentItem(currpos+1);

            }
        });
    }


    public void setDots(int pos)
    {
        dots =new TextView[4];
        dotsLayout.removeAllViews();

        for (int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(40);
            dots[i].setTextColor(getResources().getColor(R.color.album_title));
            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0)
        {
            dots[pos].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }



    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDots(position);
            currpos=position;
            if(position == (dots.length-1))
            {
                nxt.setEnabled(true);
                skip.setVisibility(View.INVISIBLE);
                nxt.setVisibility(View.VISIBLE);
                nxt.setText("LOGIN");
                nxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(intro_main.this, LoginActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(launchNextActivity);
                    }
                });
            }
            else
            {
                nxt.setEnabled(true);
                skip.setVisibility(View.VISIBLE);
                nxt.setVisibility(View.VISIBLE);
                nxt.setText("NEXT");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}