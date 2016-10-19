package com.okason.simplenotepad.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okason.simplenotepad.R;
import com.okason.simplenotepad.models.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Valentine on 9/28/2015.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

        private List<Note> mNotes;
        private Context mContext;

    //constructor
    public NoteListAdapter(List<Note> notes, Context context){
        mNotes = notes;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.noteTitle.setText(mNotes.get(position).getTitle());
        holder.noteCreateDate.setText(getReadableModifiedDate(mNotes.get(position).getDateModified()));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView noteTitle, noteCreateDate;



        public ViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView)itemView.findViewById(R.id.text_view_note_title);
            noteCreateDate = (TextView)itemView.findViewById(R.id.text_view_note_date);
        }

    }

    public static String getReadableModifiedDate(long date){

        String displayDate = new SimpleDateFormat("MMM dd, yyyy - h:mm a").format(new Date(date));
        return displayDate;
    }



    public void promptForDelete(final int position){
        String fieldToBeDeleted = mNotes.get(position).getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Delete " + fieldToBeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + fieldToBeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNotes.remove(position);
                notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
