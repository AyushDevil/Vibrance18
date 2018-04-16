package com.vit.ayush.vibrance18.other;

/**
 * Created by DELL on 12-Jan-18.
 */

public class RegisteredTeesClass {
    String name,front,size,qty,status;

    public RegisteredTeesClass(String name, String front, String size, String qty, String status) {
        this.name = name;
        this.front = front;
        this.size = size;
        this.qty = qty;
        this.status = status;
    }

    public RegisteredTeesClass() {
        this.name = null;
        this.front = null;
        this.size = null;
        this.qty = null;
        this.status = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
