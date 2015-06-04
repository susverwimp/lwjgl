package graphics;

import entities.Particle;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import math.Maths;
import math.Matrix4f;
import models.RawModel;
import models.TexturedModel;
import shaders.ParticleShader;
import textures.ModelTexture;

public class ParticleRenderer {
	
private ParticleShader shader;
	
	public ParticleRenderer(ParticleShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Particle>> particles){
		for(TexturedModel model : particles.keySet()){
			prepareTexturedModel(model);
			List<Particle> batch = particles.get(model);
			for(Particle particle : batch){
				prepareInstance(particle);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.isHasTransparency())
			MasterRenderer.disableCulling();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureId());
		
	}
	
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Particle particle){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				particle.getPosition(), particle.getRotX(), particle.getRotY(),
				particle.getRotZ(), particle.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(particle.getTextureXOffset(), particle.getTextureYOffset(), particle.getTextureXOffset2(), particle.getTextureYOffset2());
		shader.loadBlendFactor(particle.getBlendFactor());
	}

}
