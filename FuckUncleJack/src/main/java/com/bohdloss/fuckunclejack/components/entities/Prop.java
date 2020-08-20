package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class Prop extends Entity {

protected float xscale;
protected float yscale;
	
//cache
private Object[] data=new Object[7];	
static Model square;
Texture txt;
//
	
static {
	square=Assets.models.get("square");
}

	public Prop(float xscale, float yscale, String texture, float width, float height, boolean collision, boolean physics) {
		super("", "", 1);
		this.width=width;
		this.height=height;
		this.collision=collision;
		this.physics=physics;
		this.xscale=xscale;
		this.yscale=yscale;
		txt=Assets.textures.get(texture);
		updateBounds();
	}

	@Override
	protected Block[] getSurroundings() {
		Block[] blocks = new Block[100];
		int bx = CMath.fastFloor(x);
		int by = CMath.fastFloor(y);
		int ii=0;
		
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				try {
				blocks[ii]=world.getBlock(bx+i-5, by+j-5);
				} catch(Exception e) {}
				ii++;
			}
		}
		return blocks;
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
	public void render(Shader s, Matrix4f matrix) {
		res = matrix.translate(x, y, 0, res).scale(xscale, yscale, 1, res);
		s.setUniform("projection", res);
		txt.bind(0);
		square.render();
	}
	
}
