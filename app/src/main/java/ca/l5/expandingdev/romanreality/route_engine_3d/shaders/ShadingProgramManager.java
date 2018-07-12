package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

import android.opengl.GLES20;
import android.util.Log;
import ca.l5.expandingdev.romanreality.route_engine_3d.RouteEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fives on 12/7/16.
 *
 * This massive class is the final step in passing in parameters to an OpenGL shader program.
 *
 * If we are continuing with the car building analogy, this is the part where you (or a robot) actually builds the car according to the locations on the blueprint and parts specified by the descriptions.
 */
public class ShadingProgramManager {
	private int programHandle;
	private HashMap<String, ParameterLocation> params = new HashMap<String, ParameterLocation>();
	private List<String> neededParameters = new ArrayList<String>();
	private int assigned = 0; // This is a count of the number of parameters that we successfully passed in.

	public boolean VERBOSE_FLAG = false; // This variable defines how much this class will print to the console/log file. If it is true, there will be a lot of output generated. If false, there will be silence.

	public List<String> getNeededParameters() { // Just another Getter method.
		return neededParameters;
	}

	public void applyParametersByName(HashMap<String, Object> input) throws Exception {
		GLES20.glUseProgram(programHandle); // First we tell OpenGL to switch over to the shader program that we will be working with
		for(String s : input.keySet()) { // Loop through all of the inputs we were given
			if(params.containsKey(s)) { // Check to see if the input is a possible parameter that could be passed in.
				if(neededParameters.contains(s)) { // Check to see if this parameter is even needed by OpenGL. Sometimes OpenGL will optimize (cut corners) out unneeded parameters.
					ParameterType type = params.get(s).getType(); // Grab the ParameterType of the parameter that we will be passing in
					InputType inputType = params.get(s).getInputType(); // Grab the InputType of the value we will be passing in
					if (type.equals(ParameterType.UNIFORM)) { // UNIFORM is a ParameterType that means that this value will remain constant while rendering the entire mesh,
						switch (inputType) {
							case MATRIX4: // Input Type is a 4 by 4 matrix. Call the OpenGL function to pass it in and give it the location of our parameter.
								GLES20.glUniformMatrix4fv(params.get(s).getLocation(), 1, false, (float[]) input.get(s), 0);
								break;
							case MATRIX3:
								GLES20.glUniformMatrix3fv(params.get(s).getLocation(), 1, false, (float[]) input.get(s), 0);
								break;
							case MATRIX2:
								GLES20.glUniformMatrix2fv(params.get(s).getLocation(), 1, false, (float[]) input.get(s), 0);
								break;
							case VEC4:
								GLES20.glUniform4fv(params.get(s).getLocation(), 1, (float[]) input.get(s), 0);
								break;
							case VEC3:
								GLES20.glUniform3fv(params.get(s).getLocation(), 1, (float[]) input.get(s), 0);
								break;
							case VEC2:
								GLES20.glUniform2fv(params.get(s).getLocation(), 1, (float[]) input.get(s), 0);
								break;
							case VEC1:
								GLES20.glUniform1fv(params.get(s).getLocation(), 1, (float[]) input.get(s), 0);
								break;
							case TEXTURE2D: // We are passing in a 2D texture (image)!
								TextureInfo texInfo = (TextureInfo) input.get(s); // First grab the image from the input
								GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + texInfo.getTextureUnit()); // Tell OpenGL which texture # we are using
								GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texInfo.getOpenglHandle()); // Bind (set) the mesh's texture to the texture # that we just said we are using
								GLES20.glUniform1i(params.get(s).getLocation(), texInfo.getTextureUnit()); // Set the uniform that holds the texture unit number of the texture
								break;
							// NOTE: The below "TODO" lines of code are nothing to worry about. They are never used, and probably never will be used.
							case FLOAT_BUFFER4:
								//TODO: WHAT TO DO?
								break;
							case FLOAT_BUFFER3:
								//TODO: WHAT TO DO?
								break;
							case FLOAT_BUFFER2:
								//TODO: WHAT TO DO?
								break;
							case FLOATARRAY:
								float[] array = (float[]) input.get(s);
								GLES20.glUniform1fv(params.get(s).getLocation(), array.length, array, 0);
								break;
							default:
								break;
						}
					} else if (type.equals(ParameterType.ATTRIBUTE)) { // An ATTRIBUTE ParameterType means that the input/variable changes according to each vertex. This is used for passing in things that associated with one vertex, such as coordinates, normals and colors.
						switch (inputType) {
							case FLOAT_BUFFER3:
								GLES20.glVertexAttribPointer(params.get(s).getLocation(), 3, GLES20.GL_FLOAT, false, 0, (FloatBuffer) input.get(s));
								GLES20.glEnableVertexAttribArray(params.get(s).getLocation());
								break;
							case FLOAT_BUFFER4:
								GLES20.glVertexAttribPointer(params.get(s).getLocation(), 4, GLES20.GL_FLOAT, false, 0, (FloatBuffer) input.get(s));
								GLES20.glEnableVertexAttribArray(params.get(s).getLocation());
								break;
							case FLOAT_BUFFER2:
								GLES20.glVertexAttribPointer(params.get(s).getLocation(), 2, GLES20.GL_FLOAT, false, 0, (FloatBuffer) input.get(s));
								GLES20.glEnableVertexAttribArray(params.get(s).getLocation());
								break;
						}
					} //If it is anything else then they are crazy trying to assign an input to varying (varying is an InputType that cannot be assigned/passed in)
					assigned++;
					RouteEngine.checkGLError("assignParameter " + s); // Check for errors.
				}
				else {
					//Skip assigning the un-needed parameter, and complain about it.
					if(VERBOSE_FLAG) { // First check if we are allowed to complain, if we are allowed to, then print to the error log.
						Log.e("PARAM ASSIGNMENT", "Uneeded param " + s);
					}
				}
			}
			else {
				//Skip it, if they add in more params then are needed we won't error out, we'll just complain.
				if(VERBOSE_FLAG) {
					Log.e("PARAM ASSIGNMENT", "Unknown param " + s);
				}
			}
		}
	}

	public void clearAssignedCount() { // This function clears the count of the number of parameters that have been successfully passed in.
		this.assigned = 0;
	}

	public boolean fulfilled() { // This returns true if the total needed parameters were passed in.
		return assigned == neededParameters.size();
	}

	public ShadingProgramManager(VertexShaderWrapper vertex, FragmentShaderWrapper fragment) throws Exception {
		//

		//Pool our possible parameters into one HashMap mapping to parameter name, and by using ParameterLocation, we can also store the location of the parameter to access by name as well
		List<ParameterDescription> vP = vertex.getPossibleParameters();
		for(ParameterDescription pd : vP) {
			if(params.containsKey(pd.name)) {
				//Already contains the key, duplicate? Kinda odd but whatever, don't do anything with it since it already exists.
			}
			else {
				params.put(pd.name, new ParameterLocation(pd)); // This is a new parameter, we will add it to our list.
			}
		}

		List<ParameterDescription> fP = fragment.getPossibleParameters();
		for(ParameterDescription pd : fP) {
			if(params.containsKey(pd.name)) {
				//If we already have this parameter from the vertex shader, then we don't need to add it again
			}
			else {
				if(pd.parameterType.equals(ParameterType.VARYING)) { // VARYING ParameterTypes are basically variables that are passed from the output of the Vertex shader to the Vertex shader.
					//Here we found that the Fragment shader is expecting output from the Fragment shader, but the fragment shader does not list that variable in the list of outputs.
					//That is a major issue, so we will complain and throw an error.
					throw new Exception("VARYING parameter found in fragment shader that is not set in vertex shader!");
				}
				else {
					params.put(pd.name, new ParameterLocation(pd));
				}
			}
		}

		programHandle = GLES20.glCreateProgram(); // Ask OpenGL to allocate space for our new shader program.
		RouteEngine.checkGLError("Creating program step 0");
		GLES20.glAttachShader(programHandle, vertex.getShaderHandle()); // Pass in our vertex shader.
		RouteEngine.checkGLError("Creating program step 1");
		GLES20.glAttachShader(programHandle, fragment.getShaderHandle()); // Pass in our fragment shader.
		RouteEngine.checkGLError("Creating program step 2");
		GLES20.glLinkProgram(programHandle); // Tell OpenGL to combine our vertex and fragment shaders together,
		RouteEngine.checkGLError("Creating program step 3");
		GLES20.glUseProgram(programHandle); // Tell OpenGL that we will now switch to this new shader program.
		RouteEngine.checkGLError("Creating program step 4");
		//Now that we have the program ready, let's grab the locations of the parameters

		for(String s : this.params.keySet()) { //Iterate through the possible parameters and grab their locations and determine if they are needed or if they have been optimized out by the linker
			ParameterType type = this.params.get(s).getType();
			if(type.equals(ParameterType.UNIFORM)) { // Loop through all of the UNIFORM parameters and find which ones are needed and which ones are not needed.
				params.get(s).setLocation(GLES20.glGetUniformLocation(programHandle, s));
				if(params.get(s).getLocation() != -1) { // If we find that the parameter location is -1, that means that OpenGL has optimized this parameter out of the program.. But if it some other number besides -1, OpenGL needs it so we will store that location.
					neededParameters.add(s);
				}
			}
			else if(type.equals(ParameterType.ATTRIBUTE)) { // Loop through all of the ATTRIBUTE parameters and find which ones are needed and which ones are not needed.
				params.get(s).setLocation(GLES20.glGetAttribLocation(programHandle, s));
				if(params.get(s).getLocation() != -1) { //This means that the linker did not optimize out our parameter, so it must be needed
					neededParameters.add(s);
				}
			} //If it's not an attribute or a uniform, it is a varying, which we don't need the location of
		}
		RouteEngine.checkGLError("Grabbing parameter locations");
	}

	public int getHandle() { // This is a Getter method for the shader program location.
		return this.programHandle;
	}
}
