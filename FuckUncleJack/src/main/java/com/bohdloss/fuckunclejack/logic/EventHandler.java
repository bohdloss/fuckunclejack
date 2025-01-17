package com.bohdloss.fuckunclejack.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.events.*;
import com.bohdloss.fuckunclejack.server.Server;

public class EventHandler {

private static Logic logic = new Logic();
private static List<GameEventListener> listeners = new ArrayList<GameEventListener>();
	
	public static void addListener(GameEventListener lis) {
		if(!listeners.contains(lis)) listeners.add(lis);
	}

	public static void removeListener(GameEventListener lis) {
		listeners.remove(lis);
	}
	
	public static void clearListeners() {
		listeners.clear();
	}
	
	/*
	 * EVENTS
	 */
	
	public static BlockDestroyedEvent blockDestroyed(boolean send, BlockDestroyedEvent event) {
		logic.onBlockDestroyed(event);
		try {
			listeners.forEach(i -> i.onBlockDestroyed(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
						if(t.player.getWorld().equals(event.getStart().getChunk().getWorld())) {
							t.events.add(event);
						}
				});
			}
		}
		
		return event;
	}
	
	public static BlockPlacedEvent blockPlaced(boolean send, BlockPlacedEvent event) {
		logic.onBlockPlaced(event);
		try {
			listeners.forEach(i -> i.onBlockPlaced(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					if(t.player.getWorld().equals(event.getDestination().getChunk().getWorld())) {
						t.events.add(event);
					}
				});
			}
		}
		
		return event;
	}
	
	public static ItemPickupEvent itemPicked(boolean send, ItemPickupEvent event) {
		logic.onItemPickedup(event);
		try {
			listeners.forEach(i -> i.onItemPickedup(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
/*		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					t.events.add(event);
				});
			}
		} */
		
		return event;
	}
	
	public static ItemDroppedEvent itemDropped(boolean send, ItemDroppedEvent event) {
		logic.onItemDropped(event);
		try {
			listeners.forEach(i -> i.onItemDropped(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			}
		}
		
		return event;
	}
	
	public static EntitySpawnedEvent entitySpawned(boolean send, EntitySpawnedEvent event) {
		logic.onEntitySpawned(event);
		try {
			listeners.forEach(i -> i.onEntitySpawned(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
/*		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					t.events.add(event);
				});
			}
		} */
		
		return event;
	}
	
	public static PlayerJoinEvent playerJoined(boolean send, PlayerJoinEvent event) {
		logic.onPlayerJoin(event);
		try {
			listeners.forEach(i -> i.onPlayerJoin(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					if(t.UPID!=(event.getIssuer().getUID())) {
						t.events.add(event);
					}
				});
			}
		}
		
		return event;
	}

	public static PlayerLeaveEvent playerLeft(boolean send, PlayerLeaveEvent event) {
		logic.onPlayerLeave(event);
		try {
			listeners.forEach(i -> i.onPlayerLeave(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
/*		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					t.events.add(event);
				});
			}
		} */
		
		return event;
	}
	
	public static AddInvItemEvent invItemAdded(boolean send, AddInvItemEvent event) {
		logic.onInvItemAdded(event);
		try {
			listeners.forEach(i -> i.onInvItemAdded(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(t -> {
					if(t.UPID==(event.getIssuer().getUID())) {
						t.sendInventory=true;
					}
				});
			}
		}
		
		return event;
	}
	
	public static EnterHouseEvent enteredHouse(boolean send, EnterHouseEvent event) {
		logic.onEnterHouse(event);
		try {
			listeners.forEach(i -> i.onEnterHouse(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			}
		}
		
		return event;
	}
	
	public static HitEvent hitEntity(boolean send, HitEvent event) {
		logic.onEntityHit(event);
		try {
			listeners.forEach(i -> i.onEntityHit(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			}
		}
		
		return event;
	}
	
	public static DamageEvent damagedEntity(boolean send, DamageEvent event) {
		logic.onEntityDamaged(event);
		try {
			listeners.forEach(i -> i.onEntityDamaged(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(!GameState.isClient.getValue()) {
				Server.threads.forEach(v->{
					if(v.player.getWorld().equals(event.getIssuer().getWorld())) {
						v.events.add(event);
						if(v.UPID==event.getVictim().getUID()) {
							v.sendOwnPosition=true;
							TimerTask task = new TimerTask() {
								@Override
								public void run() {
									v.sendOwnPosition=false;
								}
							};
							Entity.timer.schedule(task, 500);
						}
					}
				});
			}
		}
		
		return event;
	}
	
	public static StartChargingEvent chargeStarted(boolean send, StartChargingEvent event) {
		logic.onStartCharging(event);
		try {
			listeners.forEach(i -> i.onStartCharging(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(v->{
					if(v.UPID!=event.getIssuer().getUID()) {
						v.events.add(event);
					}
				});
			}
		}
		
		return event;
	}
	
	public static StopChargingEvent chargeStopped(boolean send, StopChargingEvent event) {
		logic.onStopCharging(event);
		try {
			listeners.forEach(i -> i.onStopCharging(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			} else {
				Server.threads.forEach(v->{
					if(v.UPID!=event.getIssuer().getUID()) {
						v.events.add(event);
					}
				});
			}
		}
		
		return event;
	}
	
	public static ExitHouseEvent exitedHouse(boolean send, ExitHouseEvent event) {
		logic.onExitHouse(event);
		try {
			listeners.forEach(i -> i.onExitHouse(event));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(send&!event.isCancelled()) {
			if(GameState.isClient.getValue()) {
				Client.events.add(event);
			}
		}
		
		return event;
	}
}
