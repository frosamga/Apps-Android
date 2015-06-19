package com.wildwestspace;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener,
		SensorEventListener {

	private static final String TAG = "GameScene.java";

	private static final int MIN_DURACION = 3;
	private static final int MAX_DURACION = 6;

	private static final int MIN_DURACION_BONUS = 5;
	private static final int MAX_DURACION_BONUS = 7;
	
	protected static final float TAM_EXPLOSION = 1;

	private static final float TAM_PROYECTIL = 0.5f;

	private static final float TAM_ENEMIGO = 1;

	//hace que el bonus aparezca 1 por cada 3 enemigos
	private int bonus_delay=0;

	private Camera mCamera;

	// enemigos
	private LinkedList<AnimatedSprite> targetLL;
	private LinkedList<AnimatedSprite> TargetsToBeAdded;

	// bonus
	private LinkedList<AnimatedSprite> bonusLL;
	private LinkedList<AnimatedSprite> bonusToBeAdded;

	// Proyectiles
	private LinkedList<AnimatedSprite> projectileLL;
	private LinkedList<AnimatedSprite> projectilesToBeAdded;
	private AnimatedSprite player;
	private Text score;
	boolean finJuego = false;
	private CameraScene mPauseScene;
	private int hitCount = 0;

	// sensor de movimiento y coordenadas del Sprite de la nave.
	private SensorManager sensorManager;
	public int accellerometerSpeedX;
	private int accellerometerSpeedY;
	private int sY; // Sprite coordinates
	private CameraScene mResultScene;
	private AnimatedSprite target;
	private AnimatedSprite bonus;
	private AnimatedSprite projectile;
	private AnimatedSprite sprExplosion;

	private BaseActivity activity;
	private IUpdateHandler detect;

	private AdaptadorBD puntuacionMaxima;

	public GameScene(String modo) {

		initDetection();

		activity = BaseActivity.getSharedInstance();
		mCamera = activity.getmCamera();

		activity.setCurrentScene(this);

		this.insertarPuntuacionMaxima();

		sensorManager = (SensorManager) activity
				.getSystemService(BaseGameActivity.SENSOR_SERVICE);
		SensorListener.getSharedInstance();
		sensorManager.registerListener(SensorListener.getSharedInstance(),
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		setOnSceneTouchListener(this);

		// lista de enemigos
		targetLL = new LinkedList<AnimatedSprite>();
		TargetsToBeAdded = new LinkedList<AnimatedSprite>();

		// lista de projectiles
		projectileLL = new LinkedList<AnimatedSprite>();
		projectilesToBeAdded = new LinkedList<AnimatedSprite>();

		// crea el fondo que se va moviendo
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = BaseActivity
				.getSharedInstance().getVertexBufferObjectManager();

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f,
				new Sprite(0, 0, mCamera.getWidth(), mCamera.getHeight(),
						activity.getmParallaxLayerBack(),
						vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f,
				new Sprite(0, 80, activity.getmParallaxLayerMid(),
						vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f,
				new Sprite(0, mCamera.getHeight()
						- activity.getmParallaxLayerFront().getHeight(),
						activity.getmParallaxLayerFront(),
						vertexBufferObjectManager)));
		setBackground(autoParallaxBackground);

		// carga el personaje en mitad de la pantalla.
		final float playerX = (mCamera.getWidth() - BaseActivity
				.getSharedInstance().getmPlayerTextureRegion().getWidth()) / 2;
		final float playerY = mCamera.getHeight()
				- activity.getmPlayerTextureRegion().getHeight() - 5;

		// crea el sprite del personaje y le da un tamaño y una animacion.
		player = new AnimatedSprite(playerX, playerY,
				activity.getmPlayerTextureRegion(), vertexBufferObjectManager);
		player.setScaleCenterY(activity.getmPlayerTextureRegion().getHeight());
		// player.setScale(4);
		player.animate(new long[] { 200, 200, 200 }, 0, 2, true);

		projectile = new AnimatedSprite(player.getX(), player.getY() + 40,
				activity.getmProjectileTextureRegion(),
				vertexBufferObjectManager);
		projectile.setScale(TAM_PROYECTIL);
		projectile.animate(new long[] { 200, 200, 200 }, 0, 2, true);

		// añade la puntuacion en la esquina superior derecha
		// score = new Text(0, 0, activity.getmFont(), String.valueOf(hitCount),
		// vertexBufferObjectManager);
		// score.setPosition(mCamera.getWidth() - score.getWidth() - 10,
		// score.getWidth()+100);
		// score.setRotation(90f);
		// llama al onUpdate cada cierto tiempo
		registerUpdateHandler(new FPSLogger());
		registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateSpritePosition();
			}

			public void reset() {
			}
		});

		// llama a un contador para que spawnen los enemigos
		createSpriteSpawnTimeHandler();
		setOnSceneTouchListener(this);

		// registra el manejador para ver si el enemigo esta fuera de
		// pantalla
		registerUpdateHandler(detect);

		// añade musica de fondo, si quiero pararlo llamo al stop dentro del
		// pause.
		// SUBESCENA DE PAUSA
		// permite añadir la pausa para el juego con una nueva escena
		// mPauseScene = new CameraScene(mCamera);
		// final int x = (int) (mCamera.getWidth() / 2 - BaseActivity
		// .getSharedInstance().getmPausedTextureRegion().getWidth() / 2);
		// final int y = (int) (mCamera.getHeight() / 2 - BaseActivity
		// .getSharedInstance().getmPausedTextureRegion().getHeight() / 2);
		// final Sprite pausedSprite = new Sprite(x, y,
		// activity.getmPausedTextureRegion(), vertexBufferObjectManager);
		//
		// mPauseScene.attachChild(pausedSprite);
		// mPauseScene.setBackgroundEnabled(false);

		activity.getBackgroundMusic().play();

		attachChild(player);
		restart();

	}

	private void initDetection() {
		detect = new IUpdateHandler() {

			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				Iterator<AnimatedSprite> targets = targetLL.iterator();
				AnimatedSprite _target;
				boolean hit = false;
				if (!finJuego) {
					while (targets.hasNext()) {
						_target = targets.next();
						if (_target.getX() <= -_target.getWidth()) {
							removeSpriteAnimado(_target, targets);
							break;
						}

						if (_target.collidesWith(player)) {
							Explosion(_target.getX(), _target.getY(),
									TAM_EXPLOSION);
							finJuego = true;
							break;
						}

						Iterator<AnimatedSprite> projectiles = projectileLL
								.iterator();
						AnimatedSprite _projectile;
						while (projectiles.hasNext()) {
							_projectile = projectiles.next();
							// si sale de camara
							if (_projectile.getX() >= mCamera.getWidth()
									|| _projectile.getY() >= mCamera
											.getHeight()
											+ _projectile.getHeight()
									|| _projectile.getY() <= -_projectile
											.getHeight()) {
								removeSpriteAnimado(_projectile, projectiles);
								continue;
							}
							// si el proyectil colisiona con el enemigo
							if (_target.collidesWith(_projectile)) {
								Explosion(_target.getX(), _target.getY(),
										TAM_EXPLOSION);
								removeSpriteAnimado(_projectile, projectiles);
								hit = true;
								break;
							}

						}

						if (hit) {
							removeSpriteAnimado(_target, targets);
							hitCount++;
							// score.setText(String.valueOf(hitCount));
							hit = false;
						}

						// si enemigo fuera de pantalla
						if (_target.getX() <= -_target.getWidth()) {
							removeSpriteAnimado(_target, targets);
							break;
						}
					}
					if (finJuego) {
						finDeJuego();
					}
				} else {
					// si enemigo fuera de pantalla
					while (targets.hasNext()) {
						_target = targets.next();
						if (_target.getX() <= -_target.getWidth()) {
							removeSpriteAnimado(_target, targets);
							break;
						}
					}

				}

				projectileLL.addAll(projectilesToBeAdded);
				projectilesToBeAdded.clear();

				targetLL.addAll(TargetsToBeAdded);
				TargetsToBeAdded.clear();
			}
		};
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	// obtiene el desplazamiento en el eje y del dispositivo
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {

			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellerometerSpeedX = (int) event.values[1];
				accellerometerSpeedY = (int) event.values[0];
				break;
			}
		}

	}

	// actualiza la posicion del personaje, que se mueve con el acelerometro.
	private void updateSpritePosition() {
		if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
			// Establece los limites
			int tL = 0;
			int bL = (int) (mCamera.getHeight() - (int) player.getHeight());

			// Calcula la nueva x e y sin limites
			if (sY >= tL)
				sY += accellerometerSpeedX;
			else
				sY = tL;
			if (sY <= bL)
				sY += accellerometerSpeedX;
			else
				sY = bL;

			// Hace un check doble de que las cordenadas tienen limites.
			if (sY < tL)
				sY = tL;
			else if (sY > bL)
				sY = bL;
			player.setPosition(5, sY);
		}
	}

	// dispara solo en el eje X
	private void shootProjectile(final float pX) {

		Log.d(TAG, "init Shoot Projectile");
		// no permite disparar muchas veces, establece un coldown
		if (!CoolDown.sharedCoolDown().checkValidity()) {
			return;
		}

		int offX = (int) (pX - player.getX());
		if (offX <= 0)
			return;

		final VertexBufferObjectManager vertexBufferObjectManager = BaseActivity
				.getSharedInstance().getVertexBufferObjectManager();

		// el +40 depende del tamaño del personaje inicial, esto hace que el
		// proyectil salga de la posicion de la nave
		projectile = new AnimatedSprite(player.getX(), player.getY() + 40,
				activity.getmProjectileTextureRegion(),
				vertexBufferObjectManager);
		projectile.setScale(TAM_PROYECTIL);
		projectile.animate(new long[] { 200, 200, 200 }, 0, 2, true);
		int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
		float velocity = 0.9f;
		MoveXModifier mod = new MoveXModifier(velocity, projectile.getX(),
				realX);
		projectile.registerEntityModifier(mod.deepCopy());
		projectilesToBeAdded.add(projectile);
		attachChild(projectile);
		activity.getShootingSound().play();

	}

	// habilita los disparos con el metodo onTouch
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		final float touchX = pSceneTouchEvent.getX();

		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			Log.d(TAG, "Shooting - 349");
			shootProjectile(touchX);

			return true;
		}
		return false;
	}

	// añadimos el bonus
	public void addBonus() {

		final VertexBufferObjectManager vertexBufferObjectManager = BaseActivity
				.getSharedInstance().getVertexBufferObjectManager();
		Random rand = new Random();
		// Se calcula la posición aleatoria del objetivo
		int x = (int) (mCamera.getWidth() + activity.getmBonusTextureRegion()
				.getWidth());
		int minY = (int) activity.getmBonusTextureRegion().getHeight();
		int maxY = (int) (mCamera.getHeight() - BaseActivity
				.getSharedInstance().getmBonusTextureRegion().getHeight());
		int rangeY = maxY - minY;
		int y = rand.nextInt(rangeY) + minY;
		// Se crea el sprite en la posición determinada anteriormente
		bonus = new AnimatedSprite(x, y, activity.getmBonusTextureRegion(),
				vertexBufferObjectManager);
		bonus.setScale(TAM_ENEMIGO);
		bonus.animate(new long[] { 200, 200, 200 }, 0, 2, true);

		attachChild(bonus);
		// Se determina la velocidad del movimiento del objetivo
		int minDuration = MIN_DURACION_BONUS;
		int maxDuration = MAX_DURACION_BONUS;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		// Se crea el modificador y se le aplica al objetivo
		MoveXModifier mod = new MoveXModifier(actualDuration, bonus.getX(),
				-bonus.getWidth());
		bonus.registerEntityModifier(mod.deepCopy());
		// Almacenamos el objetivo en la lista
		//bonusToBeAdded.add(bonus);
	}

	// añadimos los enemigos
	public void addTarget() {

		final VertexBufferObjectManager vertexBufferObjectManager = BaseActivity
				.getSharedInstance().getVertexBufferObjectManager();
		Random rand = new Random();
		// Se calcula la posición aleatoria del objetivo
		int x = (int) (mCamera.getWidth() + activity.getmTargetTextureRegion()
				.getWidth());
		int minY = (int) activity.getmTargetTextureRegion().getHeight();
		int maxY = (int) (mCamera.getHeight() - BaseActivity
				.getSharedInstance().getmTargetTextureRegion().getHeight());
		int rangeY = maxY - minY;
		int y = rand.nextInt(rangeY) + minY;
		// Se crea el sprite en la posición determinada anteriormente
		target = new AnimatedSprite(x, y, activity.getmTargetTextureRegion(),
				vertexBufferObjectManager);
		target.setScale(TAM_ENEMIGO);
		target.animate(new long[] { 200, 200, 200 }, 0, 2, true);

		attachChild(target);
		// Se determina la velocidad del movimiento del objetivo
		int minDuration = MIN_DURACION;
		int maxDuration = MAX_DURACION;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		// Se crea el modificador y se le aplica al objetivo
		MoveXModifier mod = new MoveXModifier(actualDuration, target.getX(),
				-target.getWidth());
		target.registerEntityModifier(mod.deepCopy());
		// Almacenamos el objetivo en la lista
		TargetsToBeAdded.add(target);
	}

	// crea un spaw de enemigos con un tiempo x random
	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 1f;
		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						addTarget();
						bonus_delay++;
						if(bonus_delay==5){
							addBonus();
							bonus_delay=0;
						}
						
					}
					
				});
		activity.getEngine().registerUpdateHandler(spriteTimerHandler);
	}

	// permite eliminar un sprite
	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it) {
		detachChild(_sprite);
		it.remove();
	}

	// permite eliminar un sprite animado
	public void removeSpriteAnimado(final AnimatedSprite _sprite,
			Iterator<AnimatedSprite> it) {
		detachChild(_sprite);
		it.remove();
	}

	// si pausa
	public void pauseGame() {
		// Activamos la escena de pausa
		setChildScene(mPauseScene, false, true, true);

	}

	public void unPauseGame() {
		clearChildScene();
	}

	public void pauseMusic() {
		if (activity.getBackgroundMusic().isPlaying())
			activity.getBackgroundMusic().pause();
	}

	public void resumeMusic() {
		if (!activity.getBackgroundMusic().isPlaying())
			activity.getBackgroundMusic().resume();
	}

	public void finDeJuego() {
		player.setVisible(false);
		projectile.setVisible(false);
		// score.setVisible(false);
		this.insertarPuntuacionMaxima();
		setChildScene(mResultScene);

	}

	public void Explosion(float x, float y, float scale) {
		sprExplosion = new AnimatedSprite(x, y, activity.getmExplosion(),
				activity.getVertexBufferObjectManager());
		sprExplosion.setScale(scale);
		sprExplosion.animate(new long[] { 50, 50, 50, 50, 50, 50, 50, 50, 50,
				50, 50, 50, 50, 50, 50, 50 }, 0, 15, false);
		attachChild(sprExplosion);

	}

	public void restart() {

		detachChildren();
		attachChild(player);
		// attachChild(score);
		detachChild(sprExplosion);
		clearChildScene();
		mResultScene.detachSelf();
		this.insertarPuntuacionMaxima();
		player.setVisible(true);
		projectile.setVisible(true);
		// score.setVisible(true);
		hitCount = 0;
		// score.setText(String.valueOf(hitCount));
		projectileLL.clear();
		projectilesToBeAdded.clear();
		TargetsToBeAdded.clear();
		targetLL.clear();
		finJuego = false;
	}

	public void insertarPuntuacionMaxima() {
		puntuacionMaxima = new AdaptadorBD(this.activity);
		puntuacionMaxima.open();
		// siempre añade un 0 a la base, si es mas grande el hitvount entonces
		// lo modifica, HighScore!
		puntuacionMaxima.insertar(String.valueOf(0));
		Log.d("BASE DE DATOS QUE DEBERIA FUNCIONAR", puntuacionMaxima.get(1)
				.getString(1));

		if (hitCount > Integer.parseInt(puntuacionMaxima.get(1).getString(1))) {
			puntuacionMaxima.actualizar(1, String.valueOf(hitCount));
		}
		mResultScene = new ResultScene(mCamera,
				Integer.parseInt(puntuacionMaxima.get(1).getString(1)));

		puntuacionMaxima.close();

	}
}
