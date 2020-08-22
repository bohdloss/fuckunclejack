package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.AnimationSet;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;

public class Player extends Entity{
	
private String name;

public AnimationSet character;
public Animation anim;
	
private static Matrix4f translation=new Matrix4f();
private static Matrix4f res=new Matrix4f();
private static Shader gui;
private static Model model;

//Animation related

private boolean activeMov=false;
private int direction=1;

	public Player(String name) {
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
			if(entity instanceof ItemDrop) {
				
				ItemDrop drop = (ItemDrop) entity;
				drop.pickUp(this, true);
				
			}
		});
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
		}
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		float strety = stretchyY();
		
		//calc scale
		
		float xscale = 1+stretchyX();
		float yscale = strety+1;
		
		translation.identity().translate(x, y+strety/2f, 0).scale(direction*xscale*anim.getScale().x,yscale*anim.getScale().y,1);
		res=matrix.mul(translation, res);
		gui.bind();
		gui.setUniform("red", red);
		gui.setUniform("projection", res);
		anim.bind();
		model.render();
		if(this!=ClientState.lPlayer) {
		FontManager.renderString(x-(FontManager.strWidth(name)/2f), y+1.25f, Assets.sheets.get("font"), gui, matrix, Assets.models.get("item"), name);
		}
		gui.setUniform("red", false);
		s.bind();
		s.setUniform("red", red);
		
		if(inventory.slots[inventory.selected].getContent()!=null) {
			inventory.slots[inventory.selected].getContent().render(s, matrix.mul(translation, res), direction*anim.getHandPosition().x, anim.getHandPosition().y, false);
		}
		
		s.setUniform("red", false);
		
		renderHitboxes(s, matrix);
	}
	
	public String getName() {
		return name;
	}

	@Override
	protected Block[] getSurroundings() {
		Block[] blocks = new Block[35];
		int bx = CMath.fastFloor(x);
		int by = CMath.fastFloor(y);
		int ii=0;
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<7;j++) {
				try {
				blocks[ii]=world.getBlock(bx+i-2, by+j-3);
				} catch(Exception e) {}
				ii++;
			}
		}
		return blocks;
	}
	
	public void setActiveMov(boolean in) {
		activeMov=in;
	}

	@Override
	public int getId() {
		return 0;
	}
	
}
