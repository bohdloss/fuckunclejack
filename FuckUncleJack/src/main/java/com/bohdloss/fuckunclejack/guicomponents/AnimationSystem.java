package com.bohdloss.fuckunclejack.guicomponents;

import java.util.HashMap;

public class AnimationSystem {

public static HashMap<String, AnimationSystem> systems = new HashMap<String, AnimationSystem>();
	
public HashMap<String, AnimationPacket> animations = new HashMap<String, AnimationPacket>();
	
protected String name;
	public AnimationSystem(String name, AnimationPacket...packets) {
		this.name=name;
		systems.put(name, this);
		for(int i=0;i<packets.length;i++) {
			animations.put(packets[i].name, packets[i]);
			packets[i].owner=this;
		}
	}
	
	public void start() {
		animations.forEach((k,v)->v.start());
	}
	
	public void reset() {
		animations.forEach((k,v)->v.reset());
	}
	
private boolean found;

	public boolean over() {
		found=false;
		animations.forEach((k,v)->{
			//System.out.println(k+": "+v.ended);
			if(!v.ended) {
				found=true;
			}
		});
		//System.out.println(!found);
		return !found;
	}
	
}
