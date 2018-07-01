package com.objectrecogniser.listeners;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.objectrecogniser.callbacks.SimpleCallback;
import com.objectrecogniser.helpers.SingletonHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by aishwaryagm on 6/30/18.
 */

public class ImageEventListener implements ValueEventListener {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Object databaseSnapshot = dataSnapshot.getValue();
        if (databaseSnapshot == null) {
            return;
        }
        Log.i("INFO", String.format("Database snapshot : %s", databaseSnapshot));
        HashMap<Object, Object> databaseHM = (HashMap<Object, Object>) databaseSnapshot;
        Log.i("INFO", String.format("Database hash Map %s", databaseHM));
        Set<Object> keys = databaseHM.keySet();
        Object key = keys.iterator().next();
        Log.i("INFO", String.format("First key element %s", key));
        HashMap<String, SimpleCallback> singletonHashMap = SingletonHashMap.getHashMap();
        Log.i("INFO", String.format("singletonHashMap %s ", singletonHashMap));
        SimpleCallback callBackInterfaceObject = singletonHashMap.get(key.toString());
        HashMap<Object, Object> visionDataHM = (HashMap<Object, Object>) databaseHM.get(key);
        Log.i("INFO", String.format("visionDataHM %s", visionDataHM));
        Set<Object> visiondataHMkeys = visionDataHM.keySet();
        Object visiondataKey = visiondataHMkeys.iterator().next();
        HashMap<Object, Object> visionData = (HashMap<Object, Object>) visionDataHM.get(visiondataKey);
        List<Object> labelAnnotations = (ArrayList<Object>) visionData.get("labelAnnotations");
        Log.i("INFO", String.format("labelAnnotations : %s", labelAnnotations));
        List<String> descriptions = new ArrayList<>();
        for (Object labelAnnotation : labelAnnotations) {
            HashMap<Object, Object> labelAnnotationHM = (HashMap<Object, Object>) labelAnnotation;
            String description = labelAnnotationHM.get("description").toString();
            descriptions.add(description);
        }
        Log.i("INFO", String.format("Descrptions : %s", descriptions));
        String[] imageDescriptionArray = new String[descriptions.size()];
        int count = 0;
        for (Object description : descriptions) {
            imageDescriptionArray[count++] = description.toString();
        }
        Log.i("INFO", String.format("ImageDescription %s", Arrays.toString(imageDescriptionArray)));
        Log.i("INFO", String.format("Label annotations data %s", labelAnnotations));
        if (callBackInterfaceObject != null) {
            Log.i("INFO", "About to call update on the call back object");
            callBackInterfaceObject.updateUserInterface(imageDescriptionArray);
        } else {
            Log.i("INFO", String.format("Callback Interface object is null %s", callBackInterfaceObject));
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e("ERROR", String.format("Image Ref database error : %s", databaseError.getMessage()));
        databaseError.toException().printStackTrace();
    }
}

