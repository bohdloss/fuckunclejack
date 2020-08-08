package com.bohdloss.fuckunclejack.components.entities;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.render.CMath;

public class Table extends Entity {

public byte variant;
	
	public Table(boolean collision, byte variant) {
		super("table", "table"+(variant&0xff), 1);
		width=3.25f;
		height=1.3f;
		blockCollision=collision;
		this.variant=variant;
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

	public int getId() {
		return 3;
	}
	
}
