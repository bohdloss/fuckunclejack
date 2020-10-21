package com.bohdloss.fuckunclejack.input;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Point;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWScrollCallback;

import static com.bohdloss.fuckunclejack.main.Game.*;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;

import com.bohdloss.fuckunclejack.api.CharacterInfo;
import com.bohdloss.fuckunclejack.api.FUJApi;
import com.bohdloss.fuckunclejack.api.GamemodeInfo;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.Tickable;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.main.Main;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Point2f;
import com.bohdloss.fuckunclejack.server.Server;

public class InputManager extends Thread implements Tickable{

public static boolean[] keys = new boolean[GLFW_KEY_LAST];
public static boolean[] button = new boolean[GLFW_MOUSE_BUTTON_LAST];

public static Listener listener = new Listener();

//cache
public double blockx;
public double blocky;
public float visiblex;
public float visibley;

public long lastTime=0;
//end

static {
	for(int i=0;i<keys.length;i++) {
		keys[i]=false;
	}
	for(int i=0;i<button.length;i++) {
		button[i]=false;
	}
}

public void gameUpdate(float delta) {
	if(lPlayer==null||lWorld==null) return;
	
	hoverCalc(scaleAmount);
	
	if(ClientState.lPlayer!=null&camera!=null) {
		camera.setX(Game.scaleAmount*ClientState.lPlayer.x);
		camera.setY(Game.scaleAmount*ClientState.lPlayer.y);
	}
	
	nearHouseB=false;
	if(lWorld!=null) ClientState.lWorld.tick(delta);
	if(nearHouseB==true) {
		nearHouse=lastHouse;
	} else {
		nearHouse=null;
		
	}
	
	if(!ClientState.locked) {
	
	if(keys[GLFW_KEY_SPACE]) {
		ClientState.lPlayer.jump();
	}
	boolean moved=false;
	if(keys[GLFW_KEY_A]) {
		ClientState.lPlayer.moveLateral(-1, delta);
		moved=true;
	}
	if(keys[GLFW_KEY_D]) {
		ClientState.lPlayer.moveLateral(1, delta);
		moved=true;
	}
	if(keys[GLFW_KEY_W]) {
		ClientState.lPlayer.moveVertical(1, delta);
	}
	if(keys[GLFW_KEY_S]) {
		ClientState.lPlayer.moveVertical(-1, delta);
	}
	
	if(keys[GLFW_KEY_R]) {
		ClientState.lPlayer.x=0;
		ClientState.lPlayer.y=80f;
		ClientState.lPlayer.updateBounds();
	}
	
	if(keys[GLFW_KEY_LEFT_SHIFT]) {
		ClientState.lPlayer.setRunning(true);
	} else {
		ClientState.lPlayer.setRunning(false);
	}
	/*if(keys[GLFW_KEY_B]) {
		if(ClientState.lWorld!=null) {
			ClientState.lWorld.chunks.forEach((k,v)->{
				v.lightmap.calculate();
			});
		}
	}*/
	
	}
}

public void run() {
	listener.init();
	FUJApi.init();
	MenuTab.init();
	hud=new HUD();
	//ClientState.showMenu(false, true, "main");
	ClientState.connect(Main.ip, Server.port);
	while(true) {
		
		long current = System.currentTimeMillis();
		
		if(lastTime==0) lastTime = current;
		
		long time = System.currentTimeMillis();
		
		long pre = time-lastTime;
		float delta = (float)pre;
		
		lastTime = current;
		
		//System.out.println("LOG\ncurrent: "+current+"\nlastTime: "+lastTime+"\ntime: "+time+"\npre: "+pre+"\ndelta: "+delta);
		
		try {
			tick(delta);
			sleep(3);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

public void hoverCalc(float scale) {
	
	blockx=CMath.fastFloor(Game.mouse.x+camera.getPosition().x/Game.scaleAmount+0.5f);
	blocky=CMath.fastFloor(Game.mouse.y+camera.getPosition().y/Game.scaleAmount+0.5f);
	
	ClientState.hover((int)blockx, (int)blocky);
}

public void baseUpdate() {
	for(int i=0;i<keys.length;i++) {
		boolean down = Game.window.isKeyDown(i);
		if(keys[i]!=down) {
			keys[i]=down;
			if(down) listener.onKeyPressed(i);
			if(!down) listener.onKeyReleased(i);
		}
	}
	for(int i=0;i<button.length;i++) {
		boolean down = Game.window.isMouseButtonDown(i);
		if(button[i]!=down) {
			button[i]=down;
			if(down) listener.onMouseButtonPressed(i);
			if(!down) listener.onMouseButtonReleased(i);
		}
	}
	if(queueDisconnect) {
		queueDisconnect=false;
		disconnect();
	}
}

public void menuUpdate(float delta) {
	if(MenuTab.active!=null) {
		MenuTab.active.tick(delta);
	}
}

@Override
public void tick(float delta) {
	baseUpdate();
	switch(state) {
	case GAME:
		gameUpdate(delta);
	break;
	case EDITMODE:
		gameUpdate(delta);
	break;
	case MENU:
		menuUpdate(delta);
	break;
	}
}

}
