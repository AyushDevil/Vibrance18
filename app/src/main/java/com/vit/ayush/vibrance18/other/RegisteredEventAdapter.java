package com.vit.ayush.vibrance18.other;

/**
 * Created by DELL on 11-Jan-18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;

import java.text.ParseException;
import java.util.List;


public class RegisteredEventAdapter extends RecyclerView.Adapter<RegisteredEventAdapter.ViewHolder>{

    private Context context;
    private List<RegisteredEventClass> my_data;


    public RegisteredEventAdapter(Context context, List<RegisteredEventClass> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_e_card,parent,false);

        return new ViewHolder(itemview);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(my_data.get(position).getName());
        holder.venue.setText(my_data.get(position).getVenue());
        try {
            holder.date.setText(my_data.get(position).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(my_data.get(position).getTime());
        if(Integer.parseInt(my_data.get(position).getStatus())==0){
            holder.status.setText("Not Paid");
        }
        else {
            holder.status.setText("Paid");
        }
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,venue,date,time,status;

        public ViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.R_e_name);
            venue=itemView.findViewById(R.id.R_e_venue);
            date=itemView.findViewById(R.id.R_e_date);
            time=itemView.findViewById(R.id.R_e_time);
            status=itemView.findViewById(R.id.R_e_status);

        }
    }


}

