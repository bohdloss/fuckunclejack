package com.bohdloss.fuckunclejack.api;

import java.util.ArrayList;
import java.util.List;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.AnimationSet;

public final class SkinInfo {

public static final SkinInfo[] AVAILABLE;
public static final SkinInfo[][] SUBDIVIDED;
	
public static final SkinInfo UNKNOWN;

public static final SkinInfo DAD_D;
public static final SkinInfo SON_D;
public static final SkinInfo JACK_D;
public static final SkinInfo DAD_NEW;

static {
	
	UNKNOWN = new SkinInfo(-1, "", "Unknown");
	
	DAD_D = new SkinInfo(0, "dad_default", "Dad");
	SON_D = new SkinInfo(1, "son_default", "Son");
	JACK_D = new SkinInfo(2, "jack_default", "Jack");
	DAD_NEW = new SkinInfo(3, "afswq", "Milk store dad");
	
	AVAILABLE = new SkinInfo[4];
	AVAILABLE[0]=DAD_D;
	AVAILABLE[1]=SON_D;
	AVAILABLE[2]=JACK_D;
	AVAILABLE[3]=DAD_NEW;
	
	SUBDIVIDED = new SkinInfo[CharacterInfo.AVAILABLE.length][0];
	
	for(int i=0;i<SUBDIVIDED.length;i++) {
		SUBDIVIDED[i] = new SkinInfo[CharacterInfo.AVAILABLE[i].getSkinsAmount()];
		for(int j=0;j<SUBDIVIDED[i].length;j++) {
			int index = CharacterInfo.AVAILABLE[i].getSkins()[j];
			SUBDIVIDED[i][j] = AVAILABLE[index];
		}
	}
}
	
public static void init() {}

private int id;
private String name;
private String displayName;
private AnimationSet data;

private SkinInfo(int id, String name, String displayName) {
	this.id=id;
	this.name=name;
	this.displayName=displayName;
	this.data=Assets.animationSets.get(name);
}

public int getId() {
	return id;
}

public String getDisplayName() {
	return displayName;
}

public String getName() {
	return name;
}

public AnimationSet getData() {
	return data;
}

}
