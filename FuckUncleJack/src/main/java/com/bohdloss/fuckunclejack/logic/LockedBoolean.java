package com.bohdloss.fuckunclejack.logic;

public class LockedBoolean {

private boolean locked=false;
private boolean value=false;
	
public LockedBoolean() {
	value=false;
}

public LockedBoolean(boolean value) {
	this.value=value;
}

public void setValue(boolean value) {
	if(!locked) {
	this.value=value;
	}
}

public boolean getValue() {
	return value;
}

public void lock() {
	locked=true;
}

}
