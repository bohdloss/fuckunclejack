package com.bohdloss.fuckunclejack.server;

import java.util.ConcurrentModificationException;
import java.util.Random;

import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;

import static com.bohdloss.fuckunclejack.logic.GameState.*;

public class TickThread extends Thread {

public long lastTime;
	
	public TickThread() {
		dimensions.put("world", new OverworldWorld(new Random().nextLong(), "world"));
		start();
	}

	public void run() {
		while(true) {
			
			long current = System.currentTimeMillis();
			
			if(lastTime==0) lastTime = current;
			
			long time = System.currentTimeMillis();
			
			long pre = time-lastTime;
			float delta = (float)pre;
			
			lastTime = current;
			
			try {
				update(delta);
				sleep(3);
			} catch(Exception e) {
				if(!(e instanceof ConcurrentModificationException)) {
				e.printStackTrace();
				}
			}
		}
	}
	
	public void update(float delta) {
		dimensions.forEach((k,v)->{
			if(v.player.size()>0) {
			v.tick(delta);
			}
		});
	}
	
}
