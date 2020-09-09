package com.bohdloss.fuckunclejack.api;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.AnimationSet;

public class CharacterInfo {
	
public static final CharacterInfo UNKNOWN;

public static final CharacterInfo DAD;

	
static {
	UNKNOWN=new CharacterInfo(-1, null, "Unknown");
	DAD=new CharacterInfo(0, Assets.animationSets.get("dad"), "Dad");
}
	
public static void init() {}

private AnimationSet anims;
private String name;
private int id;

public AnimationSet getAnimationSet() {
	return anims;
}
	
public String getName() {
	return name;
}

public int id() {
	return id;
}

private CharacterInfo(int id, AnimationSet anims, String name) {
	this.anims=anims;
	this.id=id;
	this.name=name;
}

}
