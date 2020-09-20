package com.bohdloss.fuckunclejack.api;


import java.util.ArrayList;
import java.util.List;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.AnimationSet;

public class CharacterInfo {
	
public static final CharacterInfo[] AVAILABLE;
public static final CharacterInfo[][] SUBDIVIDED;
	
public static final CharacterInfo UNKNOWN;

public static final CharacterInfo DAD;
public static final CharacterInfo SON;
public static final CharacterInfo JACK;
	
static {
	UNKNOWN=new CharacterInfo(-1, null, "Unknown");
	DAD=new CharacterInfo(0, Assets.animationSets.get("dad"), "Dad", GamemodeInfo.NORMAL_FAMILY.getId());
	SON=new CharacterInfo(1, Assets.animationSets.get("son"), "Son", GamemodeInfo.NORMAL_FAMILY.getId());
	JACK=new CharacterInfo(2, Assets.animationSets.get("jack"), "Jack", GamemodeInfo.NORMAL_JACK.getId());
	
	AVAILABLE=new CharacterInfo[3];
	AVAILABLE[0]=DAD;
	AVAILABLE[1]=SON;
	AVAILABLE[2]=JACK;
	
	SUBDIVIDED=new CharacterInfo[GamemodeInfo.AVAILABLE.length][0];
	
	for(int i=0;i<GamemodeInfo.AVAILABLE.length;i++) {
		List<CharacterInfo> res = new ArrayList<CharacterInfo>();
		
		for(int j=0;j<AVAILABLE.length;j++) {
			if(AVAILABLE[j].hasGamemode(i)) res.add(AVAILABLE[j]);
		}
		
		SUBDIVIDED[i]=cast(res.toArray());
	}
	
	/*for(int i=0;i<SUBDIVIDED.length;i++) {
		for(int j=0;j<SUBDIVIDED[i].length;j++) {
			System.out.println("["+i+"]["+j+"]="+SUBDIVIDED[i][j].id());
		}
	}*/
	
}
	
private static CharacterInfo[] cast(Object[] in) {
	CharacterInfo[] out = new CharacterInfo[in.length];
	for(int i = 0 ; i < in.length ; i++) {
		out[i] = (CharacterInfo) in[i];
	}
	return out;
}

public boolean hasGamemode(int id) {
	for(int i=0;i<gamemodes.length;i++) {
		if(gamemodes[i]==id) {
			return true;
		}
	}
	return false;
}

public static void init() {}

private AnimationSet anims;
private String name;
private int id;
private int[] gamemodes;

public AnimationSet getAnimationSet() {
	return anims;
}
	
public String getName() {
	return name;
}

public int getId() {
	return id;
}

private CharacterInfo(int id, AnimationSet anims, String name, int...gamemodes) {
	this.anims=anims;
	this.id=id;
	this.name=name;
	this.gamemodes=gamemodes;
}

public int[] getGamemodes() {
	return gamemodes;
}

}
