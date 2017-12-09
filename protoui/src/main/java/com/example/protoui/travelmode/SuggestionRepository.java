package com.example.protoui.travelmode;


import android.content.Context;

import com.example.protoui.travelmode.viewmodel.CommuteSuggestion;


/**
 * Repository handling all suggestions.
 */
public class SuggestionRepository {

    public static final int WORK_TO_HOME = 0;
    public static final int HOME_TO_WORK = 1;

    private static SuggestionRepository sInstance;

    private final Context mContext;

    private SuggestionRepository(Context context) {
        mContext = context;
    }

    public static SuggestionRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SuggestionRepository.class) {
                if (sInstance == null) {
                    sInstance = new SuggestionRepository(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public CommuteSuggestion getCommuteSuggestion(int type, boolean withRouteOptions) {
        return new CommuteSuggestion(mContext, type)
                .buildRouteOptions(withRouteOptions)
                .buildPredictedApps(true);
    }

}
