package com.scholarlyapps.pathlingo.ui.utils;

import android.app.Activity;

import com.scholarlyapps.pathlingo.R;

public class NavAnim {

    public static void slideForward(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void slideBack(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
