package math;

public class Vector3f {

	public float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f() {
		this(0, 0, 0);
	}
	
	public float length(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public void normalize(){
		float length = length();
		if(length!=1 && length > 0){
			x /= length;
			y /= length;
			z /= length;
		}
	}

}
