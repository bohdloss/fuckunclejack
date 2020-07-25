package com.bohdloss.fuckunclejack.components.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.items.blocks.BedrockBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.GrassBlockItem;

public class GrassBlock extends Block{

	public GrassBlock(Chunk chunk, int chunkx, int y) {
		super(chunk, chunkx, y, "grass");
	}

	@Override
	public Item[] generateDrop() {
		Item[] i = {new GrassBlockItem(1)};
		return i;
	}

	@Override
	public void breakBlock() {
		dropItems();
	}

	@Override
	public int getId() {
		return 3;
	}
	
}
