package ca.l5.expandingdev.romanreality.route_engine_3d;

import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ShadingProgramManager;
import ca.l5.expandingdev.romanreality.shaders.fragment.BasicFragmentShader;
import ca.l5.expandingdev.romanreality.shaders.vertex.BasicVertexShader;

import java.util.HashMap;

/**
 * Created by fives on 2/25/17.
 */

/*
 * A BasicTriggerMesh is a TriggerMesh that only has vertex colors and is shadeless.
 * This class is later used by the DataPoint objects
 */
public abstract class BasicTriggerMesh extends TriggerMesh {

	public BasicTriggerMesh() throws Exception {
		ShadingProgramManager shader = new ShadingProgramManager(new BasicVertexShader(), new BasicFragmentShader());
		this.shader = shader;
	}

	public BasicTriggerMesh(float x, float y, float z) throws Exception {
		ShadingProgramManager shader = new ShadingProgramManager(new BasicVertexShader(), new BasicFragmentShader());
		this.shader = shader;
		this.translateTo(x, y, z);
	}

	@Override
	public HashMap<String, Object> getParameterValues() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("a_Position", this.getVertexBuffer());
		params.put("a_Color", this.getColorBuffer());
		params.put("a_Normal", this.getNormalBuffer());
		params.put("u_MVPMatrix", this.getMVPMatrix());
		params.put("u_MVMatrix", this.getMVMatrix());
		return params;
	}
}
