package com.bohdloss.fuckunclejack.components;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.bohdloss.fuckunclejack.components.entities.ItemDropEntity;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.EntitySpawnedEvent;
import com.bohdloss.fuckunclejack.logic.events.ItemDroppedEvent;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public abstract class Block implements Tickable{

protected String texture;
protected int worldx;
protected int chunkx;
protected int y;
protected Chunk chunk;
protected boolean hasCollision=true;
protected CRectanglef bounds;
protected boolean replaceable=false;
protected boolean background;
protected boolean opaque;
protected float hardness;

private BlockTexture txt;
private Texture bg;
private Model model;
	
//GC
protected Matrix4f res=new Matrix4f();
protected Vector3f vector = new Vector3f(0,0,0);
//end

public Block(Chunk chunk, int chunkx, int y, String texture) {
	this.chunk=chunk;
	this.worldx=chunk.getOffsetx()*16+chunkx;
	this.chunkx=chunkx;
	this.y=y;
	this.hardness=1f;
	this.texture=texture;
	vector.x=worldx;
	vector.y=y;
	bounds = new CRectanglef(worldx-0.5f, y-0.5f, 1f, 1f);
	txt=Assets.blocks.get(texture);
	bg=Assets.textures.get("background_block");
	model=Assets.models.get("square");
}



public void render(Shader s, Matrix4f input, int index) {
	txt.txt[(int)CMath.clamp(background?index-5:index, 0, 19)].bind(0);
	model.render();
}

public CRectanglef getBounds() {
	return bounds;
}

public void tick() {
	
}

public String getTexture() {
	return texture;
}

public int getWorldx() {
	return worldx;
}

public int getChunkx() {
	return chunkx;
}

public int getY() {
	return y;
}

public boolean hasCollision() {
	if(background) return false;
	return hasCollision;
}

public boolean isReplaceable() {
	return replaceable;
}

public boolean isBackground() {
	return background;
}

public void setBackground(boolean background) {
	this.background=background;
}

public Chunk getChunk() {
	return chunk;
}

public boolean isOpaque() {
	return opaque;
}

public float getHardness() {
	return hardness;
}

public boolean isUnbreakable() {
	return hardness<0;
}

protected void setUnbreakable() {
	hardness=-1f;
}

public abstract void breakBlock();

public abstract Item[] generateDrop(); 

public void dropItems() {
	Item[] items = generateDrop();
	
	//NOTICE: Call to event handler: ITEM DROP / ENTITY SPAWN
	
	for(int i=0;i<items.length;i++) {
		
		//First check if i am supposed to drop an item
		
		boolean cancelled = EventHandler.itemDropped(!GameState.isClient.getValue(), new ItemDroppedEvent(GameEvent.blockDrop, this, items[i])).isCancelled();
	
		if(!cancelled) {
			
			ItemDropEntity drop = FunctionUtils.genItemEntity(items[i]);
			
			drop.x=worldx;
			drop.y=y;
			
			//Then check if i can actually spawn an entity in the world
			
			boolean cancelspawn = EventHandler.entitySpawned(!GameState.isClient.getValue(), new EntitySpawnedEvent(GameEvent.dropSpawn, drop, drop.getItem())).isCancelled();
			
			if(!cancelspawn) {
				
				//Join the drop entity into the world
				
				chunk.getWorld().join(drop, worldx, y);
				
			}
			
		}
	
	}
	
	//END
}



public abstract int getId();

}
