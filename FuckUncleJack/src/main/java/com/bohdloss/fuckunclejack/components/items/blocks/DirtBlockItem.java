package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.AirBlock;
import com.bohdloss.fuckunclejack.components.blocks.DirtBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class DirtBlockItem extends BlockItem {

	public DirtBlockItem(int amount) {
		super(amount, "dirt");
	}

	@Override
	public int getId() {
		return 2;
	}

}
