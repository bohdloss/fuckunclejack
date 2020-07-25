package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.DirtBlock;
import com.bohdloss.fuckunclejack.components.blocks.GrassBlock;
import com.bohdloss.fuckunclejack.components.blocks.SandBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class CactusBlockItem extends BlockItem {

	public CactusBlockItem(int amount) {
		super(amount, "cactus");
	}

	@Override
	public int getId() {
		return 8;
	}

}
