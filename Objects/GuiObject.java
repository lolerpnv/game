package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.ResourceManager;

public class GuiObject extends GameObject
{
    public enum GuiType {BUTTON, SCROLL}
    public enum ButtonType {LEFT, RIGHT, JUMP, MENU}
    GuiType type;
    ButtonType buttonType;
    boolean clicked = false;
    boolean draw = false;
    int offsetX;
    int offsetY;

    GuiObject(float x, float y, int width, int height, Texture texture)
    {
        super.init(x, y, width, height, texture);
    }

    public void setClicked(boolean clicked)
    {
        this.clicked = clicked;
        changeTexture();
    }
    public boolean getClicked(){return clicked;}

    public void setDraw(boolean draw){this.draw = draw;}
    public boolean getDraw(){return draw;}

    public int getOffsetX(){return offsetX;}
    public int getOffsetY(){return offsetY;}

    public ButtonType getButtonType() {return buttonType;}

    public void changeTexture()
    {
        if(clicked)
            setTexture(ResourceManager.instance.getTexture("Menu Button Clicked.png"));
        else
            setTexture(ResourceManager.instance.getTexture("Menu Button.png"));
    }
    public void setOverlayTexture(String name){}
    public boolean checkCollision(float inputX , float inputY)
    {
        return  x + width + offsetX > inputX &&
                x - offsetX < inputX &&
                y + height + offsetY > inputY &&
                y - offsetY < inputY ;
    }
}
