package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private Context context;
    private List<Events> my_data;
    private onItemClickListener mListener;
    int prevpos=0;

    public interface onItemClickListener{
        void onclick(int position) throws ParseException;
    }

    public void setOnClick(onItemClickListener listener)
    {
        mListener=listener;
    }

    public HomeAdapter(Context context, List<Events> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.homecard,parent,false);

        return new ViewHolder(itemview);
    }





    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(my_data.get(position).getEventname());

       // Picasso.with(context).load(R.drawable.loading_ani).placeholder(R.drawable.loading_ani).error(R.drawable.loading_ani).into(holder.img);
        Log.d("...Before HomeAd asynk","asc");
        LoadImg obj=new LoadImg(context,holder.img,my_data.get(position).getIdevent().trim(),0);
        obj.loader();
        obj.setImage();


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        public TextView name;


        public ViewHolder(View itemView) {
            super(itemView);

            img= itemView.findViewById(R.id.trend_img);
            name=itemView.findViewById(R.id.trend_name);


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
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }


}

