package com.bohdloss.fuckunclejack.server;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.client.ChunkRequest;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.entities.DesertHouse;
import com.bohdloss.fuckunclejack.components.entities.House;
import com.bohdloss.fuckunclejack.components.entities.ItemDrop;
import com.bohdloss.fuckunclejack.components.entities.Player;
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
						Player player = new Player(name);
						System.out.println("Assigned uid "+player.getUID()+" to player "+player.getName());
						dimensions.get("world").join(player, 0, 100);
						players.put(player.getUID(), player);
						
						
						input.auth=true;
						input.writeAuth=true;
						input.UPID=player.getUID();
						input.player=player;
					}
				}
			break;
			case PLAYERDATA:
				
				Player p = input.player;
				p.x=buf.getFloat();
				p.y=buf.getFloat();
				float velx=buf.getFloat();
				float vely=buf.getFloat();
				p.setVelocity(velx, vely);
				
				p.getInventory().selected=(int)CMath.limit(buf.getInt(), 0, 8);
				
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
						
						Player e_BDE_ISSUER = players.get(e_BDE_UID);
						Inventory e_BDE_INV = e_BDE_ISSUER.getInventory();
						Item e_BDE_I = e_BDE_INV.slots[e_BDE_INV.selected].getContent();
						if(e_BDE_I!=null) e_BDE_I.onLeftClickBegin(e_BDE_X, e_BDE_Y, e_BDE_ISSUER);
						e_BDE_ISSUER.getWorld().destroyBlock(cause, e_BDE_ISSUER, e_BDE_X, e_BDE_Y, e_BDE_BG, true);
						
						
						
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
						
						Player e_BPE_ISSUER = players.get(e_BPE_UID);
						Inventory e_BPE_INV = e_BPE_ISSUER.getInventory();
						Item e_BPE_I = e_BPE_INV.slots[e_BPE_INV.selected].getContent();
						if(e_BPE_I!=null) {
							e_BPE_I.onRightClickBegin(e_BPE_X, e_BPE_Y, e_BPE_ISSUER);
						}
						
						
						
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							input.sendInventory=true;
						}
						
					break;
					case EnterHouseEvent:
						try {
						Player e_EHE_ISSUER=input.player;
						buf.getInt();
						int e_EHE_UID = buf.getInt();
						House e_EHE_HOUSE = (House) e_EHE_ISSUER.getWorld().entities.get(e_EHE_UID);
						
						e_EHE_HOUSE.enterHouse(e_EHE_ISSUER);
						
						} catch(Exception e) {
							e.printStackTrace();
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
				input.player.getWorld().join(new DesertHouse(), input.player.getX(), input.player.getY()+20);;
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
