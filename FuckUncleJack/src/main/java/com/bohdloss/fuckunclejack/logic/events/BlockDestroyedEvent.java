package com.bohdloss.fuckunclejack.logic.events;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.server.CSocketUtils;

public class BlockDestroyedEvent extends GameEvent{

private Block start;
private Block destination;

private ByteBuffer buf;

public BlockDestroyedEvent(byte cause, Entity issuer, Block start, Block destination) {
	super(issuer, cause, false);
	this.start=start;
	this.destination=destination;
	buf=ByteBuffer.allocate(15);
	buf.put(BlockDestroyedEvent);//object.put("type", "BlockDestroyedEvent");
	buf.put(cause);//object.put("cause", cause);
	buf.putInt(issuer.getUID());//object.put("issuer", issuer.getUID());
	buf.putInt(start.getWorldx());//object.put("startx", ""+start.getWorldx());
	buf.putInt(start.getY());//object.put("starty", ""+start.getY());
	buf.put(start.isBackground()?(byte)1:(byte)0);//object.put("startbg", ""+start.isBackground());
}
	
@Override
public Entity getIssuer() {
	return (Entity)issuer;
}

public Block getStart() {
	return start;
}

public Block getDestination() {
	return destination;
}

@Override
public ByteBuffer bytes() {
	return buf;
}

}
