package com.bohdloss.fuckunclejack.generator.generators;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.Camera;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class DeserthouseWorld extends World {

protected static Matrix4f scale=new Matrix4f().scale(Game.scaleAmount);
protected static Matrix4f translate=new Matrix4f();
	
	public DeserthouseWorld(long seed, String name) {
		super(seed, name);
		generator=new HouseGenerator(this, seed);
	}

	public DeserthouseWorld(String name) {
		super(name);
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	protected Texture getTexture() {
		return Assets.textures.get("deserthouse_world_sky");
	}
	
	@Override
	protected void renderBackground(Shader s, Matrix4f matrix) {
		Camera c = Game.camera;
		Game.scaleAmount=10;
		scale.identity().scale(Game.scaleAmount*14, Game.scaleAmount*9, 1);
		translate.identity().translate((-c.getX()/(Game.scaleAmount*14))/2f, -c.getY(), 0);
		res=c.unTransformedProjection().mul(scale, res).mul(translate, res);
		s.setUniform("projection", res);
		bg.bind(0);
		square.render();
	}
	
}
