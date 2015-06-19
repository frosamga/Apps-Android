package com.wildwestspace;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

public class ResultScene extends CameraScene implements IOnSceneTouchListener {

	boolean done;
	BaseActivity activity;
	GameScene game;

	public ResultScene(Camera mCamera, int puntuacion) {
		super(mCamera);
		activity = BaseActivity.getSharedInstance();
		setBackgroundEnabled(false);
		Text result = new Text(0, 0, activity.getmFont(), "Tu puntuación : "
				+ puntuacion, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());

		final int x = (int) (mCamera.getWidth() / 2 - result.getWidth() / 2);
		final int y = (int) (mCamera.getHeight() / 2 - result.getHeight() / 2);

		done = false;
		result.setPosition(x, mCamera.getHeight() + result.getHeight());
		result.setRotation(+90f);

		MoveYModifier mod = new MoveYModifier(5, result.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				done = true;
			}
		};
		attachChild(result);
		result.registerEntityModifier(mod);
		setOnSceneTouchListener(this);
	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		if (!done) {
			return true;
		} else {
			((GameScene) activity.getmCurrentScene()).restart();
			return false;
		}
	}

}
