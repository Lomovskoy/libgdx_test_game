package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class GameOverScreen implements Screen {

    final Drop game;
    OrthographicCamera camera;
    Texture background;
    final int dropsGathered;

    public GameOverScreen(Drop game, int dropsGathered) {
        this.game = game;
        this.dropsGathered = dropsGathered;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        // загрузка изображений для фона 800x480 пикселей
        background = new Texture(Gdx.files.internal("backgroundGame.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.font.draw(game.batch, "Game Over: ", 330, 230);
        game.font.draw(game.batch, "Victory Point : " + dropsGathered, 320, 210);
        game.batch.end();
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

    }
}
