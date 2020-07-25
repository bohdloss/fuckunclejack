package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class AddInvItemEvent extends GameEvent {

private Item item;
	
private ByteBuffer buf;
	
	public AddInvItemEvent(Entity issuer, byte cause, Item add) {
		super(issuer, cause, true);
		item=add;
		
		buf=ByteBuffer.allocate(14);
		buf.put(AddInvItemEvent);
		buf.put(cause);
		buf.putInt(add.getId());
		buf.putInt(add.getAmount());
	}

	@Override
	public Entity getIssuer() {
		return (Entity)issuer;
	}

	public Item getItem() {
		return item;
	}
	
	@Override
	public ByteBuffer bytes() {
		return buf;
	}

}
