package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;

import java.text.ParseException;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

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

    public EventAdapter(Context context, List<Events> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.eventcard,parent,false);

        return new ViewHolder(itemview);
    }





    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(my_data.get(position).getEventname());
        holder.price.setText(my_data.get(position).getFee());
        try {
            holder.date.setText(my_data.get(position).getEventdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.member.setText(my_data.get(position).getTeamsize());


        if(prevpos<position)
        {
            animateCard.animate(holder,true);

        }
        else {
            animateCard.animate(holder,false);
        }
        prevpos=position;
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView price;
        public TextView date;
        public TextView member;

        public ViewHolder(View itemView) {
            super(itemView);

            //img= itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.namenavevent);
            price=itemView.findViewById(R.id.price);

            date=itemView.findViewById(R.id.event_date);
            member=itemView.findViewById(R.id.capacity);

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

