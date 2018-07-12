package ca.l5.expandingdev.romanreality.shaders.fragment;

import ca.l5.expandingdev.romanreality.R;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.FragmentShaderWrapper;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.InputType;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ParameterDescription;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ParameterType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fives on 12/25/16.
 */
public class BasicUVFragmentShader extends FragmentShaderWrapper {
	public BasicUVFragmentShader() throws IOException {
	}

	@Override
	protected int getResourceID() {
		return R.raw.basic_uv_fragment;
	}

	@Override
	protected List<ParameterDescription> getPossibleParameters() {
		List<ParameterDescription> params = new ArrayList<>();
		params.add(new ParameterDescription("v_TexCoord", InputType.VEC2, ParameterType.VARYING));
		params.add(new ParameterDescription("u_Texture", InputType.TEXTURE2D, ParameterType.UNIFORM)); //u_Texture is the texture number, not the texture handle!

		params.add(new ParameterDescription("gl_FragColor" ,InputType.VEC4, ParameterType.GL_FRAGMENT_COLOR));
		return params;
	}
}
