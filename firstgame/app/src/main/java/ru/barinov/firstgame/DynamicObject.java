package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;

/**
 * Created by Barinov Aleksey on 17.04.2017.
 */

abstract public class DynamicObject {
    protected Bitmap bitmap;
    protected Point position;
    protected int speed = 1;
    protected int minX;
    protected int maxX;
    protected int maxY;
    protected int minY;
    protected Rect detectCollision;

    public DynamicObject(Context context, int resourceId, Point initial, Point screen) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        detectCollision = new Rect(screen.x, screen.y, bitmap.getWidth(), bitmap.getHeight());
        position = initial;
    }

    public void update() {
        onUpdateAction();
        fillDetectCollision();
    }

    abstract public void onUpdateAction();

    private void fillDetectCollision() {
        detectCollision.left = position.x;
        detectCollision.top = position.y;
        detectCollision.right = position.x + bitmap.getWidth();
        detectCollision.bottom = position.y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return position.x;
    }

    public void setX(int x) {
        position.x = x;
    }

    public int getY() {
        return position.y;
    }

    public int getSpeed() {
        return speed;
    }
}
