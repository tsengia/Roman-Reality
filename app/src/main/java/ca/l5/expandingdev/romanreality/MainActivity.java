package ca.l5.expandingdev.romanreality;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Bundle;

import android.os.Vibrator;
import ca.l5.expandingdev.romanreality.route_engine_3d.*;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.*;

import ca.l5.expandingdev.romanreality.shaders.fragment.BasicUVFragmentShader;
import ca.l5.expandingdev.romanreality.shaders.vertex.BasicUVVertexShader;
import com.google.vr.sdk.base.*;

import javax.microedition.khronos.egl.EGLConfig;
import java.util.HashMap;

public class MainActivity extends GvrActivity implements GvrView.StereoRenderer {

	public static MonoLightEngine engine;

	public static float[] dataPointCoords = new float[] { // This is a list of the X, Y and Z coordinates representing the position of the datapoints
		105f, 51.3f, 37f,
		25f, 51.3f, 64f,
		-82f, 51.3f, -86f,
		-79f, 51.3f, 51.3f,
		98f, 51.3f, -78f,
		0f, 134f, 0f
	};

	public static int[] dataPointImages = new int[] { // This is a list of the image IDs that each datapoint is linked to
		R.raw.text0, R.raw.text1, R.raw.text2, R.raw.text3, R.raw.text4, R.raw.text5
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) { // The below code is all to set up the Virtual Reality screen and controls.
		super.onCreate(savedInstanceState);
		// The below lines of code set up OpenGL and the Virtual Reality controls
		setContentView(R.layout.activity_main);
		GvrView gvrView = (GvrView) findViewById(R.id.gvr_view);
		gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
		gvrView.setRenderer(this);
		gvrView.setTransitionViewEnabled(true);
		if (gvrView.setAsyncReprojectionEnabled(true)) {
			AndroidCompat.setSustainedPerformanceMode(this, true);
		}
		setGvrView(gvrView);
		gvrView.setOnCardboardTriggerListener(new MainActivity.OnTriggerListener()); // Set a handler for when the trigger is pressed.
		engine = new MonoLightEngine(getResources());
		engine.setVibrator((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)); // Get the device's vibrator
		engine.getEnvironmentalLight().setStrength(0.001f); // Set up the lighting for the world
		engine.getLight().setStrength(0.001f);
		gvrView.recenterHeadTracker(); // Calibrate the headtracker
	}

