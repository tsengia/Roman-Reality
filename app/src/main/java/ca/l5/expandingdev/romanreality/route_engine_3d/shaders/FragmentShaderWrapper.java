package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

import android.opengl.GLES20;

import java.io.IOException;

/**
 * Created by fives on 12/7/16.
 *
 * This class is a top layer template for other FragmentShaders.
 * It defines the OpenGL shader type parameter.
 */
public abstract class FragmentShaderWrapper extends ShaderWrapper {
	public FragmentShaderWrapper() throws IOException {
		super();
	}

	@Override
	int getShaderType() {
		return GLES20.GL_FRAGMENT_SHADER;
	}
}
