package ca.l5.expandingdev.romanreality.route_engine_3d.geometry;


import android.opengl.Matrix;
import android.util.Log;

import java.util.Vector;

/**
 * Created by fives on 3/8/17.
 *
 * This class is useful for creating virtual 3D planes and doing math with them.
 * It includes a method to find the signed distance from a point to the plane.
 * This allows us to tell if a point is in front of or behind the plane.
 */
public class Plane {
	protected float[] normal;
	protected float[] centerPoint;
	protected float dConstant;

	public float[] getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(float[] centerPoint) {
		this.centerPoint = centerPoint;
		this.dConstant = -1 * ((normal[0] * centerPoint[0]) + (normal[1] * centerPoint[1]) + (normal[2] * centerPoint[2]));
	}

	public float[] getNormal() {
		return normal;
	}

	public void setNormal(float[] normal) {
		this.normal = normal;
		this.dConstant = -1 * ((normal[0] * centerPoint[0]) + (normal[1] * centerPoint[1]) + (normal[2] * centerPoint[2]));
	}

	/*
		Returns the signed distance of a point from a plane.
		If it is positive, then the point is along the side of the normal.
		If negative, opposite side of the normal.
		If it is 0, then the point is on the plane.
	 */
	public float getSignedDistanceFromPlane(float[] a) {
		return (normal[0] * a[0]) + (normal[1] * a[1]) + (normal[2] * a[2]) + dConstant;
	}

	public Plane(float[] point, float[] normalVector) {
		this.normal = normalVector;
		this.centerPoint = point;
		this.dConstant = -1 * ((normalVector[0] * centerPoint[0]) + (normalVector[1] * centerPoint[1]) + (normalVector[2] * centerPoint[2]));
	}

	public static Plane planeFrom3Points(float[] a, float[] b, float[] c) {
		float[] v = VectorMath.subtractVectors(a, b);
		float[] u = VectorMath.subtractVectors(c, b);
		float[] normal = VectorMath.crossProduct(v, u);
		normal = VectorMath.normalizeVector(normal);
		return new Plane(a, normal);
	}
}
