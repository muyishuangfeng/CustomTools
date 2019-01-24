package com.yk.customsdk.customlibiary;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class TabLayoutUtil {

    public static void setTabWidth(final Context context, final TabLayout tab){
        tab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Class<?> tabLayout = tab.getClass();
                Field tabStrip = null;
                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                tabStrip.setAccessible(true);
                LinearLayout ll_tab = null;
                try {
                    ll_tab = (LinearLayout) tabStrip.get(tab);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                int maxLen = 0;
                int maxTextSize = 0;
                int tabCount = ll_tab.getChildCount();
                for (int i = 0; i < tabCount; i++) {
                    View child = ll_tab.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    if (child instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) child;
                        for (int j = 0; j < ll_tab.getChildCount(); j++) {
                            if (viewGroup.getChildAt(j) instanceof TextView) {
                                TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                int length = tabTextView.getText().length();
                                maxTextSize = (int) tabTextView.getTextSize() > maxTextSize ? (int) tabTextView.getTextSize() : maxTextSize;
                                maxLen = length > maxLen ? length : maxLen;
                            }
                        }

                    }

                    int margin = (tab.getWidth() / tabCount - (maxTextSize + DensityUtils.dp2px(context,2)) * maxLen) / 2 - DensityUtils.dp2px(context,2);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = margin;
                    params.rightMargin = margin;
                    child.setLayoutParams(params);
                    child.invalidate();
                }


            }
        });

    }
}
