package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.server;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private onItemClickListener mListener;

    List<String> mResources;

    public interface onItemClickListener{
        void onImageclick(int position);
    }

    public void setOnClick(onItemClickListener listener)
    {
        mListener=listener;
    }



    public ImageAdapter(Context context,List<String> mResources) {

        this.mResources=mResources;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 10000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_slide, container, false);
        if(position > mResources.size()-1)
            position=position%(mResources.size());
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //imageView.setImageResource(mResources.get(position));
        Log.d("....Resource pos",Integer.toString(position)+mResources.toString());

        //String imgurl="http://27.251.102.156/proshow/"+mResources.get(position);
        String imgurl= server.server_ip()+"AppProshows/"+mResources.get(position);
        /*Picasso.Builder builder = new Picasso.Builder(mContext);
        final int finalPosition1 = position;
        builder.listener(new Picasso.Listener()
        {

            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();

            }

        });


        builder.build().load(imgurl).placeholder(R.drawable.loading_ani).error(R.drawable.album2).into(imageView);*/

        Picasso.with(mContext).load(R.drawable.loading_ani).placeholder(R.drawable.loading_ani).error(R.drawable.loading_ani).into(imageView);
        LoadImg obj=new LoadImg(mContext,imageView,imgurl,1);
        obj.setUrl(imgurl);
        obj.setImage();


        final int finalPosition = position;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null)
                {
                    Log.d(".....position",Integer.toString(finalPosition));

                    mListener.onImageclick(finalPosition);

                }
            }
        });

        container.addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}