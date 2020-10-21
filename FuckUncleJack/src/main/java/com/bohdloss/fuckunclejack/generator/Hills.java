package com.bohdloss.fuckunclejack.generator;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.blocks.*;

public class Hills extends ColumnGenerator{

	
	
	public Hills(long seed, WorldGenerator gen) {
		super(seed, gen);
	}

	@Override
	public int heightPhase(OpenSimplexNoise noise, int x, Chunk c) {
		int height=0;
			for(int y=0;y<100;y++) {
				height=height(noise, x, c);
				if(y<height) {
					c.blocks[x][y].setTop(new DirtBlock(c, x, y));
				}
				if(y==height) {
					c.blocks[x][y].setTop(new GrassBlock(c, x, y));
				}
				if(y<=height-5) {
					c.blocks[x][y].setTop(new StoneBlock(c, x, y));
					c.blocks[x][y].setBackground(new StoneBlock(c, x, y));
				}
				if(y==0) {
					c.blocks[x][y].setTop(new BedrockBlock(c, x, y));
				}
				
			}
			return height;
	}

	@Override
	public int threePhase(OpenSimplexNoise noise, int x, int lastTree, Random use, Random less, Chunk c) {
		return 0;
	}

	@Override
	public int height(OpenSimplexNoise noise, int x, Chunk c) {
		return roundNoise(noise(noise, x+16*c.getOffsetx()), 10d, 60);
	}

	@Override
	public void genStructure(OpenSimplexNoise noise, Chunk c) {
		
	}

}
