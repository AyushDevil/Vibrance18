package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;

import java.util.List;

/**
 * Created by DELL on 12-Jan-18.
 */

public class RegisteredTeesAdapter extends RecyclerView.Adapter<RegisteredTeesAdapter.ViewHolder>{

    private Context context;
    private List<RegisteredTeesClass> my_data;


    public RegisteredTeesAdapter(Context context, List<RegisteredTeesClass> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_t_card,parent,false);

        return new ViewHolder(itemview);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(my_data.get(position).getName());
        //holder.venue.setText(my_data.get(position).getVenue());
        String size="";
        if (my_data.get(position).getSize().equals("1"))
            size="S";
        else if (my_data.get(position).getSize().equals("2"))
            size="M";
        else if (my_data.get(position).getSize().equals("3"))
            size="L";
        else if (my_data.get(position).getSize().equals("4"))
            size="XL";
        else if (my_data.get(position).getSize().equals("5"))
            size="XXL";
        holder.size.setText(size);
        holder.qty.setText(my_data.get(position).getQty());
        if(Integer.parseInt(my_data.get(position).getStatus())==0){
            holder.status.setText("Not Paid");
        }
        else
        holder.status.setText("Paid");
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name,size,qty,status;

        public ViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.R_t_name);
            //venue=itemView.findViewById(R.id.R_e_venue);
            size=itemView.findViewById(R.id.R_t_size);
            qty=itemView.findViewById(R.id.R_t_qty);
            status=itemView.findViewById(R.id.R_t_status);

        }
    }


}

