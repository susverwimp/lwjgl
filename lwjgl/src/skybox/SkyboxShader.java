package skybox;

import math.Maths;
import math.Matrix4f;
import math.Vector3f;
import entities.Camera;
import shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram{
	 
    private static final String VERTEX_FILE = "shaders/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "shaders/skyboxFragmentShader.txt";
     
    private static final float ROTATE_SPEED = 0.01f;
    
    private int location_projectionMatrix;
    private int location_viewMatrix;
    
    private int location_fogColor;
    
    private float rotation = 0;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    
        location_fogColor = super.getUniformLocation("fogColor");
    }
    
    public void loadFogColor(float r, float g, float b){
    	super.loadVector(location_fogColor, new Vector3f(r,g,b));
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}