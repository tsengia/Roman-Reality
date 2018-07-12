package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

/**
 * Created by fives on 12/7/16.
 *
 * This class provides an outline/"description" of a parameter for an OpenGL shader program.
 * Descriptions are used to dynamically handle shaders and assigning inputs.
 *
 * Consider this class to be the description of a part in a massive assembly guide for a car.
 * Each description contains the part number, name, and dimensions.
 * All of the parts are needed to build the final product, and finding each part as your build the car would take much longer than if you assembled all of the needed parts beforehand.
 * Still, building a car from scratch even with instructions would take forever. But it's a good analogy for this code's purpose.
 * */
public class ParameterDescription {
	public String name;
	public InputType inputType;
	public ParameterType parameterType;

	public ParameterDescription(String name, InputType input, ParameterType parameterType) {
		this.name = name;
		this.inputType = input;
		this.parameterType = parameterType;
	}
}
