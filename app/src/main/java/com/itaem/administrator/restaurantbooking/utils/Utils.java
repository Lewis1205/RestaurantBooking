package com.itaem.administrator.restaurantbooking.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

public class Utils {
    public static void beArtFront(Context context, TextView textView){
        TypefaceCollection typefaceCollection = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL,Typeface.createFromAsset(context.getAssets(), "front.TTF"))
                .create();
        TypefaceHelper.init(typefaceCollection);
        TypefaceHelper.typeface(textView);
    }
}
