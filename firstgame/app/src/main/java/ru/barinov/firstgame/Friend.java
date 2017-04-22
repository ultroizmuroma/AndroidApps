package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by Barinov Aleksey on 16.04.2017.
 */

public class Friend extends DynamicObject {

    private int impactFactor;

    public Friend(Context context, Point screen) {
        super(context, R.drawable.friend, new Point(screen), screen);
        maxX = screen.x;
        maxY = screen.y;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        refresh();
        System.out.println("Friend" + position);
        impactFactor = 0;
    }

    // TODO: 17.04.2017 Переделать на что-то внятное, а то сейчас начинается неразбериха см. setImpactFactor
    @Override
    public void onUpdateAction() {
        position.x -= impactFactor;
        position.x -= speed;
    }

    public void setImpactFactor(int playerSpeed) {
        impactFactor = playerSpeed;
    }

    public boolean beyondEdge() {
        return position.x < 0;
    }
}
