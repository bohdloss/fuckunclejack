package com.bohdloss.fuckunclejack.input;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import static com.bohdloss.fuckunclejack.main.Game.*;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Point;

import org.lwjgl.glfw.GLFWScrollCallback;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.events.ItemMovedEvent;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;

public class Listener implements KeyListen {
	
//GC
	
//end
	
	public void init() {
		glfwSetScrollCallback(window.getWindow(), new GLFWScrollCallback() {
		    @Override public void invoke (long win, double dx, double dy) {
		        lPlayer.getInventory().selected=(int)CMath.limit(sel()-(int)dy, 27, 35);
		        //Game.scaleAmount+=(int)dy;
		    }
		});
	}
	
	@Override
	public void onKeyPressed(int code) {
		
		switch(code) {
		case GLFW_KEY_F11:
			pendingToggle=true;
			break;
		case GLFW_KEY_T:
			lPlayer.x=32000;
			lPlayer.y=100;
			break;
		case GLFW_KEY_E:
			hud.toggleInventory();
			break;
		case GLFW_KEY_F:
			if(nearHouse!=null) nearHouse.enterHouse(lPlayer);
			break;
		case GLFW_KEY_P:
			Client.sendDebug=true;
			break;
		}
	}

	@Override
	public void onKeyReleased(int code) {
	}

	@Override
	public void onMouseButtonPressed(int code) {
		double blockx=hovx;
		double blocky=hovy;
		if(code==GLFW_MOUSE_BUTTON_1) {
			if(hud.isInvOpen()) {
				
				// [M1] Actions within inventory GUI in case it is open
				
				if(CMath.inrange(hud.invdisplay.hovIndex, 0f, 35f)) {
						if(!grabbing) {
							Item i = lPlayer.getInventory().slots[Game.hud.invdisplay.hovIndex].getContent();
							if(i!=null) {
							grabbing=true;
							grabSlot=Game.hud.invdisplay.hovIndex;
							grabbed=lPlayer.getInventory().slots[Game.hud.invdisplay.hovIndex].getContent();
							lPlayer.getInventory().slots[Game.hud.invdisplay.hovIndex].setContent(null);
							}
						} else {
							Item i = lPlayer.getInventory().slots[Game.hud.invdisplay.hovIndex].getContent();
							if(i==null) {
								grabbing=false;
								lPlayer.getInventory().slots[Game.hud.invdisplay.hovIndex].setContent(grabbed);
								grabbed=null;
								EventHandler.invItemMoved(true, new ItemMovedEvent(lPlayer, GameEvent.userInput, grabSlot, Game.hud.invdisplay.hovIndex));
							}
						}
				}
				
				//
				
			} else {
				
				// [M1] Actions within the game world
				
				Item i = lPlayer.getInventory().slots[sel()].getContent();
				if(i!=null) i.onLeftClickBegin((int)blockx, (int)blocky, null);
				lWorld.destroyBlock(GameEvent.handDestroy, lPlayer, (int)blockx, (int) blocky, false, true);
			
				//
			
			}
		} else if(code==GLFW_MOUSE_BUTTON_2) {
			if(hud.isInvOpen()) {
				
				// [M2] Actions withing inventory GUI in case it is open
				
				
				
				//
				
			} else {
				
				// [M2] Actions withing the game world
				
				Item i = lPlayer.getInventory().slots[sel()].getContent();
				if(i!=null) i.onRightClickBegin((int)blockx, (int)blocky, null);
			
				//
				
			}
		}
	}

	@Override
	public void onMouseButtonReleased(int code) {
		double blockx=hovx;
		double blocky=hovy;
		if(code==GLFW_MOUSE_BUTTON_1) {
			Item i = lPlayer.getInventory().slots[sel()].getContent();
			if(i!=null) i.onLeftClickEnd((int)blockx, (int)blocky, null);
		} else if(code==GLFW_MOUSE_BUTTON_2) {
			Item i = lPlayer.getInventory().slots[sel()].getContent();
			if(i!=null) i.onRightClickEnd((int)blockx, (int)blocky, null);
		}
	}
	
}
