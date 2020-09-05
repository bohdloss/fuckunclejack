package com.bohdloss.fuckunclejack.guicomponents;

import com.bohdloss.fuckunclejack.render.CMath;

public abstract class AnimationPacket {
	
protected String name;
	
public boolean ended=false;
public boolean started=false;
public boolean loop=false;
public long start=0;
public long duration;
public AnimationSystem owner;

protected float x,y,xscale,yscale,rot;

	public AnimationPacket(String name) {
		this.name=name;
	}
	
	public AnimationPacket start() {
		ended=false;
		started=true;
		start=System.currentTimeMillis();
		return this;
	}
	
	public String getName() {
		return name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getXscale() {
		return xscale;
	}

	public float getYscale() {
		return yscale;
	}

	public float getRot() {
		return rot;
	}

	public long delta() {
		if(ended) return duration;
		return System.currentTimeMillis()-start;
	}
	
	public AnimationPacket reset() {
		ended=false;
		started=false;
		start=0;
		return this;
	}
	
	public float getPercent() {
		if(!started) return 0;
		if(ended) return 1;
		return (float)CMath.clamp((float)((float)delta()/(float)duration), 0, 1);
	}
	
	public abstract AnimationPacket calc();
	
}
