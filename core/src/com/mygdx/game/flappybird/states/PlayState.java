package com.mygdx.game.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.flappybird.FlappyBird;
import com.mygdx.game.flappybird.dataBase.DataBase;
import com.mygdx.game.flappybird.sprites.Bird;
import com.mygdx.game.flappybird.sprites.Tube;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class PlayState extends State {

    public static final int TUBE_WIDTH = 52;
    public static final int TUBE_SPACING = 125;
    public static final int TUBE_COUNT = 4;
    public static final int GROUND_Y_OFFSET = -30;
    private Bird bird;
    private int score;
    private BitmapFont font;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private DataBase db;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        db = new DataBase();
        bird = new Bird(50, 300);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(camera.position.x - camera.viewportWidth / 2 + ground.getWidth(), GROUND_Y_OFFSET);
        font = new BitmapFont();
        score = 0;

        tubes = new Array<>();

        for (int i = 0; i < TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + TUBE_WIDTH) - 10));
        }
    }

    @Override
    protected void handleInput() {

        if (Gdx.input.justTouched())
            bird.jump();
    }

    @Override
    public void update(float dt) {

        handleInput();
        updateGround();
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80;


        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth())
                tube.reposition(tube.getPosTopTube().x + ((TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));

            if (tube.collides(bird.getBounds())) {

                db.update(String.valueOf(score), getDate());
                db.select();
                gsm.set(new GameOver(gsm));
            }

            if (tube.getPosTopTube().x < bird.getPosition().x && !tube.isChecked) {
                tube.isChecked = true;
                score++;
                Gdx.app.log("TAG", score + "");
            }

        }
        camera.update();
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        font.draw(sb, "Score: " + score, bird.getPosition().x - 25, 390);
        sb.end();
    }


    private void updateGround() {
        if (camera.position.x - camera.viewportWidth / 2 > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (camera.position.x - camera.viewportWidth / 2 > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy 'at' HH:mm");
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void dispose() {

        bird.dispose();
        bg.dispose();
        ground.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
    }
}
