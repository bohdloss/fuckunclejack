package com.bohdloss.fuckunclejack.logic;

import java.util.Random;

import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.blocks.AirBlock;
import com.bohdloss.fuckunclejack.components.blocks.StoneBlock;
import com.bohdloss.fuckunclejack.components.entities.ItemDropEntity;
import com.bohdloss.fuckunclejack.logic.events.AddInvItemEvent;
import com.bohdloss.fuckunclejack.logic.events.BlockDestroyedEvent;
import com.bohdloss.fuckunclejack.logic.events.BlockPlacedEvent;
import com.bohdloss.fuckunclejack.logic.events.DamageEvent;
import com.bohdloss.fuckunclejack.logic.events.EnterHouseEvent;
import com.bohdloss.fuckunclejack.logic.events.EntitySpawnedEvent;
import com.bohdloss.fuckunclejack.logic.events.HitEvent;
import com.bohdloss.fuckunclejack.logic.events.ItemDroppedEvent;
import com.bohdloss.fuckunclejack.logic.events.ItemPickupEvent;
import com.bohdloss.fuckunclejack.logic.events.PlayerJoinEvent;
import com.bohdloss.fuckunclejack.logic.events.PlayerLeaveEvent;
import com.bohdloss.fuckunclejack.logic.events.StartChargingEvent;
import com.bohdloss.fuckunclejack.logic.events.StopChargingEvent;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import static com.bohdloss.fuckunclejack.logic.GameEvent.*;

public class Logic implements GameEventListener{

//GC
private CRectanglef bbounds;
private CRectanglef ebounds;
//END
	
	private boolean canPlace(Entity ent, int x, int y) {
		return (valid(ent, x-1,y)|valid(ent, x+1,y)|valid(ent, x,y+1)|valid(ent, x,y-1));
	}
	
	private boolean valid(Entity ent, int x, int y) {
		return !(ent.getWorld().getBlock(x, y) instanceof AirBlock);
	}
	
	@Override
	public void onBlockPlaced(BlockPlacedEvent e) {
		switch(e.getCause()) {
			default: e.cancel("nocause");
			case invPlace:
			
			//In case the block is being placed from the inventory
			
			double distance = CMath.distance(e.getDestination().getWorldx(), e.getDestination().getY(), e.getIssuer().getX(), e.getIssuer().getY());
			bbounds=e.getDestination().getBounds();
			ebounds=e.getIssuer().getBounds();
			if(distance>8|!canPlace(e.getIssuer(), e.getDestination().getWorldx(), e.getDestination().getY())|bbounds.intersects(ebounds)|!e.getStart().isReplaceable()) {
				e.cancel();
			} else {
				
			}
			
			//End of condition
			
		}
	}

	@Override
	public void onBlockDestroyed(BlockDestroyedEvent e) {
		switch(e.getCause()) {
			
		default: e.cancel("nocause");
		case handDestroy:
		
			//In case the block is being broken regularly by an entity
			
			double distance = CMath.distance(e.getDestination().getWorldx(), e.getDestination().getY(), e.getIssuer().getX(), e.getIssuer().getY());
			if(distance>8|e.getStart().isUnbreakable()) {
				e.cancel();
			}
			
			//End of condition
		break;
		case tickDestroy:
			
			
			
		break;
		}
	}

	@Override
	public void onItemPickedup(ItemPickupEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case groundPick:
		
			//In case the item is being picked up from an Item Drop on the ground
			
			if(e.getEntity().pickDelay>0) {
				e.cancel();
				return;
			}
			
			double distance = CMath.distance(e.getIssuer().getX(), e.getIssuer().getY(), e.getEntity().getX(), e.getEntity().getY());
			
			if(distance>1) {
				
				//If the item is too far away don't pick up
				
				e.cancel();
				
			} else {
				
				//If close enough
				
				ItemSlot[] slots = e.getInventory().slots;
				
				//To see if there has been found an empty slot
				
				boolean found=false;
				
				for(int i=0;i<slots.length;i++) {
					
					if(slots[i].getContent()==null) {
						
						/*	If the current slot is empty then found
						 */
						
						found=true;
						
					} else if(slots[i].getContent().getClass().equals(e.getItem().getClass())) {
						
						/*
						 * If an item of the same type is already
						 * in the inventory, just calculate the total amount
						 */
						
						int total = (slots[i].getContent().getAmount()+e.getItem().getAmount());
						if(total>e.getItem().getMax()) {
							
							/*	If the calculated total exceeds the
							*	maximum for items, try to calculate the
							*	maximum that can be added without completely consuming
							*	the entity
							*/
							
							int toRemove=e.getItem().getMax()-slots[i].getContent().getAmount();
							
							if(toRemove>0) {
							
							found=true;
							
							}
							
						} else {
							
							/*
							 * Total doesn't exceed maximum item amount,
							 * tag as found
							 */
							
							found=true;
							
						}
					}
				}
				
				//End of LOOP
				
				if(!found) {
					e.cancel();
				}
				
			}
			
			//End of condition
			
		}
	}

	@Override
	public void onItemDropped(ItemDroppedEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case blockDrop:
			
		break;
		case invDrop:
			
		break;
		}
		
	}

	@Override
	public void onEntitySpawned(EntitySpawnedEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case dropSpawn:
			
		break;
		case tickSpawn:
			
		break;
		}
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case join:
			
		break;
		case changeDim:
			
		break;
		}
	}

	@Override
	public void onPlayerLeave(PlayerLeaveEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case leave:
			
		break;
		}
	}

	@Override
	public void onInvItemAdded(AddInvItemEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case userInput:
			
		break;
		}
		
	}

	@Override
	public void onEnterHouse(EnterHouseEvent e) {
		switch(e.getCause()) {
		default: e.cancel("nocause");
		case enterHouse:
			
			double distance = CMath.distance(e.getTarget().getX(),e.getTarget().getY(),e.getIssuer().getX(),e.getIssuer().getY());
			
			if(distance>4) {
				e.cancel();
			}
			
		break;
		}
		
	}

	@Override
	public void onEntityHit(HitEvent e) {
		switch(e.getCause()) {
		default:
			e.cancel("nocause");
		case hitEntity:
			double distance = CMath.distance(e.getIssuer().getX(), e.getIssuer().getY(), e.getTarget().getX(), e.getTarget().getY());
			
			if(distance>6) {
				e.cancel();
			}
			
		break;
		}
	}

	@Override
	public void onEntityDamaged(DamageEvent e) {
		switch(e.getCause()) {
		default:
			e.cancel("nocause");
		case damagedByEntity:
			
		break;
		case damagedItself:
			
		break;
		}
	}

	@Override
	public void onStartCharging(StartChargingEvent e) {
		switch(e.getCause()) {
		default:
			e.cancel("nocause");
		case chargeItem:
			
		break;
		}
	}

	@Override
	public void onStopCharging(StopChargingEvent e) {
		switch(e.getCause()) {
		default:
			e.cancel("nocause");
		case stopChargeItem:
			
		break;
		}
	}
	
}
