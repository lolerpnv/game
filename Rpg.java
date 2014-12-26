/*package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Rpg extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}*/
package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class Rpg implements ApplicationListener, InputProcessor
{
    //============================================================================================================
    //                                                GAME
    //============================================================================================================
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
    public void resume(){}

    //============================================================================================================
    //                                                 INPUT
    //============================================================================================================
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}  //implement that if draged to other button to make that movement happen, and reset the old one
    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override
    public boolean scrolled(int amount) {return false;}
}

