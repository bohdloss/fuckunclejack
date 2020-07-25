package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.Shader;

public class DesertHouse extends House {

Matrix4f res = new Matrix4f();
Shader gui = Assets.shaders.get("gui");	

	public DesertHouse() {
		super("deserthouse", "", 10, 10);
	}

	@Override
	public void render(Shader s, Matrix4f matrix) {
		res = matrix.translate(x, y-0.5f, 0, res);
		gui.bind();
		gui.setUniform("projection", res);
		Animation a = Assets.animations.get("deserthouse");
		a.setSpeed(0.25f);
		a.bind();
		Assets.models.get(model).render();
		s.bind();
	}
	
	@Override
	public int getId() {
		return 2;
	}

}
