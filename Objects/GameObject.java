package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract public class GameObject
{
    public enum ObjectType {PLAYER, CUBE, BALL}

    ObjectType type;
    private boolean alive;
    protected boolean collidable;

    protected float x;
    protected float y;

    protected float velX;
    protected float velY;

    protected float dirX;
    protected float dirY;

    protected float width;
    protected float height;

    protected int maxFrame;
    protected int curFrame;
    protected int frameCount;
    protected int frameDelay;
    protected int frameWidth;
    protected int frameHeight;
    protected int animationColumns;

    protected Texture texture;

    public GameObject(){}

    public void init(float x, float y, int width, int height)
    {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }
    public void init(float x, float y, int width, int height, Texture texture)
    {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.texture = texture;
    }
    public void init(float x, float y, float velX, float velY, int dirX, int dirY, int width, int height)
    {
        this.x = x;
        this.y = y;

        this.velX = velX;
        this.velY = velY;

        this.dirX = dirX;
        this.dirY = dirY;

        this.width = width;
        this.height = height;
    }

    public void update() {}

    public boolean getCollidable(){return collidable;}

    public float getX() {return x;}
    public float getY() {return y;}

    public void setX(float newX) {x = newX;}
    public void setY(float newY) {y = newY;}

    float getVelX() {return velX;}
    float getVelY() {return velY;}

    void setVelX(float newX) {velX = newX;}
    void setVelY(float newY) {velY = newY;}

    public float getWidth() {return width;}
    public float getHeight() {return height;}

    public ObjectType getType() {return this.type;}
    public void setType(ObjectType type) {this.type = type;}

    public void setTexture(Texture tx) {this.texture = tx;}
    public Texture getTexture() {return texture;}

    public boolean checkCollision(GameObject otherObject){return true;}
    public void collided(GameObject otherObject){}
    public void render(SpriteBatch batch){}
}