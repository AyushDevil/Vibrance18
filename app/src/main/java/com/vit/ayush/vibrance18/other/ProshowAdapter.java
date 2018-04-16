package com.vit.ayush.vibrance18.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.server;

import java.text.ParseException;
import java.util.List;

public class ProshowAdapter extends RecyclerView.Adapter<ProshowAdapter.ViewHolder>{

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

    public ProshowAdapter(Context context, List<Events> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.proshow_card,parent,false);

        return new ViewHolder(itemview);
    }


    String changeName(String name)
    {

        StringBuilder finalname=new StringBuilder();
        for (int i=0;i<name.length();i++) {
            finalname.append(Character.toUpperCase(name.charAt(i)));

            finalname.append("\n");
        }

        return finalname.toString();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        //Log.d("name ||| ",finalname.toString());
        holder.ComboL.setVisibility(View.GONE);
        if (my_data.get(position).getIdevent().equals("82"))
        {

            holder.name.setText(changeName(my_data.get(position).getEventname().trim()));
            LoadImg load=new LoadImg(context,holder.img,"",1);
            load.setUrl(server.server_ip_only()+ "register/AppProshows/vs.jpg");
            load.setImage();
        }
        else if (my_data.get(position).getIdevent().equals("80"))
        {
            holder.name.setText(changeName(my_data.get(position).getEventname().trim()));
            LoadImg load=new LoadImg(context,holder.img,"",1);
            load.setUrl(server.server_ip_only()+ "register/AppProshows/candice.jpg");
            load.setImage();
        }
        else if (my_data.get(position).getIdevent().equals("81"))
        {
            holder.name.setText(changeName(my_data.get(position).getEventname().trim()));
            LoadImg load=new LoadImg(context,holder.img,"",1);
            load.setUrl(server.server_ip_only()+ "register/poster/22.jpg");
            load.setImage();

        }
        else{
            holder.Vname.setVisibility(View.GONE);
            holder.ComboL.setVisibility(View.VISIBLE);
            holder.comboname.setText(my_data.get(position).getEventname().trim());
            LoadImg load=new LoadImg(context,holder.img,"",0);
            load.setUrl(server.server_ip_only()+ "register/poster/"+my_data.get(position).getIdevent());
            load.setImage();
        }


        holder.price.setText(my_data.get(position).getFee());
        try {
            holder.date.setText(my_data.get(position).getEventdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        public ImageView img;
        public TextView name;
        public TextView price;
        public TextView date;
        public TextView comboname;
        public LinearLayout Vname,ComboL;

        public ViewHolder(View itemView) {
            super(itemView);
            Vname=itemView.findViewById(R.id.proshownameL);
            ComboL=itemView.findViewById(R.id.middle);
            comboname=itemView.findViewById(R.id.comboname);
            img= itemView.findViewById(R.id.proshowimg);
            name=itemView.findViewById(R.id.proshowname);
            price=itemView.findViewById(R.id.proshowprice);

            date=itemView.findViewById(R.id.proshowdate);

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

