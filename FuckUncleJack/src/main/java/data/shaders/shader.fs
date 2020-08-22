# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying vec2 redbool;

void main() {
	if(redbool.x==1) {
		vec4 txt = texture2D(sampler, tex_coords);
		txt.x+=0.5;
		gl_FragColor=txt;
	} else {
		vec4 txt = texture2D(sampler, tex_coords);
		gl_FragColor=txt;
	}
}