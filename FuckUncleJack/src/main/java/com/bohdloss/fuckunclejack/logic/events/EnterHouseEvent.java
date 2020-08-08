package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.entities.House;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.logic.GameEvent;

public class EnterHouseEvent extends GameEvent {

private ByteBuffer buf;

private House target;
	
	public EnterHouseEvent(Player issuer, byte cause, House target) {
		super(issuer, cause, false);
		
		this.target=target;
		
		buf=ByteBuffer.allocate(10);
		buf.put(EnterHouseEvent);
		buf.put(cause);
		
		buf.putInt(issuer.getUID());
		buf.putInt(target.getUID());
		
	}

	@Override
	public Player getIssuer() {
		return (Player)issuer;
	}
	
	public House getTarget() {
		return target;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
