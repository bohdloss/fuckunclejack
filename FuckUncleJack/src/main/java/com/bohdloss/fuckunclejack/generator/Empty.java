package com.bohdloss.fuckunclejack.generator;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.blocks.StoneBlock;

public class Empty extends ColumnGenerator {

	public Empty(long seed, WorldGenerator gen) {
		super(seed, gen);
	}

	@Override
	public int heightPhase(OpenSimplexNoise noise, int x, Chunk c) {
		int height=height(noise, x, c);
		for(int y=0;y<256;y++) {
			if(y<=height) {
				c.blocks[x][y].setTop(new StoneBlock(c, x, y));
			}
		}
		return height;
	}

	@Override
	public void cavePhase(OpenSimplexNoise noise, int x, Chunk c) {
		
	}
	
	@Override
	public int threePhase(OpenSimplexNoise noise, int x, int lasttree, Random height, Random freq, Chunk c) {
		
		return 0;
	}

	@Override
	public int height(OpenSimplexNoise noise, int x, Chunk c) {
		return 2;
	}

	@Override
	public void genStructure(OpenSimplexNoise noise, Chunk c) {
		
	}

}
