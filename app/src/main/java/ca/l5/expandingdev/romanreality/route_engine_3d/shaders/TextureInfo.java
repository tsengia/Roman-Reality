package ca.l5.expandingdev.romanreality.route_engine_3d.shaders;

import android.opengl.GLES20;

/**
 * Created by fives on 12/14/16.
 *
 * This class stores information about 2D textures that have been loaded into OpenGL. Thus the name "TextureInfo".
 */
public abstract class TextureInfo {
	private int resourceIndex; // This is the location of the image in memory.
	private int openglHandle; // This is the location of the image in OpenGL's texture memory.
	private int minFilter = GLES20.GL_NEAREST; // These are the filters used by OpenGL.
	private int magFilter = GLES20.GL_NEAREST; // Changing the filters allows for adjusting clarity/shading resolution. By default we have this set to the lowest filter.
	private int textureUnit = 0; // This is the index of the texture in OpenGL's list of 2D textures.

	public abstract int load() throws Exception; // TextureInfo objects must have there own load method, this is implemented in RouteEngine. There are different ways to load a texture.

	public TextureInfo(int resourceIndex, int minFilter, int magFilter) throws Exception {
		this.resourceIndex = resourceIndex;
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		this.openglHandle = this.load();
	}

	// Getters and Setters below.

	public int getTextureUnit() {
		return textureUnit;
	}

	public void setTextureUnit(int unit) {
		this.textureUnit = unit;
	}

	public int getMagFilter() {
		return this.magFilter;
	}

	public int getMinFilter() {
		return this.minFilter;
	}

	public int getResourceIndex() {
		return resourceIndex;
	}

	public int getOpenglHandle() {
		return openglHandle;
	}

	public void setOpenglHandle(int openglHandle) {
		this.openglHandle = openglHandle;
	}
}
