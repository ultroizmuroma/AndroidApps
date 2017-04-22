package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Barinov Aleksey on 16.04.2017.
 */

public class Boom {
    private Bitmap bitmap;

    private int x;
    private int y;

    public Boom(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);

        x = -300;
        y = -300;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
