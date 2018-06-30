package com.objectrecogniser.helpers;

import com.objectrecogniser.callbacks.SimpleCallback;

import java.util.HashMap;

/**
 * Created by aishwaryagm on 6/30/18.
 */

public class SingletonHashMap {

    private static HashMap<String, SimpleCallback> hashMap;

    public static synchronized HashMap<String, SimpleCallback> getHashMap() {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        return hashMap;
    }
}
