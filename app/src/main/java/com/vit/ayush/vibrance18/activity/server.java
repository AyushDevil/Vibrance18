package com.vit.ayush.vibrance18.activity;

/**
 * Created by DELL on 04-Jan-18.
 */

public class server {

    private static String ip;

    server()
    {
        ip="";
    }
    public static String server_ip()
    {
        ip="http://27.251.102.156/register/";
        return ip;
    }
    public static String server_ip_only()
    {
        ip="http://27.251.102.156/";
        return ip;
    }
}
