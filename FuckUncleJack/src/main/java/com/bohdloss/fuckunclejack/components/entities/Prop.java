package com.bohdloss.fuckunclejack.components.entities;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.render.CMath;

public class Prop extends Entity {

	public Prop(String model, String texture, float width, float height, boolean collision, boolean physics) {
		super(model, texture, 1);
		this.width=width;
		this.height=height;
		this.collision=collision;
		this.physics=physics;
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
		return 4;
	}

}
