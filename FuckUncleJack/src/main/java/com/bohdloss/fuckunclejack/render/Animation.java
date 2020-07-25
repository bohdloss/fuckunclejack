package com.bohdloss.fuckunclejack.render;

import com.bohdloss.fuckunclejack.main.Assets;

public class Animation {

protected TileSheet frames;
private long last=0;	
private Shader gui;
private static final long wait=250;
protected float speed;
protected int currentIndex=0;
protected boolean looping;

public Animation(TileSheet frames, boolean looping) {
	this.frames=frames;
	this.looping=looping;
	speed=1f;
	gui=Assets.shaders.get("gui");
}

public void setSpeed(float speed) {
	this.speed=speed;
}

public float getSpeed() {
	return speed;
}

public void clear() {
	last=System.currentTimeMillis();
	currentIndex=0;
}

private void calcIndex() {
	if(currentIndex==frames.getAmount()-1) {
		if(looping) {
		currentIndex=0;
		}
	} else {
		currentIndex++;
	}
}

public void bind() {
	float add=(wait*(1/speed));
	long res=last+(long)add;
	long time=System.currentTimeMillis();
	if(time>=res) {
		last=System.currentTimeMillis();
		calcIndex();
	}
	frames.bindTile(gui, currentIndex);
}

public boolean isLooping() {
	return looping;
}

public void setLooping(boolean looping) {
	this.looping = looping;
}

}
