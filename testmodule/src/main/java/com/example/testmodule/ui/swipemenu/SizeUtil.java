package com.example.testmodule.ui.swipemenu;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by mengtao1 on 2018/02/07.
 */

public class SizeUtil {
    //dpi to px
    public static float Dp2Px(Context context, float dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }

    //px to dp
    public static float Px2Dp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }

    //sp to px
    public static float Sp2Px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    //px to sp
    public static float Px2Sp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }
}
