package com.bohdloss.fuckunclejack.guicomponents;

import java.util.Timer;
import java.util.TimerTask;

import com.bohdloss.fuckunclejack.render.CMath;

import javafx.animation.Interpolator;

public class MainAnimationPacket extends AnimationPacket{

	public MainAnimationPacket(String name) {
		super(name);
		duration=1300;
	}
	
	@Override
	public AnimationPacket calc() {
		if(ended&loop) start();
		float percent = getPercent();
		
		if(name.equals("left")) {
			x=(float)CMath.lerpEaseOut(percent, -20, -8.5d);
			y=-3.1f;
			xscale=10;
			yscale=9;
			rot=0;
		} else if(name.equals("right")) {
			x=(float)CMath.lerpEaseOut(percent, 16, 9.5d);
			y=-3.1f;
			xscale=3.0f;
			yscale=9f;
			rot=-0;
		} else if(name.equals("title")) {
			x=0;
			y=(float)CMath.lerpEaseOut(percent, 12, 5.6);
			xscale=12;
			yscale=4;
			rot=0f;
		}
		
		if(percent==1) {
			if(!owner.animations.get("play").started) {
				owner.animations.get("play").start();
				Timer t = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						owner.animations.get("settings").start();
					}
				};
				t.schedule(task, 500);
			}
			ended=true;
		}
		
		return this;
	}
	
}
