package entities;

import math.Vector3f;
import models.TexturedModel;

public class Particle extends Entity {

	private float expireTime;
	private float timePerTexture;
	private float startTime = 0;
	private Vector3f velocity;

	public Particle(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, Vector3f velocity,
			float expireTime) {
		this(model, 0, position, rotX, rotY, rotZ, scale, velocity, expireTime);
	}

	public Particle(TexturedModel model, int textureIndex, Vector3f position,
			float rotX, float rotY, float rotZ, float scale, Vector3f velocity,
			float expireTime) {
		super(model, textureIndex, position, rotX, rotY, rotZ, scale);
		this.velocity = velocity;
		this.expireTime = expireTime;
		int numberOfRows = getModel().getTexture().getNumberOfRows();
		timePerTexture = expireTime / (numberOfRows * numberOfRows);
	}

	public void update(Vector3f gravity, float elapsedTime) {
		velocity.x += gravity.x * elapsedTime;
		velocity.y += gravity.y * elapsedTime;
		velocity.z += gravity.z * elapsedTime;
		increasePosition(velocity.x, velocity.y, velocity.z);
		startTime += elapsedTime;
		if (startTime >= (getTextureIndex() + 1) * timePerTexture) {
			increaseTextureIndex();
		}
	}

	public boolean isDestroyed() {
		return startTime >= expireTime;
	}

	public float getTextureXOffset2() {
		int numberOfRows = getModel().getTexture().getNumberOfRows();
		if (getTextureIndex() + 1 < numberOfRows * numberOfRows) {
			int column = (getTextureIndex() + 1)
					% getModel().getTexture().getNumberOfRows();
			return (float) column
					/ (float) getModel().getTexture().getNumberOfRows();
		}
		return super.getTextureYOffset();
	}

	public float getTextureYOffset2() {
		int numberOfRows = getModel().getTexture().getNumberOfRows();
		if (getTextureIndex() + 1 < numberOfRows * numberOfRows) {
			int row = (getTextureIndex() + 1)
					/ getModel().getTexture().getNumberOfRows();
			return (float) row
					/ (float) getModel().getTexture().getNumberOfRows();
		}
		return super.getTextureYOffset();
	}

	public float getBlendFactor() {
		return startTime % timePerTexture;
	}
}