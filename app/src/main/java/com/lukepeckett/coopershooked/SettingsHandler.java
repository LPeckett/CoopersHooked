package com.lukepeckett.coopershooked;

import java.util.HashMap;

public class SettingsHandler {

    public static String loadStoriesAutomatically = "loadStoriesAutomatically";
    public static String randomExit = "randomExit";

    public static boolean loadStoriesAuto = true;
    public static boolean randomExitBoolean = false;

    public static HashMap<String, Boolean> settingsValues = new HashMap<>();

    public SettingsHandler() {

    }
}
