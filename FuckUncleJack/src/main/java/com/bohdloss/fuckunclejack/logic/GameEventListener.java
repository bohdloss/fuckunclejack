package com.bohdloss.fuckunclejack.logic;

import com.bohdloss.fuckunclejack.logic.events.*;

public interface GameEventListener {

	abstract void onBlockPlaced(BlockPlacedEvent e);
	abstract void onBlockDestroyed(BlockDestroyedEvent e);
	abstract void onItemPickedup(ItemPickupEvent e);
	abstract void onItemDropped(ItemDroppedEvent e);
	abstract void onEntitySpawned(EntitySpawnedEvent e);
	abstract void onPlayerJoin(PlayerJoinEvent e);
	abstract void onPlayerLeave(PlayerLeaveEvent e);
	abstract void onInvItemAdded(AddInvItemEvent e);
	abstract void onInvItemMoved(ItemMovedEvent e);
	abstract void onEnterHouse(EnterHouseEvent e);
	
}
