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
		
		List<Entity> entities = new ArrayList<>();
		List<Light> lights = new ArrayList<>();
		
		//create sun
		Light sun = new Light(new Vector3f(1200, 1000, 1200), new Vector3f(
				1, 1, 1));
		lights.add(sun);

		// create terrain textures and blendmap, used for multitexturing
		// terrains

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

		// create terrains

		Terrain[][] terrains = new Terrain[3][3];
		for (int i = 0; i < terrains.length; i++) {
			for (int j = 0; j < terrains[i].length; j++) {
				terrains[i][j] = new Terrain(i, j, loader, texturePack,
						blendMap, "heightmap");
			}
		}

		// create camera

		Camera camera = new Camera(new Vector3f(1200, 1, 2400));

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

		float x = 1250;
		float z = 2000;
		int terrainColumn = (int) (x / Terrain.SIZE);
		int terrainRow = (int) (z / Terrain.SIZE);
		float y = terrains[terrainColumn][terrainRow].getHeightOfTerrain(x, z);
		Entity shop = new Entity(shopTexturedModel, new Vector3f(x, y, z), 0,
				0, 0, 1);
		shop.increaseRotation(0, 180, 0);

		// put all entities to draw in list
		entities.add(shop);

		// add fern
		ModelTexture fernTextureAtlas = new ModelTexture(
				loader.loadTexture("res/fern-model-texture.png"));
		fernTextureAtlas.setNumberOfRows(2);

		// create fern
		
		data = OBJFileLoader.loadOBJ("res/fern-model.obj");
		TexturedModel fernTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), fernTextureAtlas);

		Random random = new Random(676452);
		for (int i = 0; i < 3000; i++) {
			if (i % 2 == 0) {
				x = random.nextFloat() * terrains.length * Terrain.SIZE;
				z = random.nextFloat() * terrains[0].length * Terrain.SIZE;
				terrainColumn = (int) (x / Terrain.SIZE);
				terrainRow = (int) (z / Terrain.SIZE);
				y = terrains[terrainColumn][terrainRow]
						.getHeightOfTerrain(x, z);
				entities.add(new Entity(fernTexturedModel, random.nextInt(4), new Vector3f(
						x, y, z), 0, 0, 0, 1));
			}
		}
		
		//create lamp with light
		data = OBJFileLoader.loadOBJ("res/lamp-model.obj");
		TexturedModel lampTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/lamp-model-texture.png")));

		x = 1200;
		z = 2300;
		terrainColumn = (int) (x / Terrain.SIZE);
		terrainRow = (int) (z / Terrain.SIZE);
		y = terrains[terrainColumn][terrainRow].getHeightOfTerrain(x, z);
		Entity lamp = new Entity(lampTexturedModel, new Vector3f(x, y, z), 0,
				0, 0, 1);
		lamp.getModel().getTexture().setUseFakeLighting(true);
		entities.add(new Entity(lampTexturedModel, new Vector3f(x, y, z), 0,
				0, 0, 1));
		Light light2 = new Light(new Vector3f(x, y+15, z), new Vector3f(2,
				0, 0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(light2);
		
		// main loop

		while (isRunning) {
			if (window.WindowShouldBeClosed())
				isRunning = false;
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			for (int i = 0; i < terrains.length; i++) {
				for (int j = 0; j < terrains[i].length; j++) {
					renderer.processTerrain(terrains[i][j]);
				}
			}

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
