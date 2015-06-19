package com.wildwestspace;

import java.io.IOException;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.view.Display;

public class BaseActivity extends SimpleBaseGameActivity {

	private Font mFont;
	private EngineOptions enOpts;
	private Camera mCamera;
	private int cameraWidth;
	private int cameraHeight;
	// Referencia a la escena actual
	private Scene mCurrentScene;
	private Sound shootingSound;
	private Music backgroundMusic;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTargetTextureRegion;
	private TiledTextureRegion mPlayerTextureRegion;
	private TiledTextureRegion mProjectileTextureRegion;
	private BitmapTextureAtlas mFontTexture;
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private TextureRegion mParallaxLayerFront;
	private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerMid;
	private static BaseActivity instance;
	private BitmapTextureAtlas fondoSplashTexture;
	private TiledTextureRegion mFondoSplash;
	private TextureRegion menuBtnPlayReg;
	private TextureRegion easy;
	private TextureRegion medium;
	private TextureRegion hard;
	private TiledTextureRegion mExplosion;
	private AlmacenPuntuaciones puntuacionMaxima;
	private TextureRegion menuBtnRateUs;
	private TiledTextureRegion mBonusTextureRegion;

	public Font getmFont() {
		return mFont;
	}

	public void setmFont(Font mFont) {
		this.mFont = mFont;
	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public Sound getShootingSound() {
		return shootingSound;
	}

	public void setShootingSound(Sound shootingSound) {
		this.shootingSound = shootingSound;
	}

	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

	public void setBackgroundMusic(Music backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	public TiledTextureRegion getmTargetTextureRegion() {
		return mTargetTextureRegion;
	}

	public void setmTargetTextureRegion(TiledTextureRegion mTargetTextureRegion) {
		this.mTargetTextureRegion = mTargetTextureRegion;
	}

	public TiledTextureRegion getmPlayerTextureRegion() {
		return mPlayerTextureRegion;
	}

	public void setmPlayerTextureRegion(TiledTextureRegion mPlayerTextureRegion) {
		this.mPlayerTextureRegion = mPlayerTextureRegion;
	}


	public TiledTextureRegion getmProjectileTextureRegion() {
		return mProjectileTextureRegion;
	}

	public void setmProjectileTextureRegion(
			TiledTextureRegion mProjectileTextureRegion) {
		this.mProjectileTextureRegion = mProjectileTextureRegion;
	}

	public BitmapTextureAtlas getmAutoParallaxBackgroundTexture() {
		return mAutoParallaxBackgroundTexture;
	}

	public void setmAutoParallaxBackgroundTexture(
			BitmapTextureAtlas mAutoParallaxBackgroundTexture) {
		this.mAutoParallaxBackgroundTexture = mAutoParallaxBackgroundTexture;
	}

	public TextureRegion getmParallaxLayerFront() {
		return mParallaxLayerFront;
	}

	public void setmParallaxLayerFront(TextureRegion mParallaxLayerFront) {
		this.mParallaxLayerFront = mParallaxLayerFront;
	}

	public TextureRegion getmParallaxLayerBack() {
		return mParallaxLayerBack;
	}

	public void setmParallaxLayerBack(TextureRegion mParallaxLayerBack) {
		this.mParallaxLayerBack = mParallaxLayerBack;
	}

	public TextureRegion getmParallaxLayerMid() {
		return mParallaxLayerMid;
	}

	public void setmParallaxLayerMid(TextureRegion mParallaxLayerMid) {
		this.mParallaxLayerMid = mParallaxLayerMid;
	}

	public static BaseActivity getInstance() {
		return instance;
	}

	public static void setInstance(BaseActivity instance) {
		BaseActivity.instance = instance;
	}

	public BitmapTextureAtlas getFondoSplashTexture() {
		return fondoSplashTexture;
	}

	public void setFondoSplashTexture(BitmapTextureAtlas fondoSplashTexture) {
		this.fondoSplashTexture = fondoSplashTexture;
	}

	public TiledTextureRegion getmFondoSplash() {
		return mFondoSplash;
	}

	public void setmFondoSplash(TiledTextureRegion mFondoSplash) {
		this.mFondoSplash = mFondoSplash;
	}

	public static BaseActivity getSharedInstance() {
		return instance;
	}

	public Scene getmCurrentScene() {
		return mCurrentScene;
	}

	public void setmCurrentScene(Scene mCurrentScene) {
		this.mCurrentScene = mCurrentScene;
	}

	public TiledTextureRegion getmExplosion() {
		return mExplosion;
	}

	public void setmExplosion(TiledTextureRegion mExplosion) {
		this.mExplosion = mExplosion;
	}

	public TextureRegion getMenuBtnPlayReg() {
		return menuBtnPlayReg;
	}

	public void setMenuBtnPlayReg(TextureRegion menuBtnPlayReg) {
		this.menuBtnPlayReg = menuBtnPlayReg;
	}

	public TextureRegion getEasy() {
		return easy;
	}

	public void setEasy(TextureRegion easy) {
		this.easy = easy;
	}

	public TextureRegion getMedium() {
		return medium;
	}

	public void setMedium(TextureRegion medium) {
		this.medium = medium;
	}

	public TextureRegion getHard() {
		return hard;
	}

	public void setHard(TextureRegion hard) {
		this.hard = hard;
	}

	public TextureRegion getMenuBtnRateUs() {
		return menuBtnRateUs;
	}

	public void setMenuBtnRateUs(TextureRegion menuBtnRateUs) {
		this.menuBtnRateUs = menuBtnRateUs;
	}

	public TiledTextureRegion getmBonusTextureRegion() {
		return mBonusTextureRegion;
	}

	public void setmBonusTextureRegion(TiledTextureRegion mBonusTextureRegion) {
		this.mBonusTextureRegion = mBonusTextureRegion;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;

		puntuacionMaxima = new AlmacenPuntuaciones();
		puntuacionMaxima.setPuntuacion(400);

		final Display display = getWindowManager().getDefaultDisplay();
		cameraWidth = display.getWidth();
		cameraHeight = display.getHeight();

		/*
		 * if (cameraWidth > 720 || cameraHeight > 1280) { mCamera = new
		 * Camera(0, 0, cameraWidth, cameraHeight); } else { mCamera = new
		 * Camera(0, 0, cameraWidth, cameraHeight); }
		 */
		mCamera = new Camera(0, 0, cameraWidth, cameraHeight);

		enOpts = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera);
		// de esta manera añadimos el sonido de fondo y de los Sprites
		enOpts.getAudioOptions().setNeedsMusic(true);
		enOpts.getAudioOptions().setNeedsSound(true);

		return enOpts;

	}

	@Override
	protected void onCreateResources() {
		// se carga la fuente del texto
		mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
		mFont.load();

		// Cargamos los sonidos dentro del juego, tanto de background como de
		// disparos
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

		// cargamos los AnimatedSprites y los Sprites
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"enemigo.png", 0, 0, 3, 1);

		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"nave.png", 0, 70, 3, 1);

	
		this.mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"disparo.png", 301, 0, 3, 1);
		this.menuBtnPlayReg = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, getAssets(),
						"jugar.png", 451, 0);
		this.mBonusTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"disparo.png", 0, 200, 3, 1);
		
		//TODO modificar
		this.menuBtnRateUs = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, getAssets(),
						"jugar.png", 550, 0);
		this.mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		this.mBitmapTextureAtlas.load();

		this.mFontTexture = new BitmapTextureAtlas(this.getTextureManager(),
				256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(null, mFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.ITALIC), 40, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);
		// this.mBitmapTextureAtlas.load();

		// Cargamos el Fondo con Parallax Background
		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"parallax_background_layer_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"parallax_background_layer_back.png", 0, 721);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"parallax_background_layer_mid.png", 1281, 0);
		this.mAutoParallaxBackgroundTexture.load();

		// fondo para el splash y explosion
		this.fondoSplashTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 2048, 2048,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFondoSplash = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.fondoSplashTexture, this,
						"logoInicio.png", 0, 0, 7, 1);
		this.mExplosion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.fondoSplashTexture, this,
						"explosion.png", 0, 300, 4, 4);
		this.easy = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.fondoSplashTexture, this, "facil.png", 205, 300);
		this.medium = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.fondoSplashTexture, this, "medium.png", 350, 300);
		this.hard = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.fondoSplashTexture, this, "hard.png", 500, 300);
		this.fondoSplashTexture.load();

	}

	@Override
	protected Scene onCreateScene() {
		// evita que cambie de sentido la rotacion
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new SplashScene();

		return mCurrentScene;
	}

	// Cambiar el estado
	public void setCurrentScene(Scene scene) {
		mCurrentScene = null;
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);

	}

	public void onBackPressed() {
		if (mCurrentScene instanceof GameScene)
			((GameScene) mCurrentScene).detachSelf();
		mCurrentScene = null;
		SensorListener.instance = null;
		super.onBackPressed();
	}

}