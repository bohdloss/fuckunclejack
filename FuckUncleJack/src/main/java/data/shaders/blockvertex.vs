#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying float light_var[9];
varying vec4 color_var;

uniform mat4 projection;
uniform float light_uniform[9];
uniform vec4 color_uniform = vec4(0,0,0,0);

void main() {
	color_var = color_uniform;
	light_var = light_uniform;
	tex_coords = textures;
	gl_Position = projection * vec4(vertices, 1);
}
