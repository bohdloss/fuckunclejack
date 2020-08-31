package com.bohdloss.fuckunclejack.input;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Point;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWScrollCallback;

import static com.bohdloss.fuckunclejack.main.Game.*;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;

import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Point2f;

public class InputManager extends Thread{

public static boolean[] keys = new boolean[GLFW_KEY_LAST];
public static boolean[] button = new boolean[GLFW_MOUSE_BUTTON_LAST];

public static Listener listener = new Listener();

//faster GC
private Point2f pos;
public double blockx;
public double blocky;
public float visiblex;
public float visibley;
//end

static {
	for(int i=0;i<keys.length;i++) {
		keys[i]=false;
	}
	for(int i=0;i<button.length;i++) {
		button[i]=false;
	}
}

public static boolean isKeyDown(int code) {
	return glfwGetKey(Game.window.getWindow(), code)==1;
}

public static boolean isMouseButtonDown(int code) {
	return glfwGetMouseButton(Game.window.getWindow(), code)==1;
}

public void gameUpdate() {
	baseUpdate();
	
	hoverCalc(scaleAmount);
	
	if(!ClientState.locked) {
	
	if(keys[GLFW_KEY_SPACE]) {
		ClientState.lPlayer.jump();
	}
	boolean moved=false;
	if(keys[GLFW_KEY_A]) {
		ClientState.lPlayer.moveLateral(-1);
		moved=true;
	}
	if(keys[GLFW_KEY_D]) {
		ClientState.lPlayer.moveLateral(1);
		moved=true;
	}
	if(keys[GLFW_KEY_W]) {
		ClientState.lPlayer.moveVertical(1);
	}
	if(keys[GLFW_KEY_S]) {
		ClientState.lPlayer.moveVertical(-1);
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
	
	}
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
	listener.init();
	while(true) {
		try {
			long time = System.currentTimeMillis();
			if(lWorld!=null&lPlayer!=null) {
			gameUpdate();
			}
			delay(time+GameState.tickDelay);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

public void hoverCalc(float scale) {
	pos = CMath.mGLCoord(Game.scaleAmount);
	
	blockx=CMath.fastFloor(pos.x+camera.getPosition().x/Game.scaleAmount+0.5f);
	blocky=CMath.fastFloor(pos.y+camera.getPosition().y/Game.scaleAmount+0.5f);
	
	ClientState.hover((int)blockx, (int)blocky);
}

public void baseUpdate() {
	for(int i=0;i<keys.length;i++) {
		boolean down = isKeyDown(i);
		if(keys[i]!=down) {
			keys[i]=down;
			if(down) listener.onKeyPressed(i);
			if(!down) listener.onKeyReleased(i);
		}
	}
	for(int i=0;i<button.length;i++) {
		boolean down = isMouseButtonDown(i);
		if(button[i]!=down) {
			button[i]=down;
			if(down) listener.onMouseButtonPressed(i);
			if(!down) listener.onMouseButtonReleased(i);
		}
	}
	if(ClientState.lPlayer!=null&camera!=null) {
		camera.setX(Game.scaleAmount*ClientState.lPlayer.x);
		camera.setY(Game.scaleAmount*ClientState.lPlayer.y);
	}
	nearHouseB=false;
	if(lWorld!=null) ClientState.lWorld.tick();
	if(nearHouseB==true) {
		nearHouse=lastHouse;
	} else {
		nearHouse=null;
		
	}
}

}
