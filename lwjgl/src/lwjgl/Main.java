package lwjgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector3f;
import models.ModelData;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import graphics.Loader;
import graphics.MasterRenderer;
import graphics.OBJFileLoader;
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
		// initialize glfw window

		Window window = new Window(1280, 720, "Open World");
		window.init();

		Loader loader = new Loader();

		// create terrain textures and blendmap, used for multitexturing terrains

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("res/grass-terrain-texture.png"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("res/dirt-terrain-texture.png"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("res/flowergrass-terrain-texture.png"));
		TerrainTexture bTexture = new TerrainTexture(
				loader.loadTexture("res/rock-terrain-texture.png"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("res/blendmap.png"));

		// create terrain

		Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain3 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");

		// create light

		Light light1 = new Light(new Vector3f(0, 100, -100), new Vector3f(1,1,1));
		Light light2 = new Light(new Vector3f(185, 10, -293f), new Vector3f(2, 0, 0), new Vector3f(1,  0.01f, 0.002f));
		Light light3 = new Light(new Vector3f(370, 17, -300f), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f));
		Light light4 = new Light(new Vector3f(293, 7, -305f), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f));
		
		List<Light> lights = new ArrayList<>();
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
		lights.add(light4);

		// create camera

		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();

		// create stall entity

		ModelData data = OBJFileLoader.loadOBJ("res/stall-model.obj");
		TexturedModel shopTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/stall-model-texture.png")));
		shopTexturedModel.getTexture().setShineDamper(10);
		shopTexturedModel.getTexture().setReflectivity(1);
		shopTexturedModel.getTexture().setAmbient(0.5f);

		Entity shop = new Entity(shopTexturedModel, new Vector3f(0, 0, -50f),
				0, 0, 0, 1);
		shop.increaseRotation(0, 180, 0);
		
		// create grass on terrain

		data = OBJFileLoader.loadOBJ("res/grass-model.obj");
		TexturedModel grassTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/grass-model-texture.png")));
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);

		// put all entities to draw in list

		List<Entity> entities = new ArrayList<>();

		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(grassTexturedModel, new Vector3f(random
					.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 1));
		}
		entities.add(shop);

		// main loop

		while (isRunning) {
			if (window.WindowShouldBeClosed())
				isRunning = false;
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.processTerrain(terrain1);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);

			renderer.render(lights, camera);

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
