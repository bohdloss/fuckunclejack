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
		needsLightmap=false;
	}

	public DeserthouseWorld(String name) {
		super(name);
		needsLightmap=false;
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
		Game.scaleAmount=60;
		scale.identity().scale(Game.scaleAmount*20, Game.scaleAmount*14, 1);
		for(int i=0;i<3;i++) {
			
			float movFactor = (-c.getX()/(Game.scaleAmount*20));
			float yCoord = ((-c.getY()+400)/(Game.scaleAmount*14));
			
			translate.identity().translate((i-1)+movFactor, yCoord, 0);
			res=c.unTransformedProjection().mul(scale, res).mul(translate, res);
			s.setUniform("projection", res);
			bg.bind(0);
			square.render();
		}
	}
	
}
