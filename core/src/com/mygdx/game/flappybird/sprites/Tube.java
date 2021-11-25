package com.mygdx.game.flappybird.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tube {
    Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;
    public static final int FLUCTUATION = 130;
    public static final int TUBE_GAP = 100;
    public static final int LOWEST_OPENING = 120;
    public boolean isChecked = false;
    Rectangle boundsTop, boundsBot;

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public Tube(float x){
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();
        posTopTube = new Vector2(x,rand.nextInt(FLUCTUATION)+TUBE_GAP+LOWEST_OPENING);
        posBotTube = new Vector2(x,posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop = new Rectangle(getPosTopTube().x,getPosTopTube().y,getTopTube().getWidth(),getTopTube().getHeight());
        boundsBot = new Rectangle(getPosBotTube().x,getPosBotTube().y,getBottomTube().getWidth(),getBottomTube().getHeight());
    }
    public void reposition(float x){
        posTopTube.set(x,rand.nextInt(FLUCTUATION)+TUBE_GAP+LOWEST_OPENING);
        posBotTube.set(x,posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsBot.setPosition(posBotTube.x,posBotTube.y);
        boundsTop.setPosition(posTopTube.x,posTopTube.y);
        isChecked = false;

    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}
