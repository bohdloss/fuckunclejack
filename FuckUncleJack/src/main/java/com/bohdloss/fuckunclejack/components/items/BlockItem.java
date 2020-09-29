package com.bohdloss.fuckunclejack.components.items;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemEventProperties;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.render.CMath;

public abstract class BlockItem extends Item{
	
	public BlockItem(int amount, String texture) {
		super(0, 0, amount, texture);
	}
	
	public Block generateInstance(int x, int y, World w) {
		return FunctionUtils.genBlockById(getId(), w, x, y);
	}
	
	@Override
	public ItemEventProperties onRightClickBegin(float x, float y, Entity entity) {
		int floorx = CMath.fastFloor(x);
		int floory = CMath.fastFloor(y);
		Entity executor=owner.owner.owner;
		Block b = executor.getWorld().getBlock(floorx, floory);
		if(b==null) return properties();
		if(available) {
			boolean placed = executor.getWorld().placeBlock(GameEvent.invPlace, executor, floorx, floory, generateInstance(floorx, floory, executor.getWorld()), false, true);
			if(placed) {
			decrease(1);
			}
		}
		return super.onRightClickBegin(x, y, entity);
	}

	@Override
	public void tick(float delta) {
		
	}
	
}
