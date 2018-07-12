package ca.l5.expandingdev.romanreality.route_engine_3d;

import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ShadingProgramManager;
import ca.l5.expandingdev.romanreality.shaders.fragment.BasicFragmentShader;
import ca.l5.expandingdev.romanreality.shaders.vertex.BasicVertexShader;

import java.util.HashMap;

/**
 * Created by fives on 3/5/17.
 */
public class EmptyMesh extends Mesh {
	/*
		This is a placeholder mesh used for navigation, placement, orientation and testing.
		It will never be rendered or used for physics, hence "Empty" Mesh.
	 */
	public EmptyMesh() throws Exception {
		super(new ShadingProgramManager(new BasicVertexShader(), new BasicFragmentShader()));
	}

	@Override
	public boolean getVisible() { // This mesh is supposed to invisible, so always return false when asked if it is visible or not.
		return false;
	}

	@Override
	public HashMap<String, Object> getParameterValues() {
		return new HashMap<String, Object>(); // Since this mesh will never be rendered, we can tell Java that we will return an empty list.
	}
}
