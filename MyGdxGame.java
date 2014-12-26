package com.mygdx.game;

import com.badlogic.gdx.*;

public class MyGdxGame implements ApplicationListener, InputProcessor
{
    @Override
    public void create()
    {
        Game.instance.initialize();
        Gdx.input.setInputProcessor(this);
    }
    @Override
    public void render()
    {
        Game.instance.render(Gdx.graphics.getRawDeltaTime());
    }
    @Override
    public void dispose()
    {
        Game.instance.dispose();
    }
    @Override
    public void resize(int width, int height)
    {
       Game.instance.resize(width, height);
    }
    @Override
    public void pause(){}
    @Override
    public void resume()
    {
        ResourceManager.instance.initialize();
    }
    //INPUT==============================================================================================
    @Override
    public boolean keyDown(int keycode)
    {
        Game.instance.keyDown(keycode);
        return false;
    }
    @Override
    public boolean keyUp(int keycode)
    {
        Game.instance.keyUp(keycode);
        return false;
    }
    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Game.instance.touchDown(screenX, screenY);
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        Game.instance.touchUp(screenX, screenY);
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        Game.instance.touchDragged(screenX, screenY);
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {return false;}
}