package com.bohdloss.fuckunclejack.client;

import com.bohdloss.fuckunclejack.components.Chunk;

public class ChunkRequest {
	
public static final int UNSENT=0;
public static final int ELABORATING=1;
public static final int READY=2;
	
public int x;
public int status=UNSENT;

public Chunk chunk;

	public ChunkRequest(int x) {
		this.x=x;
	}
	
}
