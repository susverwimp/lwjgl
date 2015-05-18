package entities;

import math.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f();
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){
		
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
