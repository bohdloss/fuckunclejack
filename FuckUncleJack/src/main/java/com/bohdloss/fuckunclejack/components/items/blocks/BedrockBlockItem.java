package com.bohdloss.fuckunclejack.components.items.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.BedrockBlock;
import com.bohdloss.fuckunclejack.components.blocks.DirtBlock;
import com.bohdloss.fuckunclejack.components.items.BlockItem;

public class BedrockBlockItem extends BlockItem {

	public BedrockBlockItem(int amount) {
		super(amount, "bedrock");
	}

	@Override
	public int getId() {
		return 1;
	}

}
