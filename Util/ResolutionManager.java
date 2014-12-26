package com.mygdx.game.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Game;

public class ResolutionManager
{
    public static final ResolutionManager instance = new ResolutionManager();
    int i = 0;
    Vector2[] resolutionList;
    public void initialize()
    {
        resolutionList = new Vector2[]
                {
//            new Vector2(320,  240),         //not supported
//            new Vector2(400,  240),         //not supported
//            new Vector2(432,  240),         //not supported
//            new Vector2(480,  240),
//            new Vector2(800,  400),
                new Vector2(800,  480),
                new Vector2(854,  480),
                new Vector2(960,  540),
                new Vector2(1024, 600),
                new Vector2(1024, 768),
                new Vector2(1024, 800),
                new Vector2(1280, 768),
                new Vector2(1280, 800),
                new Vector2(1920, 1080),
                };
    }
    public void changeResolution()
    {
        if(++i > resolutionList.length -1)
            i = 0;
        Game.instance.resize((int) resolutionList[i].x, (int) resolutionList[i].y);
        printResolution();
    }
    public void printResolution()
    {
        System.out.println((int)resolutionList[i].x+" x "+(int)resolutionList[i].y+" \t\t" + Gdx.graphics.getWidth()+" x "+Gdx.graphics.getHeight());
    }
}
