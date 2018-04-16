package com.vit.ayush.vibrance18.other;



import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Events implements Parcelable{
    int sln;
    private String idevent,schoolid,clubid,teamsize,istrending;
    private String eventname,eventdesc,eventtime,venue,eventdate;
    private String fee,status;


    public Events() {
        idevent=null;
        schoolid=null;
        clubid=null;
        teamsize=null;
        istrending=null;
        eventdate=null;
        eventname=null;
        eventdesc=null;
        venue=null;
        eventtime=null;
        fee=null;
        status=null;
    }

    public Events(int sln, String idevent, String eventname, String eventdesc, String eventtime, String eventdate, String venue,
                  String fee, String schoolid, String clubid, String teamsize, String istrending,String status) {
        this.sln=sln;
        this.status=status;
        this.idevent = idevent;
        this.schoolid = schoolid;
        this.clubid = clubid;
        this.teamsize = teamsize;
        this.istrending = istrending;
        this.eventname = eventname;
        this.eventdesc = eventdesc;
        this.eventtime = eventtime;
        this.venue = venue;
        this.eventdate = eventdate;
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSln(int sln) {
        this.sln = sln;
    }

    public int getSln() {
        return sln;
    }

    public void setIdevent(String idevent) {
        this.idevent = idevent;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public void setClubid(String clubid) {
        this.clubid = clubid;
    }

    public void setTeamsize(String teamsize) {
        this.teamsize = teamsize;
    }

    public void setIstrending(String istrending) {
        this.istrending = istrending;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public void setEventdesc(String eventdesc) {
        this.eventdesc = eventdesc;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getIdevent() {
        return idevent;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public String getClubid() {
        return clubid;
    }

    public String getTeamsize() {
        return teamsize;
    }

    public String getIstrending() {
        return istrending;
    }

    public String getEventname() {
        return eventname;
    }

    public String getEventdesc() {
        return eventdesc;
    }

    public String getEventtime() {
        return eventtime;
    }

    public String getVenue() {
        return venue;
    }

    public String getEventdate() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1=df.parse(eventdate);
        DateFormat d2=new SimpleDateFormat("E, dd MMM");


        return String.valueOf(d2.format(d1));
    }



    public String getFee() {
        return fee;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idevent);
        parcel.writeString(eventname);
        parcel.writeString(eventdesc);
        parcel.writeString(eventtime);
        parcel.writeString(eventdate);
        parcel.writeString(venue);
        parcel.writeString(fee);
        parcel.writeString(schoolid);
        parcel.writeString(clubid);
        parcel.writeString(teamsize);
        parcel.writeString(istrending);
    }
    //constructor to read from parcel

    public Events(Parcel in)
    {
        idevent=in.readString();
        eventname=in.readString();
        eventdesc=in.readString();
        eventtime=in.readString();
        eventdate=in.readString();
        venue=in.readString();
        fee=in.readString();
        schoolid=in.readString();
        clubid=in.readString();
        teamsize=in.readString();
        istrending=in.readString();
    }

    //De-serialize object
    public static final Parcelable.Creator<Events> CREATOR = new Parcelable.Creator<Events>(){
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        public Events[] newArray(int size) {
            return new Events[size];
        }
    };
}
