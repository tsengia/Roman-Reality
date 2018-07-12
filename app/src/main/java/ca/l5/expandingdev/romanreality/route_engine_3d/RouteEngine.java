package ca.l5.expandingdev.romanreality.route_engine_3d;
// The following lines "import" external libraries. By importing them, we are able to use the code they have.

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Vibrator;
import android.util.Log;
import ca.l5.expandingdev.romanreality.route_engine_3d.lights.Light;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.TextureInfo;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fives on 12/17/16.
 *
 * This class is the framework that works along with the Cardboard APIs and controlls the rendering of Mesh objects and handles responses to user interactions
 */
public class RouteEngine {
	public static final float[] POS_FROM_MATRIX_VECTOR = {0.0f, 0.0f, 0.0f, 1.0f};
	public static BitmapFactory.Options BITMAP_OPTIONS = new BitmapFactory.Options(); //Let's not waste memory recreating this blank object over and over again. This is simply a flag used when textures are loaded in.
	// The following lines create fields (variables) for 3D transformation matrices. 3D transformation matrices allow for translation and rotation of coordinates in 3D space. They are a 4x4 grid of numbers, making them 16 numbers total.
	protected static float[] headViewMatrix = new float[16];
	protected static float[] cameraMatrix = new float[16];
	protected static float[] viewMatrix = new float[16];
	protected static float[] projectionMatrix = new float[16];
	protected static Camera camera = new Camera();
	protected static float PADDING_YAW = (float) degreesToRadians(5); // The user will have to look within 10 degrees side to side of a datapoint to select it.
	protected static float PADDING_PITCH = (float) degreesToRadians(5); // The user will have to look within 10 degrees up and down of a datapoint to select it.
	protected static List<Mesh> meshes = new ArrayList<Mesh>(); // This is the list of meshes in our world to be rendered.
	protected static List<Integer> triggerMeshes = new ArrayList<Integer>(); // This is a list of the indexes of meshes that can be interacted with.
	protected static HashMap<String, Integer> meshNames = new HashMap<String, Integer>(); // This is a table/map from defined names of meshes to the index of the mesh in the master mesh list.
	protected static Resources resources;
	protected Light environmentalLight = new Light(0.25f, new float[]{1.0f, 1.0f, 1.0f});
	protected Vibrator vibrator;
	protected float Z_NEAR = 0.1f; // This number specifies how close a mesh can get before being "clipped". "clipped" means not rendered/shown. Basically how close an object can get before it is considered behind us.
	protected float Z_FAR = 2500.0f; // This number specifies how far our gaze goes. Anything farther away than this number is clipped as it is out of view.

	public RouteEngine(Resources r) { // Constructor for a RouteEngine object. Constructors are called immediately when a new object of this type is created.
		resources = r; // Set the RouteEngine's Resources reference to the Resources object we were given.
		RouteEngine.BITMAP_OPTIONS.inScaled = false;
	}

	public static Resources getResources() { // Returns the Resources object that RouteEngine has referenced
		return resources;
	}

	public static Camera getCamera() { // Returns the Camera object that this RouteEngine uses
		return camera;
	}

	public static float[] getHeadViewMatrix() { // This is another Getter. Java commonly uses Getters and Setters. Getters are function that simply return a value. Setters set a value. You will see many more of these Getters and Setters as using them is a good coding habit.
		return headViewMatrix;
	}

	public static float[] getCameraMatrix() {
		return cameraMatrix;
	}

	public static float[] getViewMatrix() {
		return viewMatrix;
	}

	public static float[] getProjectionMatrix() {
		return projectionMatrix;
	}

	public static float[] getCoordsFromMatrix(float[] matrix) { // Returns the position/coords from a 3d transformation matrix
		float[] pos = new float[4];
		Matrix.multiplyMV(pos, 0, matrix, 0, RouteEngine.POS_FROM_MATRIX_VECTOR, 0);
		return pos;
	}

	public static int addTriggerMesh(TriggerMesh tm) { // Adds the trigger mesh to the list of meshes and registers it as a trigger. It will be tested to see if it is being looked at when the trigger is pressed.
		meshes.add(tm);
		int index = meshes.size() - 1;
		triggerMeshes.add(index);
		return index;
	}

	public static boolean unregisterTriggerMesh(int meshIndex) { // Attempts to unregister a trigger mesh from the listener (interactable) list. Returns true if it worked.
		if (triggerMeshes.contains(meshIndex)) {
			triggerMeshes.remove(meshIndex);
			return true;
		}
		return false;
	}

