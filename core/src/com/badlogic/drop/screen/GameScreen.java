package com.badlogic.drop.screen;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameScreen implements Screen {

    private final Drop game;
    private final Texture dropImage;
    private final Texture bucketImage;
    private final Texture background;
    private final Sound dropSound;
    private final Music rainMusic;
    private final OrthographicCamera camera;
    private final Rectangle bucket;
    private final Array<Rectangle> raindrops;
    private long lastDropTime;
    private int dropsGathered;
    private int spawnRainDropTime = 1000000000;

    public GameScreen(final Drop game) {
        this.game = game;

        // загрузка изображений для фона 800x480 пикселей
        background = new Texture(Gdx.files.internal("backgroundGame.png"));

        // загрузка изображений для капли и ведра, 64x64 пикселей каждый
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // создает камеру
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // создается Rectangle для представления ведра
        bucket = new Rectangle();
        // центрируем ведро по горизонтали
        bucket.x = 800 / 2 - 64 / 2;
        // размещаем на 20 пикселей выше нижней границы экрана.
        bucket.y = 20;

        bucket.width = 64;
        bucket.height = 64;

        // создает массив капель и возрождает первую
        raindrops = new Array<>();
        spawnRaindrop();

    }

    @Override
    public void show() {
        // воспроизведение фоновой музыки
        // когда отображается экрана
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        // Очищаем экран темно-синим цветом.
        // Аргументы для glClearColor красный, зеленый
        // синий и альфа компонент в диапазоне [0,1]
        // цвета используемого для очистки экрана.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // сообщает камере, что нужно обновить матрицы.
        camera.update();

        // сообщаем SpriteBatch о системе координат
        // визуализации указанных для камеры.
        game.getBatch().setProjectionMatrix(camera.combined);

        // начитаем новую серию, рисуем ведро и
        // все капли
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0);
        game.getFont().draw(game.getBatch(), "Drops Collected: " + dropsGathered, 5, 475);
        game.getFont().draw(game.getBatch(), "Spawn Rain Drop Time: " + spawnRainDropTime, 560, 475);

        game.getBatch().draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
            game.getBatch().draw(dropImage, raindrop.x, raindrop.y);
        }
        game.getBatch().end();

        // обработка пользовательского ввода
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // убедитесь, что ведро остается в пределах экрана
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // проверка, нужно ли создавать новую каплю
        if (TimeUtils.nanoTime() - lastDropTime > spawnRainDropTime)
            spawnRaindrop();

        if (spawnRainDropTime > 0) {
            spawnRainDropTime -= 1000000;
        } else {
            spawnRainDropTime = 0;
        }

        // движение капли, удаляем все капли выходящие за границы экрана
        // или те, что попали в ведро. Воспроизведение звукового эффекта
        // при попадании.
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
                game.setScreen(new GameOverScreen(game, dropsGathered));
                dispose();
            }
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
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
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
