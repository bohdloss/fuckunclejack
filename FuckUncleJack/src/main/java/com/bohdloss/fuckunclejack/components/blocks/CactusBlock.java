package com.bohdloss.fuckunclejack.components.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.items.blocks.BedrockBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.CactusBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.DirtBlockItem;

public class CactusBlock extends Block{

	public CactusBlock(Chunk chunk, int chunkx, int y) {
		super(chunk, chunkx, y, "cactus");
	}

	@Override
	public Item[] generateDrop() {
		Item[] i = {new CactusBlockItem(1)};
		return i;
	}

	@Override
	public void breakBlock() {
		dropItems();
	}

	@Override
	public int getId() {
		return 8;
	}
	
}
