package com.bohdloss.fuckunclejack.components.entities;

import java.util.TimerTask;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.items.BowItem;
import com.bohdloss.fuckunclejack.hud.HUD;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.AnimationSet;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.server.Server;
import com.bohdloss.fuckunclejack.server.SocketThread;

public class PlayerEntity extends Entity{
	
private String name;

public AnimationSet character;
public Animation anim;
	
private static Matrix4f translation=new Matrix4f();
private static Matrix4f res=new Matrix4f();
private static Shader gui;
private static Model model;

//Animation related

private boolean damApplied=false;
private boolean damTasks=false;

private int direction=1;

private static Model item;
private static Model smallItem;
private static Texture dot;

protected boolean bowAimDots=false; //Should draw projectile path?

static {
	item=Assets.models.get("item");
	smallItem=Assets.models.get("smallitem");
	dot=Assets.textures.get("dot");
}

	public PlayerEntity(String name) {
		super("player_model", "", 20f);
		
		if(GameState.isClient.getValue()) {
		
		gui=Assets.shaders.get("gui");
		model=Assets.models.get("player_model");
		
		character = Assets.animationSets.get("dad");
		anim=character.idle;
		
		}
		
		this.name=name;
		width=0.8f;
		height=1.8f;
		inventory=new Inventory(this, 9);
		inventory.selected=0;
		updateBounds();
	}
	
	public void placeBlock() {
		
	}

	@Override
	public void tick() {
		
		//For animation orientation
		
		if(velx>0) direction=1;
		if(velx<0) direction=-1;
		
		super.tick();
		
		//Choose animation to play
		
		updateAnimation();
		
		//Pickup items
		
		world.entities.forEach((key,entity) -> {
			if(entity instanceof ItemDropEntity) {
				
				ItemDropEntity drop = (ItemDropEntity) entity;
				drop.pickUp(this, true);
				
			}
		});
		
		//tick items in inventory
		
		for(int i=0;i<9;i++) {
			ItemSlot slot = inventory.slots[i];
			if(!slot.isEmpty()) slot.getContent().tick();
		}
	}
	
	public void updateAnimation() {
		if(GameState.isClient.getValue()) {
			
			//UPDATE THE ANIMATION (CLIENT ONLY)
			
			if((velx==0&vely==0)) {
				Animation a = character.idle;
				if(a!=anim) a.clear();
				anim=a;
			} 
			if(velx!=0&vely==0) {
				Animation a = character.walking;
				if(a!=anim) a.clear();
				anim=a;
				anim.setSpeed((float)CMath.fastAbs(velx*20));
			}
			
			if(vely>0) {
				Animation a = character.jumping;
				if(a!=anim) a.clear();
				anim=a;
				anim.setSpeed(5f);
			}
			
			if(vely<0) {
				Animation a = character.falling;
				if(a!=anim) a.clear();
				anim=a;
			}
			
			//OVERRIDE
			
			//damage
			
			if(red&!damApplied) {
				Animation a = character.damage;
				if(a!=anim) a.clear();
				anim=a;
				if(!damTasks) {
					//Might break at some point
					//It's just hacked together to work
					TimerTask task = new TimerTask() {
						public void run() {
							damApplied=true;
						}
					};
					TimerTask task2 = new TimerTask() {
						public void run() {
							damApplied=false;
							damTasks=false;
						}
					};
					timer.schedule(task, 300);
					timer.schedule(task2, 500);
					damTasks=true;
				}
			}
			
		}
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		if(this==ClientState.lPlayer&!ClientState.renderPlayer) return;
		
		//render aiming dots for bow-like items
		
		if(bowAimDots) {
			float aimAngle = CMath.oppositeTo(HUD.mpoint);
			float aimCos = (float)Math.cos(aimAngle);
			float aimSin = (float)Math.sin(aimAngle);
			for(int i=0;i<5;i++) {
				float force = ((BowItem) inventory.getSelectedItem()).calculateForce();
				float percent = (float)i/5;
				
				float transx = (float)CMath.lerp(percent, x, x+force*aimCos*3);
				float transy = (float)CMath.lerp(percent, y, y+force*aimSin*3);
				
				translation.identity().translate(transx, transy, 0);
				s.setUniform("projection", matrix.mul(translation, res));
				dot.bind(0);
				smallItem.render();
			}
		}
		
		float strety = stretchyY();
		
		//calc scale
		
		float xscale = 1+stretchyX();
		float yscale = strety+1;
		
		//Player model translation
		//Calculation
		translation.identity().translate(x, y+strety/2f, 0).scale(direction*xscale, yscale, 1);
		//
		gui.bind();
		gui.setUniform("red", red);
		gui.setUniform("projection", matrix.mul(translation, res));
		anim.bind();
		model.render();
		gui.setUniform("red", false);
		
		if(!inventory.slots[inventory.selected].isEmpty()) {
		
			//Item translation
			Item i = inventory.slots[inventory.selected].getContent();
			
			float itemx = anim.getHandPosition().x;
			float itemy = anim.getHandPosition().y;
			float itemrot = anim.getHandPosition().z;
			
			//Calculation
			translation.translate(itemx, itemy, 0).rotate(itemrot, 0, 0, 1);
			//
			s.bind();
			s.setUniform("red", red);
			s.setUniform("projection", matrix.mul(translation, res));
			
			boolean found=false;
			boolean smallmodel=false;
			Texture t = Assets.textures.get(i.texture);
			if(t!=null) {
				t.bind(0);
				found=true;
			} else {
				BlockTexture load = Assets.blocks.get(i.texture);
				if(load!=null) {
					t=load.txt[19];
					smallmodel=true;
					found=true;
				}
			}
			
			if(found) {
				s.setUniform("gray", i.grayFactor());
				t.bind(0);
				if(smallmodel) {
					//Adjust to animation offset
					
					translation.translate(-0.1f, -0.1f, 0);
					s.setUniform("projection", matrix.mul(translation, res));
					
					smallItem.render();
				} else {
					item.render();
				}
				s.setUniform("gray", 0f);
			}
			
			s.setUniform("red", false);
			
		}
		
		if(this!=ClientState.lPlayer) {
			FontManager.renderString(x-(FontManager.strWidth(name)/2f), y+1.25f, Assets.sheets.get("font"), gui, matrix, Assets.models.get("item"), name);
			}
		
		s.bind();
		
		renderHitboxes(s, matrix);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int getId() {
		return 0;
	}
	
private SocketThread thread;
	
	@Override
	public void destroy() {
		world.player.remove(getUID());
		if(!GameState.isClient.getValue()) {
			thread=null;
			Server.threads.forEach(v->{
				if(v.UPID==getUID()&thread==null) thread=v;
			});
			if(thread!=null) {
				thread.leaveEvent();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						thread.termination(false);
					}
				};
				SocketThread.timer.schedule(task, 10000);
			}
		}
		try {
			finalize();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void itemRightBegin(Item i) {
		if(BowItem.class.isAssignableFrom(i.getClass())) {
			bowAimDots=true;
		}
	}
	
	@Override
	public void itemRightEnd(Item i) {
		if(BowItem.class.isAssignableFrom(i.getClass())) {
			bowAimDots=false;
		}
	}
	
}
