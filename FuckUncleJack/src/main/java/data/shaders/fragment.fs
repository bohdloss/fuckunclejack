# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying vec2 redbool;
varying vec4 grayscale;
varying vec2 lightlevel;

float lerp(float val, float min, float max);

void main() {
	vec4 txt = texture2D(sampler, tex_coords);
	if(redbool.x==1) {
		txt.x+=0.5;
	}
	
	float sum = (txt.x)+(txt.y)+(txt.z);
	float average = sum/3;
	
	vec4 interpolation = vec4(lerp(grayscale.x, txt.x, average), lerp(grayscale.y, txt.y, average), lerp(grayscale.z, txt.z, average), txt.w);
	
	vec4 lightInterpolation = interpolation;
	if(lightlevel.x!=-1) {
		vec4 black = vec4(0,0,0,1);
		lightInterpolation=vec4(lerp(lightlevel.x, black.x, interpolation.x), lerp(lightlevel.x, black.y, interpolation.y), lerp(lightlevel.x, black.z, interpolation.z), interpolation.w);
	}
	
	gl_FragColor=lightInterpolation;
}

float lerp(float val, float min, float max) {
	return (max-min)*val+min;
}