uniform mat4 u_MVPMatrix;  // Cube in projection space
uniform mat4 u_MVMatrix; // Cube in view space
uniform sampler2D u_Texture;

uniform vec3 u_LightPosition; // Light position in eye/camera space (projection space)

uniform float u_EnvironmentalLightStrength;

varying vec3 v_Position; //Fragment position
varying vec3 v_Normal; //Fragment normal
varying vec2 v_TexCoord;

void main() {
   float distance = distance(u_LightPosition, v_Position);
   vec3 lightVector = normalize(u_LightPosition - v_Position);
   float diffuse = max(dot(v_Normal, lightVector),u_EnvironmentalLightStrength);
   diffuse = diffuse * (1.0f/(1.0f+(distance*distance)));
   gl_FragColor = vec4(diffuse,diffuse,diffuse,1.0f) * texture2D(u_Texture, v_TexCoord);
}
