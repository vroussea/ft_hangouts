package com.vroussea.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.vroussea.myapplication.App;
import com.vroussea.myapplication.R;

public class Colors {
    private final Context context = App.getContext();

    private final String blue = context.getString(R.string.blue);
    private final String darkBlue = context.getString(R.string.darkBlue);
    private final String red = context.getString(R.string.red);
    private final String green = context.getString(R.string.green);
    private final String yellow = context.getString(R.string.yellow);
    private final String black = context.getString(R.string.black);

    private final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    private final String menuColor = prefs.getString(context.getString(R.string.pref_color), blue);

    public Drawable getActionBarColor() throws NullPointerException {
        if (menuColor.equals(darkBlue))
            return new ColorDrawable(context.getColor(R.color.colorPrimaryDark));
        else if (menuColor.equals(red))
            return new ColorDrawable(context.getColor(R.color.red));
        else if (menuColor.equals(green))
            return new ColorDrawable(context.getColor(R.color.green));
        else if (menuColor.equals(yellow))
            return new ColorDrawable(context.getColor(R.color.yellow));
        else if (menuColor.equals(black))
            return new ColorDrawable(context.getColor(R.color.black));
        return new ColorDrawable(context.getColor(R.color.colorPrimary));
    }

    public int getSatusBarColor() throws NullPointerException {
        if (menuColor.equals(darkBlue))
            return R.color.colorPrimaryDark;
        else if (menuColor.equals(red))
            return R.color.red;
        else if (menuColor.equals(green))
            return R.color.green;
        else if (menuColor.equals(yellow))
            return R.color.yellow;
        else if (menuColor.equals(black))
            return R.color.black;
        return R.color.colorPrimary;
    }
}
