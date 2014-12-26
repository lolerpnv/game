package com.mygdx.game.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Game;
import com.mygdx.game.GuiManager;
import com.mygdx.game.ObjectManager;
import com.mygdx.game.Objects.Button;
import com.mygdx.game.Objects.GuiObject;
import com.mygdx.game.ResourceManager;

import java.util.HashMap;

public class Menu
{

    public static final Menu instance = new Menu();
    enum Buttons { MENU, OBJECTS, CUBE, DEBUG, SAVE, RES }
    public HashMap<String , GuiObject> buttons = new HashMap<String,  GuiObject>();
    int width = 100;                            //Button width
    int height = 50;                            //Button height
    int startX = Constants.WIDTH - width;       //Starting positon
    int startY = Constants.HEIGHT - height;
    boolean DrawObjectsDrop = false;
    boolean DrawMenuDrop = false;

    public void initialize()
    {
        Texture texture =  ResourceManager.instance.getTexture("Menu Button.png");
        buttons.put("Menu",    new Button(Button.ButtonType.MENU, startX, startY, width, height, 0, 0, texture));
        buttons.put("Objects", new Button(Button.ButtonType.MENU, startX - width, startY, width, height, 0, 0, texture));
        buttons.put("Cube",    new Button(Button.ButtonType.MENU, startX - (2*width), startY, width, height, 0, 0, texture));
        buttons.put("Debug",   new Button(Button.ButtonType.MENU, startX - width, startY - height, width, height, 0, 0, texture));
        buttons.put("Save",    new Button(Button.ButtonType.MENU, startX - width, startY - (2*height), width, height, 0, 0, texture));
        buttons.put("Res",     new Button(Button.ButtonType.MENU, startX - width, startY - (3*height), width, height, 0, 0, texture));

        //Set overlay textures
        for(String name : buttons.keySet())
                buttons.get(name).setOverlayTexture(name);

        buttons.get("Menu").setDraw(true);
    }
    public void render(SpriteBatch batch)
    {
        for(String name : buttons.keySet())
        {
            if(buttons.get(name).getDraw())
                buttons.get(name).render(batch);
        }
    }
    public void update(boolean clickedOnButtons)
    {
        if(clickedOnButtons)
        {
            buttons.get("Objects").setDraw(DrawMenuDrop);
            buttons.get("Debug").setDraw(DrawMenuDrop);
            buttons.get("Save").setDraw(DrawMenuDrop);
            buttons.get("Res").setDraw(DrawMenuDrop);
            buttons.get("Cube").setDraw(DrawObjectsDrop);
            if(!DrawObjectsDrop)
            {
                buttons.get("Cube").setClicked(false);
            }
        }
        else if(buttons.get("Cube").getClicked())
        {
            //Spawn Cubes
            ObjectManager.instance.cubes(Game.instance.getGameInput().x, Game.instance.getGameInput().y, Game.instance.objects, false);
        }
    }
    public void updateState(String name)
    {
        Buttons type = Buttons.valueOf(name.toUpperCase());
        switch (type)
        {
            case MENU:
                DrawMenuDrop = buttons.get(name).getClicked();
                if(!DrawMenuDrop)
                    collapseAll();
                if(GuiManager.instance.debugInfo)
                    buttons.get("Debug").setClicked(true);
                break;
            case OBJECTS:
                DrawObjectsDrop = buttons.get(name).getClicked();
                break;
            case DEBUG:
                GuiManager.instance.debugInfo = !GuiManager.instance.debugInfo;
                break;
            case SAVE:
                ObjectManager.instance.save(Game.instance.objects);
                buttons.get(name).setClicked(false);
                break;
            case RES:
                ResolutionManager.instance.changeResolution();
                buttons.get(name).setClicked(false);
                break;
            default:
                break;
        }
    }
    public void handleInput(Vector3 input)
    {
        int numOfClickedButtons = 0;
        //Decide where input happened, game or button menu
        for(String name : Menu.instance.buttons.keySet())
        {
            GuiObject object = Menu.instance.buttons.get(name);
            if(object.checkCollision(input.x, input.y) && object.getDraw())
            {
                if(object.getClicked())
                    object.setClicked(false);
                else object.setClicked(true);
                Menu.instance.updateState(name);
                numOfClickedButtons++;
            }
        }
        //Update for buttons
        Menu.instance.update(true);
        //Update for screen spawning
        if(numOfClickedButtons == 0)
            Menu.instance.update(false);
    }
    public void collapseAll()
    {
        DrawMenuDrop = false;
        DrawObjectsDrop = false;
        GuiObject object;
        for(String name : Menu.instance.buttons.keySet())
        {
            object = Menu.instance.buttons.get(name);
            object.setClicked(false);
            if(!name.equals("Menu"))
                object.setDraw(false);
        }
    }
}