package ca.l5.expandingdev.romanreality.route_engine_3d;

import android.opengl.Matrix;
import android.util.Log;

import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ShadingProgramManager;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.TextureInfo;
import com.momchil_atanasov.data.front.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fives on 12/17/16.
 */
// This is the Mesh class. This class outlines things that all Mesh objects will need to have and do.
// In 3D space, almost anything can be considered a mesh. Boxes, people, walls, the ground, anything that has volume can be considered a mesh.
public abstract class Mesh {

	// These are default colors for a mesh
	public static float DEFAULT_RED = 0.5f;
	public static float DEFAULT_GREEN = 0.5f;
	public static float DEFAULT_BLUE = 0.5f;
	public static float DEFAULT_ALPHA = 1.0f;

	public abstract HashMap<String, Object> getParameterValues(); // Each mesh object will have to return a list of parameters that its shader needs when it is asked.

	private FloatBuffer vertexBuffer; // This is an array of the 3D points that make up this mesh
	private FloatBuffer colorBuffer; // This is an array of colors of each point of the mesh
	private FloatBuffer normalBuffer; // This is an array of the normals the mesh has. Normals are used to calculate how bright light is reflected off of a surface.
	private FloatBuffer uvBuffer; // This is an array that holds all of the mesh's UV coordinates. A UV coordinate maps a face to an area of an image.

	private TextureInfo texInfo;

	protected ShadingProgramManager shader;

	private float[] positionVector = {0,0,0}; //X, Y, and Z of the Mesh

	private float[] localRotation = {0,0,0}; //Rotation amount around the X, Y, and Z axises

	private boolean visible = true;

	public boolean isVisible() { // Returns whether or not this mesh can be seen
		return visible;
	}

	public void setVisible(boolean v) { // Sets whether or not this mesh can be seen
		this.visible = v;
	}

	public boolean getVisible() {
		return this.visible;
	}

	public float[] getPositionMatrix() { // Returns the 3D transformation matrix of this object. Any translations and rotations are held in that matrix.
		float[] m = new float[16];
		Matrix.setIdentityM(m,0);
		Matrix.translateM(m, 0, positionVector[0], positionVector[1], positionVector[2]);
		Matrix.rotateM(m, 0, localRotation[0], 1.0f, 0.0f, 0.0f);
		Matrix.rotateM(m, 0, localRotation[1], 0.0f, 1.0f, 0.0f);
		Matrix.rotateM(m, 0, localRotation[2], 0.0f, 0.0f, 1.0f);
		return m;
	}

	protected ShadingProgramManager getShader() {
		return shader;
	}

	protected void setShader(ShadingProgramManager shader) { // Returns this mesh's shader
		this.shader = shader;
	}

	public void localRotate(float degrees, float xAxis, float yAxis, float zAxis) { // Rotates the mesh by the given degrees and along along the given axis.
		localRotation[0] += degrees * xAxis;
		localRotation[1] += degrees * yAxis;
		localRotation[2] += degrees * zAxis;
	}

	public void translateTo(float x, float y, float z) { // Translates the mesh to the given coords
		this.positionVector[0] = x;
		this.positionVector[1] = y;
		this.positionVector[2] = z;
	}

	public void translate(float x, float y, float z) { // Moves the mesh by the given amounts.
		this.positionVector[0] += x;
		this.positionVector[1] += y;
		this.positionVector[2] += z;
	}

	public float[] getPositionVector() { // This returns the x, Y, and Z
		return this.positionVector;
	}

	public float[] getMVPMatrix() { // Returns the Model View Projection Matrix. Basically it returns a 3D transformation matrix that can be used to move 3D coords in the mesh into the correct position on a 2d plane. That plane can be though of as the rendered image/display.
		float[] modelViewProjectionMatrix = new float[16];
		Matrix.multiplyMM(modelViewProjectionMatrix, 0, RouteEngine.getProjectionMatrix(), 0, this.getMVMatrix(), 0);
		return modelViewProjectionMatrix;
	}

	public float[] getMVMatrix() { // Returns the Model View Matrix. Basically the Model View Matrix is used to move coords into the correct place
		float[] modelViewMatrix = new float[16];
		Matrix.multiplyMM(modelViewMatrix, 0, RouteEngine.getViewMatrix(), 0, this.getPositionMatrix(), 0);
		return modelViewMatrix;
	}

