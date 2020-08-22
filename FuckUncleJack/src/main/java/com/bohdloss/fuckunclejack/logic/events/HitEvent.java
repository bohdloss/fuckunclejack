package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class HitEvent extends GameEvent {

private ByteBuffer buf;
private Entity target;	

	public HitEvent(Entity issuer, Entity target, byte cause) {
		super(issuer, cause, false);
		this.target=target;
		buf=ByteBuffer.allocate(6);
		buf.put(HitEvent);
		buf.put(cause);
		
		buf.putInt(target.getUID());
	}

	@Override
	public Entity getIssuer() {
		return (Entity) issuer;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
