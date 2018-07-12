package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

import android.opengl.GLES20;

import java.io.IOException;

/**
 * Created by fives on 12/7/16.
 *
 * This class is a top layer template for other VertexShaders.
 * It defines the OpenGL shader type parameter needed for Vertex Shaders.
 */
public abstract class VertexShaderWrapper extends ShaderWrapper {

	public VertexShaderWrapper() throws IOException {
		super();
	}

	@Override
	int getShaderType() {
		return GLES20.GL_VERTEX_SHADER;
	}

}
