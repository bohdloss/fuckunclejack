package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.List;
import static com.bohdloss.fuckunclejack.render.CMath.*;
import org.joml.Vector3i;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;

public class MetaData {

public float[][] values = new float[16][100];
public int[][] sides = new int[16][100];

private List<Vector3i> sources = new ArrayList<Vector3i>();
	
public Chunk origin;

private int x;
private int y;

private static final byte B0=(byte)0;
private static final byte B1=(byte)1;

//cache
private Block[] surrounding=new Block[9];
//

public MetaData(Chunk origin) {
	this.origin=origin;
}

public float get(int x, int y) {
	if(y<0||y>=100) return 0;
	boolean minus = x<0;
	if(!minus&&x<16) {
		return values[x][y];
	} else {
		Chunk other = origin.getWorld().cachedChunks.get(origin.offsetx + (minus ? -1 : 1));
		if(other!=null) {
			return other.lightmap.get(minus ? x+16 : x-16, y);
		}
		return 0;
	}
}

public void lights() {
	calcSources();
	
	outer:
	for(x=0;x<16;x++) {
		for(y=0;y<100;y++) {
			if (sources.size()==0) break outer;
			if(CMath.distance2((double)x+origin.getOffsetx()*16, (double)y, ClientState.lPlayer.getX(), ClientState.lPlayer.getY())>ClientState.xdrawDistance) continue;
			
			origin.world.chunks.forEach((k,v)->{
			
			Object[] s=v.lightmap.sources.toArray();
			float result=0;
			for(int i=0;i<s.length;i++) {
				Vector3i cur =(Vector3i)s[i];
				double first = clamp((float)distance(x+origin.getOffsetx()*16, y, cur.x, cur.y)/cur.z, 0, 1);
				double second = lerp((-first)+1, 0, 20);
				result=(float)clamp((float)(result+second), 0f, (float)19);
			}
			values[x][y]=fastFloor(result);
			
			});
			
		}
	}
	calcSunlight();
}

public void calculate() {
	lights();
	borders();
}

private boolean getBlocks(int xorig, int yorig) {
	yorig = (int)clamp(yorig, 0, 99);
	
	//Left column
	
	if(xorig==0) {
		Chunk prev = origin.getWorld().cachedChunks.get(origin.offsetx-1);
		if(prev==null) return false;
		
		surrounding[0] = prev.blocks[15][(int)clamp(yorig+1, 0, 99)].getTop();
		surrounding[1] = prev.blocks[15][(int)clamp(yorig, 0, 99)].getTop();
		surrounding[2] = prev.blocks[15][(int)clamp(yorig-1, 0, 99)].getTop();
		
	} else {
		
		surrounding[0] = origin.blocks[xorig-1][(int)clamp(yorig+1, 0, 99)].getTop();
		surrounding[1] = origin.blocks[xorig-1][(int)clamp(yorig, 0, 99)].getTop();
		surrounding[2] = origin.blocks[xorig-1][(int)clamp(yorig-1, 0, 99)].getTop();
		
	}
	
	//Middle column
	
	{
	
		surrounding[3] = origin.blocks[xorig][(int)clamp(yorig+1, 0, 99)].getTop();
		surrounding[4] = origin.blocks[xorig][(int)clamp(yorig, 0, 99)].getTop();
		surrounding[5] = origin.blocks[xorig][(int)clamp(yorig-1, 0, 99)].getTop();
	
	}
		
	//Right column
	
	if(xorig==15) {		
			
		Chunk next = origin.getWorld().cachedChunks.get(origin.offsetx+1);
		if(next==null) return false;
		
		surrounding[6] = next.blocks[0][(int)clamp(yorig+1, 0, 99)].getTop();
		surrounding[7] = next.blocks[0][(int)clamp(yorig, 0, 99)].getTop();
		surrounding[8] = next.blocks[0][(int)clamp(yorig-1, 0, 99)].getTop();
			
	} else {
		
		surrounding[6] = origin.blocks[xorig+1][(int)clamp(yorig+1, 0, 99)].getTop();
		surrounding[7] = origin.blocks[xorig+1][(int)clamp(yorig, 0, 99)].getTop();
		surrounding[8] = origin.blocks[xorig+1][(int)clamp(yorig-1, 0, 99)].getTop();
		
	}
	
	return true;
}

public void borders() {
	for(int x=0;x<16;x++) {
		for(int y=0;y<100;y++) {
			if(!getBlocks(x,y)) continue;
			
			Block cur = surrounding[4];
			
			//If it is opaque it doesn't need a
			//border. Air is an example
			
			if(cur.isOpaque()) {
				sides[x][y]=BlockTexture.NOSIDES;
				continue;
			}
			
			//check the sides where a border is
			//needed individually
			
			boolean top = surrounding[3].opaque;
			boolean bottom = surrounding[5].opaque;
			boolean left = surrounding[1].opaque;
			boolean right = surrounding[7].opaque;
			
			//Actual side calculation
			
			byte btop = top?B1:B0, bbottom = bottom?B1:B0, bleft = left?B1:B0, bright = right?B1:B0;
			int out = ((btop & 0xFF) << 24) | ((bbottom & 0xFF) << 16) | ((bright & 0xFF) << 8 ) | ((bleft & 0xFF) << 0 );
			sides[x][y]=out;
			
		}
	}
}

private void calcSources() {
	sources.clear();
}

private void calcSunlight() {
	BlockLayer[][] layers = origin.blocks;
	for(int x=0;x<16;x++) {
		boolean lerp=false;
		int lerpstart=0;
		for(int y=99;y>=0;y--) {
			if(!lerp) {
			if(layers[x][y].getTop().isOpaque()) {
				values[x][y]=1;
			} else {
				lerp=true;
				lerpstart=y;
			}
			}
			if(lerp){
				double remap = reverseLerp(y, lerpstart-3, lerpstart);
				values[x][y]=(float)remap;
			}
		}
		
	}
}

}
