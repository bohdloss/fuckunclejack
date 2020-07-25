package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class PlayerJoinEvent extends GameEvent {

public String dimension;
	
private ByteBuffer buf;

	public PlayerJoinEvent(Player issuer, byte cause, String dimension) {
		super(issuer, cause, true);
		this.dimension=dimension;
		
		buf=ByteBuffer.allocate(6+dimension.length());
		buf.put(PlayerJoinEvent);//object.put("type", "PlayerJoinEvent");
		buf.put(cause);//object.put("cause", cause);
		//object.put("issuer", issuer.getX()+"/"+issuer.getY()+"/"+issuer.getName()+"/"+issuer.getUID());
	
		//Some other shit
		//TODO
		//i dont give a fuck
		
		//rip i must do it now
		
		CSocketUtils.writeString(buf, dimension);
		
		//aha done fuck you
	}

	@Override
	public Player getIssuer() {
		return (Player)issuer;
	}

	public String getDimension() {
		return dimension;
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
