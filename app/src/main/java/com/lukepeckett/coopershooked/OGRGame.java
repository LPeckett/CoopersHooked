package com.lukepeckett.coopershooked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class OGRGame extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    private GameView view;
    private RelativeLayout relativeLayout;

    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton backButton;
    private ImageButton pauseButton;
    private TextView scoreText;

    private Thread gameThread;

    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrgame);

        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadComponents();
        playGame();
    }

    public static Intent makeIntent(Context c) {
        Intent i = new Intent(c, OGRGame.class);
        return i;
    }

    private void playGame() {
        gameThread = new Thread() {
            public void run() {
                long currentTime;
                long lastTime = System.nanoTime();
                int fps = 7;
                float delta = 0;
                float frameTime = 1000000000 / fps;
                while(!isFinishing()) {
                    currentTime = System.nanoTime();
                    delta += (currentTime - lastTime) / frameTime;
                    if(delta >= 1) {
                        view.invalidate();
                        delta --;
                    }
                    lastTime = currentTime;
                }
            }
        };

        gameThread.start();

    }

    private void loadComponents() {

        scoreText = findViewById(R.id.ogrScoreText);
        scoreText.setText("Score: 0");

        relativeLayout = findViewById(R.id.ogrlayout);
        view = new GameView(this, scoreText);
        view.setBackgroundColor(Color.LTGRAY);
        relativeLayout.addView(view);

        upButton = findViewById(R.id.ogrUpButton);
        upButton.setOnTouchListener(this);
        upButton.setOnClickListener(this);

        downButton = findViewById(R.id.ogrDownButton);
        downButton.setOnTouchListener(this);
        downButton.setOnClickListener(this);

        rightButton = findViewById(R.id.ogrRightButton);
        rightButton.setOnTouchListener(this);
        rightButton.setOnClickListener(this);

        leftButton = findViewById(R.id.ogrLeftButton);
        leftButton.setOnTouchListener(this);
        leftButton.setOnClickListener(this);

        backButton = findViewById(R.id.ogrBackButton);
        backButton.setOnClickListener(this);
        backButton.setOnTouchListener(this);

        pauseButton = findViewById(R.id.ogrPauseButton);
        pauseButton.setOnTouchListener(this);
        pauseButton.setOnClickListener(this);

    }

    private void pausePressed() {
        if(paused) {
            pauseButton.setImageResource(R.drawable.ic_pause_64);
            paused = false;
            view.setPaused(paused);
        }
        else {
            pauseButton.setImageResource(R.drawable.ic_play_64);
            paused = true;
            view.setPaused(paused);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ogrBackButton:
                backPressed();
                return;

            case R.id.ogrDownButton:
                view.setVelX(0);
                view.setVelY(1);
                return;

            case R.id.ogrUpButton:
                view.setVelX(0);
                view.setVelY(-1);
                return;

            case R.id.ogrLeftButton:
                view.setVelX(-1);
                view.setVelY(0);
                return;

            case R.id.ogrRightButton:
                view.setVelX(1);
                view.setVelY(0);
                return;

            case R.id.ogrPauseButton:
                pausePressed();
                return;
        }
    }

    private void backPressed() {
        if(SettingsHandler.randomExitBoolean) {
            Random rand = new Random();
            int randInt = rand.nextInt(5);
            if(randInt == 0) {
                Intent closeIntent = new Intent(Intent.ACTION_MAIN);
                closeIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(closeIntent);
            }
            else {
                finish();
            }
        }
        else {
            finish();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.getBackground().setColorFilter(0xe0dddddd, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;

            case MotionEvent.ACTION_UP:
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
        }
        return false;
    }

    public static class GameView extends View {

        private int xPos = 150;
        private int yPos = 150;
        private int velX = 0;
        private int velY = 0;
        private int prevVelX = 2;
        private int prevVelY = 2;
        private int snakeWidth = 50;
        private boolean paused = false;

        private ArrayList<RectF> snakeParts = new ArrayList<>();

        private RectF apple;
        private int score = 0;
        private boolean gameOver = false;
        private boolean addSnakePart = false;

        private TextView scoreView;

        private Paint paint;

        private Random random;

        public GameView(Context c, TextView scoreText) {
            super(c);
            setMinimumWidth(300);
            setMinimumHeight(300);
            snakeParts.add(new RectF(xPos, yPos, xPos + snakeWidth, yPos + snakeWidth));
            paint = new Paint();
            random = new Random();
            this.scoreView = scoreText;
        }

        @Override
        public void onDraw(Canvas canvas) {
            tick();
            render(canvas);
        }

        private void tick() {

            if(gameOver) {
                return;
            }

            if(paused) {
                return;
            }

            int screenWidth = this.getWidth();

            if(apple == null) {
                int randXPos = random.nextInt((int)(screenWidth / snakeWidth));
                int randYPos = random.nextInt((int)(screenWidth / snakeWidth));
                apple = new RectF(randXPos * snakeWidth, randYPos * snakeWidth, randXPos * snakeWidth + snakeWidth, randYPos * snakeWidth + snakeWidth);
            }

            for(int i = 1; i < snakeParts.size(); i++) {
                if(snakeParts.get(0).left == snakeParts.get(i).left && snakeParts.get(0).top == snakeParts.get(i).top) {
                    gameOver = true;
                    return;
                }
            }
            if(xPos + snakeWidth > screenWidth || xPos < 0) {
                gameOver = true;
            }
            if(yPos + snakeWidth > screenWidth || yPos < 0) {
                gameOver = true;
            }

            if(snakeParts.get(0).bottom == apple.bottom && snakeParts.get(0).left == apple.left) {
                score ++;
                scoreView.setText("Score: " + String.valueOf(score));
                int randXPos = random.nextInt((int)(screenWidth / snakeWidth));
                int randYPos = random.nextInt((int)(screenWidth / snakeWidth));
                apple = new RectF(randXPos * snakeWidth, randYPos * snakeWidth, randXPos * snakeWidth + snakeWidth, randYPos * snakeWidth + snakeWidth);
                addSnakePart = true;
            }

            xPos += velX * snakeWidth;
            yPos += velY * snakeWidth;

            snakeParts.add(0, new RectF(xPos, yPos, xPos + snakeWidth, yPos + snakeWidth));
            if(!addSnakePart)
                snakeParts.remove(snakeParts.size() - 1);
            else
                addSnakePart = false;
        }

        private void render(Canvas canvas) {
            paint.setColor(Color.RED);
            for(RectF r : snakeParts) {
                canvas.drawRect(r, paint);
            }
            paint.setColor(Color.BLUE);
            canvas.drawRect(apple, paint);
        }

        public void setVelX(int x) {
            this.velX = x;
        }

        public void setVelY(int y) {
            this.velY = y;
        }

        public void setPaused(boolean paused) {
            this.paused = paused;
        }

    }

}
