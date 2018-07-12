package ca.l5.expandingdev.romanreality.route_engine_3d.geometry;

/**
 * Created by fives on 3/8/17.
 *
 * This is an extension (different version) of the Plane class.
 * However, it is not a true geometric plane as a true plane extends out infitely in 2 directions.
 * This SizedPlane class has a set heigh and width, so it does not go on forever in 2 directions.
 */
public class SizedPlane extends Plane {
	protected float width;
	protected float height;

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public SizedPlane(float width, float height, float[] centerPoint, float[] normal) {
		super(centerPoint, normal);
		this.width = width;
		this.height = height;
	}
}
