package com.objectrecogniser.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.objectrecogniser.MainActivity;
import com.objectrecogniser.constants.ApplicationState;
import java.io.File;
import java.util.Arrays;

/**
 * Created by aishwaryagm on 6/2/18.
 */

public class FirebaseUploaderAsyncTask extends AsyncTask<Void,Void,Void>  {
    private Bitmap bitmapImage;
    private File photoFile ;
    private String[] resultArray = {};
    private TextView resultTextView;
    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private TextView resultTextDescription;

    public FirebaseUploaderAsyncTask(Bitmap bitmapImage, File photoFile , TextView resultTextView, MainActivity mainActivity, ProgressBar progressBar, TextView resultTextDescription){
        this.bitmapImage=bitmapImage;
        this.photoFile=photoFile;
        this.resultTextView=resultTextView;
        this.mainActivity=mainActivity;
        this.progressBar=progressBar;
        this.resultTextDescription=resultTextDescription;
    }

    @Override
    protected Void doInBackground(Void... voids) {
       // TO DO ...
        return null;
    }

    public void print(final String[] resultArray){
        Log.i("INFO",String.format("Inside ImageTransmitter Print method %s",Arrays.toString(resultArray)));
        String preFinalText = "";
        for(String elementOfResultArray : resultArray){
            preFinalText= preFinalText+"\n"+elementOfResultArray;
        }
        final String finalText=preFinalText;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                resultTextView.setVisibility(View.VISIBLE);
                resultTextDescription.setVisibility(View.VISIBLE);
                resultTextView.setMovementMethod(new ScrollingMovementMethod());
                resultTextView.setText(finalText);
                mainActivity.setApplicationState(ApplicationState.INSPECT_OBJECT_FINISHED);
            }
        });
    }
}
