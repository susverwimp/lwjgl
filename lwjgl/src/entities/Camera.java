package entities;

import org.lwjgl.glfw.GLFW;

import graphics.Window;
import math.Vector3f;

public class Camera {

	private static final float MIN_DISTANCE_FROM_PLAYER = 5;
	private static final float MAX_DISTANCE_FROM_PLAYER = 100;

	private static final float MIN_PITCH = 0;
	private static final float MAX_PITCH = 80;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float offsetY = 10;

	private Vector3f position;
	private float pitch = 30;
	private float yaw;
	private float roll;

	private Player player;

	public Camera(Vector3f position, Player player) {
		this.position = position;
		this.player = player;
	}

	public Camera(Player player) {
		this(new Vector3f(), player);
	}

	public void update(Window window) {
		calculateZoom(window);
		calculatePitch(window);
		calculateAngleAroundPlayer(window);
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		yaw = (180 - (player.getRotY() + angleAroundPlayer)) % 360;
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

	private void calculateCameraPosition(float horizontalDistance,
			float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math
				.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math
				.toRadians(theta)));

		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticalDistance + offsetY;
		position.z = player.getPosition().z - offsetZ;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom(Window window) {
		float zoomLevel = window.getScrollDY() * 5;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer < MIN_DISTANCE_FROM_PLAYER || distanceFromPlayer > MAX_DISTANCE_FROM_PLAYER)
			distanceFromPlayer += zoomLevel;
	}

	private void calculatePitch(Window window) {
		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
			float pitchChange = window.getMouseDY() * 0.1f;
			pitch += pitchChange;
			if(pitch < MIN_PITCH || pitch > MAX_PITCH)
				pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer(Window window) {
		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
			float angleChange = window.getMouseDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

}
