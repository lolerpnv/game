package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Game;
import com.mygdx.game.Objects.Button;
import com.mygdx.game.Objects.GameObject;
import com.mygdx.game.Objects.GuiObject;
import com.mygdx.game.ResourceManager;
import com.mygdx.game.SpatialHashGrid;
import com.mygdx.game.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class GuiManager
{
    public static final GuiManager instance = new GuiManager();
    List<GuiObject> objects = new ArrayList<GuiObject>();
    Button left;
    Button right;
    Button jump;
    BitmapFont font;
    ShapeRenderer shapeRenderer;
    public boolean debugInfo = false;

    private GuiManager(){}

    public void initialize()
    {
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FNT), Gdx.files.internal(Constants.FONT_PNG), false);
        shapeRenderer = new ShapeRenderer();

        //Create buttons
        if(Game.instance.ANDROID)
        {
            left = new Button(Button.ButtonType.LEFT,20,20,40,80,25,15, ResourceManager.instance.getTexture("Left.png"));
            right = new Button(Button.ButtonType.RIGHT,120,20,40,80,25,15,ResourceManager.instance.getTexture("Right.png"));
            jump = new Button(Button.ButtonType.JUMP,700,20,80,80,25,15,ResourceManager.instance.getTexture("Jump.png"));
            objects.add(left);
            objects.add(right);
            objects.add(jump);
        }
    }
    public void handleInput(int screenX, int screenY, boolean setTo, boolean checkAndroid)
    {
        Vector3 input = Game.instance.getScreenInput(screenX, screenY);
        for(GuiObject object : GuiManager.instance.objects)
        {
            boolean collision = object.checkCollision(input.x, input.y);
            if(checkAndroid){
                collision = (Game.instance.ANDROID && !object.checkCollision(input.x, input.y));
            }
            if(collision)
            {
               /* if(object.getButtonType() == Button.ButtonType.LEFT)    // probaj samo u izraz prebaciti
                    Game.instance.left = setTo;*/
            }
        }
    }

    //Draw debug data, and interface ( input buttons )
    public void render(SpriteBatch batch)
    {
        //Draw debug data
        font.drawMultiLine(batch, String.valueOf("FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
                "Obj: " + Game.instance.objects.size() + "\n" +
                "CC: " + SpatialHashGrid.instance.getPotentialColliders(Game.instance.player).size()+ "\n" +
                "x: " + (int) Game.instance.player.getX() + "\n" +
                "y: " + (int) Game.instance.player.getY() + "\n" +
                "Touch x: " + (int) Game.instance.getGameInput().x + "\n" +
                "Touch y: " + (int) Game.instance.getGameInput().y),0,480);

        //Draw Gui
/*        for(GameObject object : objects)
        {
            object.render(batch);
        }*/
    }

    //Draw debug Info such as collision boxes and spatial hash grid
    void drawDebugInfo()
    {
        if(debugInfo)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            //Drawing on game
            shapeRenderer.setProjectionMatrix(Game.instance.cameraGUI.combined);
            inputCollision();
//          menuButtonsCollision();

            //Drawing on screen
            shapeRenderer.setProjectionMatrix(Game.instance.camera.combined);
            objectCollision();
            spatialHashGrid();

            shapeRenderer.end();
        }
    }
    void inputCollision()
    {
        shapeRenderer.setColor(0, 0.9f, 1, 1);
        for(GuiObject object : objects)
            shapeRenderer.rect(object.getX() - object.getOffsetX(), object.getY() - object.getOffsetY(), object.getWidth() + object.getOffsetX() * 2, object.getHeight() + object.getOffsetY() * 2);
    }
    void menuButtonsCollision()
    {
/*            for(String name :  Menu.instance.buttons.keySet())
            shapeRenderer.rect(Menu.instance.buttons.get(name).getX(),
                               Menu.instance.buttons.get(name).getY(),
                               Menu.instance.buttons.get(name).getWidth(),
                               Menu.instance.buttons.get(name).getHeight());*/
    }
    void objectCollision()
    {
        shapeRenderer.setColor(0, 1, 0.5f, 1);
        for(GameObject collider : SpatialHashGrid.instance.getPotentialColliders(Game.instance.player))
        {
            if(collider.getCollidable())
                shapeRenderer.rect(collider.getX()-1,collider.getY()-1, collider.getWidth()+1, collider.getHeight()+1);
        }
    }

    void drawRectangle(int x, int y, int width, int height)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(Game.instance.camera.combined);
        shapeRenderer.setColor(0, 1, 0.5f, 1);
        shapeRenderer.rect(x, y, 25,25);
        shapeRenderer.end();
    }

    public void spatialHashGrid()
    {
        shapeRenderer.setColor(0, 0.9f, 1, 1);
        int w = 0, h = 0;
        int size = 24*5;
        for(int i = 0; i< 17; i++)
        {
            w++;
            h++;
            shapeRenderer.line(size*w,0,size*w,Constants.GRID_WIDTH);       //Vertical
            shapeRenderer.line(0,size*h,Constants.GRID_HEIGHT,size*h);      //Horizontal
        }
    }
}
