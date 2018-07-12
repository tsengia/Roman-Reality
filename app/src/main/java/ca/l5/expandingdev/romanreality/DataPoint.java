package ca.l5.expandingdev.romanreality;

import android.opengl.Matrix;
import android.util.Log;
import ca.l5.expandingdev.romanreality.route_engine_3d.BasicTriggerMesh;
import ca.l5.expandingdev.romanreality.route_engine_3d.Mesh;
import ca.l5.expandingdev.romanreality.route_engine_3d.RouteEngine;
import ca.l5.expandingdev.romanreality.route_engine_3d.geometry.Plane;

import java.util.HashMap;

/**
 * Created by fives on 2/25/17.
 *
 * This class is the class for the colored points you see round the world that when selected provide information.
 * Each one has coordinates and an image associated with it.
 */
public class DataPoint extends BasicTriggerMesh {

	private int imageResourceID = -1; // This is the index of the file of the image that the datapoint displays
	private int panoramaImageIndex = -1; // This is the index of the image when it is passed onto the panorama viewer.
	private int verticesCount = 0;
	private final float[] RESTING_COLOR = new float[]{0.3f, 0.5f, 1.0f, 1.0f}; // This is the normal color of the datapoint.
	private final float[] GAZE_COLOR = new float[]{1.0f, 0.3f, 0.5f, 1.0f}; // This is the color of the datapoint when the user is looking at it.

	public DataPoint(int imageResourceID, float x, float y, float z) throws Exception {
		super(x, y, z); // Set our datapoint's location
		this.imageResourceID = imageResourceID;
		this.panoramaImageIndex = ((TextPanorama) RouteEngine.getMesh(RouteEngine.getMeshIDByName("panorama"))).addTextImage(imageResourceID); // Add in the image to the panorama viewer
		HashMap<String, float[]> info = Mesh.loadObject(R.raw.sphere); // Load in the vertex data, which is just a sphere model
		this.setVertexBuffer(info.get("vertices"));
		this.setNormalBuffer(info.get("normals"));
		verticesCount = info.get("vertices").length/3;
		this.setColorBuffer(Mesh.generateVertexColorArray(verticesCount, RESTING_COLOR)); // Set the color of the datapoint to the default/non-active color.
	}

	@Override
	public void onTrigger() { // When this datapoint is selected, it will switch the panorama's image and display the panorama.
		Plane xzPlane = Plane.planeFrom3Points(new float[]{5, 0.1f, 0.1f}, new float[] {-2, 0.1f, 6}, new float[] {2, 0.1f, 1});
		float[] modelView = new float[16];
		Matrix.multiplyMM(modelView, 0, RouteEngine.getHeadViewMatrix(), 0, this.getPositionMatrix(), 0);
		float[] position = RouteEngine.getCoordsFromMatrix(modelView);
		float signedDistance = xzPlane.getSignedDistanceFromPlane(position);
		if(signedDistance > 0) {
			int panoIndex = RouteEngine.getMeshIDByName("panorama");
			((TextPanorama) RouteEngine.getMesh(panoIndex)).setCurrentImage(this.panoramaImageIndex);
			((TextPanorama) RouteEngine.getMesh(panoIndex)).setVisible(true);
		}
	}

	@Override public void onGazeEnter() { // Change colors when the user looks at the datapoint
		this.setColorBuffer(Mesh.generateVertexColorArray(verticesCount, GAZE_COLOR));
	}

	@Override public void onGazeLeave() { // Change color back to the default color when the user looks away from the datapoint
		this.setColorBuffer(Mesh.generateVertexColorArray(verticesCount, RESTING_COLOR));
	}
}
