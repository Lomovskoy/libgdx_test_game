package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class Drop extends ApplicationAdapter {

	Texture dropImage;
	Texture bucketImage;
	Texture background;
	Sound dropSound;
	Music rainMusic;
	SpriteBatch batch;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	
	@Override
	public void create () {

		// загрузка изображений для фона 800x480 пикселей
		background = new Texture(Gdx.files.internal("background.png"));

		// загрузка изображений для капли и ведра, 64x64 пикселей каждый
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// сразу же воспроизводиться музыка для фона
		rainMusic.setLooping(true);
		rainMusic.play();

		// создается камера и SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

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
	public void render () {

		// сообщает камере, что нужно обновить матрицы
		camera.update();

		// сообщаем SpriteBatch о системе координат
		// визуализации указанной для камеры.
		batch.setProjectionMatrix(camera.combined);

		// начинаем новую серию, рисуем ведро и
		// все капли
		batch.begin();
		batch.draw(background, 0, 0);
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		// обработка пользовательского ввода
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// убедитесь что ведро остается в пределах экрана
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// проверка, нужно ли создавать новую каплю
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		// движение капли, удаляем все капли выходящие за границы экрана
		// или те, что попали в ведро. Воспроизведение звукового эффекта
		// при попадании.
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		// высвобождение всех нативных ресурсов
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
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