	public static int addMesh(Mesh m) { //Adds the given mesh to the meshes list and returns the index of the added mesh
		meshes.add(m);
		return meshes.size() - 1;
	}

	public static void removeMesh(int meshIndex) { //Removes the given mesh from the mesh list
		meshes.remove(meshIndex); // Remove the mesh from our master list
		if (meshNames.containsValue(meshIndex)) { // If our mesh is registered with a name as well, remove it as well.
			meshNames.remove(meshIndex);
		}
	}

	public static void nameMesh(int meshIndex, String name) { // Assigns a name to the mesh at the specifies index and adds that name to the current table of names.
		meshNames.put(name, meshIndex);
	}

	public static int getMeshIDByName(String name) { // Returns the index of the Mesh in the master List of meshes by looking up the specified name.
		return meshNames.get(name);
	}

	public static Mesh getMesh(int index) { // Returns the Mesh object found at the specified index
		return meshes.get(index);
	}

	public static double radiansToDegrees(double rad) { //Converts Radians to degrees
		// degrees = rad * (180/pi)
		// 180/pi simplifies down to 57.2957795131
		// pi -> 3.14159265359
		return rad * 57.295;
	}

	public static double degreesToRadians(double degrees) { // Converts Degrees to Radians
		// degrees = rad * (180/pi)
		// degrees / (180/pi) = rad
		// degrees / 57.295 = rad
		return (degrees / 57.265);
	}

	public static boolean meshInGaze(Mesh m) { // Returns true if the Mesh is within the view of the current screen/near the center of the screen.
		float[] pitchYaw = getPitchYawRelativeToScreen(m);
		return pitchYaw[0] < PADDING_PITCH && pitchYaw[1] < PADDING_YAW; // Here we are using the constants defined at the beginning which specify the area which can be considered "looking" at the mesh.
	}

	/*
	 * Remember: Pitch is up and down, Yaw is left and right.
	 */
	public static float[] getPitchYawRelativeToScreen(Mesh m) { //Gets the vector between the center of the screen and the given object. The first index is pitch, the second index is yaw
		// Convert object space to camera space. Use the headView from onNewFrame.
		float[] modelView = new float[16];
		Matrix.multiplyMM(modelView, 0, RouteEngine.getHeadViewMatrix(), 0, m.getPositionMatrix(), 0);
		float[] position = RouteEngine.getCoordsFromMatrix(modelView);
		float pitch = (float) (Math.atan2(position[1], -position[2]));
		float yaw = (float) Math.atan2(position[0], -position[2]);
		return new float[]{pitch, yaw};
	}

	public static int loadShader(int resourceID, int type) throws IOException { // Loads an OpenGL shader from a file and returns the ID OpenGL assigned it

		// The below chunk of code grabs the shader file, opens it, copies its contents into RAM, and then closes it.
		InputStream inputStream = resources.openRawResource(resourceID); // Open the file
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close(); // Close the file

		String code = sb.toString(); // The variable "code" now contains the contents of the shader program file

		int shader = GLES20.glCreateShader(type); // Ask OpenGL to reserve a spot for our new Shader Program. It should give us an ID corresponding to our reserved spot
		checkGLError("Loading Shader Check A"); // Error checking. Makes sure OpenGL reserved out spot correctly.

		GLES20.glShaderSource(shader, code); // Passes on the shader code to OpenGL
		checkGLError("Loading Shader Check B"); // Error checking.

		GLES20.glCompileShader(shader); // Now we tell OpenGL that it can compile our shader program.
		checkGLError("Loading Shader Check C"); // Error checking. If OpenGL complains about our shader program's code, we'll catch the error message here.

		// The below chunk of code asks OpenGL if the shader program compiled correctly
		final int[] compileStatus = new int[1]; // Variable "compileStatus" will hold the value whether or not the shader program compiled correctly
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		checkGLError("Loading Shader Check D");

		// Checks the compilation status. If it failed we will throw a new error.
		if (compileStatus[0] == 0) {
			Log.e("ERROR 193", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader)); // Prints an error message to the debug console.
			GLES20.glDeleteShader(shader); // We tell OpenGL that our reservation for our shader program is no longer needed as our shader program doesn't work.
			throw new RuntimeException("Error compiling shader."); // Throwing a new error that will be handled outside of this function.
		}

