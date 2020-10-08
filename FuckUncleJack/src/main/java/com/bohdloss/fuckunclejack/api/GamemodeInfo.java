package com.bohdloss.fuckunclejack.api;

public final class GamemodeInfo {

public static final GamemodeInfo[] AVAILABLE;
	
public static final GamemodeInfo UNKNOWN;
	
public static final GamemodeInfo NORMAL_FAMILY;
public static final GamemodeInfo NORMAL_JACK;
	
static {
	UNKNOWN=new GamemodeInfo(-1, "Unknown");
	NORMAL_FAMILY=new GamemodeInfo(0, "Family member");
	NORMAL_JACK=new GamemodeInfo(1, "Jack");
	
	AVAILABLE=new GamemodeInfo[2];
	AVAILABLE[0]=NORMAL_FAMILY;
	AVAILABLE[1]=NORMAL_JACK;
}

public static void init() {}

private int id;
private String name;

private GamemodeInfo(int id, String name) {
	this.id=id;
	this.name=name;
}

public String getName() {
	return name;
}

public int getId() {
	return id;
}

}
