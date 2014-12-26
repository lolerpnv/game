package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.ResourceManager;

public class Player extends DynamicObject
{
    private TextureRegion textureRegion;
    private static int textureCounter = 0;
    boolean flipAnimation = false;

    public Player(float x, float  y)
    {
        super(x, y, 1.5f, 1.5f, 0, 0, 18, 18);
        setType(ObjectType.PLAYER);
        setTexture(ResourceManager.instance.getTexture("Player.png"));
        //collidable = true;
        maxFrame = 2;
        curFrame = 0;
        frameCount = 0;
        frameDelay = 5;
        animationColumns = 0;
        frameWidth = getTexture().getWidth() / maxFrame;
        frameHeight = getTexture().getHeight() / (animationColumns + 1);
    }
    @Override
    public void render(SpriteBatch batch)
    {
        int fx = curFrame * frameWidth;
        textureRegion = new TextureRegion(texture, fx , 0, frameWidth, frameHeight);
        if(flipAnimation)
            textureRegion.flip(true, false);
        batch.draw(textureRegion, x, y, 18, 18);
    }
    public void update()
    {
        super.update();
        if(dirX > 0)
            flipAnimation = true;
        if(dirX < 0)
            flipAnimation = false;

        if(dirX != 0 || dirY != 0)
            frameCount++;

        if(frameCount > frameDelay)
        {
            frameCount = 0;
            curFrame++;
            if(curFrame == maxFrame)
                curFrame = 0;
        }
    }

    public void moveRight() {dirX = 1f; }
    public void moveLeft()  {dirX = -1f;}
    public  void moveUp()
    {
        dirY = 1f;
    }
    public  void moveDown()
    {
        dirY = -1f;
    }

    public void resetAnimationX()
    {
        dirX = 0;
    }
    public void resetAnimationY()
    {
        dirY = 0;
    }

    public void switchTexture()
    {
        if(++textureCounter > 9)
            textureCounter = 0;
        String name[] = {"Black", "Purple", "Red", "Pink", "Orange", "Olive", "Green", "Turquoise", "Cyan", "Blue"};
        setTexture(ResourceManager.instance.getTexture("Starseed Pilgrim " + name[textureCounter] + ".png"));
    }
    public boolean checkCollision(GameObject otherObject)
    {
        float oX = otherObject.getX();
        float oY = otherObject.getY();

        float obX = otherObject.getWidth();
        float obY = otherObject.getWidth();

        return x + width > oX - obX &&
                x - width < oX + obX &&
                y + height > oY - obY &&
                y - height < oY + obY;
    }
    public void collided(GameObject otherObject)
    {
        if(otherObject.getCollidable())
        {
            float oX = otherObject.getX();
            float oBottom = otherObject.getY();

            float obW = otherObject.getWidth();
            float obH = otherObject.getHeight();

            float Left = x;
            float Right = x + width;
            float Top = y + height;
            float Bottom = y;

            float oLeft = oX;
            float oRight = oX + obW;
            float oTop = oBottom + obH;

            boolean collision = false;

            if(Right - 3 > oLeft && Left + 3 < oRight)                                                                 //In width of object
            {
                if(Bottom < oTop && Top > oTop && !collision)                                                         //Top
                {
                    y = oBottom + obH;
                    collision = true;
                }
                if(Top + velY > oBottom && Bottom < oBottom && !collision)                                            //Bottom
                {
                    y = oBottom - height;
                    collision = true;
                }
            }
            if(Bottom + 1 < oTop && Top > oBottom)                                                                    //In height of object
            {
                if(Right >= oLeft && Left < oLeft && !collision && !(dirX == -1))                                    //Left  !(dirX == -1) == MoveLeft
                {
                    x = oLeft - width;
                    collision = true;
                }
                if(Left <= oRight && Right > oRight && !collision && !(dirX == 1))                                   //Right !(dirX == 1) == MoveRight
                {
                    x = oRight;
                    //collision = true;
                }
            }
        }
    }
}
