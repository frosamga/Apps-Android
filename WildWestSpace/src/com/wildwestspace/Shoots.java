package com.wildwestspace;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class Shoots {

	private BaseActivity activity;
	private AnimatedSprite player, projectile;
	private Camera mCamera;
	private float tamaño;
	private LinkedList<AnimatedSprite> projectilesLista;


	public Shoots(AnimatedSprite playerSprite, AnimatedSprite projectileSprite,
			Camera camera, float tamañoProyectil,LinkedList<AnimatedSprite> projectilesToBeAdded) {
		player = playerSprite;
		projectile = projectileSprite;
		mCamera = camera;
		tamaño=tamañoProyectil;
		projectilesLista=projectilesToBeAdded;
		
	}

	private void shootProjectile(final float pX) {

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
		projectile.setScale(tamaño);
		projectile.animate(new long[] { 200, 200, 200 }, 0, 2, true);
		int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
		float velocity = 0.9f;
		MoveXModifier mod = new MoveXModifier(velocity, projectile.getX(),
				realX);
		projectile.registerEntityModifier(mod.deepCopy());
		projectilesLista.add(projectile);
		//attachChild(projectile);
		activity.getShootingSound().play();

	}

}
