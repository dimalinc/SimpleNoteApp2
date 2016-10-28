package com.okason.simplenotepad.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.okason.simplenotepad.R;
import com.okason.simplenotepad.activities.MainActivity;
import com.okason.simplenotepad.activities.NoteEditorActivity;
import com.okason.simplenotepad.activities.TakePhotoActivity;
import com.okason.simplenotepad.data.NoteManager;
import com.okason.simplenotepad.models.Note;
import com.okason.simplenotepad.models.UriList;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotePlainEditorFragment extends Fragment {

    Button button;

    private View mRootView;
    private EditText mTitleEditText;
    private EditText mContentEditText;
    private Note mCurrentNote = null;

    private static final int PHOTO_INTENT_REQUEST_CODE = 100;
    Uri mUri;

    ArrayList<Uri> arrayListUri = new ArrayList<>();



    public NotePlainEditorFragment() {
        // Required empty public constructor
    }

    public static NotePlainEditorFragment newInstance(long id) {
        NotePlainEditorFragment fragment = new NotePlainEditorFragment();

        // Log.d("myLogs","inside Fragment newInstance id = " + id  );
        if (id > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private void getCurrentNote() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("id")) {

            //  Log.d("myLogs","inside NotePlainEditorFragment");

            long id = args.getLong("id", 0);
            // Log.d("myLogs","args.getLong(\"id\", 0); = " + id);

            if (id > 0) {
                mCurrentNote = NoteManager.newInstance(getActivity()).getNote(id);
                // TODO добавлено мной для теста - хз может убрать


                //  Log.d("myLogs", "got mCurrentNote = " + mCurrentNote.getId());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getCurrentNote();

        // TODO я добавил - убрать или оставить?
        //onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_note_plain_editor, container, false);
        mTitleEditText = (EditText) mRootView.findViewById(R.id.edit_text_title);
        mContentEditText = (EditText) mRootView.findViewById(R.id.edit_text_note);
        button = (Button) mRootView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUri = generateFileUri();
                if (mUri == null) {
                    Toast.makeText(getActivity(), "SD card not available", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getActivity(),TakePhotoActivity.class);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);


                Log.d("myLogs","mCurrentNote.getId() = " + mCurrentNote.getId());

                intent.putExtra("note.id", mCurrentNote.getId());
                startActivityForResult(intent, PHOTO_INTENT_REQUEST_CODE);

                // startActivity(new Intent(getActivity(), TakePhotoActivity.class));
            }
        });


        return mRootView;
    }



    private Uri generateFileUri() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return null;
        File path = new File(Environment.getExternalStorageDirectory(), "CameraTest");
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return Uri.fromFile(newFile);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentNote != null) {
            populateFields();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_note_edit_plain, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //delete note
                if (mCurrentNote != null) {
                    promptForDelete();
                } else {
                    makeToast("Cannot delete note that has not been saved");
                }
                break;
            case R.id.action_save:
                //save note
                if (saveNote()) {
                    makeToast(mCurrentNote != null ? "Note updated" : "Note saved");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void populateFields() {
        mTitleEditText.setText(mCurrentNote.getTitle());
        mContentEditText.setText(mCurrentNote.getContent());
    }


    private boolean saveNote() {

        String title = mTitleEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            mTitleEditText.setError("Title is required");
            return false;
        }

        String content = mContentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mContentEditText.setError("Content is required");
            return false;
        }


        if (mCurrentNote != null) {
            mCurrentNote.setContent(content);
            mCurrentNote.setTitle(title);
            NoteManager.newInstance(getActivity()).update(mCurrentNote);

        } else {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            NoteManager.newInstance(getActivity()).create(note);
        }
        return true;

    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void promptForDelete() {
        final String titleOfNoteTobeDeleted = mCurrentNote.getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Delete " + titleOfNoteTobeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + titleOfNoteTobeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteManager.newInstance(getActivity()).delete(mCurrentNote);
                makeToast(titleOfNoteTobeDeleted + "deleted");
                startActivity(new Intent(getActivity(), MainActivity.class));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri;
        UriList uriList = null;

        if (requestCode == PHOTO_INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
            try {

                uriList = (UriList) data.getParcelableExtra("uriList");

                arrayListUri = uriList.takePhotoActivityUriArrayList;

                StringBuilder sb = new StringBuilder();

                for (Uri uri2:arrayListUri) {
                    sb.append(uri2.toString() + " ");
                }

                mCurrentNote.setContent(mCurrentNote.getContent() + " _-_ " + sb.toString());
            } catch (NullPointerException e) {
                mCurrentNote.setContent(mCurrentNote.getContent() + " null uriListOBJECT got from PhotoActivity");
                Log.d("myLogs"," null uriListOBJECT got from PhotoActivity");
            }


        }
    }

}
