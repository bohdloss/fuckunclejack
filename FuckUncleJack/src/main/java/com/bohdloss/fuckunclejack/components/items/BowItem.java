package com.bohdloss.fuckunclejack.components.items;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemEventProperties;
import com.bohdloss.fuckunclejack.components.entities.ProjectileEntity;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.EntitySpawnedEvent;
import com.bohdloss.fuckunclejack.logic.events.StartChargingEvent;
import com.bohdloss.fuckunclejack.logic.events.StopChargingEvent;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Point2f;

public class BowItem extends Item {

public boolean charging;
public float chargePercent;

	public BowItem() {
		super(25, 0, 1, "bowitem");
	}

	@Override
	public ItemEventProperties onRightClickBegin(float x, float y, Entity entity) {
		if(!charging) {
			if(!EventHandler.chargeStarted(true, new StartChargingEvent(owner.owner.owner, GameEvent.chargeItem, x, y)).isCancelled()) {
				charging=true;
				chargePercent=0;
			}
		}
		return super.onRightClickBegin(x, y, entity);
	}

	@Override
	public ItemEventProperties onRightClickEnd(float x, float y, Entity entity) {
		Entity entOwn = owner.owner.owner;
		if(charging) {
			if(!EventHandler.chargeStopped(true, new StopChargingEvent(entOwn, GameEvent.stopChargeItem, x, y)).isCancelled()) {
				charging=false;
				if(chargePercent>0.4f) {
					ProjectileEntity arrow = new ProjectileEntity("arrow", entOwn);
					float force = calculateForce();
					float angle = CMath.oppositeTo(x, y);
					arrow.setVelocity((float)Math.cos(angle)*force, (float)Math.sin(angle)*force);
					if(!EventHandler.entitySpawned(false, new EntitySpawnedEvent(GameEvent.tickSpawn, arrow, new Object[0])).isCancelled()) {
						entOwn.getWorld().join(arrow, entOwn.x, entOwn.y);
					}
				}
			}
		}
		return super.onRightClickEnd(x, y, entity);
	}

	@Override
	public int getId() {
		return 10;
	}

	@Override
	public int getMax() {
		return 1;
	}

	@Override
	public void tick() {
		if(charging&chargePercent<1) {
			chargePercent=CMath.limit(chargePercent+0.03f, 0, 1);
		}
	}

	public float calculateForce() {
		return chargePercent;
	}
	
}
