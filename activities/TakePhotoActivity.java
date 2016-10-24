package com.okason.simplenotepad.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.okason.simplenotepad.R;

import java.io.File;
import java.io.OutputStream;


public class TakePhotoActivity extends AppCompatActivity {


    ImageButton capture, recapture, save;
    CameraPreview preview;
    Camera mCamera;
    FrameLayout mFrame;
    Context mContext;

    LinearLayout mButtons;

    Intent intent;
    byte[] pictureData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        intent = getIntent();

        mButtons = (LinearLayout) findViewById(R.id.buttons);

        mContext = this;

       /* mCamera = openCamera(); //1
        if (mCamera == null) { //2
            Toast.makeText(this, "Opening camera failed", Toast.LENGTH_LONG).show();
            return;
        }


        preview = new CameraPreview(this, mCamera); //3
        mFrame = (FrameLayout) findViewById(R.id.layout); //4
        mFrame.addView(preview, 0);*/

        capture = (ImageButton) findViewById(R.id.capture); //5
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // делаем снимок
                mCamera.takePicture(null, null, null, mPictureCallback);
            }
        });

        recapture = (ImageButton) findViewById(R.id.recapture);
        recapture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideConfirm();
                        mCamera.startPreview();
                        pictureData = null;
                    }
                }
        );

        save = (ImageButton) findViewById(R.id.finish);
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri pictureFile;



                        try {
                            pictureFile = generateFile();
                            savePhotoInFile(pictureData, pictureFile);
                            // TODO передать имя файла в NoteActivity

                            Intent intent = new Intent();
                            intent.putExtra("pictureFileUri", pictureFile.toString());
                            setResult(RESULT_OK, intent);

                            Toast.makeText(mContext, "Save file: " + pictureFile, Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
                            Toast.makeText(mContext, "Error: can't save file", Toast.LENGTH_LONG).show();
                        }


                        mCamera.startPreview();

                        hideConfirm();

                        finish();

			        	/*if (intent.hasExtra(MediaStore.EXTRA_OUTPUT)) {
			        		pictureFile = (Uri)intent.getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);
			        	}
			        	else
			        		pictureFile = generateFile();

			    		try {
							savePhotoInFile (pictureData, pictureFile);
							intent.putExtra("data", pictureData);
				    		intent.setData(pictureFile);
				    		setResult(RESULT_OK, intent);
						} catch (Exception e) {
							setResult(2, intent);
						}
			    		mCamera.release();
			            finish();  */
                    }
                }
        );
    }

    private Camera openCamera() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) return null;
        Camera cam = null;
        if (Camera.getNumberOfCameras() > 0) {
            try {
                cam = Camera.open(0);
            } catch (Exception exc) { //
            }
        }
        return cam;
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

           /* if (intent.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
                pictureData = data;
                showConfirm();
            } else {*/

            pictureData = data;
            showConfirm();


           // }

        }
    };

    private Uri generateFile() {
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

    private void savePhotoInFile(byte[] data, Uri pictureFile) throws Exception {
        if (pictureFile == null) throw new Exception();
        OutputStream os = getContentResolver().openOutputStream(pictureFile);
        os.write(data);
        os.close();
    }

    private void showConfirm() {
        capture.setVisibility(View.INVISIBLE);
        mButtons.setVisibility(View.VISIBLE);
    }

    private void hideConfirm() {
        mButtons.setVisibility(View.INVISIBLE);
        capture.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() { //6
        super.onPause();
       /* if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }*/

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mFrame.removeView(preview);
            mCamera = null;
            preview = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = openCamera();
        if (mCamera == null) {
            Toast.makeText(this, "Opening camera failed", Toast.LENGTH_LONG).show();
            return;
        }

        preview = new CameraPreview(this, mCamera);
        mFrame = (FrameLayout) findViewById(R.id.layout);
        mFrame.addView(preview, 0);
    }
}