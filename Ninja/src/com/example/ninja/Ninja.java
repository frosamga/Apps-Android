package com.example.ninja;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

public class Ninja extends BaseGameActivity implements IOnSceneTouchListener {

	private Camera mCamera;
	private Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mPlayerTextureRegion;
	private AnimatedSprite player;

	//Enemigos
	private TextureRegion mTargetTextureRegion;
	private LinkedList<AnimatedSprite> targetLL;
	private LinkedList<AnimatedSprite> TargetsToBeAdded;

	// animacion de explosion
	private TiledTextureRegion regExplosion;
	private AnimatedSprite sprExplosion;
	private static int SPR_COLUMNAS = 5;
	private static int SPR_FILAS = 5;

	// animacion de enemigo1
	private TiledTextureRegion regEnemigo1;
	private static int SPR_COLUMNAS_enemigo = 2;
	private static int SPR_FILAS_enemigo = 1;

	// animacion de enemigo2
	private TiledTextureRegion regEnemigo2;
	private static int SPR_COLUMNAS_enemigo2 = 3;
	private static int SPR_FILAS_enemigo2 = 1;
	private int muerteBoss = 5;
	private boolean muerteJefe = false;

	// animacion del protagonista
	private TiledTextureRegion regProtagonista;
	private static int SPR_COLUMNAS_protagonista = 4;
	private static int SPR_FILAS_protagonista = 1;

	// Proyectiles
	private LinkedList<Sprite> projectileLL;
	private LinkedList<Sprite> projectilesToBeAdded;
	private TextureRegion mProjectileTextureRegion;

	private Sound shootingSound;
	private Music backgroundMusic;

	private TextureRegion mPausedTextureRegion;
	private CameraScene mPauseScene;

	// Variables de estado
	private boolean runningFlag = false;
	private boolean pauseFlag = false;

	// Texto y variables de puntuación
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText score;
	private int hitCount;
	private final int maxScore = 20;
	private int enemigosRestantes = 10;

	// Escena e imágenes para mostrar victoria o derrota
	private CameraScene mResultScene;

	// fondo
	private TextureRegion mBackgroundTextureRegion, regTitulo, regJugar,
			regReglas, regPerder, regGanar, regReglasTexto;
	private Sprite fondoSprite, tituloSprite, jugarSprite, reglasSprite,
			perderSprite, ganarSprite, reglasTexto;

	// para cambio de escenas
	private boolean empezar = true;

