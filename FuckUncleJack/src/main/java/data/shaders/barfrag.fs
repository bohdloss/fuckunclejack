# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying vec4 mask;
varying vec3 percentRadiusBool;

float lerp(float val, float min, float max);
float reverseLerp(float val, float min, float max);
float remap(float val, float min1, float max1, float min2, float max2);
vec4 calcColorMask(vec4 current, vec4 addition);
vec4 calcGray(vec4 current, float value);
int inRangeIgnoreSign(float val, float a, float b);

void main() {
	vec4 txt = texture2D(sampler, tex_coords);
	
	float percent = percentRadiusBool.x;
	float radius = percentRadiusBool.y;
	bool reverse = (percentRadiusBool.z==1);
	
	vec4 add = mask;
	
	if(reverse) {
		add.x=remap(tex_coords.x, percent, percent-radius, 0, add.x);
		add.y=remap(tex_coords.x, percent, percent-radius, 0, add.y);
		add.z=remap(tex_coords.x, percent, percent-radius, 0, add.z);
	} else {
		add.x=remap(tex_coords.x, percent, percent+radius, add.x, 0);
		add.y=remap(tex_coords.x, percent, percent+radius, add.y, 0);
		add.z=remap(tex_coords.x, percent, percent+radius, add.z, 0);
	}
	
	txt=calcColorMask(txt, add);
	
	//float grayval=remap(tex_coords.x, percent, percent+radius, 0, 1);
	
	//txt=calcGray(txt, grayval);
	
	gl_FragColor=txt;
}

vec4 calcGray(vec4 current, float value) {
	if(value>0) {
	
		float sum = (current.x)+(current.y)+(current.z);
		float average = sum/3;
	
		vec4 interpolation = vec4(lerp(value, current.x, average), lerp(value, current.y, average), lerp(value, current.z, average), current.w);
		return interpolation;
	}
	return current;
}

vec4 calcColorMask(vec4 current, vec4 addition) {
	if(addition.x>=0) {
		vec4 res=vec4(current.x+addition.x, current.y+addition.y, current.z+addition.z, current.w);
		return res;
	}
	return current;
}

int inRangeIgnoreSign(float val, float a, float b) {
		if(a<=b) {
			return val<a?-1:(val>b?1:0);
		} else {
			return val>a?-1:(val<b?1:0);
		}
	}

float lerp(float val, float min, float max) {
	if(val>=1) return max;
	if(val<=0) return min;
	return (max-min)*val+min;
}

float reverseLerp(float val, float min, float max) {
	int range = inRangeIgnoreSign(val, min, max);
	if(range!=0) return range==-1?0:1;
	return (val-min)/(max-min);
}

float remap(float val, float min1, float max1, float min2, float max2) {
	return lerp(reverseLerp(val, min1, max1), min2, max2);
}