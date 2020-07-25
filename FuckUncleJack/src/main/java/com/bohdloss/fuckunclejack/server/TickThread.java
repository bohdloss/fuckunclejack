package com.bohdloss.fuckunclejack.server;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.World;
import static com.bohdloss.fuckunclejack.logic.GameState.*;

public class TickThread extends Thread {

	public TickThread() {
		dimensions.put("world", new World(new Random().nextLong(), "world"));
		start();
	}
	
	public void delay(long until) {
		while(System.currentTimeMillis()<until) {
			try {
				sleep(0);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		while(true) {
			try {
				long time = System.currentTimeMillis();
				update();
				
				delay(time+tickDelay);
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		dimensions.forEach((k,v)->v.tick());
	}
	
}