		// Otherwise, everything went well: Shader loaded, reserved, and compiled correctly, so we'll return its ID
		return shader;
	}

	public static void checkGLError(String label) { // This is the method we call frequently to check for errors associated with OpenGL.
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) { // We use a "while" loop in case there are multiple errors. It will loop through until it has gone through all of the errors.
			Log.e("ERROR", label + ": glError " + error); // Log the error into the debug console.
			throw new RuntimeException(label + ": glError " + error); // Throw a new error to be handled outside of this method.
		}
	}

	public static void printException(Exception e) { //This is a helpful method that allows us to log error messages into the debug console.
		String s = "";
		s.concat(e.getMessage());
		for (int i = 0; i < e.getStackTrace().length; i++) {
			s = s.concat(e.getStackTrace()[i].toString() + "\n");
		}
		Log.e("ARG", s);
	}

	public static float[] listToFloat(List<Float> input) { // This is simply a helper method that converts a List of floats to an array of floats. A List and an array are two different classes in Java.
		float[] a = new float[input.size()];
		for (int i = 0; i < input.size(); i++) {
			a[i] = input.get(i);
		}
		return a;
	}

	public static TextureInfo createTextureInfo(int resource, int minFilter, int magFilter) throws Exception { // Loads in a texture file and gets OpenGL to reserve a spot for it. We will return an object holding info about the loaded texture.
		TextureInfo texInfo = new TextureInfo(resource, minFilter, magFilter) { // Here we are creating a new TextureInfo object, but everytime you create a new TextureInfo object, you need to specify how it will be loaded.
			@Override
			public int load() throws Exception { // This is our method for loading in the texture and passing it on to OpenGL
				final int[] handle = new int[1]; // This will hold the ID of the spot that OpenGL gives us
				GLES20.glGenTextures(1, handle, 0); // Asking OpenGL to assign us a spot for our texture
				if (handle[0] != 0) { // if the handle is 0, that means that there was an error! But if it isn't equal to 0, then the spot was successfully reserved.
					final Bitmap bitmap = BitmapFactory.decodeResource(RouteEngine.getResources(), this.getResourceIndex(), RouteEngine.BITMAP_OPTIONS); // Load the image into RAM from the file
					GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle[0]); // Telling OpenGL that the reserved spot will be for a 2D image texture

					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, this.getMinFilter()); // These two lines set the filters for the texture.
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, this.getMagFilter()); // Different filters can make the rendering higher or lower quality, but at the cost of computation.
					RouteEngine.checkGLError("Bind and filter texture."); // Checking to see if any errors happended durin all of the above code.
					GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0); // Now we are giving OpenGL the actual image that we loaded into RAM earlier
					RouteEngine.checkGLError("Passing texture into binding."); // Check to see if there were any OpenGL errors when passing in the image.

					bitmap.recycle(); // Clear the image from RAM as now the image is in OpenGL's memory, so we have no need to keep it in the RAM.
					return handle[0]; // Now that the texture is loaded, let's return it's ID for the TextureInfo object to use.
				} else { //OpenGL failed to reserve a spot for the texture!
					RouteEngine.checkGLError("Texture Load"); // Check what the error is.
					throw new Exception("Error loading texture."); // Throw an error to be handled elsewhere.
				}
			}
		};

		return texInfo; // Everything went OK, we have a TextureInfo object that we can return now.
	}

	public void testTriggerMeshes() { // Cycles through the trigger meshes and checks to see if they are being looked at when the trigger is pressed. If the mesh is, it will call the event handler.
		for (int i = 0; i < triggerMeshes.size(); i++) {
			float[] pitchYaw = RouteEngine.getPitchYawRelativeToScreen(meshes.get(triggerMeshes.get(i)));
			if (((TriggerMesh) meshes.get(triggerMeshes.get(i))).getInGaze()) { // Chekc if the user's gaze is on the datapoint
				((TriggerMesh) meshes.get(triggerMeshes.get(i))).onTrigger(); // User is looking at the trigger mesh when the trigger button is pressed, so call the trigger mesh's onTrigger method.
			}
		}
	}

	public void testGazeMeshes() { // Cycles through the trigger meshes and checks to see which one is the closest to the center of the screen and if it is even near the center of the screen. If it is near the center of the screen, it will call the setGazeIn method and set it's being gazed at state to true
		int closestIndex = 0; //Ok, we need to test which mesh is CLOSEST to the center of the screen, so we have a
		double closestDistance = 10000; //variable to store the ID of the mesh that is the current closest, and another variable for the distance from the center of the screen
		for (int i = 0; i < triggerMeshes.size(); i++) { //Here we will loop through each trigger mesh to find which is the closest one
			float pitchYaw[] = RouteEngine.getPitchYawRelativeToScreen(meshes.get(triggerMeshes.get(i))); //Here we are storing the X and Y distance from the center of the screen of the mesh
			double distance = Math.sqrt(Math.pow(pitchYaw[0], 2) + Math.pow(pitchYaw[1], 2)); //Compute the distance from the center using Polythagorean's theorem. a^2 + b^2 = c^2 -->> SquareRoot(a^2 + b^2) = c
			if(distance < closestDistance) { // If the distance for this mesh is closer, replace the old record
				closestIndex = i; // Update the ID of the closest mesh
				closestDistance = distance; // Update the current record for the closest distance
			}
			((TriggerMesh) meshes.get(triggerMeshes.get(i))).setGazeIn(false); // Because we are looping through every trigger mesh, we will set them all to be out of gaze, and later turn one to be in the users gaze
		}
		if(closestDistance < RouteEngine.PADDING_YAW) { // now that we have the closest distance, let's check to make sure it is within the bounds of the "center of the screen"
			((TriggerMesh) meshes.get(triggerMeshes.get(closestIndex))).setGazeIn(true); // If it is, set it to a being looked/gazed at state
		}
	}

	public void clearMeshes() { //Removes all meshes from the meshes list
		meshes.clear();
	}

	public Vibrator getVibrator() { // Returns the Vibrator object used to control the rumble/vibration effects on the phone.
		return this.vibrator;
	}

	public void setVibrator(Vibrator v) { // Here is an example of a Setter. This Setter method (function) assigns the current vibrator variable to the Vibrator object that was passed in.
		this.vibrator = v;
	}

	public void onDrawEye(Eye eye) { // Renders one eye. There are two eyes, left and right.
		GLES20.glEnable(GLES20.GL_CULL_FACE); // Enable some OpenGL settings for optimization and nice looks
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Clear the eye
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		Matrix.multiplyMM(viewMatrix, 0, eye.getEyeView(), 0, cameraMatrix, 0);
		projectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);

		for (int i = 0; i < this.meshes.size(); i++) { // Loops through all of the meshes in the meshes List
			Mesh m = this.meshes.get(i);
			if (!m.isVisible()) { // If the Mesh is invisible, then don't render it, go to the next mesh in the loop
				continue;
			}
			GLES20.glUseProgram(m.getShader().getHandle()); // Tell OpenGL to switch over to this mesh's shader program
			try {
				m.getShader().clearAssignedCount(); // Clear the count of parameters, this is for error handling.
				m.getShader().applyParametersByName(m.getParameterValues()); // Assign the basic/required parameters needed by the shader program
				m.getShader().applyParametersByName(this.getLightParams()); // Attempts to assign the light parameters. Not every shader program accepts these parameters.
				if (!m.getShader().fulfilled()) { // Error checking, checks to see if we gave it all of the parameters that it needs.
					throw new Exception("Not all of the needed parameters were assigned! Mesh #:" + Integer.toString(i));
				}
			} catch (Exception e) { //Error handling
				RouteEngine.printException(e);
				e.printStackTrace();
				continue; // Prints the error and skips over to the next Mesh object
			}
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, m.getVertexBuffer().capacity() / 3); // Everything checks out with the shader, so we tell OpenGL to draw our mesh now.
			checkGLError("Draw, mesh #" + Integer.toString(i)); // Checks for any errors after the mesh was drawn.
		}
	}

	public HashMap<String, Object> getLightParams() { // Returns parameters and values of lighting settings that are used during rendering
		HashMap<String, Object> params = new HashMap<>();
		params.put("u_EnvironmentalLightStrength", new float[]{this.getEnvironmentalLight().getStrength()});
		params.put("u_EnvironmentalLightColor", this.getEnvironmentalLight().getColor());
		return params;
	}

	public Light getEnvironmentalLight() { // Returns the environmental light that we are currently using.
		return this.environmentalLight;
	}

	public void onNewFrame(HeadTransform headTransform) { // Called before each frame, all we do is set up the Camera
		Matrix.setLookAtM(cameraMatrix, 0, 0.0f, 0.0f, 0.01f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		headTransform.getHeadView(this.headViewMatrix, 0);
	}
}


