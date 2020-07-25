package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.AirBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class AirBlockItem extends BlockItem {

	public AirBlockItem(int amount) {
		super(amount, "");
	}

	@Override
	public int getId() {
		return 0;
	}

}
