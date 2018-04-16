package com.vit.ayush.vibrance18.other;



public class User{

    private String idparticipants,name,password,phno,email,college;
    //private String eventname,eventdesc,eventtime,venue,eventdate;
   // private String fee;


    public User() {
        idparticipants=null;
       /* name=null;
        clubid=null;
        teamsize=null;
        istrending=null;
        eventdate=null;
        eventname=null;
        eventdesc=null;
        venue=null;
        eventtime=null;
        fee=null;*/
    }

    public User( String idparticipants, String name, String password, String phno, String email, String college) {

        this.idparticipants = idparticipants;
        this.name = name;
        this.password = password;
        this.phno = phno;
        this.email = email;
        this.college = college;
    }


    public String getIdparticipants() {
        return idparticipants;
    }

    public void setIdparticipants(String idparticipants) {
        this.idparticipants = idparticipants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}
