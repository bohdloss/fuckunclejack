package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class StartChargingEvent extends GameEvent {
	
public ByteBuffer buf;
	
	public StartChargingEvent(Entity issuer, byte cause, float argx, float argy) {
		super(issuer, cause, false);
		
		buf=ByteBuffer.allocate(14);
		buf.put(StartChargingEvent);
		buf.put(cause);
		buf.putInt(issuer.getUID());
		buf.putFloat(argx);
		buf.putFloat(argy);
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
