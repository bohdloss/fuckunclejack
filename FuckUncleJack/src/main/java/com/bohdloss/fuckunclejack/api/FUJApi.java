package com.bohdloss.fuckunclejack.api;

public final class FUJApi {

private static int[] selectedCharacter;
private static Boolean[] ownsCharacter;
private static Boolean[] ownsSkin;
private static int selectedGamemode=-1;
private static int currency=-1;
private static double rank=-1;;
private static int[] selectedSkin;

static {
	CharacterInfo.init();
	GamemodeInfo.init();
	SkinInfo.init();
	selectedCharacter=new int[GamemodeInfo.AVAILABLE.length];
	for(int i=0;i<selectedCharacter.length;i++) {
		selectedCharacter[i]=-1;
	}
	selectedSkin=new int[CharacterInfo.AVAILABLE.length];
	for(int i=0;i<selectedSkin.length;i++) {
		selectedSkin[i]=-1;
	}
	ownsCharacter=new Boolean[CharacterInfo.AVAILABLE.length];
	for(int i=0;i<ownsCharacter.length;i++) {
		ownsCharacter[i]=null;
	}
	ownsSkin=new Boolean[SkinInfo.AVAILABLE.length];
	for(int i=0;i<ownsSkin.length;i++) {
		ownsSkin[i]=null;
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
			fetchSelectedSkin(i);
		}
		for(int i=0;i<CharacterInfo.AVAILABLE.length;i++) {
			fetchOwnedCharacter(i);
		}
		for(int i=0;i<SkinInfo.AVAILABLE.length;i++) {
			fetchOwnedSkin(i);
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
		for(int i=0;i<CharacterInfo.AVAILABLE.length;i++) {
			pushSelectedSkin(i);
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
		if(id>=0&&id<CharacterInfo.AVAILABLE.length) return CharacterInfo.AVAILABLE[id];
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
		if(id>=0&&id<GamemodeInfo.AVAILABLE.length) return GamemodeInfo.AVAILABLE[id];
		return GamemodeInfo.UNKNOWN;
	}
	
	//Gets a boolean that indicates wheter a character is owned or not either from cache or from the server
	public static synchronized boolean getOwnedCharacter(int id) {
		if(ownsCharacter[id]==null) return fetchOwnedCharacter(id);
		return ownsCharacter[id];
	}

	//Forces character ownage cache to be synced with the dedicated server
	public static synchronized boolean fetchOwnedCharacter(int id) {
		return (ownsCharacter[id]=(id!=1));
	}
	
	//substitute to push method for owned characters
	public static synchronized void buyCharacter(int id) {
		//TODO
		fetchOwnedCharacter(id);
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
	
	//Forces skin ownage cache to be synced with the dedicated server
	public static synchronized boolean fetchOwnedSkin(int id) {
		return (ownsSkin[id]=false);
	}
	
	//Gets a boolean that indicates wheter or not a skin is owned either from cache or from the server
	public static synchronized boolean getOwnedSkin(int id) {
		if(ownsSkin[id]==null) return fetchOwnedSkin(id);
		return ownsSkin[id];
	}
	
	//Substitute for push method for owned skins
	public static synchronized void buySkin(int id) {
		//TODO
		fetchOwnedSkin(id);
	}
	
	//Forces the selected skin cache to be synched with the dedicated server
	public static synchronized int fetchSelectedSkin(int character) {
		return (selectedSkin[character]=CharacterInfo.AVAILABLE[character].getSkins()[0]);
	}
	
	//Return the skin id of the selected character of the selected gamemode
	public static synchronized int getSelectedSkin() {
		if(selectedSkin[getSelectedCharacter()]==-1) return fetchSelectedSkin(getSelectedCharacter());
		return selectedSkin[getSelectedCharacter()];
	}
	
	//Updates the local value of the selected skin of the selected character of the selected gamemode
	public static synchronized void setSelectedSkin(int skin) {
		selectedSkin[getSelectedCharacter()]=skin;
	}
	
	//Updates the selected gamemode info to the server
	public static synchronized void pushSelectedSkin(int character) {
		//TODO
		fetchSelectedSkin(character);
	}

	public static synchronized SkinInfo getSkin() {
		return getSkinInfoById(getSelectedSkin());
	}
	
	private static synchronized SkinInfo getSkinInfoById(int id) {
		if(id>=0&&id<SkinInfo.AVAILABLE.length) return SkinInfo.AVAILABLE[id];
		return SkinInfo.UNKNOWN;
	}
	
}