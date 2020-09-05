package com.bohdloss.fuckunclejack.components.entities;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.DamageEvent;
import com.bohdloss.fuckunclejack.logic.events.EntitySpawnedEvent;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Shader;

public class ProjectileEntity extends Entity {

	private float angle;
	private Entity owner;
	
	private Object[] data = new Object[1];
	
	public ProjectileEntity(String texture, Entity owner) {
		super("square", texture, 1);
		invulnerable=true;
		collision=false;
		width=1;
		height=1;
		updateBounds();
		this.owner=owner;
	}

	@Override
	public void tick() {
		super.tick();
		angle=CMath.lookAt(velx, vely);
	}
	
	@Override
	public void physics() {
		vel.x=velx;
		vel.y=0;
		execMove(vel);
		vel.x=0;
		vel.y=vely;
		execMove(vel);
		if(!flight) {
			if(inAir()) {
				vely+=gravity.y;
				vely=(float)CMath.clampMin(vely, -0.5f);
			} else {
				vely=0;
			}
		} else {
			if(vely>0) {
				vely-=0.01f;
				vely=(float)CMath.clampMin(vely, 0);
			}
			if(vely<0) {
				vely+=0.01f;
				vely=(float)CMath.clampMax(vely, 0);
			}
		}
	if(velx>0) {
		velx-=0.01f;
		velx=(float)CMath.clampMin(velx, 0);
	}
	if(velx<0) {
		velx+=0.01f;
		velx=(float)CMath.clampMax(velx, 0);
	}
	vel.x=0;
	vel.y=0;
	collision=true;
	if(!move(vel, false)) {
		boolean hit = false;
		if(entCollisions.size()>0&!hit) {
			if(hitTarget(entCollisions.get(0))) hit=true;
			
		}
		if(collisions.size()>0&!hit) {
			if(hitTarget(collisions.get(0))) hit=true;
		}
	}
	collision=false;
	}
	
	private boolean hitTarget(Object obj) {
		if(GameState.isClient.getValue()|obj==null) return false;
		if(Block.class.isAssignableFrom(obj.getClass())) {
			StaticProjectileEntity still = new StaticProjectileEntity(texture, angle);
			if(!EventHandler.entitySpawned(false, new EntitySpawnedEvent(GameEvent.tickSpawn, still, new Object[0])).isCancelled()) {
				world.join(still, x, y);
				destroy();
				return true;
			}
		}
		if(Entity.class.isAssignableFrom(obj.getClass())) {
			if(((Entity)obj).getUID()==owner.getUID()) {
				return false;
			}
			if(((Entity)obj).hit(this)) {
				destroy();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void render(Shader s, Matrix4f input) {
		s.setUniform("red", red);
		res = input.translate(x, y, 0, res).rotate(angle, 0, 0, 1, res);
		s.setProjection(res);
		Assets.textures.get(texture).bind(0);
		Assets.models.get(model).render();
		s.setUniform("red", false);
		renderHitboxes(s, input);
	}
	
	@Override
	public int getId() {
		return 4;
	}
	
	public Object[] getData() {
		data[0]=texture;
		return data;
	}
	
}
