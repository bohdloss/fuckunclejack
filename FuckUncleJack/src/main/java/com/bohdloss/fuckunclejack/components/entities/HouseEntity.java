package com.bohdloss.fuckunclejack.components.entities;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.generator.generators.DeserthouseWorld;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.events.EnterHouseEvent;
import com.bohdloss.fuckunclejack.render.CMath;

import static com.bohdloss.fuckunclejack.logic.GameState.*;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import static com.bohdloss.fuckunclejack.render.CMath.*;
import static com.bohdloss.fuckunclejack.logic.EventHandler.*;
import static com.bohdloss.fuckunclejack.logic.GameEvent.*;
import static com.bohdloss.fuckunclejack.logic.FunctionUtils.*;

public abstract class HouseEntity extends Entity{

	public HouseEntity(String model, String texture, int width, int height) {
		super(model, texture, 5);
		this.width=width;
		this.height=height;
		updateBounds();
		physics=true;
		ignoreCollision=true;
		invulnerable=false; //set to true later
	}
	
	public void enterHouse(PlayerEntity p) {
		if(isClient.getValue()) {
			enteredHouse(true, new EnterHouseEvent(p, enterHouse, this));
		} else {
			if(!enteredHouse(false, new EnterHouseEvent(p, enterHouse, this)).isCancelled()) {
				
				if(dimensions.get("house"+getUID())==null) {
					dimensions.put("house"+getUID(), new DeserthouseWorld(0,"house"+getUID()));
				}
				World travel = dimensions.get("house"+getUID());
				travel(p, travel, 0, 100);
			}
		}
	}
	
	@Override
	public void tick(float delta) {
		super.tick(delta);
		
		if(isClient.getValue()) {
			double dist2 = distance2(x, y, lPlayer.getX(), lPlayer.getY());
			if(dist2<=16) {
				nearHouseB=true;
				lastHouse=this;
			}
		}
		
	}

}
