package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

import android.util.Log;
import ca.l5.expandingdev.romanreality.route_engine_3d.RouteEngine;

import java.io.IOException;
import java.util.List;

/**
 * Created by fives on 12/7/16.
 *
 * This class is a template for all Shader objects. It outlines values and methods that all shader objects will need.
 */
public abstract class ShaderWrapper {
	abstract int getShaderType(); // Each shader will have to return its shader program type when asked. There are two shader program types: FRAGMENT and VERTEX.
	protected abstract int getResourceID(); // Each shader will have to return the ID of the file that holds it source code when asked for it.
	protected abstract List<ParameterDescription> getPossibleParameters(); // Each Shader will have to return a list of the possible parameters that the program could use.

	private int shaderHandle; // Each Shader object will need to have a "handle'. A handle is simply an address that points to the location where the data is stored.

	public int getShaderHandle() {
		return this.shaderHandle; // Each Shader object will return it's address when asked for it.
	}

	public ShaderWrapper() throws IOException { // Each Shader object will load as soon as it is created
		shaderHandle = RouteEngine.loadShader(this.getResourceID(), this.getShaderType()); // Attempt to load the shader.
		Log.i("SHADER LOAD", "SHADER loaded"); // Log into the console that we successfully loaded a Shader. This is for debugging/benchmarking purposes. Benchmarking is simply a term used for "timing" or "scoring" a method's efficiency/speed.
	}
}
