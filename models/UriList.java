package com.okason.simplenotepad.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by СВП on 28.10.2016.
 */

public class UriList implements  Parcelable{

   public ArrayList<Uri> takePhotoActivityUriArrayList = new ArrayList<>();

    public UriList() {}

    @Override
    public int describeContents() {
        return 0;
    }




    public static final Parcelable.Creator<UriList> CREATOR = new Parcelable.Creator<UriList>() {
        // распаковываем объект из Parcel
        public UriList createFromParcel(Parcel in) {
            Log.d("myLogs", "createFromParcel");
            return new UriList(in);
        }

        public UriList[] newArray(int size) {
            return new UriList[size];
        }
    };


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d("myLogs", "write UriList.toString() ToParcel");

        parcel.writeInt(takePhotoActivityUriArrayList.size());

        for (Uri uri:takePhotoActivityUriArrayList) {
            parcel.writeString(uri.toString());

        }

    }
    // конструктор, считывающий данные из Parcel
    private UriList(Parcel parcel) {
        Log.d("myLogs", "MyObject(Parcel parcel)");



        int urlListSize = parcel.readInt();

        for (int i = 0; i < urlListSize; i++) {
            takePhotoActivityUriArrayList.add(Uri.parse(parcel.readString()));
        }


    }
}
