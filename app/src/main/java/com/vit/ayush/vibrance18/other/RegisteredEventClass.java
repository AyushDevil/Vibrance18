package com.vit.ayush.vibrance18.other;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DELL on 11-Jan-18.
 */

public class RegisteredEventClass {
    String name,venue,date,time,status;

    public RegisteredEventClass(String name, String venue, String date, String time, String status) {
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public RegisteredEventClass()
    {
        this.name = null;
        this.venue = null;
        this.date = null;
        this.time = null;
        this.status = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1=df.parse(date);
        DateFormat d2=new SimpleDateFormat("E, dd MMM");


        return String.valueOf(d2.format(d1));
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
