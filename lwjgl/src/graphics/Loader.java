package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import utils.BufferUtils;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();

	public RawModel loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadTexture(String texturePath){
		int width = 0;
		int height = 0;
		
		int[] pixels = null;
		
		try{
			BufferedImage image = ImageIO.read(new FileInputStream(texturePath));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for(int i = 0; i < width * height; i++){
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		textures.add(result);
		return result;
	}
	
	public void cleanUp(){
		for(int vao : vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture : textures){
			GL11.glDeleteTextures(texture);
		}
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices), GL15.GL_STATIC_DRAW);
	}
}
