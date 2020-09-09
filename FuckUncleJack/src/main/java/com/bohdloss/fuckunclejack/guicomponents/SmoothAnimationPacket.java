package com.bohdloss.fuckunclejack.guicomponents;

import org.joml.Vector4f;

import com.bohdloss.fuckunclejack.render.CMath;

public class SmoothAnimationPacket extends AnimationPacket {

public Vector4f start;
public Vector4f end;
	
	public SmoothAnimationPacket(String name, Vector4f start, Vector4f end, long duration) {
		super(name);
		this.start=start;
		this.end=end;
		this.duration=duration;
	}
	
	@Override
	public AnimationPacket calc() {
		if(ended&loop) start();
		float percent = getPercent();
		
		x=(float)CMath.lerpEaseBoth(percent, start.x, end.x);
		y=(float)CMath.lerpEaseBoth(percent, start.y, end.y);
		xscale=(float)CMath.lerpEaseBoth(percent, start.z, end.z);
		yscale=(float)CMath.lerpEaseBoth(percent, start.w, end.w);
		
		if(percent==1&!ended) {
			ended=true;
		}
		
		return this;
	}

}
