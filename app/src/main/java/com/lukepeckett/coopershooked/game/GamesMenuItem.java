package com.lukepeckett.coopershooked.game;

import com.lukepeckett.coopershooked.GamesMenu;

public class GamesMenuItem {

    private int iconId;
    private String name;

    public GamesMenuItem(String name, int iconId) {
        this.iconId = iconId;
        this.name = name;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
