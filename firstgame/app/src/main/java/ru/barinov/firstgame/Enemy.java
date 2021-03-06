package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by Barinov Aleksey on 16.04.2017.
 */
// TODO: 17.04.2017 Переделать по аналогии с классом Friend 
public class Enemy extends DynamicObject {

    private int impactFactor;

    private boolean alive;

    public Enemy(Context context, Point screen) {
        super(context, R.drawable.enemy, new Point(screen), screen);
        maxX = screen.x;
        maxY = screen.y;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        position.x = screen.x;
        position.y = generator.nextInt(maxY) - bitmap.getHeight();
        System.out.println("Enemy" + position);
        impactFactor = 0;
        alive = true;
    }

    @Override
    public void onUpdateAction() {
        position.x -= impactFactor;
        position.x -= speed;
//        if (position.x < minX - bitmap.getWidth()) {
//            Random generator = new Random();
//            speed = generator.nextInt(6) + 10;
//            position.x = maxX;
//            position.y = generator.nextInt(maxY) - bitmap.getHeight();
//            System.out.println("Enemy" + position);
//        }
    }

    public void setImpactFactor(int playerSpeed) {
        impactFactor = playerSpeed;
    }

    public boolean isAlive() {
        return alive;
    }

    // TODO: 18.04.2017 Здесь же и будет обновление объекта 
    public void kill() {
        alive = false;
    }

    public boolean beyondEdge() {
        return position.x < 0;
    }
}
