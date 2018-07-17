package com.lukepeckett.coopershooked.game.OGRSweeper;

public class Difficulty {

    public static String EASY = "Easy", MEDIUM = "Medium", HARD = "Hard", EXPERT = "Expert", CUSTOM = "Custom";
    public static int NUM_DIFFICULTIES = 4;

    public static class EasyDifficulty {
        public static int WIDTH = 8, HEIGHT = 8, BOMB_COUNT = 10;
    }

    public static class MediumDifficulty {
        public static int WIDTH = 10, HEIGHT = 10, BOMB_COUNT = 25;
    }

    public static class HardDifficulty {
        public static int WIDTH = 12, HEIGHT = 12, BOMB_COUNT = 45;
    }

    public static class ExpertDifficulty {
        public static int WIDTH = 16, HEIGHT = 16, BOMB_COUNT = 64;
    }

}
