package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.DirtBlock;
import com.bohdloss.fuckunclejack.components.blocks.LogBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class LogBlockItem extends BlockItem{

	public LogBlockItem(int amount) {
		super(amount, "wood");
	}

	@Override
	public int getId() {
		return 4;
	}

}
