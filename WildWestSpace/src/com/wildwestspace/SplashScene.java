package com.wildwestspace;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

public class SplashScene extends Scene {
	private BaseActivity activity;
	private AnimatedSprite fondo;
	private final int TIEMPO_ESPERA = 4;
	private Camera mCamera;

	public SplashScene() {

		activity = BaseActivity.getSharedInstance();
		mCamera = activity.getmCamera();
		setBackground(new Background(Color.CYAN));
		fondo = new AnimatedSprite(0, 0, mCamera.getWidth(),
				mCamera.getHeight(), activity.getmFondoSplash(),
				activity.getVertexBufferObjectManager());
		fondo.setScale(0.5f);
		fondo.animate(new long[] { 200, 200, 200, 200, 200, 200, 200 }, 0, 6,
				false);
		attachChild(fondo);

		loadResources();
	}

	private void loadResources() {
		DelayModifier dMod = new DelayModifier(TIEMPO_ESPERA,
				new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> arg0,
							IEntity arg1) {
					}

					@Override
					public void onModifierFinished(IModifier<IEntity> arg0,
							IEntity arg1) {
						activity.setCurrentScene(new MainMenuScene());
					}
				});

		registerEntityModifier(dMod);
	}

}
