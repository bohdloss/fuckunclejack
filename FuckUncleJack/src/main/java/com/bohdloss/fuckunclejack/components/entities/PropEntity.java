package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class PropEntity extends Entity {

protected float xscale;
protected float yscale;
	
//cache
private Object[] data=new Object[7];	
static Model square;
String texture;
Texture txt;
//
	
static {
	square=Assets.models.get("square");
}

	public PropEntity(float xscale, float yscale, String texture, float width, float height, boolean collision, boolean physics) {
		super("", "", 1);
		this.width=width;
		this.height=height;
		this.collision=collision;
		this.physics=physics;
		this.xscale=xscale;
		this.yscale=yscale;
		invulnerable=true;
		this.texture=texture;
		txt=Assets.textures.get(texture);
		prioritizeRender=true;
		updateBounds();
	}

	@Override
	public int getId() {
		return 3;
	}

	public Object[] getData() {
		data[0]=xscale;
		data[1]=yscale;
		data[2]=texture;
		data[3]=width;
		data[4]=height;
		data[5]=collision;
		data[6]=physics;
		return data;
	}
	
	@Override
	public void tick(float delta) {
		super.tick(delta);
		
		//System.out.println("id: " + getUID() + " collision: "+collision+" physics: "+physics+" x: "+x+" y: "+y+" width: "+width+" height: "+height+" xscale: "+xscale+" yscale: "+yscale);
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		s.setUniform("red", red);
		
		res = matrix.translate(x, y, 0, res).scale(xscale, yscale, 1, res);
		s.setProjection(res);
		Assets.textures.get(texture).bind(0);
		square.render();
		
		s.setUniform("red", false);
		
		renderHitboxes(s, matrix);
	}
	
}
