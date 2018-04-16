package com.vit.ayush.vibrance18.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.vit.ayush.vibrance18.R;
import com.vit.ayush.vibrance18.activity.server;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImg {
    @SuppressLint("StaticFieldLeak")
    private ImageView img;
    String id;

    @SuppressLint("StaticFieldLeak")
    Context context;
    private String url,finalurl;
    private int flag;

    public LoadImg() {

    }

    public void setImage() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Boolean> image=new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected void onPreExecute() {


                Log.d("...Pre asynk", "asc");
            }


            @Override
            protected Boolean doInBackground(Void... voids) {

                Log.d("...Background asynk", "asc");
                if (flag == 0)
                    finalurl = url + ".jpeg";
                else
                    finalurl = url;


                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(finalurl).openConnection();
                    con.setRequestMethod("HEAD");
                    System.out.println(con.getResponseCode());

                    if (!(con.getResponseCode() == HttpURLConnection.HTTP_OK)) {

                        if (flag == 0)
                            finalurl = url + ".jpg";
                        else
                            finalurl = url;
                        con = (HttpURLConnection) new URL(finalurl).openConnection();
                        con.setRequestMethod("HEAD");
                        Log.d("...URL : ", finalurl);
                        if (!(con.getResponseCode() == HttpURLConnection.HTTP_OK)) {
                            if (flag == 0)
                                finalurl = url + ".png";
                            else
                                finalurl = url;
                            con = (HttpURLConnection) new URL(finalurl).openConnection();
                            con.setRequestMethod("HEAD");
                            Log.d("...URL : ", finalurl);
                            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
                        } else return true;

                    } else return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }


            }

            @Override
            protected void onPostExecute(Boolean integer) {
                Log.d("...finale asynk" + finalurl, integer.toString());
                if (!integer) {
                    Picasso.with(context).load(R.drawable.album2).placeholder(R.drawable.loading_ani).into(img);
                } else
                    Picasso.with(context).load(finalurl).placeholder(R.drawable.loading_ani).into(img);

            }
        };

        image.execute();
    }

    public LoadImg(Context context, ImageView img, String id,int flag) {

        this.context=context;
        this.img = img;
        this.id = id;
        this.flag=flag;
        url=server.server_ip()+"poster/"+id;
        Log.d("...Construcytor asynk","asc");
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url){this.url=url;}


    public void loader()
    {
        Picasso.with(context).load(R.drawable.loading_ani).resize(5, 5).into(img);
    }

}
