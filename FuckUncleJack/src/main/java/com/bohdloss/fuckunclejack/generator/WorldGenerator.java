package com.bohdloss.fuckunclejack.generator;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.render.CMath;

import static com.bohdloss.fuckunclejack.render.CMath.*;

public class WorldGenerator {

//TODO rewrite literally everything here
	
protected OpenSimplexNoise noise;
protected World world;
protected long seed;
	
protected ColumnGenerator[] gen = {new Empty(seed, this), new Desert(seed, this), new Hills(seed, this)};
protected int[] biomes = new Random(seed).ints(1000, 0, gen.length-1).toArray();


	public double difference(double a, double b) {
		return Math.abs(a-b);
	}
	
	public int intdiff(int a, int b) {
		return (int)Math.abs(a-b);
	}
	
	public WorldGenerator(World world, long seed) {
		this.world=world;
		this.seed=seed;
		noise=new OpenSimplexNoise(seed);
	}
	
	public double noise(int target) {
		return noise.eval((double)target/10d, 0)*0.5d;
	}

	public Chunk generateChunk(int offsetx) {
		ColumnGenerator biome=gen[0];
		Chunk res = new Chunk(world, offsetx);
		int lastTree=0;
		Random height = new Random(seed+offsetx*16);
		Random freq = new Random(seed+offsetx*16);
		
		boolean spawn=CMath.random(new Random(), 2);
		
		int b = biome(res.getOffsetx());
		biome=gen[b];
		biome.spawn=spawn;
		
		boolean forLerp=false;
		boolean backLerp=false;
		
		if(biome(res.getOffsetx()+1)!=b) forLerp=true;
		if(biome(res.getOffsetx()-1)!=b) backLerp=true;
		
		for(int x=0;x<16;x++) {
			
			if(x<=3&backLerp) {
				
			}
			
			biome.heightPhase(noise, x, res);
			biome.cavePhase(noise, x, res);
			lastTree=biome.threePhase(noise, x, lastTree, height, freq, res);
		}
		
		if(spawn) {
			biome.genStructure(noise, res);
		}
		
		biome.spawn=false;
		
		return res;
	}
	
	public int biome(int offset) {
		try {
		return biomes[biomes.length/2+offset/4]+1;
		} catch(Exception e) {
			return 0;
		}
	}
	
	public double noise(OpenSimplexNoise noise, int target) {
		return noise.eval((double)target/10d, 0)*0.5d;
	}
	
	public int roundNoise(double in, double multiplier, double adder) {
		return (int)(multiplier*(in)+adder);
	}
	
}
