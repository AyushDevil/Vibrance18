package com.vit.ayush.vibrance18.other;



import android.os.Parcel;
import android.os.Parcelable;

public class Tees implements Parcelable{

    private String id,name,front,back,side,description,price;


    public Tees() {

        name=null;
        front=null;
        back=null;
        side=null;
        description=null;
        price=null;
    }

    public Tees(String id,String name,String front,String back, String side, String description, String price) {

        this.id=id;
        this.name=name;
        this.front=front;
        this.back=back;
        this.side=side;
        this.description=description;
        this.price=price;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name.trim();
    }

    public String getFront() {
        return front.trim();
    }

    public String getBack() {
        return back.trim();
    }

    public String getSide() {
        return side.trim();
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public static Creator<Tees> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(front);
        parcel.writeString(back);
        parcel.writeString(side);
        parcel.writeString(description);
        parcel.writeString(price);

    }
    //constructor to read from parcel

    public Tees(Parcel in)
    {
        id=in.readString();
        name=in.readString();
        front=in.readString();
        back=in.readString();
        side=in.readString();
        description=in.readString();
        price=in.readString();

    }

    //De-serialize object
    public static final Parcelable.Creator<Tees> CREATOR = new Parcelable.Creator<Tees>(){
        public Tees createFromParcel(Parcel in) {
            return new Tees(in);
        }

        public Tees[] newArray(int size) {
            return new Tees[size];
        }
    };
}
