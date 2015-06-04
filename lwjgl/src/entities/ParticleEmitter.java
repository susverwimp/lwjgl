package entities;

import graphics.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector2f;
import math.Vector3f;
import models.TexturedModel;
import textures.ModelTexture;

public class ParticleEmitter {

	private static Random randomGenerator = new Random();
	private final List<Particle> particles = new ArrayList<>();
	private int spawningRate;
	private float particleLifeTime;
	private Vector3f gravity;
	private Vector3f initialVelocity;
	private Vector3f position;
	private float velocityModifier = 1;
	private Vector2f randomXBoundaries;
	private Vector2f randomYBoundaries;
	private Vector2f randomZBoundaries;
	private Vector2f randomExpireTimeBoundaries;

	private boolean start = false;

	private static TexturedModel model;

	public ParticleEmitter(Loader loader, Vector3f position) {
		this(loader, position, 3, 1, new Vector3f(), new Vector3f(), 1,
				new Vector2f(-0.5f, 0.5f), new Vector2f(-0.5f, 0.5f),
				new Vector2f(-0.5f, 0.5f), new Vector2f(-0.5f, 0.5f));
	}

	public ParticleEmitter(Loader loader, Vector3f position, int spawningRate,
			float particleLifeTime, Vector3f gravity, Vector3f initialVelocity,
			float velocityModifier, Vector2f randomXBoundaries,
			Vector2f randomYBoundaries, Vector2f randomZBoundaries,
			Vector2f randomExpireTimeBoundaries) {
		this.gravity = gravity;
		this.position = position;
		this.initialVelocity = initialVelocity;
		this.particleLifeTime = particleLifeTime;
		this.spawningRate = spawningRate;
		this.velocityModifier = velocityModifier;

		this.randomXBoundaries = randomXBoundaries;
		this.randomYBoundaries = randomYBoundaries;
		this.randomZBoundaries = randomZBoundaries;

		this.randomExpireTimeBoundaries = randomExpireTimeBoundaries;

		model = new TexturedModel(loader.loadToVao(new float[] { -0.1f, 0.1f,
				0f, -0.1f, -0.1f, 0f, 0.1f, -0.1f, 0f, 0.1f, 0.1f, 0f },
				new float[] { 0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f }, new float[] {
						0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f },
				new int[] { 0, 1, 3, 3, 1, 2 }), new ModelTexture(
				loader.loadTexture("res/particles/snow-particle.png"), 2));
	}

	private Particle generateNewParticle(float dx, float dy, float dz) {
		float randomX = randomGenerator.nextFloat()
				* Math.abs(randomXBoundaries.x - randomXBoundaries.y)
				+ randomXBoundaries.x;
		float randomY = randomGenerator.nextFloat()
				* Math.abs(randomYBoundaries.x - randomYBoundaries.y)
				+ randomYBoundaries.x;
		float randomZ = randomGenerator.nextFloat()
				* Math.abs(randomZBoundaries.x - randomZBoundaries.y)
				+ randomZBoundaries.x;
		float randomLifeTime = randomGenerator.nextFloat()
				* Math.abs(randomExpireTimeBoundaries.x
						- randomExpireTimeBoundaries.y)
				+ randomExpireTimeBoundaries.x;
		Vector3f particleVelocity = new Vector3f(initialVelocity.x
				* velocityModifier + randomX / 10 + dx, initialVelocity.y
				* velocityModifier + randomY / 10 + dy, initialVelocity.z
				* velocityModifier + randomZ / 10 + dz);
		Particle particle = new Particle(model, new Vector3f(position.x,
				position.y, position.z), 0, 0,
				0, 1, particleVelocity,
				particleLifeTime + randomLifeTime);
		particle.getModel().getTexture().setAmbient(1);
		return particle;
	}

	public void update(float elapsedTime) {
		if (start) {
			for (int i = 0; i < spawningRate; i++) {
				particles.add(generateNewParticle(0, 0, 0));
			}
		}
		for (int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			particle.update(gravity, elapsedTime);
			if (particle.isDestroyed()) {
				particles.remove(i);
				i--;
			}
		}
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void start() {
		start = true;
	}

	public void stop() {
		start = false;
	}

}
