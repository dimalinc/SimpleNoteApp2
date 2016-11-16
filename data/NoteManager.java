package com.okason.simplenotepad.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.okason.simplenotepad.models.Note;
import com.okason.simplenotepad.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 9/29/2015.
 */
public class NoteManager {
    //instance variables
    private Context mContext;
    private static NoteManager sNoteManagerInstance = null;

    public static NoteManager newInstance(Context context){

        if (sNoteManagerInstance == null){
            sNoteManagerInstance = new NoteManager(context.getApplicationContext());
        }

        return sNoteManagerInstance;
    }

    //private constructor, cannot be instantiated from the outside
    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public long create(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        // TODO поменять на ручной парсинг
        //String arrayListString= new Gson().toJson(note.getUriList(), ArrayList.class);

        String arrayListString = convertArrayListUriToString(note.getUriList());

       // if (arrayListString.length()>0)
        values.put(Constants.COLUMN_PHOTOS_URI_LIST, arrayListString);

        Log.d("myLogsUri","arrayListString before putting to DB in NoteManager.create() = " + arrayListString);

        Uri result = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI, values);
        long id = Long.parseLong(result.getLastPathSegment());
        return id;
    }

    public void update(Note note) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        String arrayListString = convertArrayListUriToString(note.getUriList());

        values.put(Constants.COLUMN_PHOTOS_URI_LIST, arrayListString);

        Log.d("myLogsUri","arrayListString before putting to DB in NoteManager.update() = " + arrayListString);

        values.put(Constants.COLUMN_PHOTOS_URI_LIST, arrayListString);

        mContext.getContentResolver().update(NoteContentProvider.CONTENT_URI,
                values, Constants.COLUMN_ID  + "=" + note.getId(), null);

    }

    static String convertArrayListUriToString(ArrayList<Uri> uriList) {
        StringBuilder sb = new StringBuilder();
       // sb.append(Constants.URI_DELIMITER);

        for (Uri uri: uriList) {


            if (uriList.size() == 0)
                return null;

            if (uri.toString().length() > 2) {
                sb.append(uri.toString());
                sb.append(Constants.URI_DELIMITER);
            }
        }

        Log.d("myLogsUri"," convertArrayListUriToString = " + sb.toString().trim());

        // DELIMITER is space that's why trim актуален. При изменении разделителя поменять трим на отрезание последнего делимитера
        return sb.toString().trim();

    }

    public void delete(Note note) {
        mContext.getContentResolver().delete(
                NoteContentProvider.CONTENT_URI, Constants.COLUMN_ID + "=" + note.getId(), null);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI, Constants.COLUMNS, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                notes.add(Note.getNoteFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notes;
    }

    public Note getNote(Long id) {
        Note note;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI,
                Constants.COLUMNS, Constants.COLUMN_ID + " = " + id, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            note = Note.getNoteFromCursor(cursor);
            return note;
        }
        return null;
    }





}
