package ca.l5.expandingdev.romanreality.route_engine_3d;

import android.content.res.Resources;
import ca.l5.expandingdev.romanreality.route_engine_3d.lights.PointLight;

import java.util.HashMap;

/**
 * Created by fives on 12/20/16.
 *
 * The MonoLightEngine is a type of RouteEngine that has one point light as the only light source.
 * It is useful to extend RouteEngine and include specific lighting parameters as lighting can be a pain to handle.
 */
public class MonoLightEngine extends RouteEngine {
	protected PointLight light = new PointLight(1.0f, new float[]{0.5f,0.5f,0.5f}, new float[]{0.0f,0.0f,0.0f});
	/*
		u_LightPosition should be a vec3, but the parameter description will be a VEC4
		u_LightColor should be a vec3, as well as its parameter description
		u_LightStrength will always be a vec1
	 */

	public PointLight getLight() {
		return this.light;
	}

	public MonoLightEngine(Resources r) {
		super(r);
	}

	@Override
	public HashMap<String,Object> getLightParams() { // Here is our specific light parameters.
		HashMap<String, Object> params = new HashMap<>();
		params.put("u_LightStrength", new float[] {this.getLight().getStrength()});
		params.put("u_LightColor", this.getLight().getColor());
		params.put("u_LightPosition", this.getLight().getPosition());
		params.put("u_EnvironmentalLightStrength", new float[] {this.getEnvironmentalLight().getStrength()});
		params.put("u_EnvironmentalLightColor", this.getEnvironmentalLight().getColor());
		return params;
	}
}
