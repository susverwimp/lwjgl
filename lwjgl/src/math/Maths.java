package math;

import entities.Camera;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrixWithQuaternion(Vector3f translation, float rx, float ry, float rz, float scale){
		Quaternion qx = new Quaternion((float) (1*Math.sin(Math.toRadians(rx)/2)), 0, 0, (float)Math.cos(Math.toRadians(rx)/2));
		qx.normalize();
		Quaternion qy = new Quaternion(0, (float) (1*Math.sin(Math.toRadians(ry)/2)), 0, (float)Math.cos(Math.toRadians(ry)/2));
		qy.normalize();
		Quaternion qz = new Quaternion(0, 0, (float) (1*Math.sin(Math.toRadians(rz)/2)), (float)Math.cos(Math.toRadians(rz)/2));
		qz.normalize();
		Quaternion finalQuaternion = multiply3Quaternions(qx, qy, qz);
		Matrix4f matrix = quaternionToMatrix(finalQuaternion);
		matrix.m30 = translation.x;
		matrix.m31 = translation.y;
		matrix.m32 = translation.z;
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f quaternionToMatrix(Quaternion quaternion) {
		float xx = quaternion.x * quaternion.x;
		float xy = quaternion.x * quaternion.y;
		float xz = quaternion.x * quaternion.z;
		float xw = quaternion.x * quaternion.w;

		float yy = quaternion.y * quaternion.y;
		float yz = quaternion.y * quaternion.z;
		float yw = quaternion.y * quaternion.w;

		float zz = quaternion.z * quaternion.z;
		float zw = quaternion.z * quaternion.w;

		Matrix4f matrix = new Matrix4f();

		matrix.m00 = 1 - 2 * (yy + zz);
		matrix.m01 = 2 * (xy - zw);
		matrix.m02 = 2 * (xz + yw);

		matrix.m10 = 2 * (xy + zw);
		matrix.m11 = 1 - 2 * (xx + zz);
		matrix.m12 = 2 * (yz - xw);

		matrix.m20 = 2 * (xz - yw);
		matrix.m21 = 2 * (yz + xw);
		matrix.m22 = 1 - 2 * (xx + yy);

		matrix.m03 = matrix.m13 = matrix.m23 = matrix.m30 = matrix.m31 = matrix.m32 = 0;
		matrix.m33 = 1;
		
		return matrix;
	}
	
	public static Quaternion multiplyQuaternions(Quaternion q1, Quaternion q2){
		float w = q2.w*q1.w-q2.x*q1.x-q2.y*q1.y-q2.z*q1.z;
		float x = q2.w*q1.x + q2.x*q1.w - q2.y*q1.z + q2.z*q1.y;
		float y = q2.w*q1.y + q2.x*q1.z + q2.y*q1.w-q2.z*q1.x;
		float z = q2.w*q1.z - q2.x*q1.y+q2.y*q1.x+q2.z*q1.w;
		Quaternion result = new Quaternion(x, y, z, w);
		result.normalize();
		return result;
	}
	
	public static Quaternion multiply3Quaternions(Quaternion q1, Quaternion q2, Quaternion q3){
		return multiplyQuaternions(q1, multiplyQuaternions(q2, q3));
	}
	
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), matrix, matrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

}
