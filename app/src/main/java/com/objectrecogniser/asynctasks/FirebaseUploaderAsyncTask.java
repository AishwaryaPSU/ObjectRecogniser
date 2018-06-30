package com.objectrecogniser.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.objectrecogniser.MainActivity;
import com.objectrecogniser.constants.ApplicationState;

import java.io.ByteArrayOutputStream;
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
        byte[] byteImage = convertBitmapImageToBytes(bitmapImage);
        String[] pathTokens = photoFile.getAbsolutePath().split("/");
        String filename = pathTokens[pathTokens.length-1];
        Log.i("INFO",String.format("The filename obtained from the path is : %s",filename));
        uploadToFirebaseStorage(byteImage,filename);
        return null;
    }

    public byte[] convertBitmapImageToBytes(Bitmap bitmapImage) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return byteStream.toByteArray();
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

    public void uploadToFirebaseStorage(byte[] bytes,String filename){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRecogniserStorageRef = storage.getReference();
        StorageReference imagesFolderRef = imageRecogniserStorageRef.child("images");
        StorageReference imageRef = imagesFolderRef.child(filename);
        UploadTask uploadImageTask = imageRef.putBytes(bytes);
        uploadImageTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("ERROR",String.format("Error uploading the image %s",exception.getMessage()));
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.i("INFO",String.format("upload successful %s",taskSnapshot.getMetadata()));
            }
        });

    }
}
