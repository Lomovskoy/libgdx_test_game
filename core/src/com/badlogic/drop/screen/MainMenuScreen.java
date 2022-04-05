package com.badlogic.drop.screen;

import com.badlogic.drop.Drop;
import com.badlogic.drop.KeyboardInputProcessor;
import com.badlogic.drop.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    private final Drop game;
    private final OrthographicCamera camera;
    private final Texture background;
    private final SaveManager saveManager;
    private final KeyboardInputProcessor inputProcessor;

    public MainMenuScreen(final Drop game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture(Gdx.files.internal("backgroundMenu.jpg"));
        saveManager = new SaveManager();
        inputProcessor = new KeyboardInputProcessor();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0);
        game.getFont().draw(game.getBatch(), "Record: " + saveManager.getSave(), 5, 15);
        game.getFont().draw(game.getBatch(), "Welcome to Drop!!! ", 330, 193);
        game.getFont().draw(game.getBatch(), "Tap Enter to begin!", 330, 160);
        game.getBatch().end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
