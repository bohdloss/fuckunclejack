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
	public ItemEventProperties onRightClickBegin(int x, int y, Entity entity) {
		Entity executor=owner.owner.owner;
		Block b = executor.getWorld().getBlock(x, y);
		if(b==null) return defaultProperties();
		if(available) {
			boolean placed = executor.getWorld().placeBlock(GameEvent.invPlace, executor, x, y, generateInstance(x, y, executor.getWorld()), false, true);
			if(placed) {
			decrease(1);
			}
		}
		return defaultProperties();
	}

	@Override
	public ItemEventProperties onRightClickEnd(int x, int y, Entity entity) {
		return defaultProperties();
	}

	@Override
	public ItemEventProperties onLeftClickBegin(int x, int y, Entity entity) {
		return defaultProperties();
	}

	@Override
	public ItemEventProperties onLeftClickEnd(int x, int y, Entity entity) {
		return defaultProperties();
	}

}
