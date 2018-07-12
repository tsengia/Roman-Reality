package ca.l5.expandingdev.romanreality.shaders.fragment;


import ca.l5.expandingdev.romanreality.R;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fives on 12/18/16.
 */
public class BasicFragmentShader extends FragmentShaderWrapper {
	public BasicFragmentShader() throws IOException {
	}

	@Override
	protected int getResourceID() {
		return R.raw.basic_fragment;
	}

	@Override
	protected List<ParameterDescription> getPossibleParameters() {
		List<ParameterDescription> params = new ArrayList<ParameterDescription>();
		params.add(new ParameterDescription("v_Color", InputType.VEC4, ParameterType.VARYING));

		params.add(new ParameterDescription("gl_FragColor" ,InputType.VEC4, ParameterType.GL_FRAGMENT_COLOR));
		return params;
	}


}
