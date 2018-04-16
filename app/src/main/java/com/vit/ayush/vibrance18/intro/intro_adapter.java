package com.vit.ayush.vibrance18.intro;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;

public class intro_adapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public intro_adapter(Context context){
        this.context=context;
    }

    public String heading[]={
            "WELCOME",
            "GET ON GO",
            "NEVER MISS ACTION",
            "GET STARTED"
    };

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;



        if (position==0)
        {
            view=layoutInflater.inflate(R.layout.intro_frag1,container,false);
            TextView textView=view.findViewById(R.id.fragtitle);
            textView.setText(heading[position]);
        }
        else if(position==1)
        {
            view=layoutInflater.inflate(R.layout.intro_frag2,container,false);
            TextView textView=view.findViewById(R.id.fragtitle);
            textView.setText(heading[position]);
        }
        else if (position==2)
        {
            view=layoutInflater.inflate(R.layout.intro_frag3,container,false);
            TextView textView=view.findViewById(R.id.fragtitle);
            textView.setText(heading[position]);
        }
        else
        {
            view=layoutInflater.inflate(R.layout.intro_frag4,container,false);
            TextView textView=view.findViewById(R.id.fragtitle);
            textView.setText(heading[position]);
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }


}
