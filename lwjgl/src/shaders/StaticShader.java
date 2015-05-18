package shaders;

import entities.Camera;
import entities.Light;
import math.Maths;
import math.Matrix4f;
import math.Vector3f;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/vertexshader.txt";
	private static final String FRAGMENT_FILE = "shaders/fragmentshader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_ambient;
	private int location_useFakeLighting;
	private int location_skyColor;
	
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambient = super.getUniformLocation("ambient");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColor = super.getUniformLocation("skyColor");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getColor());
	}
	
	public void loadShineVariables(float damper, float reflectivity, float ambient){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_ambient, ambient);
	}
	
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
}
