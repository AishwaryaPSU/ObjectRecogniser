package com.objectrecogniser.callbacks;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by aishwaryagm on 6/30/18.
 */

public class ImageDescriptionCallback implements SimpleCallback {
    private TextView imageDescriptionTextView;

    public ImageDescriptionCallback(TextView imageDescriptionTextView){
        this.imageDescriptionTextView = imageDescriptionTextView;
    }
    @Override
    public void updateUserInterface(String[] imageDescriptionArray) {
        Log.i("INFO",String.format("imageDescriptionArray is %s", Arrays.toString(imageDescriptionArray)));
        String imageDescriptionResult = "";
        for(String imageDesciption : imageDescriptionArray){
            imageDescriptionResult = imageDescriptionResult.concat(imageDesciption).concat("\n");
        }
        Log.i("INFO",String.format("imageDescriptionResult is %s", imageDescriptionResult));
        imageDescriptionTextView.setText(imageDescriptionResult);
    }
}
