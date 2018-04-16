package com.vit.ayush.vibrance18.other;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Image implements Serializable{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image() {
    }

    public Image(String url) {
        this.url = url;

    }


}
