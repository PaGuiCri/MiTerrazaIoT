package com.pablo.miterrazaiot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

public class Utilities {
    public static boolean comprobarCampo(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            Log.w(Utilities.class.getSimpleName(), "Campo no encontrado");
            return true;
        }
        return false;
    }
}
