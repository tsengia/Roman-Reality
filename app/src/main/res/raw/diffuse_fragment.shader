uniform mat4 u_MVPMatrix;  // Cube in projection space
uniform mat4 u_MVMatrix; // Cube in view space

uniform vec3 u_LightPosition; // Light position in eye/camera space (projection space)
uniform vec3 u_LightColor;
uniform float u_LightStrength;

uniform float u_EnvironmentalLightStrength;
uniform vec3 u_EnvironmentalLightColor;

varying vec4 v_Color; //Fragment color
varying vec3 v_Position; //Fragment position
varying vec3 v_Normal; //Fragment normal

void main() {

   float distance = distance(u_LightPosition, v_Position);
   vec3 lightVector = normalize(u_LightPosition - v_Position);
   float diffuse = max(dot(v_Normal, lightVector),u_EnvironmentalLightStrength);
   diffuse = diffuse * (1.0/(1.0+(distance*distance)));
   gl_FragColor = vec4(diffuse, diffuse, diffuse, 1.0f) * v_Color;
}
