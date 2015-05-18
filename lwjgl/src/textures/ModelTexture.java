package textures;

public class ModelTexture {
	
	private int textureId;
	
	private float shineDamper = 5;
	private float reflectivity = 0.3f;
	private float ambient = 0.2f;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	public ModelTexture(int textureId){
		this.textureId = textureId;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getTextureId(){
		return textureId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public float getAmbient() {
		return ambient;
	}

	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}
}
