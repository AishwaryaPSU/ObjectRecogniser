package com.objectrecogniser.listeners;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.objectrecogniser.callbacks.SimpleCallback;
import com.objectrecogniser.helpers.SingletonHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by aishwaryagm on 7/1/18.
 */

public class ImageChildEventListener implements ChildEventListener {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        String datasnapshotKey = dataSnapshot.getKey();
        Log.i("INFO", String.format("datasnapshotKey : %s  ", datasnapshotKey));
        HashMap<String, SimpleCallback> singletonHashMap = SingletonHashMap.getHashMap();
        Log.i("INFO", String.format("singletonHashMap %s ", singletonHashMap));
        SimpleCallback simpleCallback = singletonHashMap.get(datasnapshotKey);
        Object databaseSnapshot = dataSnapshot.getValue();
        Log.i("INFO", String.format("Database snapshot : %s, String s %s", databaseSnapshot, s));
        HashMap<Object, Object> databaseHM = (HashMap<Object, Object>) databaseSnapshot;
        Log.i("INFO", String.format("Database hash Map %s", databaseHM));
        Set<Object> keys = databaseHM.keySet();
        Object key = keys.iterator().next();
        Log.i("INFO", String.format("First key element %s", key));
        HashMap<Object, Object> visionDataHM = (HashMap<Object, Object>) databaseHM.get(key);
        if (visionDataHM.containsKey("labelAnnotations")) {
            List<Object> labelAnnotations = (ArrayList<Object>) visionDataHM.get("labelAnnotations");
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
            if (simpleCallback != null) {
                Log.i("INFO", "About to call update on the call back object");
                simpleCallback.updateUserInterface(imageDescriptionArray);
            } else {
                Log.i("INFO", String.format("SimpleCallback Interface object is null %s", simpleCallback));
            }
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {}
}

