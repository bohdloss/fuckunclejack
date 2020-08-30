package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class DamageEvent extends GameEvent {

private ByteBuffer buf;
private Entity damageCause;

	public DamageEvent(Entity victim, Entity damageCause, byte cause) {
		super(victim, cause, true);
		this.damageCause=damageCause;
		
		buf=ByteBuffer.allocate(6);
		
		buf.put(DamageEvent);
		buf.put(cause);
		
		buf.putInt(victim.getUID());
		
	}
	
	public Entity getVictim() {
		return getIssuer();
	}
	
	@Override
	public Entity getIssuer() {
		return (Entity) issuer;
	}
	
	public Entity getDamageCause() {
		return damageCause;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
