package ca.l5.expandingdev.romanreality.route_engine_3d.lights;

/**
 * Created by fives on 12/20/16.
 *
 * This is an extension/version of the Light class but is a specific type of light model: it's a Point Light.
 * There are many different ways to simulate light sources, and a point light source is one way.
 */
public class PointLight extends Light {
	protected float[] position; // Point lights have a set position.

	public float[] getPosition() {
		return position;
	}

	public void setPosition(float [] position) {
		this.position = position;
	}

	public PointLight(float strength, float[] color, float[] position) {
		super(strength, color);
		this.position = position;
	}
}
