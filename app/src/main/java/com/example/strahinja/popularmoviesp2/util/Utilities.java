package com.example.strahinja.popularmoviesp2.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.model.Review.Review;
import com.example.strahinja.popularmoviesp2.model.Trailer.Trailer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Utilities {
    public static String getSearchCriteria(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(
                context.getResources().getString(R.string.setting_menu_key),
                context.getResources().getString(R.string.SettingEntriesTopRatedValue));
    }

    public static boolean checkNetwork(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
