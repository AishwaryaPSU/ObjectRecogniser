package objectrecogniserclient.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aishwaryagm.objectrecogniser.ObjectRecogniserAIDL;
import com.aishwaryagm.objectrecogniser.listeners.ImageChildEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ObjectRecogniserService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Info", "ObjectRecogniserService binding successful");
        FirebaseApp.initializeApp(this);
        Log.i("Info", "firebase initialize successful");
        initializeFirebaseListener();
        return objectRecogniserStub;

    }

    private void initializeFirebaseListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference imageRef = database.getReference("images");
        Log.i("INFO",String.format("Image reference %s",imageRef));
        //ImageEventListener imageEventListener = new ImageEventListener();
        //imageRef.addValueEventListener(imageEventListener);
        ImageChildEventListener imageChildEventListener = new ImageChildEventListener();
        imageRef.addChildEventListener(imageChildEventListener);
    }

   ObjectRecogniserAIDL.Stub objectRecogniserStub = new ObjectRecogniserAIDLStubImpl(this);
}
