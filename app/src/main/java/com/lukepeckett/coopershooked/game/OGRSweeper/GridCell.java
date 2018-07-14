package com.lukepeckett.coopershooked.game.OGRSweeper;

public class GridCell {

    public static int EMPTY = 0, COVERED_BOMB = 1, UNCOVERED = 2, UNCOVERED_BOMB = 3, FLAGGED_BOMB = 4, FLAGGED_EMPTY = 5;
    private int status = 0;
    private int nearBombCount = 0;
    private boolean hasBomb = false;

    public GridCell(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNearBombCount() {
        return nearBombCount;
    }

    public void setNearBombCount(int nearBombCount) {
        this.nearBombCount = nearBombCount;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }
}
