package com.bohdloss.fuckunclejack.generator;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.blocks.*;
import com.bohdloss.fuckunclejack.components.entities.DesertHouse;

public class Desert extends ColumnGenerator{
	
	public Desert(long seed, WorldGenerator gen) {
		super(seed, gen);
	}

	@Override
	public int heightPhase(OpenSimplexNoise noise, int x, Chunk c) {
		
		int height=height(noise, x, c);
		
			for(int y=0;y<255;y++) {
				
				height=height(noise, x, c);
				
				if(y<height) {
					c.blocks[x][y].setTop(new SandstoneBlock(c, x, y));
				}
				if(y==height) {
					c.blocks[x][y].setTop(new SandBlock(c, x, y));
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
	public int threePhase(OpenSimplexNoise noise, int x, int lastTree, Random heightr, Random frequency, Chunk c) {
			boolean spawn = frequency.nextBoolean();
			lastTree++;
			if(lastTree<4) {
				return lastTree;
			}
			int h = height(noise, x, c);
			int height = heightr.nextInt(3)+1;
			for(int y=0;y<255;y++) {
				if(y>h&y<=h+height) {
					if(spawn) {
						lastTree=0;
						c.blocks[x][y].setBackground(new CactusBlock(c, x, y));
					}
				}
			}
			return lastTree;
	}

	@Override
	public int height(OpenSimplexNoise noise, int x, Chunk c) {
		int i = roundNoise(noise(noise, x+16*c.getOffsetx()), spawn?1d:5d, 60);
		return i;
	}

	@Override
	public void genStructure(OpenSimplexNoise noise, Chunk c) {
		DesertHouse h = new DesertHouse();
		c.getWorld().join(h, 7+c.getOffsetx()*16, height(noise, 7, c)+4.5f);
	}

}