	public void setTextureInfo(TextureInfo texInfo) { // Gives this mesh a texture and any information about it.
		this.texInfo = texInfo;
	}

	public TextureInfo getTextureInfo() { // Returns this mesh's current texture and information related to it.
		return this.texInfo;
	}

	public void setVertexBuffer(FloatBuffer f) { // Sets the vertex buffer for this mesh. The vertex buffer is an array that holds the 3D coordinates that represent the vertices that make up the model
		this.vertexBuffer = f;
	}

	public void setNormalBuffer(FloatBuffer f) { // Sets the normal buffer. A normal is simply the direction a face is facing.
		this.normalBuffer = f;
	}

	public void setColorBuffer(FloatBuffer f) { // Sets the vertex color buffer, which assigns a color to each vertex in the mesh.
		this.colorBuffer = f;
	}

	public void setUVBuffer(FloatBuffer f) { // Sets the UV buffer, which holds coords that maps verticies to a 2D texture.
		this.uvBuffer = f;
	}

	public void setVertexBuffer(float [] f) { // An alternate way to set the vertex buffer
		ByteBuffer vBuffer = ByteBuffer.allocateDirect(f.length * 4);
		vBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vBuffer.asFloatBuffer();
		vertexBuffer.put(f);
		vertexBuffer.position(0);
	}

	public void setUVBuffer(float [] f) { // An alternate way to set the uv buffer
		ByteBuffer tBuffer = ByteBuffer.allocateDirect(f.length * 4);
		tBuffer.order(ByteOrder.nativeOrder());
		uvBuffer = tBuffer.asFloatBuffer();
		uvBuffer.put(f);
		uvBuffer.position(0);
	}

	public void setColorBuffer(float [] f) { // An alternate way to set the color buffer
		ByteBuffer cBuffer = ByteBuffer.allocateDirect(f.length * 4);
		cBuffer.order(ByteOrder.nativeOrder());
		colorBuffer = cBuffer.asFloatBuffer();
		colorBuffer.put(f);
		colorBuffer.position(0);
	}

	public void setNormalBuffer(float [] f) { // An alternate way to set the normal buffer
		ByteBuffer nBuffer = ByteBuffer.allocateDirect(f.length * 4);
		nBuffer.order(ByteOrder.nativeOrder());
		normalBuffer = nBuffer.asFloatBuffer();
		normalBuffer.put(f);
		normalBuffer.position(0);
	}

	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	public FloatBuffer getUVBuffer() {
		return uvBuffer;
	}

	public Mesh(ShadingProgramManager shader) {
		this.shader = shader;
	}

	public Mesh() {

	}

	public static float[] loadVertexColors(int resourceID) throws IOException { // This function reads in vertex colors from a given file.
		/*
		 A vertex color file has the extension .vc
		 There are three types of lines
		 	one begins with "c" and is followed by 4 floats, which are the RGBA values
		 	another begins with vc and is followed by the vertex index and a color index (defined by c)
		 	there is a special line that starts with "n" that it's value is the number of verticies that are in the model
			the "n" line should go between the color definitions (c) and the vertex colors (vc)
		 */
		InputStream inputStream = RouteEngine.getResources().openRawResource(resourceID); // Open the file
		InputStreamReader sr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(sr);
		List<float[]> colors = new ArrayList<float[]>(); // This will hold all of the colors that will be used
		float[] orderedColors = {};
		int numberOfVerticies = 0;
		String line = "";
		while((line = br.readLine()) != null) { // Loop through the file one line at a time
			if(line.startsWith("c")) { // We are defining a new color in this line
				String[] parts = line.split(" "); // Split the text up/divide it wherever there is a space
				float[] color = new float[4]; // A color is represented by 4 floats (decimals that range from 1.0 to -1.0)
				color[0] = Float.parseFloat(parts[1]); // The first number shows how much red there is in the color
				color[1] = Float.parseFloat(parts[2]); // Second number tells how much green there is
				color[2] = Float.parseFloat(parts[3]); // Thirs number is how much blue there is in the color
				color[3] = Float.parseFloat(parts[4]); // The fourth number is called the alpha. "alpha" is just a fancy term for transparency. If a color had an alpha of 0.5, it would be translucent.
				colors.add(color); // Add this newly defined color to our master list
			}
			else if (line.startsWith("n")) { // This line tells us how many vertices we are setting colors for
				String[] parts = line.split(" ");
				numberOfVerticies = Integer.parseInt(parts[1]);
				orderedColors = new float[numberOfVerticies*4]; // Allocate space for the number of vertices times the size that a color uses (remember, a color is made of 4 numbers, so it is vertex count * 4).
			}
			else if (line.startsWith("vc")) { // This line gives us a vertex and its color
				String[] parts = line.split(" ");
				int index = Integer.parseInt(parts[1])*4; // First get the vertex ID
				float[] color = colors.get(Integer.parseInt(parts[2])); // Now get the ID of the color we are using
				orderedColors[index] = color[0];
				orderedColors[index+1] = color[1];
				orderedColors[index+2] = color[2];
				orderedColors[index+3] = color[3];
			}
			else { // This line isn't recognized, so we will complain about it on the debug console.
				Log.w("WARNING", "Unknown line prefix on line " + line);
			}
		}
		return orderedColors; // Return a list of colors, each color will be attached to a vertex when passed onto OpenGL for rendering
	}

