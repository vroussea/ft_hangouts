package com.vroussea.myapplication;


import android.app.Application;
import android.widget.Toast;

import com.vroussea.myapplication.handlers.AppLifecycleHandler;
import com.vroussea.myapplication.handlers.LifecycleDelegate;

import java.util.Calendar;

public class App extends Application implements LifecycleDelegate {
    private static App myContext;

    Calendar currentTime = Calendar.getInstance();

    public static App getContext() {
        return myContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myContext = this;

        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
    }

    @Override
    public void onAppBackgrounded() {
        currentTime = Calendar.getInstance();
    }

    @Override
    public void onAppForegrounded() {
        StringBuilder toastText = new StringBuilder();
        toastText.append(currentTime.get(Calendar.HOUR));
        toastText.append("H ");
        toastText.append(currentTime.get(Calendar.MINUTE));
        toastText.append("m ");
        toastText.append(currentTime.get(Calendar.SECOND));
        toastText.append("s.");
        Toast toast = Toast.makeText(myContext, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
}