package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.entities.HouseEntity;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class ExitHouseEvent extends GameEvent {

private ByteBuffer buf;

private HouseEntity target;
	
	public ExitHouseEvent(PlayerEntity issuer, byte cause, HouseEntity target) {
		super(issuer, cause, false);
		
		this.target=target;
		
		buf=ByteBuffer.allocate(10);
		buf.put(ExitHouseEvent);
		buf.put(cause);
		
		buf.putInt(issuer.getUID());
		buf.putInt(0);
		
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
