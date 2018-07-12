uniform mat4 u_MVPMatrix;  // Cube in projection space
uniform mat4 u_MVMatrix; // Cube in view space

attribute vec4 a_Position; //Model verticies/local space
attribute vec3 a_Normal; //Model normals
attribute vec2 a_TexCoord;

varying vec3 v_Position; //Fragment position
varying vec3 v_Normal; //Fragment normal
varying vec2 v_TexCoord;

void main() {
   v_Normal = vec3(u_MVMatrix * vec4(a_Normal,0.0f));
   v_Position = vec3(u_MVMatrix * a_Position);
   v_TexCoord = a_TexCoord;
   gl_Position = u_MVPMatrix * a_Position;
}
