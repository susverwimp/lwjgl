package entities;

import math.Vector3f;

public class Camera {
	
	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;

	public Camera(Vector3f position){
		this.position = position;
	}
	
	public Camera(){
		this(new Vector3f());
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
