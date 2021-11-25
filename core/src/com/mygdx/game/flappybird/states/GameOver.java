package com.mygdx.game.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.flappybird.FlappyBird;

public class GameOver extends State {

    private Texture background;
    private Texture gameOver;

    public GameOver(GameStateManager gsm) {
        super(gsm);
        background = new Texture("bg.png");
        gameOver = new Texture("gameover.png");
        camera.setToOrtho(false,FlappyBird.WIDTH/2,FlappyBird.HEIGHT/2);
    }

    @Override
    protected void handleInput() {
        Vector3 pos = new Vector3();
        if (Gdx.input.justTouched()) {
            pos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(pos);
            if (pos.x >= 144 && pos.x <= 238 && pos.y >= 5 && pos.y <= 59) {
                gsm.set(new StatsState(gsm));
            } else{
                gsm.set(new PlayState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background,0,0);
        sb.draw(gameOver, camera.position.x - gameOver.getWidth()/2, camera.position.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        gameOver.dispose();
    }
}
