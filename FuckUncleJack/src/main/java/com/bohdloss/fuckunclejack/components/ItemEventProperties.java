package com.bohdloss.fuckunclejack.components;

public class ItemEventProperties {

private Item item;
private float damage;
private float breakspeed;

public ItemEventProperties(Item item) {
	this.item = item;
}

public float getDamage() {
	return damage;
}

public ItemEventProperties setDamage(float damage) {
	this.damage = damage;
	return this;
}

public float getBreakspeed() {
	return breakspeed;
}

public ItemEventProperties setBreakspeed(float breakspeed) {
	this.breakspeed = breakspeed;
	return this;
}

public Item getItem() {
	return item;
}
	
}
