package com.bohdloss.fuckunclejack.render;

import org.joml.Matrix4f;

public class TileSheet {

private Texture texture;

private Matrix4f scale;
private Matrix4f translation;
private int amount;
	
	public TileSheet(Texture texture, int amount) {
		this.texture=texture;
		this.amount=amount;
		
		scale = new Matrix4f().scale(1f, 1f/(float)amount, 1f);
		translation = new Matrix4f();
	}

	public void bindTile(Shader s, int y) {
		scale.translate(0, y, 0, translation);
		
		s.setUniform("sampler", 0);
		s.setUniform("texModifier", translation);
		
		texture.bind(0);
	}
	
	public void test(Shader s, float y) {
		scale.translate(0, y, 0, translation);
		s.setUniform("texModifier", translation);
	}
	
	public int getAmount() {
		return amount;
	}
	
}
