package com.bohdloss.fuckunclejack.generator.generators;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.World;
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
		ColumnGenerator biome=gen[0];
		Chunk res = new Chunk(world, offsetx);
		int lastTree=0;
		Random height = new Random(seed+offsetx*16);
		Random freq = new Random(seed+offsetx*16);
		
		biome=gen[0];
		biome.spawn=false;
		
		boolean forLerp=false;
		boolean backLerp=false;
		
		for(int x=0;x<16;x++) {
			
			biome.heightPhase(noise, x, res);
			biome.cavePhase(noise, x, res);
			lastTree=biome.threePhase(noise, x, lastTree, height, freq, res);
		}
		
		biome.spawn=false;
		
		return res;
	}
	
}
