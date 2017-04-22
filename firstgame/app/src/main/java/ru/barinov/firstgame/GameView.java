package ru.barinov.firstgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barinov Aleksey on 15.04.2017.
 */
// TODO: 18.04.2017 Нужно создавать объекты, а не обновлять один и тотже.
public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;

    private Thread gameThread = null;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Enemy enemy;
    private Friend friend;

    private List<Star> stars = new ArrayList<>();

    private Boom boom;

    private Point screen;
    private int countMisses;
    private boolean enemyAppeared;
    private boolean isGameOver;

    private int score;
    private int highScore[] = new int[4];

    SharedPreferences sharedPreferences;

    private static MediaPlayer gameOnSound;
    private final MediaPlayer killedEnemySound;
    private final MediaPlayer gameoverSound;

    private Context context;

    public GameView(Context context, Point screen) {
        super(context);

        this.context = context;

        // TODO: 17.04.2017 Включить музыку на последней стадии
        gameOnSound = MediaPlayer.create(context, R.raw.gameon);
        killedEnemySound = MediaPlayer.create(context, R.raw.killedenemy);
        gameoverSound = MediaPlayer.create(context, R.raw.gameover);
        //gameOnSound.start();

        this.screen = screen;
        countMisses = 0;
        isGameOver = false;

        player = new Player(context, screen);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screen.x, screen.y);
            stars.add(s);
        }

        createEnemy();

        createFriend();

        boom = new Boom(context);

        score = 0;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);
    }

    // TODO: 18.04.2017 Создание противника переделать на обнуление параметров, а не создание нового
    public void createEnemy() {
        enemy = new Enemy(context, screen);
    }

    public void createFriend() {
        friend = new Friend(context, screen);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        score++;
        player.update();

        boom.setX(-300);
        boom.setY(-300);

        updateStars();
        updateFriend();
        updateEnemy();
        intersectEnemy();
        intersectFriend();
    }

    private void updateStars() {
        for (Star s : stars) {
            s.update(player.getSpeed());
        }
    }

    private void updateFriend() {
        if (friend.beyondEdge()) {
            friend.kill();
        }
        if (!friend.isAlive()) {
            createFriend();
        }
        friend.setImpactFactor(player.getSpeed());
        friend.update();
    }

    private void updateEnemy() {
        if (enemy.beyondEdge()) {
            countMisses++;
            enemy.kill();
        }
        if (!enemy.isAlive()) {
            createEnemy();
        }
        enemy.setImpactFactor(player.getSpeed());
        enemy.update();
    }

    private void intersectEnemy() {
        // TODO: 16.04.2017 Добавить прибавление очков за сбитого врага
        // TODO: 16.04.2017 Пересмотреть механизм коллизий. Нужно скорее всего не прямоугольником, а круго, так будет логичней
        if (Rect.intersects(player.getDetectCollision(), enemy.getDetectCollision())) {
            boom.setX(enemy.getX());
            boom.setY(enemy.getY());
            killedEnemySound.start();
            //enemy.setX(-300);
            enemy.kill();
        } else {
            if (gameoverCondition()) {
                gameover();
            }
        }
    }

    private boolean gameoverCondition() {
        return countMisses == 3;
    }

    private void intersectFriend() {
        // TODO: 16.04.2017 Отображение взрыва должно осуществляться прямо по центру.
        if (Rect.intersects(player.getDetectCollision(), friend.getDetectCollision())) {
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            gameover();
        }
    }

    private void gameover() {
        playing = false;
        isGameOver = true;

        gameOnSound.stop();
        gameoverSound.start();

        for (int i = 0; i < 4; i++) {
            if (highScore[i] < score) {
                final int finalI = i;
                highScore[i] = score;
                break;
            }
        }

        SharedPreferences.Editor e = sharedPreferences.edit();
        for(int i = 0; i < 4; i++){
            int j = i + 1;
            e.putInt("score" + j, highScore[i]);
        }
        e.apply();
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            drawStars();
            drawMisses();
            drawScore();
            drawPlayer();
            drawFriend();
            drawEnemy();
            drawBoom();
            drawGameOver();

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawStars() {
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);

        for (Star s : stars) {
            paint.setStrokeWidth(s.getStarWidth());
            canvas.drawPoint(s.getX(), s.getY(), paint);
        }
    }

    private void drawScore() {
        paint.setTextSize(20);
        canvas.drawText("Score:" + score, 100, 50, paint);
    }

    private void drawMisses() {
        paint.setTextSize(40);
        canvas.drawText("Misses:" + countMisses, 200, 50, paint);
    }

    private void drawPlayer() {
        //canvas.drawCircle(player.getDetectCollision().centerX(), player.getDetectCollision().centerY(), (player.getBitmap().getHeight() + player.getBitmap().getWidth())/ 4, paint);
        canvas.drawBitmap(
                player.getBitmap(),
                player.getX(),
                player.getY(),
                paint);
    }

    private void drawFriend() {
        canvas.drawBitmap(
                friend.getBitmap(),
                friend.getX(),
                friend.getY(),
                paint);
    }

    private void drawEnemy() {
        canvas.drawBitmap(
                enemy.getBitmap(),
                enemy.getX(),
                enemy.getY(),
                paint);
    }

    private void drawGameOver() {
        if (isGameOver) {
            paint.setTextSize(150);
            paint.setTextAlign(Paint.Align.CENTER);
            int canvasHalfHeight = canvas.getHeight() / 2;
            float paintHalfDistance =  (paint.descent() + paint.ascent()) / 2;
            int yPos = (int) (canvasHalfHeight - paintHalfDistance);
            int xPos = canvas.getWidth() / 2;
            canvas.drawText("Game Over", xPos, yPos, paint);
        }
    }

    private void drawBoom() {
        canvas.drawBitmap(
                boom.getBitmap(),
                boom.getX(),
                boom.getY(),
                paint);
    }
    // TODO: 17.04.2017 Область столкновения определяется следующим образом, в качестве круга.

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        if (isGameOver) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }
        return true;
    }

    public static void stopMusic() {
        gameOnSound.stop();
    }
}
