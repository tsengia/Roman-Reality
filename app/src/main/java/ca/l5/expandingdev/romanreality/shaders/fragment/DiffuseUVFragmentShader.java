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
 * Created by fives on 1/20/17.
 */
public class DiffuseUVFragmentShader extends FragmentShaderWrapper {


	public DiffuseUVFragmentShader() throws IOException {
	}

	@Override
	protected int getResourceID() {
		return R.raw.diffuse_uv_fragment;
	}

	@Override
	protected List<ParameterDescription> getPossibleParameters() {
		List<ParameterDescription> params = new ArrayList<ParameterDescription>();

		params.add(new ParameterDescription("u_Texture", InputType.TEXTURE2D, ParameterType.UNIFORM)); //u_Texture is the texture number, not the texture handle!
		params.add(new ParameterDescription("u_MVMatrix", InputType.MATRIX4, ParameterType.UNIFORM));
		params.add(new ParameterDescription("u_MVPMatrix", InputType.MATRIX4, ParameterType.UNIFORM));

		params.add(new ParameterDescription("u_LightPosition", InputType.VEC3, ParameterType.UNIFORM));

		params.add(new ParameterDescription("u_EnvironmentalLightStrength", InputType.VEC1, ParameterType.UNIFORM));

		params.add(new ParameterDescription("v_TexCoord", InputType.VEC2, ParameterType.VARYING));
		params.add(new ParameterDescription("v_Normal" ,InputType.VEC3, ParameterType.VARYING));
		params.add(new ParameterDescription("v_Position" ,InputType.VEC3, ParameterType.VARYING));

		params.add(new ParameterDescription("gl_FragColor" ,InputType.VEC4, ParameterType.GL_FRAGMENT_COLOR));
		return params;
	}
}
