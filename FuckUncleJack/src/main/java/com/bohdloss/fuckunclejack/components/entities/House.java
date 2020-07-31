package com.bohdloss.fuckunclejack.components.entities;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.PlayerJoinEvent;
import com.bohdloss.fuckunclejack.render.CMath;

public abstract class House extends Entity{

public static int dimID=0;

	public House(String model, String texture, int width, int height) {
		super(model, texture, 1);
		this.width=width;
		this.height=height;
		updateBounds();
		collision=true;
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
	
	public void enterHouse(Player p) {
		if(GameState.isClient.getValue()) {
			
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		
		
		
	}

}
