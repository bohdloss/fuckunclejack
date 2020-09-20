package com.bohdloss.fuckunclejack.guicomponents;

import java.util.Timer;
import java.util.TimerTask;

import com.bohdloss.fuckunclejack.menutabs.MainTab;
import com.bohdloss.fuckunclejack.render.CMath;

public class MainAnimationPacket extends AnimationPacket{

	public MainAnimationPacket(String name) {
		super(name);
		duration=1300;
	}
	
	@Override
	public AnimationPacket reset() {
		MainTab.buttonfade.reset();
		return super.reset();
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
		
		if(percent==1&(!ended)) {
			if(name.equals("left")) {
			if(MainTab.first) {
				if(!MainTab.buttonfade.animations.get("play").started) {
					MainTab.buttonfade.animations.get("play").start();
					Timer t = new Timer();
					TimerTask task = new TimerTask() {
						public void run() {
							MainTab.buttonfade.animations.get("settings").start();
						}
					};
					t.schedule(task, 500);
					TimerTask task2 = new TimerTask() {
						public void run() {
							MainTab.buttonfade.animations.get("exit").start();
						}
					};
					t.schedule(task2, 1000);
					TimerTask task3 = new TimerTask() {
						public void run() {
							MainTab.secondfade.animations.forEach((k,v)->{v.ended=true;v.started=true;});
							MainTab.buttonfade=MainTab.secondfade;
						}
					};
					t.schedule(task3, 1500);
					MainTab.first=false;
				}
			} else {
				MainTab.buttonfade.reset();
				MainTab.buttonfade.start();
			}
			}
			ended=true;
		}
		
		return this;
	}
	
}
