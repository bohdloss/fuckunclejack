package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Shader;

public class StaticProjectileEntity extends Entity {

public float rotation;
	
private Object[] data = new Object[2];

	public StaticProjectileEntity(String texture, float rotation) {
		super("square", texture, 1);
		this.rotation=rotation;
		invulnerable=true;
		physics=true;
		collision=false;
		width=1;
		height=1;
		updateBounds();
	}

	@Override
	public int getId() {
		return 5;
	}

	@Override
	public void tick(float delta) {
		physics(delta);
		
		if(velx!=0|vely!=0) destroy();
	}
	
	@Override
	public void render(Shader s, Matrix4f input) {
		s.setUniform("red", red);
		res = input.translate(x, y, 0, res).rotate(rotation, 0, 0, 1, res);
		s.setProjection(res);
		Assets.textures.get(texture).bind(0);
		Assets.models.get(model).render();
		s.setUniform("red", false);
		renderHitboxes(s, input);
	}
	
	public Object[] getData() {
		data[0]=texture;
		data[1]=rotation;
		return data;
	}
	
}
