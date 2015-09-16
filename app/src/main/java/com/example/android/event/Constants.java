package com.example.android.event;

import android.app.Application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Constants extends Application{

    private int toolbarColor = -1;
    private int statusBarColor = -1;
    private int popupHeaderClor = -1;

    public int getPopupHeaderClor() {
        return popupHeaderClor;
    }

    public void setPopupHeaderClor(int popupHeaderClor) {
        this.popupHeaderClor = popupHeaderClor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }

    public static final Map<Integer, Integer> TOOLBAR_COLOR;

    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(0, R.color.red);
        map.put(1, R.color.pink);
        map.put(2, R.color.purple);
        map.put(3, R.color.indigo);
        map.put(4, R.color.blue);
        map.put(5, R.color.light_blue);
        map.put(6, R.color.cyan);
        map.put(7, R.color.teal);
        map.put(8, R.color.green);
        map.put(9, R.color.light_green);
        map.put(10, R.color.lime);
        map.put(11, R.color.yellow);
        map.put(12, R.color.amber);
        map.put(13, R.color.orange);
        map.put(14, R.color.deep_orange);
        map.put(15, R.color.brown);
        map.put(16, R.color.grey);
        map.put(17, R.color.blue_grey);
        TOOLBAR_COLOR = Collections.unmodifiableMap(map);
    }

    public static final Map<Integer, Integer> PUPUP_COLOR;

    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(0, R.color.medium_red);
        map.put(1, R.color.medium_pink);
        map.put(2, R.color.medium_purple);
        map.put(3, R.color.medium_indigo);
        map.put(4, R.color.medium_blue);
        map.put(5, R.color.medium_light_blue);
        map.put(6, R.color.medium_cyan);
        map.put(7, R.color.medium_teal);
        map.put(8, R.color.medium_green);
        map.put(9, R.color.medium_light_green);
        map.put(10, R.color.medium_lime);
        map.put(11, R.color.medium_yellow);
        map.put(12, R.color.medium_amber);
        map.put(13, R.color.medium_orange);
        map.put(14, R.color.medium_deep_orange);
        map.put(15, R.color.medium_brown);
        map.put(16, R.color.medium_grey);
        map.put(17, R.color.medium_blue_grey);
        PUPUP_COLOR = Collections.unmodifiableMap(map);
    }

    public static final Map<Integer, Integer> STATUS_BAR_COLOR;

    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(0, R.color.dark_red);
        map.put(1, R.color.dark_pink);
        map.put(2, R.color.dark_purple);
        map.put(3, R.color.dark_indigo);
        map.put(4, R.color.dark_blue);
        map.put(5, R.color.dark_light_blue);
        map.put(6, R.color.dark_cyan);
        map.put(7, R.color.dark_teal);
        map.put(8, R.color.dark_green);
        map.put(9, R.color.dark_light_green);
        map.put(10, R.color.dark_lime);
        map.put(11, R.color.dark_yellow);
        map.put(12, R.color.dark_amber);
        map.put(13, R.color.dark_orange);
        map.put(14, R.color.dark_deep_orange);
        map.put(15, R.color.dark_brown);
        map.put(16, R.color.dark_grey);
        map.put(17, R.color.dark_blue_grey);
        STATUS_BAR_COLOR = Collections.unmodifiableMap(map);
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
