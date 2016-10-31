package com.okason.simplenotepad.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okason.simplenotepad.utilities.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Valentine on 9/28/2015.
 */
public class Note implements Parcelable {
    private Long id;
    private String title;
    private String content;
    private long dateCreated;
    private long dateModified;

    private ArrayList<Uri> uriList;

    public ArrayList<Uri> getUriList() {
        return uriList;
    }

    public void setUriList(ArrayList<Uri> uriList) {
        this.uriList = uriList;
    }




    public Note() {
    }

    public static Note getNotefromCursor(Cursor cursor) {
        Note note = new Note();
        note.setId(
                cursor.getLong(
                        cursor.getColumnIndex(
                                Constants.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)));
        note.setDateCreated(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        note.setDateModified(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));

       /*ArrayList<Uri> uriArrayListFromGson =  new Gson().fromJson( cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PHOTOS_URI_LIST)) , ArrayList.class)  ;
        note.setPicturesUriArrayList( uriArrayListFromGson );*/

        //
        String arrayListStringFromDB = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PHOTOS_URI_LIST));

        ArrayList<Uri> uriList = parseArrayListUriFromString(arrayListStringFromDB);

        note.setUriList(uriList);
        //

        return note;
    }

     static ArrayList<Uri> parseArrayListUriFromString (String arrayListStringFromDB) {



        ArrayList<Uri> uriList = new ArrayList<>();

        //Gson gson = new Gson();
        // create the type for the collection. In this case define that the collection is of type Dataset
        /*Type datasetListType = new TypeToken<ArrayList<Uri>>() {}.getType();
        List<Uri> datasets = gson.fromJson(arrayListJSONstringFromDB, datasetListType);

        for (Uri dataset : datasets) {
            uriList.add(dataset);
        }*/


         if (arrayListStringFromDB == null)
             return uriList;

       String[] arrayListUriSplittedStrings =  arrayListStringFromDB.split(Constants.URI_DELIMITER);
        for (String uriString:arrayListUriSplittedStrings) {
            uriList.add(Uri.parse(uriString));
        }

        Log.d("myLogsUri","uriList parsed from DB size() = " + uriList.size());
        for (Uri uri: uriList) {
            Log.d("myLogsUri",uri.toString());
        }

        return uriList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d("myLogs", "writeToParcel");
        parcel.writeLong(id);

        parcel.writeString(title);
        parcel.writeString(content);

        parcel.writeLong(dateCreated);
        parcel.writeLong(dateModified);

    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        // распаковываем объект из Parcel
        public Note createFromParcel(Parcel in) {
            Log.d("myLogs", "createFromParcel");
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    // конструктор, считывающий данные из Parcel
    private Note(Parcel parcel) {
        Log.d("myLogs", "MyObject(Parcel parcel)");

        id = parcel.readLong();
        title = parcel.readString();
        content = parcel.readString();
        dateCreated = parcel.readLong();
        dateModified = parcel.readLong();
    }
}
