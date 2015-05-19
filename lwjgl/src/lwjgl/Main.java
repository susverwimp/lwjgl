package lwjgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector3f;
import models.ModelData;
import models.RawModel;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import graphics.Loader;
import graphics.MasterRenderer;
import graphics.OBJFileLoader;
import graphics.EntityRenderer;
import graphics.Window;

public class Main implements Runnable {

	private Thread thread;
	private boolean isRunning;

	public void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		Window window = new Window(1280, 720, "Open World");
		window.init();

		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("res/grass-terrain-texture.png"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("res/dirt-terrain-texture.png"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("res/flowergrass-terrain-texture.png"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("res/rock-terrain-texture.png"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("res/blendmap.png"));
		
		ModelData data = OBJFileLoader.loadOBJ("stall-model");
		TexturedModel shopTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/stall-model-texture.png")));
		ModelTexture texture = shopTexturedModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		texture.setAmbient(0.5f);

		Entity shop = new Entity(shopTexturedModel, new Vector3f(0, 0, -50f),
				0, 0, 0, 1);
		shop.increaseRotation(0, 180, 0);

		Light light = new Light(new Vector3f(0, 10, -20f),
				new Vector3f(1, 1, 1));

		Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap);
		Terrain terrain3 = new Terrain(-1, -1, loader, texturePack, blendMap);

		data = OBJFileLoader.loadOBJ("grass-model");
		TexturedModel grassTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/grass-model-texture.png")));
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);

		Camera camera = new Camera();
		camera.getPosition().y += 1;

		MasterRenderer renderer = new MasterRenderer();

		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(grassTexturedModel, new Vector3f(random
					.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 1));
		}
		entities.add(shop);

		while (isRunning) {
			if (window.WindowShouldBeClosed())
				isRunning = false;
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);

			renderer.render(light, camera);

			window.update(camera);
			window.swapBuffers();
		}

		renderer.cleanUp();
		loader.cleanUp();
		window.cleanUp();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}
