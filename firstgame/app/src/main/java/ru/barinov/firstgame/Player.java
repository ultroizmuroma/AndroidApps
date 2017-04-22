package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by Barinov Aleksey on 15.04.2017.
 */

public class Player extends DynamicObject {

    private boolean boosting;

    private final int GRAVITY = -10;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    public Player(Context context, Point screen) {
        super(context, R.drawable.player, new Point(75, 50), screen);
        maxY = screen.y - bitmap.getHeight();
        minY = 0;
        speed = 1;
        boosting = false;
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    @Override
    public void onUpdateAction() {
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        position.y -= speed + GRAVITY;
        if (position.y < minY) {
            position.y = minY;
        }

        if (position.y > maxY) {
            position.y = maxY;
        }
    }
}
