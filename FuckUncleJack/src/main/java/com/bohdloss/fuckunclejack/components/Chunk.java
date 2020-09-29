package com.bohdloss.fuckunclejack.components;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.blocks.AirBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.BlockDestroyedEvent;
import com.bohdloss.fuckunclejack.logic.events.BlockPlacedEvent;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Shader;

public class Chunk implements Tickable{
	
public BlockLayer[][] blocks = new BlockLayer[16][256];
protected int offsetx;
protected World world;

public LightMap lightmap;

public Chunk(World world, int offsetx) {
	this.world=world;
	this.offsetx=offsetx;
	for(int x=0;x<16;x++) {
		for(int y=0;y<256;y++) {
			blocks[x][y]=new BlockLayer(new AirBlock(this, x, y), new AirBlock(this, x, y));
		}
	}
	lightmap=new LightMap(this);
}

public World getWorld() {
	return world;
}

public int getOffsetx() {
	return offsetx;
}

public void setOffsetx(int x) {
	offsetx=x;
}


public void render(Shader s, Matrix4f matrix) {

	if(getWorld().needsLightmap) lightmap.calculate();
	
	for(int x=0;x<16;x++) {
		for(int y=0;y<256;y++) {
			if(CMath.distance2((double)x+offsetx*16, (double)y, ClientState.lPlayer.getX(), ClientState.lPlayer.getY())>ClientState.drawDistance) continue;
			blocks[x][y].render(s, matrix);
		}
	}
}

public void tick(float delta) {
	for(int x=0;x<16;x++) {
		for(int y=0;y<256;y++) {
			blocks[x][y].tick(delta);
		}
	}
}

public boolean destroyBlock(byte cause, Entity issuer, int x, int y, boolean background, boolean send) {
	try {
		
		BlockLayer b = blocks[x-(offsetx)*16][y];
		Block air = new AirBlock(this, x-(offsetx*16), y);
		
		//NOTICE: Call to event handler: BLOCK DESROY
		
		if(EventHandler.blockDestroyed(send, new BlockDestroyedEvent(cause, issuer, background?b.getBackground():b.getTop(), air)).isCancelled()) return false;
		
		//END
		
		if(background) {
			b.getBackground().breakBlock();
			b.setBackground(air);
		} else {
			b.getTop().breakBlock();
			b.setTop(air);
		}
		return true;
	} catch(Exception e) {
		e.printStackTrace();
	}
	return false;
}

public boolean placeBlock(byte cause, Entity issuer, int x, int y, Block place, boolean background, boolean send) {
	try {
		
		BlockLayer b = blocks[x-(offsetx)*16][y];
		
		//NOTICE: Call to event handler: BLOCK PLACE
		
		if(EventHandler.blockPlaced(send, new BlockPlacedEvent(cause, issuer, background?b.getBackground():b.getTop(), place)).isCancelled()) return false;
		
		//END
		
		if(background) {
			b.setBackground(place);
		} else {
			b.setTop(place);
		}
		return true;
	} catch(Exception e) {
		e.printStackTrace();
	}
	return false;
}

public Block getBlock(int x, int y) {
	return blocks[x-(offsetx)*16][y].getTop();
}

}
