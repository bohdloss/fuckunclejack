package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class PlayerLeaveEvent extends GameEvent {

public ByteBuffer buf;
	
	public PlayerLeaveEvent(Player issuer, byte cause) {
		super(issuer, cause, true);
		
		buf=ByteBuffer.allocate(6);
		buf.put(PlayerLeaveEvent);
		buf.put(cause);
		buf.putInt(issuer.getUID());
	}

	@Override
	public Player getIssuer() {
		return (Player)issuer;
	}

	@Override
	public ByteBuffer bytes() {
		return buf;
	}
	
}
