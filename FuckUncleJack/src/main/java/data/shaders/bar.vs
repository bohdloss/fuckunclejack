#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying vec4 mask;
varying vec3 percentRadiusBool;

uniform mat4 projection;
uniform vec4 colormask;
uniform float percent;
uniform float radius;
uniform int reverse;

void main() {
	mask=colormask;
	percentRadiusBool=vec3(percent, radius, reverse);
	tex_coords = textures;
	gl_Position = projection * vec4(vertices, 1);
}