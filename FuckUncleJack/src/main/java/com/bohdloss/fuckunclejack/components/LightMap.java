package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.List;
import static com.bohdloss.fuckunclejack.render.CMath.*;
import org.joml.Vector3i;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.CMath;

public class LightMap {

public int[][] values = new int[16][256];

private List<Vector3i> sources = new ArrayList<Vector3i>();
	
public Chunk origin;

private int x;
private int y;

public LightMap(Chunk origin) {
	this.origin=origin;
}

public void calculate() {
	calcSources();
	
	outer:
	for(x=0;x<16;x++) {
		for(y=0;y<256;y++) {
			if (sources.size()==0) break outer;
			if(CMath.distance2((double)x+origin.getOffsetx()*16, (double)y, ClientState.lPlayer.getX(), ClientState.lPlayer.getY())>ClientState.drawDistance) continue;
			
			origin.world.chunks.forEach((k,v)->{
			
			Object[] s=v.lightmap.sources.toArray();
			float result=0;
			for(int i=0;i<s.length;i++) {
				Vector3i cur =(Vector3i)s[i];
				double first = limit((float)distance(x+origin.getOffsetx()*16, y, cur.x, cur.y)/cur.z, 0, 1);
				double second = lerp((-first)+1, 0, 20);
				result=limit((float)(result+second), 0f, (float)19);
			}
			values[x][y]=fastFloor(result);
			
			});
			
		}
	}
	calcSunlight();
}

private void calcSources() {
	sources.clear();
}

private void calcSunlight() {
	BlockLayer[][] layers = origin.blocks;
	for(int x=0;x<16;x++) {
		boolean lerp=false;
		int lerpstart=0;
		for(int y=255;y>=0;y--) {
			if(!lerp) {
			if(layers[x][y].getTop().isOpaque()) {
				values[x][y]=19;
			} else {
				lerp=true;
				lerpstart=y;
			}
			}
			if(lerp){
				double calc=reverseLerp(y, lerpstart, lerpstart-5);
				double calc2=lerp(calc, 19, 0);
				//if(calc2!=0)System.out.println(y);
				values[x][y]=(int)limit(fastFloor(calc2), 0, 19);
			}
		}
		
	}
}

}
