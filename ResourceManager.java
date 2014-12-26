package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class ResourceManager
{
    public static final ResourceManager instance = new ResourceManager();
    private static final String GFXPATH = "Graphics/Textures/";
    String path;
    HashMap<String , Texture> textures = new HashMap<String, Texture>();
    private ResourceManager(){}
    public void initialize()
    {
        // Clears out HashMap, added for resume()
        textures.clear();

        // Player Skins
        path = "Player/";
        loadTexture("Player.png");

        // Objects
        path = "Objects/";
        loadTexture("New Blocks.png");

        // Input
        path = "Gui/";
        loadTexture("Left.png");
        loadTexture("Right.png");
        loadTexture("Jump.png");

        // Menu
        path = "Menu/";
        loadTexture("Menu Button.png");
        loadTexture("Menu Button Clicked.png");
        loadTexture("Menu.png");
        loadTexture("Objects.png");
        loadTexture("Cube.png");
        loadTexture("Debug.png");
        loadTexture("Save.png");
        loadTexture("Res.png");
    }
    boolean loadTexture(String filename)
    {
        if(textures.containsKey(filename))// != textures.end()
        {
            System.out.println(filename + " has already been loaded!");
            return false;
        }
        Texture t = new Texture(Gdx.files.internal(GFXPATH + path + filename));
        textures.put(filename, t);
        return true;
    }
    public Texture getTexture(String filename)
    {
        return textures.get(filename);
    }
    void dispose()
    {
        for(Texture texture : textures.values())
        {
            texture.dispose();
        }
    }
}
