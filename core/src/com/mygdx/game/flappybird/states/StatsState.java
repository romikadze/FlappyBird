package com.mygdx.game.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.flappybird.FlappyBird;
import com.mygdx.game.flappybird.dataBase.DataBase;

public class StatsState extends State {
    private BitmapFont font;
    private Texture bg;
    private DataBase db;
    private String stats;


    public StatsState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        bg = new Texture("scoreboard.png");
        font = new BitmapFont();
        db = new DataBase();
        stats = db.select();
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            Gdx.app.log("TAG", "|input|");
            gsm.push(new MenuState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
        font.draw(sb, "Score: " + stats, 30, 300);
        sb.end();
    }

    @Override
    public void dispose() {
        db.dispose();
    }


}
