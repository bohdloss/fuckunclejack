package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.entities.ItemDrop;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class ItemPickupEvent extends GameEvent {

protected ItemDrop drop;
	
private ByteBuffer buf;

	public ItemPickupEvent(byte cause, Entity issuer, ItemDrop item) {
		super(issuer, cause, true);
		drop=item;
		
		buf=ByteBuffer.allocate(10);
		buf.put(ItemPickupEvent);//object.put("type", "ItemPickupEvent");
		buf.put(cause);//object.put("cause", cause);
		buf.putInt(issuer.getUID());//object.put("issuer", issuer.getUID());
		buf.putInt(item.getUID());//object.put("itemuid", item.getUID());
	}

	@Override
	public Entity getIssuer() {
		return (Entity)issuer;
	}
	
	public ItemDrop getEntity() {
		return drop;
	}
	
	public Item getItem() {
		return drop.getItem();
	}
	
	public Inventory getInventory() {
		return getIssuer().getInventory();
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
