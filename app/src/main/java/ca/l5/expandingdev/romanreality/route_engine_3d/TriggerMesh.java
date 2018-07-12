package ca.l5.expandingdev.romanreality.route_engine_3d;

import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ShadingProgramManager;

/**
 * Created by fives on 2/25/17.
 */
public abstract class TriggerMesh extends Mesh {
	public TriggerMesh(ShadingProgramManager shader) {
		super(shader);
	}

	public TriggerMesh() {
		// This empty constructor is needed by subclasses that will not have a ShaderManager object to pass in when first created.
	}

	private boolean gazeIn = false;

	public boolean getInGaze() {
		return gazeIn;
	}

	public void setGazeIn(boolean inOut) { //inOut holds a boolean value representing whether or not the gaze is at the TriggerMesh
		if(gazeIn == false && inOut) { // If the gaze is outside of the Mesh and just a millisecond ago it is now inside of the Mesh, trigger an onGazeEnter event.
			this.onGazeEnter();
			gazeIn = true;
		}
		else if(this.gazeIn == true && !inOut) { // If the gaze was inside of the Mesh just a few milliseconds ago and now it is outside of the Mesh, trigger an onGazeLeave event.
			this.onGazeLeave();
			gazeIn = false;
		}
	}

	// The below methods are methods to be created by subclasses that will specify what to do when each of these things happen.
	public abstract void onTrigger();
	public abstract void onGazeEnter();
	public abstract void onGazeLeave();
}
