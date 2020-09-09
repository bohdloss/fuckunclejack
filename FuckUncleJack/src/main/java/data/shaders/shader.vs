#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 tex_coords;
varying vec2 redbool;
varying vec4 grayscale;
varying vec2 lightlevel;
varying vec4 mask;
varying vec2 vopacity;
varying vec4 voutline;

uniform mat4 projection;
uniform int red;
uniform float gray;
uniform float light=-1;
uniform vec4 colormask=vec4(-1,0,0,0);
uniform float opacity=-1;
uniform vec4 outline=vec4(-1,0,0,0);

void main() {
	voutline=outline;
	vopacity=vec2(opacity, 0);
	mask=colormask;
	lightlevel=vec2(light, 0);
	grayscale=vec4(gray, gray, gray, 1);
	redbool=vec2(red, 0);
	tex_coords = textures;
	gl_Position = projection * vec4(vertices, 1);
}