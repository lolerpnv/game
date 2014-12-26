package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Objects.*;
import com.mygdx.game.Util.Constants;
import com.mygdx.game.Util.Menu;
import com.mygdx.game.Util.ResolutionManager;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    public static final Game instance = new Game();

    //Game Variables
    boolean ANDROID = true;
    public boolean drawing = false;
    boolean right = false, left = false, up = false, down = false;
    private double accumulator = 0;
    private final double timestep = 1.0/60.0;
    public List<GameObject> objects = new ArrayList<GameObject>();
    public Player player;

    //Camera
    OrthographicCamera camera;
    OrthographicCamera cameraGUI;
    FillViewport viewport;
    StretchViewport GuiViewport;
    Vector2 cameraLerp;
    Vector3 lerpTarget;
    SpriteBatch batch;
    //SpatialHashGrid
    List<GameObject> colliders;

    private Game(){}
    //============================================================================================================
    //                                                GAME
    //============================================================================================================
    public void initialize()
    {
        ResourceManager.instance.initialize();
        ObjectManager.instance.initialize();
        GuiManager.instance.initialize();
        Menu.instance.initialize();

        SpatialHashGrid.instance.initialize();
        loadCamera();

        ResolutionManager.instance.initialize();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            ANDROID = true;
    }
    public void checkCollision()
    {
        for(GameObject object : objects)
        {
            if(object instanceof DynamicObject)
            {
                SpatialHashGrid.instance.removeObject(object); //removes 1 object at a time
                object.update();
                SpatialHashGrid.instance.insertDynamicObject(object);
                colliders = SpatialHashGrid.instance.getPotentialColliders(object);
                for(GameObject collider : colliders)
                {
                    if(object != collider && object.checkCollision(collider))
                        object.collided(collider);
                }
            }
        }
    }
    public void update(float deltaTime)
    {
        accumulator += deltaTime;
        while(accumulator >= timestep)
        {
            accumulator -= timestep;
            checkCollision();
        }
        //INPUT==============================================================================================
        if(left)
            player.moveLeft();
        else if(right)
            player.moveRight();
        else player.resetAnimationX();

        if(up)
            player.moveUp();
        else if(down)
            player.moveDown();
        else player.resetAnimationY();

        if(ANDROID)
        {
            if(Gdx.input.getAccelerometerX() < 0)
                player.switchTexture();
        }

        ObjectManager.instance.spawnLocations();
        //CAMERA======================================================================================
        cameraLerp = new Vector2((int) player.getX(), (int) player.getY());
        //camera.position.lerp( lerpTarget.set( cameraLerp.x,  cameraLerp.y, 0), 2f * Gdx.graphics.getRawDeltaTime());
        camera.position.set(player.getX(), player.getY(),0);
        camera.update();
    }
    public void render(float deltaTime)
    {
        update(deltaTime);
        //RENDER AND CAMERA==================================================================================
        Gdx.gl.glClearColor(0.47f, 0.47f, 0.47f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();//================

        //World Render
        for(GameObject object : objects)
        {
            if(object.getCollidable())
                object.render(batch);
        }
        for(GameObject object : objects)
        {
            if(!object.getCollidable())
                object.render(batch);
        }
        player.render(batch);
        //Screen Render

        GuiViewport.apply();
        batch.setProjectionMatrix(cameraGUI.combined);
        GuiManager.instance.render(batch);
        Menu.instance.render(batch);
        batch.end();//==================

        Gdx.gl.glEnable(GL20.GL_BLEND);
        GuiManager.instance.drawDebugInfo();
        ObjectManager.instance.drawLocation();
    }
    public void resize(int width, int height)
    {
        Gdx.graphics.setDisplayMode(width, height, false);
        GuiViewport.update(width, height, true);
        viewport.update(width, height, true);
        camera.position.set(player.getX(), player.getY(),0);
    }
    public void dispose()
    {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        ResourceManager.instance.dispose();
        GuiManager.instance.font.dispose();
        batch.dispose();
    }

    //============================================================================================================
    //                                                TOOLS
    //============================================================================================================
    public Vector2 getGameInput()           //Unproject, translates from screen to game coordinates
    {
        return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
    }
    public Vector2 transformToGameInput(int x, int y)
    {
        return viewport.unproject(new Vector2(x, y));
    }
    public Vector3 getScreenInput(int screenX, int screenY)
    {
        return  cameraGUI.unproject(new Vector3(screenX,screenY,0));
    }
    void loadCamera()
    {
        camera = new OrthographicCamera();
        cameraLerp = new Vector2(player.getX(), player.getY());
        lerpTarget = new Vector3();
//        camera.zoom = 0.5f;

        viewport = new FillViewport(Constants.WIDTH, Constants.HEIGHT, camera);
        batch = new SpriteBatch();


        cameraGUI = new OrthographicCamera();
        cameraGUI.position.set(Constants.WIDTH/2, Constants.HEIGHT/2,0);
        cameraGUI.update();
        GuiViewport = new StretchViewport(Constants.WIDTH, Constants.HEIGHT, cameraGUI);
    }
    //============================================================================================================
    //                                                 INPUT
    //============================================================================================================
    public void keyDown(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.W:                                // Up
                up = true;
                break;
            case Input.Keys.S:                                // Down
                down = true;
                break;
            case Input.Keys.A:                                // Left
                 left = true;
                break;
            case Input.Keys.D:                                // Right
                right = true;
                break;
        }
    }
    public void keyUp(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.W:                                // Up
                up = false;
                break;
            case Input.Keys.S:                                // Down
                down = false;
                break;
            case Input.Keys.A:                                //Left
                left = false;
                break;
            case Input.Keys.D:                                // Right
                right = false;
                break;
            case Input.Keys.R:                                // Destroy Object
                ObjectManager.instance.destroyObject(getGameInput().x, getGameInput().y, objects);
                break;
            case Input.Keys.SPACE:                            // Switch Texture
                player.switchTexture();
                break;
        }
    }
    public void touchDown(int screenX, int screenY)
    {
        //Input handler
        GuiManager.instance.handleInput(screenX, screenY, true, false);

        //Menu handler
        Vector3 input = getScreenInput(screenX, screenY);
        Menu.instance.handleInput(input);
        drawing = true;
    }
    public void touchUp(int screenX, int screenY)
    {
        //Input handler
        GuiManager.instance.handleInput(screenX, screenY, false, false);
        drawing = false;
    }
    public void touchDragged(int screenX, int screenY)
    {
        //Input handler
        GuiManager.instance.handleInput(screenX, screenY, false, true);

        ObjectManager.instance.selectLocation((int)transformToGameInput(screenX, screenY).x, (int)transformToGameInput(screenX, screenY).y, true);
    }
}