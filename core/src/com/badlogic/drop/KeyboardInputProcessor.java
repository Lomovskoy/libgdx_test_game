package com.badlogic.drop;

import com.badlogic.drop.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardInputProcessor {

    public void changeScreenOnClick(Input.Keys keys, GameScreen gameScreen) {
        if (Gdx.input.isKeyPressed(keys.hashCode())) {
            game.setScreen(gameScreen);
        }
    }
}
