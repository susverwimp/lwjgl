package entities;

import math.Vector3f;
import models.TexturedModel;

public class Particle extends Entity {

	private float expireTime;
	private Vector3f velocity;

	public Particle(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, Vector3f velocity, float expireTime) {
		this(model, 0, position, rotX, rotY, rotZ, scale, velocity, expireTime);
	}

	public Particle(TexturedModel model, int textureIndex, Vector3f position,
			float rotX, float rotY, float rotZ, float scale, Vector3f velocity, float expireTime) {
		super(model, textureIndex, position, rotX, rotY, rotZ, scale);
		this.velocity = velocity;
		this.expireTime = expireTime;
	}

	public void update(Vector3f gravity, float elapsedTime) {
		velocity.x += gravity.x * elapsedTime;
		velocity.y += gravity.y * elapsedTime;
		velocity.z += gravity.z * elapsedTime;
		increasePosition(velocity.x, velocity.y, velocity.z);
		expireTime -= elapsedTime;
	}
	
	public boolean isDestroyed(){
		return expireTime <= 0;
	}
	
	@Override
    public String toString() {
        return "Particle{" +
                "position=" + getPosition().x + "-"+getPosition().y + "-" + getPosition().z +
                ", velocity=" + velocity.x + "-" + velocity.y + "-" + velocity.z +
                ", expireTime=" + expireTime +
                '}';
    }
}