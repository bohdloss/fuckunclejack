package com.bohdloss.fuckunclejack.components.items;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemEventProperties;
import com.bohdloss.fuckunclejack.logic.GameState;

public class WinnerswordItem extends Item {

	public WinnerswordItem() {
		super(25, 0, 1, "winnersworditem");
		this.damage=30;
	}

	@Override
	public int getId() {
		return 9;
	}

	@Override
	public int getMax() {
		return 1;
	}

	@Override
	public void tick(float delta) {
		
	}
	
}
