package com.bohdloss.fuckunclejack.generator;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.blocks.AirBlock;

public abstract class ColumnGenerator {

public boolean spawn=false;
	
public long seed;
public WorldGenerator gen;

public ColumnGenerator(long seed, WorldGenerator gen) {
	this.seed=seed;
	this.gen=gen;
}
	
	public abstract int heightPhase(OpenSimplexNoise noise, int x, Chunk c);
	
	public abstract int height(OpenSimplexNoise noise, int x, Chunk c);
	
	public void cavePhase(OpenSimplexNoise noise, int x, Chunk c) {
			for(int y=0;y<100;y++) {
				double divide=7d;
				int n = roundNoise(noise.eval((double)x/divide+(double)c.getOffsetx()*16d/divide, (double)y/divide), 10, 60);
/*					if(y>50){
					n=(int)CMath.lerp(CMath.reverseLerp(y, 50d, 55d), n, 60d);
					} else if(y<=10){
					n=(int)CMath.lerp(CMath.reverseLerp(y, 5d, 10d), n, n-5);
				} */
					if(n>=62&y<50&y>=10) {
						c.blocks[x][y].setTop(new AirBlock(c, x, y));
					}
			}
	}
	
	public abstract	int threePhase(OpenSimplexNoise noise, int x, int lasttree, Random height, Random freq, Chunk c);
	
	public double noise(OpenSimplexNoise noise, int target) {
		return noise.eval((double)target/10d, 0)*0.5d;
	}
	
	public int roundNoise(double in, double multiplier, double adder) {
		return (int)(multiplier*(in)+adder);
	}
	
	public abstract void genStructure(OpenSimplexNoise noise, Chunk c);
	
}
