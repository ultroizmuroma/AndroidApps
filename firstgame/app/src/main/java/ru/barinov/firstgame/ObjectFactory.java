package ru.barinov.firstgame;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by Barinov Aleksey on 22.04.2017.
 */

public class ObjectFactory {
    private Context context;
    private Point screen;

    public ObjectFactory(Context context, Point screen) {
        this.context = context;
        this.screen = screen;
    }
    // TODO: 18.04.2017 Создание новых переделать на обнуление параметров, а не создание нового. Должно уменьшить потребление ресурсов
    public Player createPlayer() {
        return new Player(context, screen);
    }

    public Friend createFriend() {
        return new Friend(context, screen);
    }

    public Enemy createEnemy() {
        return new Enemy(context, screen);
    }

    // TODO: 22.04.2017 Методы обновления и рисования всех объектов произведенных фабрикой 
    public void update() {
        
    }
    
    public void draw() {
        
    }
}
