package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.logic.GameEvent;

public class ItemMovedEvent extends GameEvent {

public ByteBuffer buf;
	
	public ItemMovedEvent(Object issuer, byte cause, int original, int dest) {
		super(issuer, cause, false);
		buf=ByteBuffer.allocate(10);
		buf.put(ItemMovedEvent);
		buf.put(cause);
		buf.putInt(original);
		buf.putInt(dest);
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
