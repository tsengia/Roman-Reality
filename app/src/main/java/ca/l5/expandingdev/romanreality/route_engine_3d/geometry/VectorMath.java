package ca.l5.expandingdev.romanreality.route_engine_3d.geometry;

/**
 * Created by fives on 3/8/17.
 *
 * This class contains useful mathematical functions for vectors.
 * For example: vector addition, subtraction, finding the magnitude of a vector, normalizing a vector, and finding the cross product and dot product of two vectors.
 *
 * This vector math is used in other geometric classes such as the Plane class.
 */
public class VectorMath {

	public static float[] addVectors(float[] vectorA, float[] vectorB) {
		if(vectorA.length != vectorB.length) {
			throw new RuntimeException("Vectors being added are not the same size!");
		}
		float[] vectorC = new float[vectorA.length];
		for(int i = 0; i < vectorA.length; i++) {
			vectorC[i] = vectorA[i] + vectorB[i];
		}
		return vectorC;
	}

	public static float[] subtractVectors(float[] vectorA, float[] vectorB) {
		if(vectorA.length != vectorB.length) {
			throw new RuntimeException("Vectors being added are not the same size!");
		}
		float[] vectorC = new float[vectorA.length];
		for(int i = 0; i < vectorA.length; i++) {
			vectorC[i] = vectorA[i] - vectorB[i];
		}
		return vectorC;
	}

	public static float getMagnitude(float[] vector) {
		float mag = 0;
		for(int i = 0; i < vector.length; i++) {
			mag += vector[i] * vector[i];
		}
		return (float) Math.sqrt((double) mag);
	}

	public static float[] normalizeVector(float[] vector) {
		float mag = VectorMath.getMagnitude(vector);
		float[] norm = new float[vector.length];
		for(int i = 0; i < vector.length; i++) {
			norm[i] = vector[i] / mag;
		}
		return norm;
	}

	public static float[] crossProduct(float[] vectorA, float[] vectorB) {
		if(vectorA.length != vectorB.length) {
			throw new RuntimeException("Vectors are not the same size!");
		}
		float[] vectorC = new float[vectorA.length];
		vectorC[0] = vectorA[1] * vectorB[2] - vectorB[1] * vectorA[2];
		vectorC[1] = vectorA[2] * vectorB[0] - vectorB[2] * vectorA[0];
		vectorC[2] = vectorA[0] * vectorB[1] - vectorB[0] * vectorA[1];
		return vectorC;
	}

	public static float dotProduct(float[] vectorA, float[] vectorB) {
		if(vectorA.length != vectorB.length) {
			throw new RuntimeException("Vectors are not the same size!");
		}
		float c = 0;
		for(int i = 0; i < vectorA.length; i++) {
			c += vectorA[i] * vectorB[i];
		}
		return c;
	}


}
