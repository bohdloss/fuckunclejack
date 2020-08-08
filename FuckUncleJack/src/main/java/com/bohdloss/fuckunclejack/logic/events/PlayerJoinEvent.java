package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class PlayerJoinEvent extends GameEvent {

public World dimension;
	
private ByteBuffer buf;

	public PlayerJoinEvent(Player issuer, byte cause, World dimension) {
		super(issuer, cause, true);
		this.dimension=dimension;
		
		buf=ByteBuffer.allocate(14+dimension.getName().length());
		buf.put(PlayerJoinEvent);//object.put("type", "PlayerJoinEvent");
		buf.put(cause);//object.put("cause", cause);
		//object.put("issuer", issuer.getX()+"/"+issuer.getY()+"/"+issuer.getName()+"/"+issuer.getUID());
		
		buf.putInt(issuer.getUID());
		buf.putInt(dimension.getID());
		CSocketUtils.writeString(buf, dimension.getName());
		
	}

	@Override
	public Player getIssuer() {
		return (Player)issuer;
	}

	public World getDimension() {
		return dimension;
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
