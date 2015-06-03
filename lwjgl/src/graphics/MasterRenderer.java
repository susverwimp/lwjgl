package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Particle;
import math.Matrix4f;
import models.TexturedModel;
import shaders.ParticleShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float RED = 0.1f;
	private static final float GREEN = 0.1f;
	private static final float BLUE = 0.4f;
	
	private static boolean wireframe = false;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private ParticleRenderer particleRenderer;
	private ParticleShader particleShader = new ParticleShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Particle>> particles = new HashMap<TexturedModel, List<Particle>>();
	private List<Terrain> terrains = new ArrayList<>();
	
	public MasterRenderer(){
		enableCulling();
		enableBlending();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		particleRenderer = new ParticleRenderer(particleShader, projectionMatrix);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public static void enableBlending(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void wireframe(){
		if(!wireframe){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			wireframe = true;
		}else{
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			wireframe = false;
		}
	}
	
	public void render(List<Light> lights, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		particleShader.start();
		particleShader.loadSkyColor(RED, GREEN, BLUE);
		particleShader.loadLights(lights);
		particleShader.loadViewMatrix(camera);
		particleRenderer.render(particles);
		particleShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		particles.clear();
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processParticle(Particle particle){
		TexturedModel entityModel = particle.getModel();
		List<Particle> batch = particles.get(entityModel);
		if(batch!=null){
			batch.add(particle);
		}else{
			List<Particle> newBatch = new ArrayList<Particle>();
			newBatch.add(particle);
			particles.put(entityModel, newBatch);
		}
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		if(!wireframe)
			GL11.glClearColor(RED, GREEN, BLUE, 1);
		else
			GL11.glClearColor(1, 1, 1, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = 16f / 9f;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public void cleanUp(){
		terrainShader.cleanUp();
		shader.cleanUp();
		particleShader.cleanUp();
	}
	

}
