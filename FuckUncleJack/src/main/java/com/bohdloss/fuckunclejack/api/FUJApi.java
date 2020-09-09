package com.bohdloss.fuckunclejack.api;

public final class FUJApi {

private static int selectedCharacter=-1;
private static int selectedGamemode=-1;
	
static {
	CharacterInfo.init();
	GamemodeInfo.init();
}

	public static void init() {}

	private FUJApi() {}
	
	//This gets the character saved in the cache
	public static synchronized CharacterInfo getSelectedCharacter() {
		if(selectedCharacter==-1) return fetchSelectedCharacter();
		return getCharacterInfoById(selectedCharacter);
	}
	
	//This forces the client to fetch new data from the dedicated server and update the cache
	//then return the data
	public static synchronized CharacterInfo fetchSelectedCharacter() {
		//TODO
		
		return getCharacterInfoById(selectedCharacter=0);
	}
	
	//Change selected character only locally
	public static synchronized void setSelectetCharacter(int value) {
		selectedCharacter=value;
	}
	
	//Force info to be updates on the dedicated server
	public static synchronized void pushSelectedCharacter() {
		//TODO
	}
	
	private static synchronized CharacterInfo getCharacterInfoById(int id) {
		switch(id) {
		case 0:
			return CharacterInfo.DAD;
		}
		return CharacterInfo.UNKNOWN;
	}
	
	
	
	//This gets the gamemode saved in the cache
	public static synchronized GamemodeInfo getSelectedGamemode() {
		if(selectedGamemode==-1) return fetchSelectedGamemode();
		return getGamemodeInfoById(selectedGamemode);
	}
		
	//This forces the client to fetch new data from the dedicated server and update the cache
	//then return the data
	public static synchronized GamemodeInfo fetchSelectedGamemode() {
		//TODO
			
		return getGamemodeInfoById(selectedGamemode=0);
	}
		
	//Change selected gamemode only locally
	public static synchronized void setSelectetGamemode(int value) {
		selectedGamemode=value;
	}
		
	//Force info to be updates on the dedicated server
	public static synchronized void pushSelectedGamemode() {
		//TODO
	}
		
	private static synchronized GamemodeInfo getGamemodeInfoById(int id) {
		switch(id) {
		case 0:
			return GamemodeInfo.NORMAL_FAMILY;
		case 1:
			return GamemodeInfo.NORMAL_JACK;
		}
		return GamemodeInfo.UNKNOWN;
	}
}