	@Override
	public void onSurfaceCreated(EGLConfig eglConfig) {
		//This is where we load everything in
		try {
			//Load in and add the panorama Mesh. This mesh is what displays/holds the text.
			TextPanorama pano = new TextPanorama();
			pano.setVisible(false);
			int panoID = engine.addMesh(pano);
			engine.nameMesh(panoID, "panorama");

			//Create each datapoint
			for(int i = 0; i < MainActivity.dataPointImages.length; i++) {
				// Create a new datapoint at the coords stored in the array and set its image to the image ID stored in the dataPointImages[] array
				DataPoint dataPoint = new DataPoint(MainActivity.dataPointImages[i], MainActivity.dataPointCoords[i*3], MainActivity.dataPointCoords[(i*3)+1], MainActivity.dataPointCoords[(i*3)+2]);
				int index = engine.addTriggerMesh(dataPoint); // Add the data point to the list of meshes that respond to interaction and keep its ID for when we name it
				RouteEngine.nameMesh(index, "datapoint" + Integer.toString(i)); // We will name each datapoint
			}

			//Load in the colosseum Mesh
			ShadingProgramManager colosseumShader = new ShadingProgramManager(new BasicUVVertexShader(), new BasicUVFragmentShader());
			HashMap<String, float[]> colosseumInfo = Mesh.loadObject(R.raw.current1);
			Mesh colosseum = new Mesh(colosseumShader) {
				@Override
				public HashMap<String, Object> getParameterValues() { // Here we custom define the possible parameters that could be passed to the OpenGL shader program and what the values of each parameter is.
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("a_Position", this.getVertexBuffer());
					params.put("a_Color", this.getColorBuffer());
					params.put("a_Normal", this.getNormalBuffer());
					params.put("u_MVPMatrix", this.getMVPMatrix());
					params.put("u_MVMatrix", this.getMVMatrix());
					params.put("u_Texture", this.getTextureInfo());
					params.put("a_TexCoord", this.getUVBuffer());
					return params;
				}
			};
			//Now that we have loaded the model's information, we are going to pass the info to the Mesh object. Mesh objects are used to hold and manipulate their own position, images and rendering data.
			colosseum.setTextureInfo(RouteEngine.createTextureInfo(R.raw.main_texture, GLES20.GL_NEAREST, GLES20.GL_LINEAR));
			colosseum.setNormalBuffer(colosseumInfo.get("normals"));
			colosseum.setUVBuffer(colosseumInfo.get("texCoords"));
			colosseum.setVertexBuffer(colosseumInfo.get("vertices"));
			colosseum.setColorBuffer(Mesh.generateDefaultColors(colosseumInfo.get("vertices").length));
			colosseum.translateTo(0.0f, 0.0f, 0.0f);
			colosseum.localRotate(90, 0.0f, 1.0f, 0.0f);
			int colosseumID = engine.addMesh(colosseum);
			engine.nameMesh(colosseumID, "colosseum");

			//Load in the skydome Mesh. A sky dome is a half-sphere that covers the scene with an image of a sky on the inside of it. This gives the illusion of a sky above/around the user.
			ShadingProgramManager skydomeShader = new ShadingProgramManager(new BasicUVVertexShader(), new BasicUVFragmentShader());
			HashMap<String, float[]> skydomeInfo = Mesh.loadObject(R.raw.skydome);
			Mesh dome = new Mesh(skydomeShader) {
				@Override
				public HashMap<String, Object> getParameterValues() {
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("a_Position", this.getVertexBuffer());
					params.put("a_Color", this.getColorBuffer());
					params.put("a_Normal", this.getNormalBuffer());
					params.put("u_MVPMatrix", this.getMVPMatrix());
					params.put("u_MVMatrix", this.getMVMatrix());
					params.put("u_Texture", this.getTextureInfo());
					params.put("a_TexCoord", this.getUVBuffer());
					return params;
				}
			};
			dome.setTextureInfo(RouteEngine.createTextureInfo(R.raw.skydometex, GLES20.GL_NEAREST, GLES20.GL_LINEAR));
			dome.setNormalBuffer(skydomeInfo.get("normals"));
			dome.setUVBuffer(skydomeInfo.get("texCoords"));
			dome.setVertexBuffer(skydomeInfo.get("vertices"));
			dome.translateTo(0.0f, 0.0f, 0.0f);
			int domeID = engine.addMesh(dome);
			engine.nameMesh(domeID, "skydome");

		} catch (Exception e) { // There was an error while setting up everything, so log the error and close the app.
			engine.printException(e);
			e.printStackTrace();
		}
	}

	public static class OnTriggerListener implements Runnable { // This passes on events to the RouteEngine (framework) when the trigger is pressed.
		@Override
		public void run() {
			engine.getVibrator().vibrate(50); // Make the phone buzz a little when the user uses the trigger button.

			TextPanorama textPanorama = ((TextPanorama) RouteEngine.getMesh(RouteEngine.getMeshIDByName("panorama")));
			if (textPanorama.getVisible()) { // If the text is being shown, hide it
				textPanorama.setVisible(false);
			}
			else { // If it's not, then check to see if a datapoint was selected.
				engine.testTriggerMeshes();
			}
		}
	}

	@Override
	public void onNewFrame(HeadTransform headTransform) { // Just pass this event over to our framework (RouteEngine)
		engine.onNewFrame(headTransform);
	}

	@Override
	public void onDrawEye(Eye eye) { // Also passing this straight to the framework
		engine.onDrawEye(eye);
	}

	@Override
	public void onFinishFrame(Viewport viewport) {
		//After each frame, test to see if the user is looking at an object that responds to being looked at.
		engine.testGazeMeshes();
	}

	@Override
	public void onSurfaceChanged(int i, int i1) {
		//Do nothing
	}

	@Override
	public void onRendererShutdown() {
		//Do nothing
	}
}
