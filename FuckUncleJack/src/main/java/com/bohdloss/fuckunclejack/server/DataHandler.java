package com.bohdloss.fuckunclejack.server;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.client.ChunkRequest;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.entities.ProjectileEntity;
import com.bohdloss.fuckunclejack.components.entities.DesertHouseEntity;
import com.bohdloss.fuckunclejack.components.entities.HouseEntity;
import com.bohdloss.fuckunclejack.components.entities.ItemDropEntity;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.components.items.BowItem;
import com.bohdloss.fuckunclejack.components.items.WinnerswordItem;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.render.CMath;

import static com.bohdloss.fuckunclejack.logic.FunctionUtils.*;
import static com.bohdloss.fuckunclejack.logic.GameState.*;
import static com.bohdloss.fuckunclejack.logic.GameEvent.*;
import static com.bohdloss.fuckunclejack.server.CSocketUtils.*;

public class DataHandler {
	
	public static void handleBuffer(ByteBuffer buf, SocketThread input) {
		try {
			buf.clear();
		while(buf.hasRemaining()) {
			
			byte operation = buf.get();
			switch(operation) {
			default: 
				return;
			case CREDENTIALS:
				String name = readString(buf);
				String matchcode = readString(buf);
				if(!input.auth) {
					
					//TODO
					if(Authenticator.authenticate(matchcode)) {
						PlayerEntity player = new PlayerEntity(name);
						System.out.println("Assigned uid "+player.getUID()+" to player "+player.getName());
						dimensions.get("world").join(player, 0, 100);
						players.put(player.getUID(), player);
						player.getInventory().addItem(new BowItem(), false);
						input.sendInventory=true;
						
						input.auth=true;
						input.writeAuth=true;
						input.UPID=player.getUID();
						input.player=player;
					}
				}
			break;
			case PLAYERDATA:
				
				PlayerEntity p = input.player;
				float PX = buf.getFloat();
				float PY = buf.getFloat();
				float velx = buf.getFloat();
				float vely = buf.getFloat();
				
				int PSEL = buf.getInt();
				
				if(!input.sendOwnPosition) {
				
					p.setVelocity(velx, vely);
					p.x=PX;
					p.y=PY;
				
				}
				
				int PSELLIMIT = (int)CMath.limit(PSEL, 0, 8);
				
				if(p.getInventory().selected!=PSELLIMIT) {
					Item PITEMSEL = p.getInventory().getSelectedItem();
					if(PITEMSEL!=null) {
						PITEMSEL.onRightClickEnd(CMath.fastFloor(p.x), CMath.fastFloor(p.y), null);
					}
				}
				p.getInventory().selected=PSELLIMIT;
				
			break;
			case EVENT:
				
				int eventL = buf.getInt();
				
				for(int i=0;i<eventL;i++) {
					byte event=buf.get();
					byte cause = buf.get();
					
					switch(event) {
					case BlockDestroyedEvent:
						int e_BDE_UID =	buf.getInt();
						int e_BDE_X = buf.getInt();
						int e_BDE_Y = buf.getInt();
						boolean e_BDE_BG = buf.get()==(byte)1;
						
						try {
						
						PlayerEntity e_BDE_ISSUER = players.get(e_BDE_UID);
						Inventory e_BDE_INV = e_BDE_ISSUER.getInventory();
						Item e_BDE_I = e_BDE_INV.slots[e_BDE_INV.selected].getContent();
						
						boolean e_BDE_DONE = e_BDE_ISSUER.getWorld().destroyBlock(cause, e_BDE_ISSUER, e_BDE_X, e_BDE_Y, e_BDE_BG, true);
						if(e_BDE_I!=null&e_BDE_DONE) e_BDE_I.onLeftClickBegin(e_BDE_X, e_BDE_Y, null);
						
						
						}catch(Exception e) {
							e.printStackTrace();
						}finally {
							input.sendInventory=true;
						}
						
					break;
					case BlockPlacedEvent:
						int e_BPE_UID = buf.getInt();
						int e_BPE_X = buf.getInt();
						int e_BPE_Y = buf.getInt();
						boolean e_BPE_BG = buf.get()==(byte)1;
						int e_BPE_ID = buf.getInt();
						
						try {
						
						PlayerEntity e_BPE_ISSUER = players.get(e_BPE_UID);
						Inventory e_BPE_INV = e_BPE_ISSUER.getInventory();
						Item e_BPE_I = e_BPE_INV.slots[e_BPE_INV.selected].getContent();
						if(e_BPE_I!=null) {
							e_BPE_I.onRightClickBegin(e_BPE_X, e_BPE_Y, null);
						}
						
						
						
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							input.sendInventory=true;
						}
						
					break;
					case EnterHouseEvent:
						try {
						PlayerEntity e_EHE_ISSUER=input.player;
						buf.getInt();
						int e_EHE_UID = buf.getInt();
						HouseEntity e_EHE_HOUSE = (HouseEntity) e_EHE_ISSUER.getWorld().entities.get(e_EHE_UID);
						
						if(!e_EHE_HOUSE.dying) {
						
						e_EHE_HOUSE.enterHouse(e_EHE_ISSUER);
						
						}
						
						} catch(Exception e) {
							e.printStackTrace();
						}
					break;
					case HitEvent:
						try {
						Entity e_HE_ISSUER=input.player;
						int e_HE_VICTIM_ID=buf.getInt();
						Entity e_HE_VICTIM=e_HE_ISSUER.getWorld().getEntity(e_HE_VICTIM_ID);
						
						e_HE_VICTIM.hit(e_HE_ISSUER);
						
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							input.sendInventory=true;
						}
					break;
					case StartChargingEvent:
						buf.getInt();
						
						PlayerEntity e_SCE_ISSUER = input.player;
						Inventory e_SCE_INV = e_SCE_ISSUER.getInventory();
						Item e_SCE_I = e_SCE_INV.slots[e_SCE_INV.selected].getContent();
						if(e_SCE_I!=null) {
							e_SCE_I.onRightClickBegin(CMath.fastFloor(e_SCE_ISSUER.x), CMath.fastFloor(e_SCE_ISSUER.y), null);
						}
					break;
					case StopChargingEvent:
						buf.getInt();
						
						PlayerEntity e_SSCE_ISSUER = input.player;
						Inventory e_SSCE_INV = e_SSCE_ISSUER.getInventory();
						Item e_SSCE_I = e_SSCE_INV.slots[e_SSCE_INV.selected].getContent();
						if(e_SSCE_I!=null) {
							e_SSCE_I.onRightClickEnd(CMath.fastFloor(e_SSCE_ISSUER.x), CMath.fastFloor(e_SSCE_ISSUER.y), null);
						}
					break;
					}
					
				}
				
			break;
			case CHUNKS:
				int CHUNKS_LENGTH = buf.getInt();
				for(int i=0;i<CHUNKS_LENGTH;i++) {
					int CHUNKS_CUR = buf.getInt();
					Chunk CHUNKS_CALC = input.player.getWorld().getChunk(CHUNKS_CUR, true);
					
					if(!input.calcChunks.contains(CHUNKS_CALC)&input.calcChunks.size()<10) {
						input.calcChunks.add(CHUNKS_CALC);
					} else {
						input.calcChunks.add(null);
					}
				}
			break;
			case DEBUG:
				//input.player.getWorld().join(new Table(false, (byte)1), input.player.getX(), input.player.getY()+20);
				//input.player.getWorld().join(new DesertHouse(), input.player.getX(), input.player.getY()+20);
				ProjectileEntity spawning = new ProjectileEntity("arrow", input.player);
				spawning.setVelocity(0.2f, 0.2f);
				input.player.getWorld().join(spawning, input.player.getX(), input.player.getY()+5);
				System.out.println("spawned");
			break;
			}
		}
		} catch(BufferUnderflowException e) {
			System.out.println("Out of bounds at position "+buf.position()+" with stacktrace: ");
			e.printStackTrace();
		}
	}
	
	public static void print(byte[] b) {
		for(int i=0;i<b.length;i++) {
			System.out.print((b[i]&0xff)+".");
		}
		System.out.println();
	}
	
}
