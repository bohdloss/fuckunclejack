# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying vec2 redbool;
varying vec4 grayscale;
varying vec2 lightlevel;
varying vec4 mask;
varying vec2 vopacity;
varying vec4 voutline;

float lerp(float val, float minimum, float maximum);
vec4 calcGray(vec4 current);
vec4 calcLight(vec4 current);
vec4 calcColorMask(vec4 current);
vec4 calcOpacity(vec4 current);
vec4 calcOutline(vec4 current);

void main() {
	vec4 txt = texture2D(sampler, tex_coords);
	if(redbool.x==1.0) {
		txt.x+=0.5;
	}
	
	txt=calcGray(txt);
	txt=calcLight(txt);
	txt=calcColorMask(txt);
	txt=calcOpacity(txt);
	txt=calcOutline(txt);
	
	gl_FragColor=txt;
}

vec4 calcOpacity(vec4 current) {
	if(vopacity.x>=0.0) {
		current.w=vopacity.x;
	}
	return current;
}

vec4 calcColorMask(vec4 current) {
	if(mask.x>=0.0) {
		current.x+=mask.x;
		current.y+=mask.y;
		current.z+=mask.z;
		current.w+=mask.w;
	}
	return current;
}

vec4 calcLight(vec4 current) {
	if(lightlevel.x!=-1.0) {
		vec4 black = vec4(0.0, 0.0, 0.0, 1.0);
		vec4 lightInterpolation=vec4(lerp(lightlevel.x, black.x, current.x), lerp(lightlevel.x, black.y, current.y), lerp(lightlevel.x, black.z, current.z), current.w);
		return lightInterpolation;
	}
	return current;
}

vec4 calcGray(vec4 current) {
	if(grayscale.x>0.0) {
	
		float sum = (current.x)+(current.y)+(current.z);
		float average = sum/3.0;
	
		vec4 interpolation = vec4(lerp(grayscale.x, current.x, average), lerp(grayscale.y, current.y, average), lerp(grayscale.z, current.z, average), current.w);
		return interpolation;
	}
	return current;
}

float lerp(float val, float minimum, float maximum) {
	return (maximum-minimum)*val+minimum;
}

vec4 calcOutline(vec4 current) {
	if(voutline.x>=0) {
		vec2 stretched = tex_coords;
		vec4 stretchtxt = texture2D(sampler, tex_coords);
		return stretchtxt;
	}
	return current;
}
