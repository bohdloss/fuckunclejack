package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class ItemDroppedEvent extends GameEvent {

protected Item item;
	
private ByteBuffer buf;

	public ItemDroppedEvent(byte cause, Object issuer, Item item) {
		super(issuer, cause, false);
		this.item=item;
		int allocX=0;
		switch(cause) {
		case blockDrop:
			allocX=8;
		break;
		case invDrop:
			Entity ent = (Entity) issuer;
			allocX=4;
		break;
		}
		buf=ByteBuffer.allocate(2+allocX);
		
		buf.put(ItemDroppedEvent);//object.put("type", "ItemDroppedEvent");
		buf.put(cause);//object.put("cause", cause);
		switch(cause) {
		case blockDrop:
			Block b = (Block)issuer;
			buf.putInt(b.getWorldx());//object.put("issuer", b.getWorldx()+"/"+b.getY());
			buf.putInt(b.getY());
			break;
		case invDrop:
			Entity e = (Entity)issuer;
			buf.putInt(e.getUID());//object.put("issuer", e.getUID());
		break;
		}
	}

	public Item getItem() {
		return item;
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