	IUpdateHandler detect = new IUpdateHandler() {

		public void reset() {
		}

		public void onUpdate(float pSecondsElapsed) {

			Iterator<AnimatedSprite> targets = targetLL.iterator();
			AnimatedSprite _target;
			boolean hit = false;

			while (targets.hasNext()) {
				_target = targets.next();

				// ENEMIGO FUERA DE PANTALLA
				if (_target.getX() <= -_target.getWidth()) {
					removeSpriteAnimado(_target, targets);
					if (enemigosRestantes == 0) {
						fail();
						enemigosRestantes = 10;
					} else {
						enemigosRestantes--;
					}

				}
				Iterator<Sprite> projectiles = projectileLL.iterator();
				Sprite _projectile;
				while (projectiles.hasNext()) {
					_projectile = projectiles.next();

					// PROYECTIL FUERA DE PANTALLA
					if (_projectile.getX() >= mCamera.getWidth()
							|| _projectile.getY() >= mCamera.getHeight()
									+ _projectile.getHeight()
							|| _projectile.getY() <= -_projectile.getHeight()) {
						removeSprite(_projectile, projectiles);
						continue;
					}

					// PROYECTIL IMPACTA EN ENEMIGO
					if (_target.collidesWith(_projectile)) {
						removeSprite(_projectile, projectiles);
						hit = true;
						break;
					}
				}

				// quizas se puede mejorar esta parte de codigo, es un if
				// mal hecho, pero que funciona
				// el jefe sale mas veces de lo normal y ademas, cuando se le
				// dispara muere otro personaje
				if (hit) {
					if (_target.isScaled()) {
						if (muerteBoss == 0) {
							hitCount++;
							score.setText(String.valueOf(hitCount));
							removeSpriteAnimado(_target, targets);
							Explosion(_target.getX(), _target.getY(), 1000f);
							hit = false;
							muerteBoss = 5;

						} else {
							muerteBoss--;
						}
					} else {
						hitCount++;
						score.setText(String.valueOf(hitCount));
						removeSpriteAnimado(_target, targets);
						Explosion(_target.getX(), _target.getY(), 1000f);
						hit = false;
					}

				}
				muerteJefe = false;
				if (hitCount >= maxScore) {
					win();
				}
			}
			projectileLL.addAll(projectilesToBeAdded);
			projectilesToBeAdded.clear();

			targetLL.addAll(TargetsToBeAdded);
			TargetsToBeAdded.clear();
		}
	};

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public Engine onLoadEngine() {
		final Display display = getWindowManager().getDefaultDisplay();
		int cameraWidth = display.getWidth();
		int cameraHeight = display.getHeight();

		mCamera = new Camera(0, 0, cameraWidth, cameraHeight);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera)
				.setNeedsMusic(true).setNeedsSound(true));
	}

	@Override
	public void onLoadResources() {
		mBitmapTextureAtlas = new BitmapTextureAtlas(2048, 2048,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "fondo_1.png",
						0, 0);
		// modificar la posicion
		regTitulo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBitmapTextureAtlas, this, "fondo_2.png", 0, 401);

		mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "paused.png",
						0, 622);

		regReglas = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBitmapTextureAtlas, this, "reglas.png", 201, 622);
		regJugar = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBitmapTextureAtlas, this, "jugar.png", 402, 622);

		// explosiones
		regExplosion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"explosion.png", 0, 673, this.SPR_COLUMNAS,
						this.SPR_FILAS);

		// enemigo1
		regEnemigo1 = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"enemigo1.png", 801, 0, this.SPR_COLUMNAS_enemigo,
						this.SPR_FILAS_enemigo);
		// boss
		regEnemigo2 = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"enemigo2.png", 301, 673, this.SPR_COLUMNAS_enemigo2,
						this.SPR_FILAS_enemigo2);
		// protagonista
		regProtagonista = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this, "pj.png",
						301, 800, this.SPR_COLUMNAS_protagonista,
						this.SPR_FILAS_protagonista);

		//proyectiles
		mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "flecha.png",
						605, 625);
		// si se le da a reglas
		regReglasTexto = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this,
						"reglasTexto.png", 1024, 0);

		// si se gana entonces se muestra esto
		regGanar = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBitmapTextureAtlas, this, "ganar.png", 1024, 401);

		// en el caso de perder
		regPerder = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBitmapTextureAtlas, this, "perder.png", 1024, 802);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
				Typeface.BOLD), 40, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

		SoundFactory.setAssetBasePath("mfx/");
		try {
			shootingSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "pew_pew_lei.wav");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MusicFactory.setAssetBasePath("mfx/");

		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "background_music.wav");
			backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Explosion(float x, float y, float tiempo) {
		this.sprExplosion = new AnimatedSprite(x, y, this.regExplosion);
		this.sprExplosion.setScale(2);
		sprExplosion.animate((long) tiempo, false);
		mMainScene.attachChild(sprExplosion);
		this.removeSpriteAnimado(sprExplosion);
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mMainScene = new Scene();

		//inicializamos la lista de enemigos
		targetLL = new LinkedList<AnimatedSprite>(); 
		TargetsToBeAdded = new LinkedList<AnimatedSprite>();
		
		Sprite fondo = new Sprite(0, 0, mCamera.getWidth(),
				mCamera.getHeight(), mBackgroundTextureRegion);
		mMainScene.setBackground(new SpriteBackground(fondo));

		// final int PlayerX = this.regProtagonista.getWidth()/2;
		final int PlayerX = (int) mCamera.getMinX();
		final int PlayerY = (int) ((mCamera.getHeight() - regProtagonista
				.getHeight()) / 2);

		player = new AnimatedSprite(PlayerX, PlayerY, regProtagonista);
		player.setScale(1);
		mMainScene.attachChild(player);

		targetLL = new LinkedList<AnimatedSprite>();
		TargetsToBeAdded = new LinkedList<AnimatedSprite>();

		projectileLL = new LinkedList<Sprite>();
		projectilesToBeAdded = new LinkedList<Sprite>();

		createSpriteSpawnTimeHandler();
		mMainScene.registerUpdateHandler(detect);
		mMainScene.setOnSceneTouchListener(this);

		mPauseScene = new CameraScene(mCamera);
		final int x = (int) (mCamera.getWidth() / 2 - mPausedTextureRegion
				.getWidth() / 2);
		final int y = (int) (mCamera.getHeight() / 2 - mPausedTextureRegion
				.getHeight() / 2);
		final Sprite pausedSprite = new Sprite(x, y, mPausedTextureRegion);
		mPauseScene.attachChild(pausedSprite);
		mPauseScene.setBackgroundEnabled(false);

		mResultScene = new CameraScene(mCamera);

		fondoSprite = new Sprite(0, 0, mCamera.getWidth(), mCamera.getHeight(),
				mBackgroundTextureRegion);
		tituloSprite = new Sprite(0, 20, regTitulo);
		jugarSprite = new Sprite(x, mCamera.getHeight()
				- (regJugar.getHeight() * 4), regJugar);
		reglasSprite = new Sprite(x, mCamera.getHeight()
				- (regJugar.getHeight() * 2), regReglas);
		ganarSprite = new Sprite(0, 0, mCamera.getWidth(), mCamera.getHeight(),
				regGanar);
		perderSprite = new Sprite(0, 0, mCamera.getWidth(),
				mCamera.getHeight(), regPerder);
		reglasTexto = new Sprite(0, 0, mCamera.getWidth(), mCamera.getHeight(),
				regReglasTexto);

		mResultScene.attachChild(fondoSprite);
		mResultScene.attachChild(ganarSprite);
		mResultScene.attachChild(perderSprite);
		mResultScene.attachChild(reglasTexto);
		mResultScene.attachChild(tituloSprite);
		mResultScene.attachChild(jugarSprite);
		mResultScene.attachChild(reglasSprite);

		fondoSprite.setVisible(false);
		tituloSprite.setVisible(false);
		reglasSprite.setVisible(false);
		ganarSprite.setVisible(false);
		perderSprite.setVisible(false);
		reglasTexto.setVisible(false);

		mResultScene.setBackgroundEnabled(false);

		score = new ChangeableText(0, 0, mFont, String.valueOf(maxScore));
		score.setPosition(mCamera.getWidth() - score.getWidth() - 5, 5);

		backgroundMusic.play();
		restart();
		// Reproducimos la música de fondo

		return mMainScene;
	}

	public void restart() {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mMainScene.detachChildren();
				mMainScene.attachChild(player, 0);
				mMainScene.attachChild(score);
			}
		});
		hitCount = 0;
		score.setText(String.valueOf(hitCount));
		projectileLL.clear();
		projectilesToBeAdded.clear();
		TargetsToBeAdded.clear();
		targetLL.clear();
	}

	public void addTarget() {
		Random rand = new Random();
		// Se calcula la posición aleatoria del objetivo
		int x = (int) mCamera.getWidth() + regEnemigo1.getWidth();
		int minY = regEnemigo1.getHeight();
		int maxY = (int) (mCamera.getHeight() - regEnemigo1.getHeight());
		int rangeY = maxY - minY;
		int y = rand.nextInt(rangeY) + minY;
		// Se crea el sprite en la posición determinada anteriormente
		AnimatedSprite target = new AnimatedSprite(x, y, regEnemigo1.deepCopy());
		mMainScene.attachChild(target);
		// Se determina la velocidad del movimiento del objetivo
		int minDuration = 3;
		int maxDuration = 6;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		// Se crea el modificador y se le aplica al objetivo
		MoveXModifier mod = new MoveXModifier(actualDuration, target.getX(),
				-target.getWidth());
		target.registerEntityModifier(mod.deepCopy());
		// Almacenamos el objetivo en la lista
		TargetsToBeAdded.add(target);
		target.animate(200);
	}

	public void addBoss() {
		Random rand = new Random();
		// Se calcula la posición aleatoria del objetivo
		int x = (int) mCamera.getWidth() + regEnemigo2.getWidth();
		int minY = regEnemigo2.getHeight();
		int maxY = (int) (mCamera.getHeight() - regEnemigo2.getHeight());
		int rangeY = maxY - minY;
		int y = rand.nextInt(rangeY) + minY;
		// Se crea el sprite en la posición determinada anteriormente
		AnimatedSprite boss = new AnimatedSprite(x, y, regEnemigo2.deepCopy());
		mMainScene.attachChild(boss);
		boss.setScale(1.5f);
		// Se determina la velocidad del movimiento del objetivo
		int minDuration = 7;
		int maxDuration = 9;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		// Se crea el modificador y se le aplica al objetivo
		MoveXModifier mod = new MoveXModifier(actualDuration, boss.getX(),
				-boss.getWidth());
		boss.registerEntityModifier(mod.deepCopy());

		// Almacenamos el objetivo en la lista
		TargetsToBeAdded.add(boss);
		boss.animate(200);
	}

	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 1f;

		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						addTarget();
						if ((hitCount == 5 || hitCount == 10 || hitCount == 15)
								&& !muerteJefe) {
							addBoss();
							muerteJefe = true;
						}
					}
				});

		getEngine().registerUpdateHandler(spriteTimerHandler);
	}

	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it) {
		runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				mMainScene.detachChild(_sprite);
			}
		});
		it.remove();
	}

	public void removeSpriteAnimado(final AnimatedSprite _sprite,
			Iterator<AnimatedSprite> it) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mMainScene.detachChild(_sprite);
			}
		});
		it.remove();
	}

	public void removeSpriteAnimado(final AnimatedSprite _sprite) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mMainScene.detachChild(_sprite);
			}
		});

	}

	private void shootProjectile(final float pX, final float pY) {

		int offX = (int) (pX - player.getX());
		int offY = (int) (pY - player.getY());
		if (offX <= 0)
			return;

		final Sprite projectile;
		// 40 es porque de altura mide 80
		projectile = new Sprite(player.getX(), player.getY() + 40,
				mProjectileTextureRegion.deepCopy());
		mMainScene.attachChild(projectile, 1);

		int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
		float ratio = (float) offY / (float) offX;
		int realY = (int) ((realX * ratio) + projectile.getY());

		int offRealX = (int) (realX - projectile.getX());
		int offRealY = (int) (realY - projectile.getY());
		float length = (float) Math.sqrt((offRealX * offRealX)
				+ (offRealY * offRealY));
		float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
		float realMoveDuration = length / velocity;

		MoveModifier mod = new MoveModifier(realMoveDuration,
				projectile.getX(), realX, projectile.getY(), realY);
		projectile.registerEntityModifier(mod.deepCopy());

		projectilesToBeAdded.add(projectile);
		player.animate(100, false);
		shootingSound.play();

	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		final float touchX = pSceneTouchEvent.getX();
		final float touchY = pSceneTouchEvent.getY();

		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			shootProjectile(touchX, touchY);
			return true;
		}
		return false;
	}

	public void pauseGame() {
		mMainScene.setChildScene(mPauseScene, false, true, true);
		mEngine.stop();
	}

	public void unPauseGame() {
		mMainScene.clearChildScene();
		mEngine.start();
	}

	public void pauseMusic() {
		if (backgroundMusic.isPlaying())
			backgroundMusic.pause();
	}

	public void resumeMusic() {
		if (!backgroundMusic.isPlaying())
			backgroundMusic.resume();
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (mEngine.isRunning() && backgroundMusic.isPlaying()) {
				pauseMusic();
				pauseGame();
				Toast.makeText(this, "Menu button to resume",
						Toast.LENGTH_SHORT).show();
			} else {
				if (!backgroundMusic.isPlaying()) {
					resumeMusic();
					mEngine.start();
				}
				return true;
			}
		} else if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {

			if (!mEngine.isRunning() && backgroundMusic.isPlaying()) {
				mMainScene.clearChildScene();
				mEngine.start();
				restart();
				return true;
			}
			return super.onKeyDown(pKeyCode, pEvent);
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}

	public void fail() {
		if (mEngine.isRunning()) {
			perderSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);
			mEngine.stop();
		}
	}

	public void win() {
		if (mEngine.isRunning()) {
			ganarSprite.setVisible(true);
			jugarSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);
			mEngine.stop();
		}
	}

	public void reglas() {
		if (mEngine.isRunning()) {
			reglasSprite.setVisible(true);
			jugarSprite.setVisible(true);
			mMainScene.setChildScene(mResultScene, false, true, true);
			mEngine.stop();
		}
	}

}
