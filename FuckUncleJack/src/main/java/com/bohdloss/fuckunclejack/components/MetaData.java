package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.List;
import static com.bohdloss.fuckunclejack.render.CMath.*;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;

import sun.misc.Cache;

public class MetaData {

public static float MINLIGHT=0.05f;
public static float MAXLIGHT=1;
	
public float[][] values = new float[16][100];
public int[][] sides = new int[16][100];

//X, Y, RANGE, INTENSITY
private static List<Vector4f> sources = new ArrayList<Vector4f>();

private static Vector4f cache1 = new Vector4f();
private static Vector4f cache2 = new Vector4f();	
private static Vector4f cache3 = new Vector4f();	

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

public static void calculateStatic() {
	calcSources();
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
	
	outer:
	for(x=0;x<16;x++) {
		inner:
		for(y=0;y<100;y++) {
			if (sources.size()==0) break outer;
				
			Object[] s=sources.toArray();
			float result=0;
			for(int i=0;i<s.length;i++) {
				Vector4f cur =(Vector4f)s[i];
				
				if(diff(cur.x, x+origin.offsetx*16)>cur.z) {
					continue;
				}
				if(diff(cur.y, y)>cur.z) {
					continue;
				}
				
				double dist = distance(cur.x, cur.y, x+origin.offsetx*16, y);
				
				double remap = remap(dist, 0, cur.z, cur.w, 0);
				
				result=result+(float)remap;
			}
			values[x][y]=(float)clamp(result, MINLIGHT, MAXLIGHT);
			
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

private static void calcSources() {
	sources.clear();
	cache1.x=ClientState.lPlayer.getX();
	cache1.y=ClientState.lPlayer.getY();
	cache1.z=4;
	cache1.w=1f;
	sources.add(cache1);
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
				values[x][y]=(float)max(values[x][y], remap);
			}
		}
		
	}
}

}
