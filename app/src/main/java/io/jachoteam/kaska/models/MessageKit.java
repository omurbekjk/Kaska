package io.jachoteam.kaska.models;

import android.util.Log;

public final class MessageKit {

    private static final String TAG = "MessageKit";

    public static void logIt(String message){
        Log.d(TAG, "logIt: Hey"+message);
    }
}
