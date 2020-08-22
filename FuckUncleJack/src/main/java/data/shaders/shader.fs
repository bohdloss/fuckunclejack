# version 120

uniform sampler2D sampler;

varying vec2 tex_coords;
varying vec2 redbool;

void main() {
	if(redbool.x==1) {
		gl_FragColor = texture2D(sampler, tex_coords)+vec4(1,0,0,0.5);
	} else {
		gl_FragColor = texture2D(sampler, tex_coords);
	}
}