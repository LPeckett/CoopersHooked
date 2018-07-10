package com.lukepeckett.coopershooked.game.OGRSweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class OGRSGridAdapter extends BaseAdapter {

    private Context context;
    private List<ArrayList<Button>> buttons;

    public OGRSGridAdapter(Context c, List<ArrayList<Button>> buttons) {
        this.context = c;
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size() * buttons.size();
    }

    @Override
    public Object getItem(int position) {
        int row = position / buttons.size();
        int col = position % buttons.size();
        return buttons.get(col).get(row);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int row = position / buttons.size();
        int col = position % buttons.size();

        return buttons.get(col).get(row);
    }
}
