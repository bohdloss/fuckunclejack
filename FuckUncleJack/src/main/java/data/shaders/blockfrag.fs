# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying float light_var[9];
varying vec4 color_var;

float lerp(float val, float minimum, float maximum);
float reverseLerp(float val, float minimum, float maximum);
float remap(float val, float min1, float max1, float min2, float max2);
int inRangeIgnoreSign(float val, float a, float b);

void main() {
	vec4 txt = color_var;//texture2D(sampler, tex_coords);
	
	//determine quadrant explicitly
	
	float x = tex_coords.x;
	float y = tex_coords.y;	
	
	bool quadX = (x > 0.5);
	bool quadY = (y > 0.5);
	
	//determine correct quadrant for 4 light values
	
	float val1,val2,val3,val4;
	
	int index = (quadX ? 1 : 0) + (quadY ? 0 : 3);
	
	val1 = light_var[index];
	val2 = light_var[index+3];
	val3 = light_var[index+1];
	val4 = light_var[index+4];
	
	//determine stretch coords
	
	float lx = quadX ? reverseLerp(x, 0.5, 1.5) : reverseLerp(x, -0.5, 0.5);
	float ly = quadY ? reverseLerp(y, 0.5, 1.5) : reverseLerp(y, -0.5, 0.5);
	
	//bilinear interpolation
	
	float topLerp = lerp(lx, val1, val3);
	float bottomLerp = lerp(lx, val2, val4);
	float finalLerp = lerp(ly, bottomLerp, topLerp);
	
	float result = -finalLerp+1;
	
	txt.w = result;
	gl_FragColor=txt;
}

int inRangeIgnoreSign(float val, float a, float b) {
		if(a<=b) {
			return val < a ? -1 : (val > b ? 1 : 0);
		} else {
			return val > a ? -1 : (val < b ? 1 : 0);
		}
	}

float lerp(float val, float minimum, float maximum) {
	if(val>=1.0) return maximum;
	if(val<=0.0) return minimum;
	return (maximum-minimum)*val+minimum;
}

float reverseLerp(float val, float minimum, float maximum) {
	int range = inRangeIgnoreSign(val, minimum, maximum);
	if(range != 0.0) return range == -1.0 ? 0.0 : 1.0;
	return (val - minimum) / (maximum - minimum);
}

float remap(float val, float min1, float max1, float min2, float max2) {
	return lerp(reverseLerp(val, min1, max1), min2, max2);
}
