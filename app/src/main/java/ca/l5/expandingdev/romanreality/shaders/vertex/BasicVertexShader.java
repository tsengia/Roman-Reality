package ca.l5.expandingdev.romanreality.shaders.vertex;

import ca.l5.expandingdev.romanreality.R;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fives on 12/18/16.
 */
public class BasicVertexShader extends VertexShaderWrapper {
	public BasicVertexShader() throws IOException {}

	@Override
	protected int getResourceID() {
		return R.raw.basic_vertex;
	}

	@Override
	protected List<ParameterDescription> getPossibleParameters() {
		List<ParameterDescription> params = new ArrayList<ParameterDescription>();
		params.add(new ParameterDescription("v_Color", InputType.VEC4, ParameterType.VARYING));
		params.add(new ParameterDescription("u_MVPMatrix", InputType.MATRIX4, ParameterType.UNIFORM));
		params.add(new ParameterDescription("a_Position", InputType.FLOAT_BUFFER3, ParameterType.ATTRIBUTE));
		params.add(new ParameterDescription("a_Color", InputType.FLOAT_BUFFER4, ParameterType.ATTRIBUTE));

		params.add(new ParameterDescription("gl_Position" ,InputType.VEC4, ParameterType.GL_POSTION));
		return params;
	}
}
