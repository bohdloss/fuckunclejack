package com.bohdloss.fuckunclejack.components;

import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.render.CRectanglef;

public class Collision {

//better GC
	private static CRectanglef last=new CRectanglef(0,0,0,0);
	
	public static boolean collide(Entity p, Block b, Vector2f dest) {
		if(!b.hasCollision()) return false;
		
		last.setFrame(p.getBounds().x+dest.x, p.getBounds().y+dest.y, p.getBounds().width, p.getBounds().height);
		
		return last.intersects(b.getBounds());
	}
	
	public static boolean collideEnt(Entity p, Entity b, Vector2f dest) {
		if(p.collision|b.collision) {
		
		last.setFrame(p.getBounds().x+dest.x, p.getBounds().y+dest.y, p.getBounds().width, p.getBounds().height);
		
		return last.intersects(b.getBounds());
		} else {
			return false;
		}
	}
	
}
