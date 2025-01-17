package com.bohdloss.fuckunclejack.components;

import com.bohdloss.fuckunclejack.components.entities.ItemDropEntity;
import com.bohdloss.fuckunclejack.components.items.BowItem;
import com.bohdloss.fuckunclejack.logic.EventHandler;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.AddInvItemEvent;
import com.bohdloss.fuckunclejack.logic.events.ItemDroppedEvent;
import com.bohdloss.fuckunclejack.main.Game;

public class Inventory {

public ItemSlot[] slots;
public int selected;
public Entity owner;
	
public Inventory(Entity owner, int amount) {
	this.owner=owner;
	slots=new ItemSlot[amount];
	for(int i=0;i<slots.length;i++) {
		slots[i]=new ItemSlot(this);
	}
}

public void list() {
	System.out.print("[");
	for(int i=0;i<slots.length;i++) {
		System.out.print(" / "+slots[i].getContent());
	}
	System.out.println("]");
}


public Item getSelectedItem() {
	return slots[selected].getContent();
}

public void addItem(Item in, boolean ignoreCheck) {
	
	//lets test if we can add an item to the inventory
	
	if(!ignoreCheck&EventHandler.invItemAdded(!GameState.isClient.getValue(), new AddInvItemEvent(owner, GameEvent.userInput, in)).isCancelled()) return;
	
	//First, check of there's already an item of the same type
	
	boolean found=false;
	
	for(int	i=0;i<slots.length;i++) {
		ItemSlot cur = slots[i];
		
		if(cur.getContent()!=null) {
			if(cur.getContent().getClass().equals(in.getClass())) {
				
				//Items are of the same type
				
				int total = cur.getContent().getAmount()+in.getAmount();
				
				if(total<=in.getMax()) {
					
					//Acceptable, add the item
					
					cur.getContent().setAmount(total);
					
					found=true;
					
					break;
					
				} else {
					
					//Not acceptable, increment the amount
					//as much as possible, then recursively call this function
					
					int toRemove = (in.getMax()-cur.getContent().getAmount());
					
					if(toRemove==0) continue;
					
					in.setAmount(in.getAmount()-toRemove);
					cur.getContent().setAmount(in.getMax());
					
					addItem(in, ignoreCheck);
					
					found=true;
					
					break;
					
				}
				
			}
		}
	}
	
	//If not found, try with empty slots
	
	if(!found) {
	
		for(int i=0;i<slots.length;i++) {
			ItemSlot cur = slots[i];
			if(cur.getContent()==null) {
				cur.setContent(in);
				found=true;
				break;
			}
		}
	
	}
	if(!found) {
		//NO ROOM IN INVENTORY;
		//TODO: DROP ITEM
	}
	
}

public void dropSelectedItem() {
	if(!EventHandler.itemDropped(GameState.isClient.getValue(), new ItemDroppedEvent(GameEvent.invDrop, owner, getSelectedItem())).isCancelled()) {
		if(!GameState.isClient.getValue()) {
			ItemDropEntity ide = (ItemDropEntity) FunctionUtils.genItemEntity(getSelectedItem());
			ide.pickDelay=2000;
			if(getSelectedItem()!=null && BowItem.class.isAssignableFrom(getSelectedItem().getClass())) {
				getSelectedItem().onRightClickEnd(Game.guiMouse.x, Game.guiMouse.y, null);
			}
			owner.getWorld().join(ide, owner.getX(), owner.getY());
			slots[selected].setContent(null);
		}
	}
	
}

}
