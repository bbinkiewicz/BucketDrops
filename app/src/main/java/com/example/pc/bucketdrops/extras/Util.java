package com.example.pc.bucketdrops.extras;

import android.view.View;

import java.util.List;



public class Util {

    public static void showViews(List<View> views) {

        for (View v : views) {

            v.setVisibility(View.VISIBLE);

        }
    }

    public static void hideViews(List<View> views) {

        for (View v : views) {

            v.setVisibility(View.GONE);

        }
    }
}
