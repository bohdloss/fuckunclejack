#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying vec2 redbool;
varying vec4 grayscale;
varying vec2 lightlevel;

uniform mat4 projection;
uniform mat4 texModifier;
uniform int red;
uniform float gray;
uniform float light=-1;

void main() {
	lightlevel=vec2(light, 0);
	grayscale=vec4(gray, gray, gray, 1);
	redbool=vec2(red, 0);
	tex_coords = (texModifier * vec4(textures, 0, 1)).xy;
	gl_Position = projection * vec4(vertices, 1);
}