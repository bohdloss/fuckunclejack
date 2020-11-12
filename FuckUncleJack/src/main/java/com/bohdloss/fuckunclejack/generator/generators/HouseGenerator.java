package com.bohdloss.fuckunclejack.generator.generators;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.BlockLayer;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.blocks.StoneBlock;
import com.bohdloss.fuckunclejack.generator.ColumnGenerator;
import com.bohdloss.fuckunclejack.generator.Empty;
import com.bohdloss.fuckunclejack.generator.WorldGenerator;
import com.bohdloss.fuckunclejack.render.CMath;

public class HouseGenerator extends WorldGenerator {

	public HouseGenerator(World world, long seed) {
		super(world, seed);
		gen=new ColumnGenerator[1];
		gen[0]=new Empty(seed, this);
		//initBiomes();
	}

	public Chunk generateChunk(int offsetx) {
		Chunk res = new Chunk(world, offsetx);
		
		BlockLayer[][] blocks = res.blocks;
		for(int i=0;i<16;i++) {
			blocks[i][45].setTop(new StoneBlock(res, i, 45));
		}
		
		return res;
	}
	
}
