package lwjgl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector2f;
import math.Vector3f;
import models.ModelData;
import models.RawModel;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Particle;
import entities.ParticleEmitter;
import entities.Player;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import graphics.GuiRenderer;
import graphics.Loader;
import graphics.MasterRenderer;
import graphics.OBJFileLoader;
import graphics.Window;
import gui.GuiTexture;

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
		if (!window.init())
			System.exit(1);

		Loader loader = new Loader();

		List<Entity> entities = new ArrayList<>();
		List<Light> lights = new ArrayList<>();

		// create sun
		Light sun = new Light(new Vector3f(1200, 1000, 1200), new Vector3f(1,
				1, 1));
		lights.add(sun);

		// create terrain textures and blendmap, used for multitexturing
		// terrains

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("res/terrain/grass-terrain-texture.png"));
		TerrainTexture rTexture = new TerrainTexture(
				loader.loadTexture("res/terrain/dirt-terrain-texture.png"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("res/terrain/flowergrass-terrain-texture.png"));
		TerrainTexture bTexture = new TerrainTexture(
				loader.loadTexture("res/terrain/rock-terrain-texture.png"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);

		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("res/terrain/blendmap.png"));

		// create terrains

		Terrain[][] terrains = new Terrain[3][3];
		for (int i = 0; i < terrains.length; i++) {
			for (int j = 0; j < terrains[i].length; j++) {
				terrains[i][j] = new Terrain(i, j, loader, texturePack,
						blendMap, "res/terrain/heightmap.png");
			}
		}

		MasterRenderer renderer = new MasterRenderer(loader);

		// create stall entity

		ModelData data = OBJFileLoader.loadOBJ("res/models/stall-model.obj");
		TexturedModel shopTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/models/stall-model-texture.png")));
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
				loader.loadTexture("res/models/fern-model-texture.png"));
		fernTextureAtlas.setNumberOfRows(2);

		// create fern

		data = OBJFileLoader.loadOBJ("res/models/fern-model.obj");
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
				entities.add(new Entity(fernTexturedModel, random.nextInt(4),
						new Vector3f(x, y, z), 0, 0, 0, 1));
			}
		}

		// create lamp with light
		data = OBJFileLoader.loadOBJ("res/models/lamp-model.obj");
		TexturedModel lampTexturedModel = new TexturedModel(loader.loadToVao(
				data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices()), new ModelTexture(
				loader.loadTexture("res/models/lamp-model-texture.png")));

		x = 1200;
		z = 2300;
		terrainColumn = (int) (x / Terrain.SIZE);
		terrainRow = (int) (z / Terrain.SIZE);
		y = terrains[terrainColumn][terrainRow].getHeightOfTerrain(x, z);
		Entity lamp = new Entity(lampTexturedModel, new Vector3f(x, y, z), 0,
				0, 0, 1);
		lamp.getModel().getTexture().setUseFakeLighting(true);
		entities.add(new Entity(lampTexturedModel, new Vector3f(x, y, z), 0, 0,
				0, 1));
		Light light2 = new Light(new Vector3f(x, y + 15, z), new Vector3f(2, 0,
				0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(light2);

		// create emitters
		List<ParticleEmitter> emitters = new ArrayList<>();
		// create particle rawModel
		RawModel particleModel = loader.loadToVao(new float[] { -0.1f, 0.1f,
				0f, -0.1f, -0.1f, 0f, 0.1f, -0.1f, 0f, 0.1f, 0.1f, 0f },
				new float[] { 0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f }, new float[] {
						0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f },
				new int[] { 0, 1, 3, 3, 1, 2 });
		TexturedModel snowParticle = new TexturedModel(particleModel, new ModelTexture(loader.loadTexture("res/particles/snow-particle.png"), 2));
		ParticleEmitter iceConeEmitter = new ParticleEmitter(snowParticle, new Vector3f(1200, 6, 2010),
				9, 2, new Vector3f(0, 0.1f, 0), new Vector3f(0, 0.1f, 0), 1,
				new Vector2f(-0.5f, 0.5f), new Vector2f(), new Vector2f(-0.5f,
						0.5f), new Vector2f(-0.2f, 0.2f), 3);
		emitters.add(iceConeEmitter);
		iceConeEmitter.start();

		TexturedModel fireParticle = new TexturedModel(particleModel, new ModelTexture(loader.loadTexture("res/particles/fire-particle.png"), 4));
		ParticleEmitter fireEmitter = new ParticleEmitter(fireParticle, new Vector3f(1220, 6, 2010),
				9, 4, new Vector3f(0, 0.01f, 0), new Vector3f(0, 0.01f, 0), 1,
				new Vector2f(-0.5f, 0.5f), new Vector2f(), new Vector2f(-0.5f,
						0.5f), new Vector2f(-0.2f, 0.2f), 7);
		emitters.add(fireEmitter);
		fireEmitter.start();
		
		// create player
		Player player = new Player(lampTexturedModel, 0, new Vector3f(1200, 3,
				2000), 0, 0, 0, 1);

		// create camera to player
		Camera camera = new Camera(player);

		// create GUIs
		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture gui = new GuiTexture(
				loader.loadTexture("res/terrain/blendmap.png"), new Vector2f(
						0.6f, -0.6f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		float startTime = window.getTime();

		// main loop
		while (isRunning) {
			if (window.WindowShouldBeClosed())
				isRunning = false;

			float currentTime = window.getTime();
			float elapsedTime = currentTime - startTime;
			startTime = currentTime;

			camera.update(window);
			terrainColumn = (int) (player.getPosition().x / Terrain.SIZE);
			terrainRow = (int) (player.getPosition().z / Terrain.SIZE);
			player.update(window, elapsedTime,
					terrains[terrainColumn][terrainRow]);
			for(ParticleEmitter emitter : emitters){
				emitter.update(elapsedTime);
			}

			renderer.processEntity(player);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			for (int i = 0; i < terrains.length; i++) {
				for (int j = 0; j < terrains[i].length; j++) {
					renderer.processTerrain(terrains[i][j]);
				}
			}
			for(ParticleEmitter emitter : emitters){
				for (Particle particle : emitter.getParticles()) {
					renderer.processParticle(particle);
				}
			}
			
			renderer.render(lights, camera);

			guiRenderer.render(guis);

			window.update();
			window.swapBuffers();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		window.cleanUp();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}
