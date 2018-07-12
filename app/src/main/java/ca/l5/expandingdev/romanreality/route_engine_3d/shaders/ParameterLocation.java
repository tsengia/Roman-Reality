package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

/**
 * Created by fives on 12/7/16.
 *
 * This is the second half of the automatic passing in of parameters to an OpenGL shader program.
 * First we described the parameter with ParameterDescription, and now that we have all of the descriptions assembled, we ask OpenGL to tell us where each parameter is.
 * This class stores the location that OpenGL passes back.
 *
 * Going along with the car assembly analogy, this is like a blue print of the car with each part labeled on the diagram.
 * This class would be the location of the part on the blueprint.
 */
public class ParameterLocation {
	private  ParameterDescription pD;
	private int location = -1;

	public int getLocation() {
		return this.location;
	}

	public void setLocation(int loc) {
		this.location = loc;
	}

	public ParameterType getType() {
		return pD.parameterType;
	}

	public InputType getInputType() {
		return pD.inputType;
	}

	public ParameterLocation(ParameterDescription parameterDescription) {
		this.pD = parameterDescription;
	}
}
