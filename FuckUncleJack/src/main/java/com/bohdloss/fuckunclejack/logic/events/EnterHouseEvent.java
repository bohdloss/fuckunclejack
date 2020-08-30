package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.entities.HouseEntity;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class EnterHouseEvent extends GameEvent {

private ByteBuffer buf;

private HouseEntity target;
	
	public EnterHouseEvent(PlayerEntity issuer, byte cause, HouseEntity target) {
		super(issuer, cause, false);
		
		this.target=target;
		
		buf=ByteBuffer.allocate(10);
		buf.put(EnterHouseEvent);
		buf.put(cause);
		
		buf.putInt(issuer.getUID());
		buf.putInt(target.getUID());
		
	}

	@Override
	public PlayerEntity getIssuer() {
		return (PlayerEntity)issuer;
	}
	
	public HouseEntity getTarget() {
		return target;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
