package com.badlogic.drop.screen;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;

public class GameOverScreen implements Screen {

    private final Drop game;
    private final int dropsGathered;
    private final OrthographicCamera camera;
    private final Texture background;

    public GameOverScreen(Drop game, int dropsGathered) {
        this.game = game;
        this.dropsGathered = dropsGathered;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // загрузка изображений для фона 800x480 пикселей
        background = new Texture(Gdx.files.internal("backgroundGameOver.png"));

        setRecord(dropsGathered);
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
        game.getFont().draw(game.getBatch(), "Game Over: ", 330, 230);
        game.getFont().draw(game.getBatch(), "Victory Point : " + dropsGathered, 320, 210);
        game.getBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
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

    private void setRecord(int dropsGathered) {
        FileHandle file = Gdx.files.local("assets" + File.separator + "save.txt");
        int record = Integer.parseInt(file.readString());
        if (dropsGathered > record) {
            file.writeString(String.valueOf(dropsGathered), false);
        }
    }
}
