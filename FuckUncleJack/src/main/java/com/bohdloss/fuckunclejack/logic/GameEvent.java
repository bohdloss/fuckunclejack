package com.bohdloss.fuckunclejack.logic;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

public abstract class GameEvent {

//BLOCK PLACE
	
public static final byte invPlace=(byte)1;

//BLOCK DESTROY

public static final byte handDestroy=(byte)2;
public static final byte tickDestroy=(byte)3;

//ITEM PICKUP

public static final byte groundPick=(byte)4;
	
//ITEM DROP

public static final byte blockDrop=(byte)5;
public static final byte invDrop=(byte)6;

//ENTITY SPAWN

public static final byte dropSpawn=(byte)7;

//PLAYER JOIN

public static final byte join=(byte)8;
public static final byte changeDim=(byte)9;

//PLAYER LEAVE

public static final byte leave=(byte)10;

//MOVE INV ITEM

public static final byte userInput=(byte)11;

protected Object issuer;
protected boolean serveronly;
protected boolean cancelled=false;
protected String cancelReason=null;
protected byte cause;
	
public GameEvent(Object issuer, byte cause, boolean serveronly) {
	this.issuer=issuer;
	this.cause=cause;
	this.serveronly=serveronly;
}

public Object getIssuer() {
	return issuer;
}

public byte getCause() {
	return cause;
}

public void cancel() {
	cancel("logic");
}

public void cancel(String why) {
	cancelled=true;
	cancelReason=why;
}

public void approve() {
	cancelled=false;
	cancelReason=null;
}

public boolean isCancelled() {
	return cancelled;
}

public boolean isServerOnly() {
	return serveronly;
}

public String getCancReason() {
	return cancelReason;
}

public abstract ByteBuffer bytes();

public static final byte BlockDestroyedEvent=(byte)1;
public static final byte BlockPlacedEvent=(byte)2;
public static final byte EntitySpawnedEvent=(byte)3;
public static final byte ItemDroppedEvent=(byte)4;
public static final byte ItemPickupEvent=(byte)5;
public static final byte PlayerJoinEvent=(byte)6;
public static final byte PlayerLeaveEvent=(byte)7;
public static final byte AddInvItemEvent=(byte)8;
public static final byte ItemMovedEvent=(byte)9;

}
