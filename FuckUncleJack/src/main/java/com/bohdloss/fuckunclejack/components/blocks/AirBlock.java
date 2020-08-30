package com.bohdloss.fuckunclejack.components.blocks;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Shader;

public class AirBlock extends Block {

	public AirBlock(Chunk chunk, int chunkx, int y) {
		super(chunk, chunkx, y, "");
		hasCollision=false;
		replaceable=true;
		opaque=true;
		setUnbreakable();
	}

	@Override
	public void render(Shader s, Matrix4f matrix, int index) {
	}

	@Override
	public Item[] generateDrop() {
		return new Item[0];
	}

	@Override
	public void breakBlock() {
		
	}

	@Override
	public int getId() {
		return 0;
	}
	
	
	
}
