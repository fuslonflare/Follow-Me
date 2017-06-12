package com.example.phuwarin.followme.util;

import android.widget.Toast;

import com.example.phuwarin.followme.manager.ContextBuilder;

/**
 * Created by Phuwarin on 6/10/2017.
 */

public class Utility {
    public static void showToast(String message) {
        Toast.makeText(ContextBuilder.getInstance().getContext(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
