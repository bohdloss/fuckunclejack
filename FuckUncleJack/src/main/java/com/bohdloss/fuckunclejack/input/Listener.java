package com.bohdloss.fuckunclejack.input;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import static com.bohdloss.fuckunclejack.main.Game.*;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Point;

import org.lwjgl.glfw.GLFWScrollCallback;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.items.BowItem;
import com.bohdloss.fuckunclejack.components.items.blocks.StoneBlockItem;
import com.bohdloss.fuckunclejack.editor.Editor;
import com.bohdloss.fuckunclejack.hud.Button;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.Function;
import com.bohdloss.fuckunclejack.render.MainEvents;
import com.bohdloss.fuckunclejack.render.Point2f;

public class Listener implements KeyListen {
	
//cache
	static boolean foundEntityHit=false;
	static CRectanglef frame=new CRectanglef(0,0,0,0);
//end
	
	public void init() {
		glfwSetScrollCallback(window.getWindow(), new GLFWScrollCallback() {
		    @Override public void invoke (long win, double dx, double dy) {
		    	if(state==GAME) {
		    		
		    		int selected = (int)CMath.clamp(sel()-(int)dy, 0, 8);
		    		
		    		if(lPlayer.getInventory().selected!=selected) {
		    			Item item = lPlayer.getInventory().getSelectedItem();
		    			if(item!=null) {
		    				if(BowItem.class.isAssignableFrom(item.getClass())) {
		    					item.onRightClickEnd(HUD.mpoint.x, HUD.mpoint.y, null);
		    				}else {
		    					item.onRightClickEnd(hovx, hovy, null);
		    				}
		    			}
		    		}
		    		
		    		lPlayer.getInventory().selected=selected;
		    		//Game.scaleAmount+=(int)dy;
		    	} else if(state==EDITMODE) {
		    		Editor.scroll+=dy;
		    		if(Editor.status==Editor.MODEL|Editor.status==Editor.COLLISION) {
		    			if(Editor.vertical) {
		    				Editor.yscale+=dy*0.25f;
		    			} else {
		    				Editor.xscale+=dy*0.25f;
		    			}
		    		} else if(Editor.status==Editor.TRANSLATE) {
		    			if(Editor.vertical) {
		    				Editor.savedy+=dy*0.25f;
		    			} else {
		    				Editor.savedx+=dy*0.25f;
		    			}
		    		} else if(Editor.status==Editor.NORMAL) {
		    			if(Editor.vertical) {
		    				Editor.savedy+=dy*0.25f;
		    			} else {
		    				Editor.savedx+=dy*0.25f;
		    			}
		    		}
		    	}
		    }
		});
	}
	
	@Override
	public void onKeyPressed(int code) {
		
		switch(code) {
		case GLFW_KEY_F11:
			Function<Object> fullscreen = new Function<Object>() {
				public Object execute() throws Exception{
					Game.window.toggleFullscreen();
					return null;
				}
			};
			MainEvents.queue(fullscreen, true);
			break;
		case GLFW_KEY_T:
			lPlayer.x=32000;
			lPlayer.y=100;
			break;
		case GLFW_KEY_F:
			if(nearHouse!=null) nearHouse.enterHouse(lPlayer);
			break;
		case GLFW_KEY_P:
			Client.sendDebug=true;
			break;
		case GLFW_KEY_L:
			lPlayer.flight=!lPlayer.flight;
			lPlayer.physics=!lPlayer.physics;
			lPlayer.forcePhysics=!lPlayer.forcePhysics;
			break;
		case GLFW_KEY_O:
			if(state==GAME) {
				state=EDITMODE;
			} else if(state==EDITMODE) {
				state=GAME;
			}
			
			break;
		case GLFW_KEY_I:
			lPlayer.getInventory().addItem(new StoneBlockItem(100), true);
			break;
		case GLFW_KEY_M:
			if(state==GAME) ClientState.disconnect();
			ClientState.showMenu(false, true, MenuTab.active.getName());
			break;
		}
	}

	@Override
	public void onKeyReleased(int code) {
	}

	private static void testHit(Entity v) {
		if(!foundEntityHit) {
			CRectanglef entframe = v.getBounds();
			frame.setFrame(entframe.x-(Game.camera.getX()/Game.scaleAmount), entframe.y-(Game.camera.getY()/Game.scaleAmount), entframe.width, entframe.height);
			if(frame.pIntersects(HUD.gamepoint)) {
				if(v.hit(lPlayer)) {
					foundEntityHit=true;
				
					System.out.println("hit entity "+v.getUID());
				}
			}
		}
	}
	
	@Override
	public void onMouseButtonPressed(int code) {
		double blockx=hovx;
		double blocky=hovy;
		if(code==GLFW_MOUSE_BUTTON_3) {
			switch(state) {
			case EDITMODE:
			Editor.scroll=0;
			break;
			}
		}
		if(code==GLFW_MOUSE_BUTTON_1) {
			
			switch(state) {
			case EDITMODE:
			
				Editor.vertical=!Editor.vertical;
			
				try {
					Button.buttons.forEach(v->{
						if(v.status==Button.HOVERED) {
							v.status=Button.PRESSED;
						}
					});
				} catch(Exception e) {
					e.printStackTrace();
				}
			
			break;
			case GAME:
			
				// [M1] Actions within the game world
				
				//check if the click is on an entity
			
				foundEntityHit=false;
				
				lWorld.entities.forEach((k,v)->{
					testHit(v);
				});
			
				if(!foundEntityHit) {
					
					lWorld.player.forEach((k,v)->{
						if(v!=lPlayer) testHit(v);
					});
					
				}
				
				if(!foundEntityHit) {
					
					lWorld.destroyBlock(GameEvent.handDestroy, lPlayer, (int)blockx, (int) blocky, false, true);
					Item i = lPlayer.getInventory().slots[sel()].getContent();
					if(i!=null) i.onLeftClickBegin((int)blockx, (int)blocky, null);
				}
				
				//
			break;
			}
				
		} else if(code==GLFW_MOUSE_BUTTON_2) {
			
			switch(state) {
			case GAME:
				// [M2] Actions withing the game world
				
				Item i = lPlayer.getInventory().slots[sel()].getContent();
				if(i!=null) i.onRightClickBegin((int)blockx, (int)blocky, null);
			
				//
			break;
			}
		}
	}

	@Override
	public void onMouseButtonReleased(int code) {
		double blockx=hovx;
		double blocky=hovy;
		if(code==GLFW_MOUSE_BUTTON_1) {
			
			switch(state) {
			case EDITMODE:
			try {
				Button.buttons.forEach(v->{
					if(v.status==Button.PRESSED) {
						if(v.bounds.pIntersects(HUD.mpoint)) {
						v.click();
						v.status=Button.HOVERED;
						} else {
							v.status=Button.IDLE;
						}
					}
				});
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			break;
			case GAME:
			
			Item i = lPlayer.getInventory().slots[sel()].getContent();
			if(i!=null) i.onLeftClickEnd((int)blockx, (int)blocky, null);
			
			break;
			}
			
		} else if(code==GLFW_MOUSE_BUTTON_2) {
			
			switch(state) {
			case GAME:
			Item i = lPlayer.getInventory().slots[sel()].getContent();
			
			if(i!=null) {
				if(BowItem.class.isAssignableFrom(i.getClass())) {
					i.onRightClickEnd(HUD.mpoint.x, HUD.mpoint.y, null);
				}else {
					i.onRightClickEnd(hovx, hovy, null);
				}
			}
			break;
			}
		}
	}
	
}
