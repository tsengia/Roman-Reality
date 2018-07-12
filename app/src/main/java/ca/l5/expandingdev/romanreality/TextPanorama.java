package ca.l5.expandingdev.romanreality;

import android.opengl.GLES20;
import ca.l5.expandingdev.romanreality.route_engine_3d.Mesh;
import ca.l5.expandingdev.romanreality.route_engine_3d.RouteEngine;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.ShadingProgramManager;
import ca.l5.expandingdev.romanreality.route_engine_3d.shaders.TextureInfo;
import ca.l5.expandingdev.romanreality.shaders.fragment.BasicUVFragmentShader;
import ca.l5.expandingdev.romanreality.shaders.vertex.BasicUVVertexShader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fives on 3/1/17.
 *
 * The TextPanorama class defines the tube/panorama of text that the user is able to turn around and read.
 * It contains methods for adding, loading, and displaying images (images of text)
 */
public class TextPanorama extends Mesh {

	private HashMap<Integer, TextureInfo> images = new HashMap<Integer, TextureInfo>(); // This is a table of images and their index's/numbers
	private int currentImage = 0; // This is the index of the current image
	private int nextImageIndex = 0; // This keeps a running count of how many images have been added to this panorama regardless of how many have been removed.

	public int addTextImage(final int imageID) { // This method adds an image to the panorama.
		try {
			images.put(nextImageIndex, RouteEngine.createTextureInfo(imageID, GLES20.GL_NEAREST, GLES20.GL_LINEAR)); // Attempt to load the image
		} catch (Exception e) { // The image load failed, so return -1 for the index.
			return -1;
		}
		nextImageIndex++; // Increment the nextImageIndex as the next image added will have an ID/index/number that is one more than the image that was just added
		return nextImageIndex - 1;
	}

	public void removeTextImage(int index) { // This removes an image from the table if the entry exists
		if(images.containsKey(index)) {
			images.remove(index);
		}
	}

	public TextureInfo getImage(int index) { // Returns the image stored at the requested index.
		return images.get(index);
	}

	public int getCurrentImage() { // Returns the index of the current image
		return currentImage;
	}

	public void setCurrentImage(int imageIndex) {
		currentImage = imageIndex;
	}

	public TextPanorama() throws Exception {
		super(new ShadingProgramManager(new BasicUVVertexShader(), new BasicUVFragmentShader())); // Load in a simple UV shader
		HashMap<String, float[]> info = Mesh.loadObject(R.raw.text_tube); // Load in the tube model
		this.setNormalBuffer(info.get("normals"));
		this.setUVBuffer(info.get("texCoords"));
		this.setVertexBuffer(info.get("vertices"));
		this.translateTo(0.0f, 0.0f, 0.0f); // Translate our tube to surround the user
	}

	@Override
	public TextureInfo getTextureInfo() {
		return this.images.get(currentImage);
	}

	@Override
	public HashMap<String, Object> getParameterValues() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("a_Position", this.getVertexBuffer());
		params.put("a_Color", this.getColorBuffer());
		params.put("a_Normal", this.getNormalBuffer());
		params.put("u_MVPMatrix", this.getMVPMatrix());
		params.put("u_MVMatrix", this.getMVMatrix());
		params.put("u_Texture", this.getTextureInfo());
		params.put("a_TexCoord", this.getUVBuffer());
		return params;
	}
}
