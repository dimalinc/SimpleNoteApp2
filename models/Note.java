package com.okason.simplenotepad.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.okason.simplenotepad.utilities.Constants;

import java.util.ArrayList;

/**
 * Created by Valentine on 9/28/2015.
 */
public class Note implements Parcelable{
    private Long id;
    private String title;
    private String content;
    private long dateCreated;
    private long dateModified;

    public ArrayList<Uri> picturesUriArrayList;


    public Note() {
    }

    public static Note getNotefromCursor(Cursor cursor){
        Note note = new Note();
        note.setId(
                cursor.getLong(
                        cursor.getColumnIndex(
                                Constants.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)));
        note.setDateCreated(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        note.setDateModified(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));
        return note;
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
