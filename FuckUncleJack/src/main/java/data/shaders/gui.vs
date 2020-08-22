#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying vec2 redbool;

uniform mat4 projection;
uniform mat4 texModifier;
uniform int red;

void main() {
	redbool=vec2(red, 0);
	tex_coords = (texModifier * vec4(textures, 0, 1)).xy;
	gl_Position = projection * vec4(vertices, 1);
}