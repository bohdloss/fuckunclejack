package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.DirtBlock;
import com.bohdloss.fuckunclejack.components.blocks.StoneBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class StoneBlockItem extends BlockItem {

	public StoneBlockItem(int amount) {
		super(amount, "stone");
	}

	@Override
	public int getId() {
		return 5;
	}

}
