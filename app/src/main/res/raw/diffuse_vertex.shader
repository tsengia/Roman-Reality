uniform mat4 u_MVPMatrix;  // Cube in projection space
uniform mat4 u_MVMatrix; // Cube in view space
uniform vec4 u_LightPosition;

attribute vec4 a_Position; //Model verticies/local space
attribute vec4 a_Color; //Vertex colors
attribute vec3 a_Normal; //Model normals

varying vec4 v_Color; //Fragment color
varying vec3 v_Position; //Fragment position
varying vec3 v_Normal; //Fragment normal

void main() {
   v_Normal = vec3(u_MVMatrix * vec4(a_Normal,0.0));
   v_Color = a_Color;
   v_Position = vec3(u_MVMatrix * a_Position);
   gl_Position = u_MVPMatrix * a_Position;
}
