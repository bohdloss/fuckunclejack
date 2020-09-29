package com.bohdloss.fuckunclejack.components.blocks;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.items.blocks.BedrockBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.CactusBlockItem;
import com.bohdloss.fuckunclejack.components.items.blocks.DirtBlockItem;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;

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
	public void tick(float delta) {
		super.tick(delta);
		if(GameState.isClient.getValue()) return;
		boolean airBackground = chunk.blocks[chunkx][y-1].getBackground() instanceof AirBlock;
		boolean airTop = chunk.blocks[chunkx][y-1].getTop() instanceof AirBlock;
		
		if(airTop&airBackground) {
			chunk.destroyBlock(GameEvent.tickDestroy, null, worldx, y, this.isBackground(), true);
		}
	}
	
	@Override
	public int getId() {
		return 8;
	}
	
}
