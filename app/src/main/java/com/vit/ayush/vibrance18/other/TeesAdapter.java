package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.server;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class TeesAdapter extends RecyclerView.Adapter<TeesAdapter.ViewHolder>{

    private Context context;
    private List<Tees> my_data;
    private onItemClickListener mListener;
    int prevpos=0;

    public interface onItemClickListener{
        void onclick(int position);
        void imgClick(int p);;
    }

    public void setOnClick(onItemClickListener listener)
    {
        mListener=listener;
    }

    public TeesAdapter(Context context, List<Tees> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.teescard,parent,false);

        return new ViewHolder(itemview);
    }





    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(my_data.get(position).getName());
        holder.price.setText(my_data.get(position).getPrice());


        String F,S,B;
        F=server.server_ip()+"shirt/upload/"+my_data.get(position).getFront();
        S=server.server_ip()+"shirt/upload/"+my_data.get(position).getSide();
        B=server.server_ip()+"shirt/upload/"+my_data.get(position).getBack();
        //Picasso.with(context).load(x).resize(250,300).into(holder.imgF);
        //Log.d(".....img....",x);

        LoadImg objF=new LoadImg(context,holder.imgF,null,1);
        objF.setUrl(F);
        objF.setImage();

        LoadImg objS=new LoadImg(context,holder.imgS,null,1);
        objS.setUrl(S);
        objS.setImage();

        LoadImg objB=new LoadImg(context,holder.imgB,null,1);
        objB.setUrl(B);
        objB.setImage();

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgF,imgS,imgB;
        public TextView name;
        public TextView price;
        public LinearLayout images;


        public ViewHolder(View itemView) {
            super(itemView);

            imgF= itemView.findViewById(R.id.teeimgF);
            imgB= itemView.findViewById(R.id.teeimgB);
            imgS= itemView.findViewById(R.id.teeimgS);
            name=itemView.findViewById(R.id.teename);
            price=itemView.findViewById(R.id.teeprice);
            images=itemView.findViewById(R.id.tees_img_L);

            images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null)
                    {
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION)
                        {
                            try {
                                mListener.imgClick(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null)
                    {
                        int position=getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION)
                        {
                            try {
                                mListener.onclick(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }


}

