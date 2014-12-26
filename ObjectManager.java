package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Objects.*;
import com.mygdx.game.Util.Menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.StringTokenizer;

public class ObjectManager
{
    public static final ObjectManager instance = new ObjectManager();
    private static final String SaveFile = "SaveFile/Level 1.txt";
    private static final String LoadFile = "SaveFile/Level 1.txt";
    List<Vector2> locations = new ArrayList<Vector2>();

    private ObjectManager(){}

    public void initialize()
    {
        Game.instance.player = new Player(796, 194);
        Game.instance.objects.add(Game.instance.player);
        loadLevel(Game.instance.objects, 0);
    }
    public void loadLevel(List<GameObject> objects, int levelNumber)
    {
        try
        {
            FileHandle file = Gdx.files.internal(LoadFile);
            BufferedReader reader = new BufferedReader(file.reader());
            StringTokenizer st;
            String line;
            int t, x, y;
            boolean collidable;
            while((line = reader.readLine()) != null)
            {
                st = new StringTokenizer(line, ",");
                t = Integer.parseInt(st.nextToken());
                x = Integer.parseInt(st.nextToken());
                y = Integer.parseInt(st.nextToken());

                if(t == 1)
                    collidable = true;
                else collidable = false;

                x = (x / 24) * 24;
                y = (y / 24) * 24;

                cubes(x, y, objects, collidable);
            }
            reader.close();
        }
        catch(IOException err)
        {
            System.out.println("IOException in GameManager.LoadLevel: " + levelNumber);
        }
        System.out.println("Level " + levelNumber + " loaded");
    }
    public void save(List<GameObject> objects)
    {
        FileHandle filex = Gdx.files.local(SaveFile);
        filex.delete();
        FileHandle file = Gdx.files.local(SaveFile);
        int iterX, iterY, collidable;
        for(GameObject object : objects)
        {
            if(object.getType() == GameObject.ObjectType.CUBE)
            {
                iterX = (int) object.getX();
                iterY = (int) object.getY();
                if(object.getCollidable())
                    collidable = 1;
                else collidable = 0;
                file.writeString(collidable + "," + iterX + "," + iterY + ",\n", true);
            }
        }
        System.out.println("Game state saved.");
    }
    public void cubes(float x, float y, List<GameObject> objects, boolean collidable)
    {
        boolean skip = false;

        int X = (int) (x / 24) * 24;
        int Y = (int) (y / 24) * 24;
        for(GameObject object : objects)
        {
           if(object.getX() == X && object.getY() == Y)
               skip = true;
        }

        if(!skip)
        {
            Cube cube = new Cube(X, Y, ResourceManager.instance.getTexture("New Blocks.png"), collidable);
            objects.add(cube);
            SpatialHashGrid.instance.insertStaticObject(cube);
        }
    }

    //For drawing objects
    public void selectLocation(int x, int y, boolean rectangle)
    {
        //Save location
        if(Menu.instance.buttons.get("Cube").getClicked() && Game.instance.drawing)
        {
            boolean skip = false;
            int X = (x / 24) * 24;
            int Y = (y / 24) * 24;
            for(Vector2 vec : locations)
            {
                if(vec.x == X && vec.y == Y)
                    skip = true;
            }
            if(!skip)
            {
                locations.add(new Vector2(X, Y));
            }
        }
    }
    public void drawLocation()
    {
        for(Vector2 vec : locations)
        {
            GuiManager.instance.drawRectangle((int)vec.x, (int)vec.y, 24, 24);
        }
    }
    public void spawnLocations()
    {
        if(!Game.instance.drawing)
        {
            for(Vector2 vec :  locations)
            {
                cubes(vec.x, vec.y, Game.instance.objects, true);
            }
            locations.clear();
        }
    }

    public void destroyObject(float mouseX, float mouseY, List<GameObject> objects)
    {
        for(Iterator<GameObject> it = objects.iterator(); it.hasNext(); )
        {
            GameObject object = it.next();

            if(mouseX > object.getX() &&
                    mouseY > object.getY() &&
                    mouseX < object.getX() + object.getWidth() &&
                    mouseY < object.getY() + object.getHeight() &&
                    !(object instanceof Player))
            {
                SpatialHashGrid.instance.removeObject(object);
                it.remove();
            }
        }
    }
}