	public static HashMap<String, float[]> loadObject(int resourceID) throws IOException { // This function loads in a model. A model is then used to create a mesh. A model is a set of 3D coordinates for vertices and also UV coords
		List<Float> orderedVerticies = new ArrayList<Float>();
		List<Float> orderedNormals = new ArrayList<Float>();
		List<Float> orderedTexCoords = new ArrayList<Float>();


		HashMap<String, float[]> res = new HashMap<String, float[]>();

		InputStream inputStream = RouteEngine.getResources().openRawResource(resourceID);
		IOBJParser parser = new OBJParser(); // Here we are using a 3rd party library to parse (load) in .obj models
		OBJModel model = parser.parse(inputStream);

		for(OBJObject object : model.getObjects()) {
			for(OBJMesh mesh : object.getMeshes()) {
				for(OBJFace face : mesh.getFaces()) { // Here we are looping through each face on the model
					for(OBJDataReference ref : face.getReferences()) {
						if(face.hasVertices()) { // If the face has vertices, we will add them to our list
							orderedVerticies.add(model.getVertex(ref).x*5);
							orderedVerticies.add(model.getVertex(ref).y*5);
							orderedVerticies.add(model.getVertex(ref).z*5);
						}
						if(face.hasNormals()) { // If the face had information about its normals, we will add that to our list too
							orderedNormals.add(model.getNormal(ref).x);
							orderedNormals.add(model.getNormal(ref).y);
							orderedNormals.add(model.getNormal(ref).z);
						}
						if(face.hasTextureCoordinates()) { // If the face has UV coords associated with it, we will add those coords to our list
							orderedTexCoords.add(model.getTexCoord(ref).u);
							orderedTexCoords.add(1-model.getTexCoord(ref).v); //We must subtract the value of the V coord from 1 because blender is stupid: https://www.blender.org/forum/viewtopic.php?t=2328
						}
					}
				}
			}
		}
		// In the code below we are taking all of the lists of data we have loaded in and attaching them to a label such as "vertices" or "texCoords" and returning them
		res.put("vertices", RouteEngine.listToFloat(orderedVerticies));
		res.put("normals", RouteEngine.listToFloat(orderedNormals));
		res.put("texCoords", RouteEngine.listToFloat(orderedTexCoords));
		//Log.e("BLARP", Integer.toString(orderedTexCoords.size()));
		return res;
	}

	public static float[] generateDefaultColors(int vertices) { // This function is used to generate an array of colors for a mesh in case it doesn't have vertex colors or a texture with it already
		return generateVertexColorArray(vertices, new float[]{ Mesh.DEFAULT_RED, Mesh.DEFAULT_GREEN, Mesh.DEFAULT_BLUE, 1.0f });
	}

	public static float[] generateVertexColorArray(int vertices, float[] color) {
		float[] a = new float[vertices*4];
		for(int i = 0; i < vertices*4; i+=4) {
			a[i] = color[0];
			a[i+1] = color[1];
			a[i+2] = color[2];
			a[i+3] = color[3];
		}
		return a;
	}
}
