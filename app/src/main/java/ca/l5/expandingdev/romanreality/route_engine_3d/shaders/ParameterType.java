package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

/**
 * Created by fives on 12/7/16.
 *
 * This is a list of all the supported parameter types that an OpenGL shader program can have.
 * GL_FRAGMENT_COLOR and GL_POSITION are specialty types that are simply for clarification purposes.
 */
public enum ParameterType {
	UNIFORM, ATTRIBUTE, VARYING, GL_FRAGMENT_COLOR, GL_POSTION
	// Varying is needed to ensure that the fragment shader has all the varying vars passed to it and it is not missing any
}
