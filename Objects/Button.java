package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ResourceManager;

public class Button extends GuiObject
{
    Texture overlay;
    public Button(ButtonType buttonType, float x, float y, int width, int height,  int offsetX, int offsetY, Texture texture)
    {
        super(x, y, width, height, texture);
        this.type = GuiType.BUTTON;
        this.buttonType = buttonType;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

    }
    public void render(SpriteBatch batch)
    {
        batch.draw(texture, x, y, width, height);
        if(buttonType == ButtonType.MENU)
            batch.draw(overlay, x, y, width, height);
    }
    public void setOverlayTexture(String name)
    {
        overlay = ResourceManager.instance.getTexture(name + ".png");
    }
}
