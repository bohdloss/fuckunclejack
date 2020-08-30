package com.bohdloss.fuckunclejack.components.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.entities.ItemDropEntity;
import com.bohdloss.fuckunclejack.components.items.blocks.BedrockBlockItem;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;

public class BedrockBlock extends Block{

	public BedrockBlock(Chunk chunk, int chunkx, int y) {
		super(chunk, chunkx, y, "bedrock");
		this.setUnbreakable();
	}

	@Override
	public Item[] generateDrop() {
		Item[] i = {new BedrockBlockItem(1)};
		return i;
	}

	@Override
	public void breakBlock() {
		
		dropItems();
		
	}

	@Override
	public int getId() {
		return 1;
	}

}
