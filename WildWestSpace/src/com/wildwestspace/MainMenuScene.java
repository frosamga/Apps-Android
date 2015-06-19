package com.wildwestspace;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class MainMenuScene extends MenuScene implements
		IOnMenuItemClickListener {
	private BaseActivity activity;
	private final int MENU_START = 0;
	private final int MENU_RATE_US = 1;
	private final int MENU_EASY = 2;
	private final int MENU_MEDIUM = 3;
	private final int MENU_HARD = 4;

	private final IMenuItem startButton,botonRateUs,botonEasy,botonMedium,botonHard;
	

	public MainMenuScene() {
		// añade un fondo y un boton de jugar que saltara a la nueva escena
		super(BaseActivity.getSharedInstance().getmCamera());
		activity = BaseActivity.getSharedInstance();

		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = activity
				.getVertexBufferObjectManager();

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

		startButton = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_START,
				100, 300, activity.getMenuBtnPlayReg(),
				activity.getVertexBufferObjectManager()), 1.1f, 1);
		botonRateUs = new ScaleMenuItemDecorator(new SpriteMenuItem(
				MENU_RATE_US, 100, 300, activity.getMenuBtnRateUs(),
				activity.getVertexBufferObjectManager()), 1.1f, 1);

		startButton.setPosition(mCamera.getWidth() / 2 - startButton.getWidth()
				/ 2, mCamera.getHeight() / 2 - startButton.getHeight() / 2);

		botonRateUs.setPosition(
				mCamera.getWidth() / 2 - (2 * startButton.getWidth()),
				mCamera.getHeight() / 2 - startButton.getHeight() / 2);

		botonEasy = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_EASY, 100, 100, activity.getEasy(),
						activity.getVertexBufferObjectManager()), 1.1f, 1);
		botonMedium = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_MEDIUM, 100, 100, activity.getMedium(),
						activity.getVertexBufferObjectManager()), 1.1f, 1);
		botonHard = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_HARD, 100, 100, activity.getHard(),
						activity.getVertexBufferObjectManager()), 1.1f, 1);
		
		
		botonEasy.setPosition(
				mCamera.getWidth() / 2 - botonEasy.getWidth() / 2,
				mCamera.getHeight() / 2 - 4*botonEasy.getHeight() / 2);

		botonMedium.setPosition(
				mCamera.getWidth() / 2 - botonEasy.getWidth() / 2, 
				mCamera.getHeight() / 2 - botonEasy.getHeight() / 2);
		botonHard.setPosition(
				mCamera.getWidth() / 2 - botonEasy.getWidth() / 2,
				mCamera.getHeight() / 2 + 2*botonEasy.getHeight() / 2);
		

		addMenuItem(startButton);
		addMenuItem(botonRateUs);

		setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_START:
			clearMenuItems();
			addMenuItem(botonEasy);
			addMenuItem(botonMedium);
			addMenuItem(botonHard);
			
			return true;
		case MENU_RATE_US:
			//TODO: ir al google store
			//activity.setCurrentScene();
			return true;
		case MENU_EASY:
			activity.setCurrentScene(new GameScene(null));
			//activity.setCurrentScene();
			return true;
		case MENU_MEDIUM:
			activity.setCurrentScene(new GameScene(null));
			return true;
		case MENU_HARD:
			activity.setCurrentScene(new GameScene(null));
			
			return true;
		default:
			break;
		}
		return false;
	}

}
