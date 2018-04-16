package com.vit.ayush.vibrance18.other;

/**
 * Created by DELL on 10-Jan-18.
 */

public class Club {
    String id,name;

    public Club(){
        id="";
        name="";
    }

    public Club(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
