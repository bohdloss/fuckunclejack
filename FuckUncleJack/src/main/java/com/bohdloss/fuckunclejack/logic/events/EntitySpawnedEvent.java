package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class EntitySpawnedEvent extends GameEvent {

private ByteBuffer buf;
	
	public EntitySpawnedEvent(byte cause, Entity issuer, Object...data) {
		super(issuer, cause, true);
		buf=ByteBuffer.allocate(26+(cause==dropSpawn?8:0));
		buf.put(EntitySpawnedEvent);//object.put("type", "EntitySpawnedEvent");
		buf.put(cause);//object.put("cause", cause);
		buf.putInt(issuer.getUID());//object.put("uid", issuer.getUID());
		buf.putFloat(issuer.getX());//object.put("x", ""+issuer.getX());
		buf.putFloat(issuer.getY());//object.put("y", ""+issuer.getY());
		buf.putFloat(issuer.getVelocity().x);//object.put("velx", ""+issuer.getVelocity().x);
		buf.putFloat(issuer.getVelocity().y);//object.put("vely", ""+issuer.getVelocity().y);
		buf.putInt(issuer.getId());//object.put("id", issuer.getId());
		if(cause==dropSpawn) {
			Item i = (Item) data[0];
			buf.putInt(i.getId());//object.put("itemid", i.getId());
			buf.putInt(i.getAmount());//object.put("itemamount", i.getAmount());
		}
	}
	
	@Override
	public Entity getIssuer() {
		return (Entity)issuer;
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
