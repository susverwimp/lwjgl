package entities;

import org.lwjgl.glfw.GLFW;

import terrains.Terrain;
import graphics.Window;
import math.Vector3f;
import models.TexturedModel;

public class Player extends Entity {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, int textureIndex, Vector3f position,
			float rotX, float rotY, float rotZ, float scale) {
		super(model, textureIndex, position, rotX, rotY, rotZ, scale);
	}
	
	public void update(Window window, float elapsedTime, Terrain terrain){
		checkInputs(window);
		super.increaseRotation(0, currentTurnSpeed*elapsedTime, 0);
		float distance = currentSpeed * elapsedTime;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * elapsedTime;
		super.increasePosition(0, upwardsSpeed * elapsedTime, 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeight){
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump(){
		if(!isInAir){
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs(Window window){
		if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
			currentSpeed = RUN_SPEED;
		} else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
			currentSpeed = -RUN_SPEED;
		} else{
			currentSpeed = 0;
		}
		if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
			currentTurnSpeed = -TURN_SPEED;
		} else if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
			currentTurnSpeed = TURN_SPEED;
		} else{
			currentTurnSpeed = 0;
		}
		
		if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
			jump();
		}
	}
	
}
