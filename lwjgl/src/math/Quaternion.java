package math;

public class Quaternion {

	public float x;
	public float y;
	public float z;
	public float w;

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void normalize(){
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}
	
	public float length(){
		return (float) Math.sqrt(x*x+y*y+z*z+w*w);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getW() {
		return w;
	}
}
