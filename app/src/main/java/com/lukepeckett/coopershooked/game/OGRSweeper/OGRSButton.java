package com.lukepeckett.coopershooked.game.OGRSweeper;

import android.content.Context;


public class OGRSButton extends android.support.v7.widget.AppCompatButton {

    private int id;

    public OGRSButton(Context context, int id) {
        super(context);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
