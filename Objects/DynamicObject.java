package com.mygdx.game.Objects;

public class DynamicObject extends GameObject
{
    public DynamicObject(float x, float y, float velX, float velY, int dirX, int dirY, int  width, int  height)
    {
        super.init(x, y, velX, velY, dirX, dirY,  width, height);
    }
    @Override
    public void update()
    {
        x += velX * dirX;
        y += velY * dirY;
    }
}
