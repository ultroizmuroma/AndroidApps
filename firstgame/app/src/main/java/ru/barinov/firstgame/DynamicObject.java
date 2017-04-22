package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;

import java.util.Random;

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
    protected Point screen;
    protected boolean alive;

    public DynamicObject(Context context, int resourceId, Point initial, Point screen) {
        this.screen = screen;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        detectCollision = new Rect(this.screen.x, this.screen.y, bitmap.getWidth(), bitmap.getHeight());
        position = initial;
        alive = true;
    }

    public void update() {
        onUpdateAction();
        fillDetectCollision();
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(
                getBitmap(),
                getX(),
                getY(),
                paint);
    }

    abstract public void onUpdateAction();

    private void fillDetectCollision() {
        detectCollision.left = position.x;
        detectCollision.top = position.y;
        detectCollision.right = position.x + bitmap.getWidth();
        detectCollision.bottom = position.y + bitmap.getHeight();
    }

    public void refresh() {
        Random generator = new Random();
        int newX = screen.x;
        int newY = generator.nextInt(maxY) - bitmap.getHeight();
        refresh(new Point(newX, newY));
    }

    public void refresh(Point position) {
        alive = true;
        this.position = position;
    }

    public boolean isAlive() {
        return alive;
    }

    // TODO: 18.04.2017 Здесь же и будет обновление объекта
    public void kill() {
        alive = false;
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
