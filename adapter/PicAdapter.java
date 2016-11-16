package com.okason.simplenotepad.adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.okason.simplenotepad.R;
import com.okason.simplenotepad.activities.NoteEditorActivity;
import com.okason.simplenotepad.fragments.NotePlainEditorFragment;
import com.okason.simplenotepad.models.Note;

public class PicAdapter extends BaseAdapter {

    //use the default gallery background image
    int defaultItemBackground;

    //gallery context
    private Context galleryContext;

    //array to store bitmaps to display
    private Bitmap[] imageBitmaps;

    private final int imageBitmapsArraySize = 9;

    //placeholder bitmap for empty spaces in gallery
    Bitmap placeholder;

    public PicAdapter(Context c,Note mCurrentNote) {

        //instantiate context
        galleryContext = c;

        //create bitmap array
        imageBitmaps  = new Bitmap[/*mCurrentNote.getUriList().size()+1*/ imageBitmapsArraySize];

        //decode the placeholder image
        placeholder = BitmapFactory.decodeResource(galleryContext.getResources(), R.drawable.ic_launcher);

        //set placeholder as all thumbnail images in the gallery initially
        for(int i=0; i<imageBitmaps.length; i++)
            imageBitmaps[i]=placeholder;

        //get the styling attributes - use default Andorid system resources
        TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);

//get the background resource
        defaultItemBackground = styleAttrs.getResourceId(
                R.styleable.PicGallery_android_galleryItemBackground, 0);

//recycle attributes
        styleAttrs.recycle();

    }

    //return number of data items i.e. bitmap images
    public int getCount() {
        return imageBitmaps.length;
    }

    //return item at specified position
    public Object getItem(int position) {
        return position;
    }

    //return item ID at specified position
    public long getItemId(int position) {
        return position;
    }

    //get view specifies layout and display options for each thumbnail in the gallery
    public View getView(int position, View convertView, ViewGroup parent) {

        //create the view
        ImageView imageView = new ImageView(galleryContext);
        //specify the bitmap at this position in the array
        imageView.setImageBitmap(imageBitmaps[position]);
        //set layout options
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
        //scale type within view area
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //set default gallery item background
        imageView.setBackgroundResource(defaultItemBackground);
        //return the view
        return imageView;
    }

    public void clear() {
        imageBitmaps  = new Bitmap[/*mCurrentNote.getUriList().size()+1*/ imageBitmapsArraySize];
        NotePlainEditorFragment.currentPic = 0;
    }

    //helper method to add a bitmap to the gallery when the user chooses one
    public void addPic(Bitmap newPic)
    {
        //set at currently selected index
        imageBitmaps[NotePlainEditorFragment.currentPic] = newPic;
        NotePlainEditorFragment.currentPic++;
    }

    //return bitmap at specified position for larger display
    public Bitmap getPic(int posn)
    {
        //return bitmap at posn index
        return imageBitmaps[posn];
    }
}
