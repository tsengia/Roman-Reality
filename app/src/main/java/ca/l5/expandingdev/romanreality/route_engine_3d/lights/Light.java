package ca.l5.expandingdev.romanreality.route_engine_3d.lights;

/**
 * Created by fives on 12/20/16.
 *
 * This is a class representing a Light for use in the shader programs.
 * Currently not used much in this app.
 */
public class Light {
	protected float strength;
	protected float[] color;

	public float getStrength() {
		return this.strength;
	}

	public float[] getColor() {
		return this.color;
	}

	public void setColor(float[] newColor) {
		this.color = newColor;
	}

	public void setStrength(float newStrength) {
		this.strength = newStrength;
	}

	public Light(float strength, float[] color) {
		this.strength = strength;
		this.color = color;
	}
}
