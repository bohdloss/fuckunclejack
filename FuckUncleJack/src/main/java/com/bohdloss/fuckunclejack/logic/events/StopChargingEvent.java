package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class StopChargingEvent extends GameEvent {
	
public ByteBuffer buf;
	
	public StopChargingEvent(Entity issuer, byte cause) {
		super(issuer, cause, false);
		
		buf=ByteBuffer.allocate(6);
		buf.put(StopChargingEvent);
		buf.put(cause);
		buf.putInt(issuer.getUID());
	}

	@Override
	public Entity getIssuer() {
		return (Entity) issuer;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
