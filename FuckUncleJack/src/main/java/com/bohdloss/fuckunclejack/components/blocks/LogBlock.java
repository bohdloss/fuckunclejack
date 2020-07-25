package com.bohdloss.fuckunclejack.components.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.items.blocks.BedrockBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.LogBlockItem;

public class LogBlock extends Block{

	public LogBlock(Chunk chunk, int chunkx, int y) {
		super(chunk, chunkx, y, "wood");
	}

	@Override
	public Item[] generateDrop() {
		Item[] i = {new LogBlockItem(1)};
		return i;
	}

	@Override
	public void breakBlock() {
		dropItems();
	}

	@Override
	public int getId() {
		return 4;
	}

}
