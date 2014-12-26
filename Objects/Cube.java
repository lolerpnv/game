package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cube extends StaticObject
{
    private TextureRegion textureRegion;
    public Cube(float x, float  y, Texture texture, boolean collidable)
    {
        super(x, y, 24, 24);
        setType(ObjectType.CUBE);
        setTexture(texture);
        this.collidable = collidable;

        int maxWidth = 9;
        int  maxHeight = 4;
        curFrame = 0;
        int randomX = (int) (Math.random() * 3)*24;
        int randomY = (int) (Math.random() * 1)*24;
        randomX +=144;
        if(collidable)
        {
            randomX = (int) (Math.random() * 3)*24;
            randomY = (int) (Math.random() * 1)*24;
            randomX += 144;
            randomY += 48;
        }
        frameWidth = getTexture().getWidth() / maxWidth;
        frameHeight = getTexture().getHeight()/ maxHeight;
        textureRegion = new TextureRegion(texture, randomX, randomY, frameWidth, frameHeight);
    }
    @Override
    public void render(SpriteBatch batch)
    {
        batch.draw(textureRegion, x, y,frameWidth, frameHeight);
    }
}