package com.bohdloss.fuckunclejack.guicomponents;

import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;

public class SmashButtonPacket extends AnimationPacket{

public float targetx;
public float targety;
public float targetxscale;
public float targetyscale;
	
	public SmashButtonPacket(String name, float targetx, float targety, float targetxscale, float targetyscale) {
		super(name);
		duration=300;
		this.targetx=targetx;
		this.targety=targety;
		this.targetxscale=targetxscale;
		this.targetyscale=targetyscale;
	}
	
	@Override
	public AnimationPacket calc() {
		if(ended&loop) start();
		float percent = getPercent();
		
		if(percent==0) {
			
			x=100;
			y=100;
			xscale=0;
			yscale=0;
			
		} else if(percent==1) {
			
			x=targetx;
			y=targety;
			xscale=targetxscale;
			yscale=targetyscale;
			
		} else {
		
			x=(float)CMath.lerpEaseIn(percent,0,targetx);
			y=(float)CMath.lerpEaseIn(percent,0,targety);
			xscale=(float)CMath.lerpEaseIn(percent,20,targetxscale);
			yscale=(float)CMath.lerpEaseIn(percent,20,targetyscale);
		
		}
		
		if(percent==1&!ended) {
			ended=true;
			Game.camera.shake(500, 30);
		}
		
		return this;
	}

	
	
}
