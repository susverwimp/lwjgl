package shaders;

import math.Maths;
import math.Matrix4f;
import math.Vector2f;
import entities.Camera;

public class ParticleShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/home/susverwimp/kamidesigns/gamedesign/java/lwjgl/lwjgl/shaders/particleVertexShader.txt";
    private static final String FRAGMENT_FILE = "/home/susverwimp/kamidesigns/gamedesign/java/lwjgl/lwjgl/shaders/particleFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_numberOfRows;
    private int location_offset1;
    private int location_offset2;

    private int location_blendFactor;

    public ParticleShader() {
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

        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset1 = super.getUniformLocation("offset1");
        location_offset2 = super.getUniformLocation("offset2");

        location_blendFactor = super.getUniformLocation("blendFactor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadOffset(float x1, float y1, float x2, float y2) {
        super.load2DVector(location_offset1, new Vector2f(x1, y1));
        super.load2DVector(location_offset2, new Vector2f(x2, y2));
    }

    public void loadBlendFactor(float blendFactor) {
        super.loadFloat(location_blendFactor, blendFactor);
    }
}
