package com.bohdloss.fuckunclejack.api;

public final class FUJApi {

private static int[] selectedCharacter;
private static Boolean[] ownsCharacter;
private static int selectedGamemode=-1;
private static int currency=-1;
private static double rank=-1;;

static {
	CharacterInfo.init();
	GamemodeInfo.init();
	selectedCharacter=new int[GamemodeInfo.AVAILABLE.length];
	for(int i=0;i<selectedCharacter.length;i++) {
		selectedCharacter[i]=-1;
	}
	ownsCharacter=new Boolean[CharacterInfo.AVAILABLE.length];
	for(int i=0;i<ownsCharacter.length;i++) {
		ownsCharacter[i]=null;
	}
}

	public static synchronized void init() {}

	private FUJApi() {}

	//fetch all data at once
	//to be used for example after a match finishes
	//or before a menu screen appears to avoid freezing
	//the thread to connect to the api
	public static synchronized void fetchAll() {
		for(int i=0;i<GamemodeInfo.AVAILABLE.length;i++) {
			fetchSelectedCharacter(i);
		}
		for(int i=0;i<CharacterInfo.AVAILABLE.length;i++) {
			fetchOwnedCharacter(i);
		}
		fetchSelectedGameMode();
		fetchOwnedCurrency();
		fetchRank();
	}
	
	//push all changes in the cache to the server
	//to be used before a match starts
	//to let the server know which character has been chosen
	public static synchronized void pushAll() {
		for(int i=0;i<GamemodeInfo.AVAILABLE.length;i++) {
			pushSelectedCharacter(i);
		}
		pushSelectedGamemode();
	}
	
	//this returns the raw id for the selected character
	public static synchronized int getSelectedCharacter() {
		if(selectedCharacter[getSelectedGameMode()]==-1) return fetchSelectedCharacter(getSelectedGameMode());
		return selectedCharacter[getSelectedGameMode()];
	}
	
	//This parses the cached character id
	public static synchronized CharacterInfo getCharacter() {
		return getCharacterInfoById(getSelectedCharacter());
	}
	
	//This forces the client to fetch new data from the dedicated server and update the cache
	//then return the data
	public static synchronized int fetchSelectedCharacter(int gm) {
		//TODO
		
		return (selectedCharacter[getSelectedGameMode()]=CharacterInfo.SUBDIVIDED[getSelectedGameMode()][0].getId());
	}
	
	//Change selected character only locally
	public static synchronized void setSelectetCharacter(int value) {
		selectedCharacter[getSelectedGameMode()]=value;
	}
	
	//Force info to be updates on the dedicated server
	public static synchronized void pushSelectedCharacter(int gm) {
		//TODO
		fetchSelectedCharacter(getSelectedGameMode());
	}
	
	private static synchronized CharacterInfo getCharacterInfoById(int id) {
		switch(id) {
		case 0:
			return CharacterInfo.DAD;
		}
		return CharacterInfo.UNKNOWN;
	}
	
	//this returns the raw id of the selected gamemode
	public static synchronized int getSelectedGameMode() {
		if(selectedGamemode==-1) return fetchSelectedGameMode();
		return selectedGamemode;
	}
	
	//This parses the cached gamemode
	public static synchronized GamemodeInfo getGamemode() {
		return getGamemodeInfoById(getSelectedGameMode());
	}
		
	//This forces the client to fetch new data from the dedicated server and update the cache
	//then return the data
	public static synchronized int fetchSelectedGameMode() {
		//TODO
			
		return (selectedGamemode=0);
	}
		
	//Change selected gamemode only locally
	public static synchronized void setSelectetGamemode(int value) {
		selectedGamemode=value;
	}
		
	//Force info to be updated on the dedicated server
	public static synchronized void pushSelectedGamemode() {
		//TODO
		fetchSelectedGameMode();
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
	
	//Gets a boolean that indicates wheter a character is owned or not either from cache or from the server
	public static synchronized boolean getOwnedCharacter(int id) {
		if(ownsCharacter[id]==null) return fetchOwnedCharacter(id);
		return ownsCharacter[id];
	}

	//Forces character ownage cache to be synced with the dedicated server
	public static synchronized boolean fetchOwnedCharacter(int id) {
		return (ownsCharacter[id]=false);
	}
	
	//substitute to push method for owned characters
	public static synchronized void buyCharacter(int id) {
		
	}
	
	//Gets an integer that indicates the amount of currency owned
	public static synchronized int getOwnedCurrency() {
		if(currency==-1) return fetchOwnedCurrency();
		return currency;
	}
	
	//Forces currency cache to be synced with the dedicated server
	public static synchronized int fetchOwnedCurrency() {
		return (currency=100);
	}
	
	//No push method: clients have no permissions to edit the currency
	//only the server can handle currency calculations
	
	//Gets a double precision value that indicates the current rank of the player
	//not integer, to store data about the progress to the next rank
	public static synchronized double getRank() {
		if(rank==-1) return fetchRank();
		return rank;
	}
	
	//Forces rank cache to be synced with the dedicated server
	public static synchronized double fetchRank() {
		return (rank=2.3d);
	}
	
	//No push method: clients have no permissions to edit the rank
	//only the server can handle rank calculations
	
}
