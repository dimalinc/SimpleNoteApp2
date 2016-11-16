package com.okason.simplenotepad.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.okason.simplenotepad.R;
import com.okason.simplenotepad.activities.MainActivity;
import com.okason.simplenotepad.activities.TakePhotoActivity;
import com.okason.simplenotepad.adapter.PicAdapter;
import com.okason.simplenotepad.data.NoteManager;
import com.okason.simplenotepad.models.Note;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotePlainEditorFragment extends Fragment {

    //variable for selection intent
    private final int PICKER = 1;
    //variable to store the currently selected image
    public static int currentPic = 0;

    //adapter for gallery view

    private PicAdapter imgAdapt;

    //gallery object
    private Gallery picGallery;
    //image view for larger display
    private ImageView picView;

    Button buttonTakePhoto;

    private View mRootView;
    private EditText mTitleEditText;
    private EditText mContentEditText;

    private Note mCurrentNote;

    private static final int PHOTO_INTENT_REQUEST_CODE = 100;
    Uri mUri;

    ArrayList<Uri> fragmentArrayListUri/* = new ArrayList<Uri>()*/;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getCurrentNote();

        try {
            Log.d("myLogs", "in onCreate got  mCurrentNote.getUriList().toString() = "
                    + mCurrentNote.getUriList().toString());
            Toast.makeText(getActivity(), mCurrentNote.getUriList().toString(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {Log.d("myLogs",
                "in onCreate got  mCurrentNote.getUriList().toString() = "
                + " No uriList found");
            Toast.makeText(getActivity(), " No uriList found", Toast.LENGTH_SHORT).show();}

    // currentPic = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fragmentArrayListUri = new ArrayList<>();


        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_note_plain_editor, container, false);
        mTitleEditText = (EditText) mRootView.findViewById(R.id.edit_text_title);
        mContentEditText = (EditText) mRootView.findViewById(R.id.edit_text_note);
        buttonTakePhoto = (Button) mRootView.findViewById(R.id.button);

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUri = generateFileUri();
                if (mUri == null) {
                    Toast.makeText(getActivity(), "SD card not available", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

               // saveNote();

                //  Log.d("myLogs","mCurrentNote.getId() = " + mCurrentNote.getId());

                //  intent.putExtra("note.id", mCurrentNote.getId());
                startActivityForResult(intent, PHOTO_INTENT_REQUEST_CODE);

                // startActivity(new Intent(getActivity(), TakePhotoActivity.class));
            }
        });


        //get the large image view
        picView = (ImageView) mRootView.findViewById(R.id.picture);

        //get the gallery view
        picGallery = (Gallery) mRootView.findViewById(R.id.gallery);

        //create a new adapter
        imgAdapt = new PicAdapter(getContext(), mCurrentNote);

        currentPic = 0;
        /*try {
            if (mCurrentNote.getUriList().size() > 0)
                for (Uri uri : mCurrentNote.getUriList()) {
                    addImageFromUriToAdapter(uri);
                }
        } catch (NullPointerException e) {}*/
        populateImageAdapter();

       // picGalleryInit();

        return mRootView;
    }

    void picGalleryInit() {

//set the gallery adapter
        picGallery.setAdapter(imgAdapt);

        //set long click listener for each gallery thumbnail item
        picGallery.setOnItemLongClickListener(new Gallery.OnItemLongClickListener() {
            //handle long clicks
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //take user to choose an image
//update the currently selected position so that we assign the imported bitmap to correct item
                currentPic = position;

//take the user to their chosen image selection app (gallery or file manager)
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
//we will handle the returned data in onActivityResult
                startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);

                return true;
            }
        });


        //set the click listener for each item in the thumbnail gallery
        picGallery.setOnItemClickListener(new Gallery.OnItemClickListener() {
            //handle clicks
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //set the larger image view to display the chosen bitmap calling method of adapter class
                picView.setImageBitmap(imgAdapt.getPic(position));
            }
        });


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
                  Log.d("myLogsUri", "got mCurrentNote = " + mCurrentNote.getId());
            }
        }

        /*// TODO экспериментально добавил создание mCurrentNote в onCreate
        if (mCurrentNote == null)
            mCurrentNote = new Note();*/
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentNote != null) {
            populateFields();

           currentPic = 0;
           /*  try {
                if (mCurrentNote.getUriList().size() > 0)
                    for (Uri uri : mCurrentNote.getUriList()) {
                        addImageFromUriToAdapter(uri);
                    }
            } catch (NullPointerException e) {}

             picGalleryInit();*/

            populateImageAdapter();

        }
    }

    void populateImageAdapter() {

       // imgAdapt = new PicAdapter(getContext(), mCurrentNote);

        currentPic = 0;

        imgAdapt.clear();

        try {
            if (mCurrentNote.getUriList().size() > 0)
                for (Uri uri : mCurrentNote.getUriList()) {
                    addImageFromUriToAdapter(uri);
                }
        } catch (NullPointerException e) {}

        picGalleryInit();
    }



    void addImageFromUriToAdapter(Uri uri) {
        Uri addingUri = uri;

        //declare the bitmap
        Bitmap pic = null;

        //declare the path string
        String imgPath = "";

        //retrieve the string using media data
        String[] medData = {MediaStore.Images.Media.DATA};
        //query the data
        /*Cursor picCursor = getContext().getContentResolver().query(addingUri, medData, null, null, null);
        if(picCursor!=null)
        {
            //get the path string
            int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            picCursor.moveToFirst();
            imgPath = picCursor.getString(index);
        }
        else */
        imgPath = addingUri.getPath();


        // picCursor.close();

        // ТУТ НАЧИНАЕТСЯ ДЕКОДИРОВАНИЕ РАЗМЕРА РИСУНКА
        // - может вынести в отдельную процедуру?

        //if we have a new URI attempt to decode the image bitmap
        if (addingUri != null) {
            //set the width and height we want to use as maximum display
            // TODO вынести параметры ресемпла изображений в настройки или в параметры
            int targetWidth = 600;
            int targetHeight = 400;

            //create bitmap options to calculate and use sample size
            BitmapFactory.Options bmpOptions = new BitmapFactory.Options();

            //first decode image dimensions only - not the image bitmap itself
            bmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgPath, bmpOptions);

            //image width and height before sampling
            int currHeight = bmpOptions.outHeight;
            int currWidth = bmpOptions.outWidth;

            //variable to store new sample size
            int sampleSize = 1;

            //calculate the sample size if the existing size is larger than target size
            if (currHeight > targetHeight || currWidth > targetWidth) {
                //use either width or height
                if (currWidth > currHeight)
                    sampleSize = Math.round((float) currHeight / (float) targetHeight);
                else
                    sampleSize = Math.round((float) currWidth / (float) targetWidth);
            }

            //use the new sample size
            bmpOptions.inSampleSize = sampleSize;

            //now decode the bitmap using sample options
            bmpOptions.inJustDecodeBounds = false;

            //get the file as a bitmap
            pic = BitmapFactory.decodeFile(imgPath, bmpOptions);

            //pass bitmap to ImageAdapter to add to array
            imgAdapt.addPic(pic);
//redraw the gallery thumbnails to reflect the new addition
            picGallery.setAdapter(imgAdapt);

            //display the newly selected image at larger size
            picView.setImageBitmap(pic);
//scale options
            picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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

        //TODO убрать в конце тестирования

        mContentEditText.setText(mCurrentNote.getContent()
                /*+ " ------------------------- "
                + mCurrentNote.getUriList().toString()*/);

    }


    private boolean saveNote() {

        String title = mTitleEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            title = "note" + String.valueOf(System.currentTimeMillis());;
            /*mTitleEditText.setError("Title is required");
            return false;*/
        }

        String content = mContentEditText.getText().toString();
        /*if (TextUtils.isEmpty(content)) {
            mContentEditText.setError("Content is required");
            return false;
        }*/


        if (mCurrentNote != null) {

            mCurrentNote.setContent(content);
            mCurrentNote.setTitle(title);

           // это может это делать в onActivityResult??

           // mCurrentNote.setUriList(fragmentArrayListUri);

            Log.d("myLogsUri","mCurrentNote.getUriList().toString() before updating note" + mCurrentNote.getUriList().toString());

            if (fragmentArrayListUri.size() > 0) {
                mCurrentNote.addToUriList(fragmentArrayListUri);

            }

            Log.d("myLogsUri","mCurrentNote.getUriList().toString() after updating note" + mCurrentNote.getUriList().toString());

            NoteManager.newInstance(getActivity()).update(mCurrentNote);

            fragmentArrayListUri.clear();

        } else {

           // Note note = new Note();

            mCurrentNote = new Note();

            mCurrentNote.setTitle(title);
            mCurrentNote.setContent(content);


            mCurrentNote.setUriList(new ArrayList<Uri>());
            // TODO разобраться что сюда добавлять - uriList или fragmentArrayListUri

            Log.d("myLogsUri","mCurrentNote.getUriList().toString() before creating note" + mCurrentNote.getUriList().toString());

            // mCurrentNote = note;

            if (fragmentArrayListUri.size() > 0) {
                mCurrentNote.addToUriList(fragmentArrayListUri);

            }

            Log.d("myLogsUri","mCurrentNote.getUriList().toString() after creating note" + mCurrentNote.getUriList().toString());

           mCurrentNote.setId(NoteManager.newInstance(getActivity()).create(mCurrentNote));

            fragmentArrayListUri.clear();
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
                makeToast(titleOfNoteTobeDeleted + " deleted");
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


        if (requestCode == PHOTO_INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // try {

                ArrayList<Uri> uriListFromPhotoIntent = new ArrayList<>();

                ArrayList<String> stringUriList;

                stringUriList = data.getStringArrayListExtra("uriList");

                Log.d("myLogsUri", "stringUriList.size() in NotePlainEditorFragment = " + stringUriList.size()
                        + ", stringUriList = " + stringUriList.toString());

                // TODO как лучше оформить проверку на наличие чего-либо в получаемом stringUriList
                if (stringUriList.size() > 0) {

                    for (String string : stringUriList) {
                        uriListFromPhotoIntent.add(Uri.parse(string));
                    }

                    Log.d("myLogsUri","uriListFromPhotoIntent.size() = " + uriListFromPhotoIntent.size() + ", uriListFromPhotoIntent = " + uriListFromPhotoIntent.toString());

                    Log.d("myLogsUri","fragmentArrayListUri.size() before addAll = " + fragmentArrayListUri.size() + ", fragmentArrayListUri = " + fragmentArrayListUri.toString());
                    fragmentArrayListUri.addAll(uriListFromPhotoIntent/*.takePhotoActivityUriArrayList*/);
                    Log.d("myLogsUri","fragmentArrayListUri.size() after addAll = " + fragmentArrayListUri.size() + ", fragmentArrayListUri = " + fragmentArrayListUri.toString());


                    saveNote();
                    populateImageAdapter();


                   /* try {
                        if (uriListFromPhotoIntent.size() > 0)

                            imgAdapt.clear();

                        for (Uri uri : uriListFromPhotoIntent) {
                                addImageFromUriToAdapter(uri);
                            }

                        picGalleryInit();

                    } catch (NullPointerException e) {}*/


                }

                // Выводим УРИ полученных фото в содержиое заметки - было нужно для отладки
                /*StringBuilder sb = new StringBuilder();

                for (Uri uri2 : fragmentArrayListUri) {
                    sb.append(uri2.toString() + " .*^*. ");
                }

                mCurrentNote.setContent(mCurrentNote.getContent()
                        /+ " ----GOT_PHOTOS_LIST_FROM_PHOTO_INTENT--------------------- " + sb.toString());*/
            }
            /* catch (NullPointerException e) {

                Toast.makeText(getContext()," null uriListOBJECT got from PhotoActivity",Toast.LENGTH_LONG);
                mCurrentNote.setContent(mCurrentNote.getContent() + " null uriListOBJECT got from PhotoActivity");
                Log.d("myLogs"," null uriListOBJECT got from PhotoActivity");
            }*/

           // mCurrentNote = new Note();
           // Log.d("myLogsUri","mCurrentNote.getUriList() onActivityResult = " + mCurrentNote.getUriList());

            /*if (mCurrentNote.getUriList().size() > 0)
                for (Uri uri : mCurrentNote.getUriList()) {
                    addImageFromUriToAdapter(uri);
                }*/

          //  saveNote(); //



          //  saveNote(); //
           // populateImageAdapter();



        }

        if (resultCode == RESULT_OK) {
            //check if we are returning from picture selection
            if (requestCode == PICKER) {
                //import the image

                //the returned picture URI
                Uri pickedUri = data.getData();

                //declare the bitmap
                Bitmap pic = null;

                //declare the path string
                String imgPath = "";

                //retrieve the string using media data
                String[] medData = {MediaStore.Images.Media.DATA};
                //query the data
                Cursor picCursor = getContext().getContentResolver().query(pickedUri, medData, null, null, null);
                if (picCursor != null) {
                    //get the path string
                    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    picCursor.moveToFirst();
                    imgPath = picCursor.getString(index);
                } else
                    imgPath = pickedUri.getPath();


                picCursor.close();

                // ТУТ НАЧИНАЕТСЯ ДЕКОДИРОВАНИЕ РАЗМЕРА РИСУНКА
                // - может вынести в отдельную процедуру?

                //if we have a new URI attempt to decode the image bitmap
                if (pickedUri != null) {
                    //set the width and height we want to use as maximum display
                    // TODO вынести параметры ресемпла изображений в настройки или в параметры
                    int targetWidth = 600;
                    int targetHeight = 400;

                    //create bitmap options to calculate and use sample size
                    BitmapFactory.Options bmpOptions = new BitmapFactory.Options();

                    //first decode image dimensions only - not the image bitmap itself
                    bmpOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgPath, bmpOptions);

                    //image width and height before sampling
                    int currHeight = bmpOptions.outHeight;
                    int currWidth = bmpOptions.outWidth;

                    //variable to store new sample size
                    int sampleSize = 1;

                    //calculate the sample size if the existing size is larger than target size
                    if (currHeight > targetHeight || currWidth > targetWidth) {
                        //use either width or height
                        if (currWidth > currHeight)
                            sampleSize = Math.round((float) currHeight / (float) targetHeight);
                        else
                            sampleSize = Math.round((float) currWidth / (float) targetWidth);
                    }

                    //use the new sample size
                    bmpOptions.inSampleSize = sampleSize;

                    //now decode the bitmap using sample options
                    bmpOptions.inJustDecodeBounds = false;

                    //get the file as a bitmap
                    pic = BitmapFactory.decodeFile(imgPath, bmpOptions);

                    //pass bitmap to ImageAdapter to add to array
                    imgAdapt.addPic(pic);

//redraw the gallery thumbnails to reflect the new addition
                    picGallery.setAdapter(imgAdapt);

                    //display the newly selected image at larger size
                    picView.setImageBitmap(pic);
//scale options
                    picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }


                picGalleryInit();


            }
        }
    }

}
