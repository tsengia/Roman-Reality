package ca.l5.expandingdev.romanreality.shaders.vertex;

import ca.l5.expandingdev.romanreality.R;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.InputType;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ParameterDescription;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ParameterType;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.VertexShaderWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fives on 12/25/16.
 */
public class BasicUVVertexShader extends VertexShaderWrapper {
	public BasicUVVertexShader() throws IOException {
	}

	@Override
	protected int getResourceID() {
		return R.raw.basic_uv_vertex;
	}

	@Override
	protected List<ParameterDescription> getPossibleParameters() {
		List<ParameterDescription> params = new ArrayList<>();
		params.add(new ParameterDescription("v_TexCoord", InputType.VEC2, ParameterType.VARYING));
		params.add(new ParameterDescription("a_TexCoord", InputType.FLOAT_BUFFER2, ParameterType.ATTRIBUTE));
		params.add(new ParameterDescription("u_MVPMatrix", InputType.MATRIX4, ParameterType.UNIFORM));
		params.add(new ParameterDescription("a_Position", InputType.FLOAT_BUFFER3, ParameterType.ATTRIBUTE));

		params.add(new ParameterDescription("gl_Position", InputType.VEC4, ParameterType.GL_POSTION));
		return params;
	}
}
