package com.bohdloss.fuckunclejack.server;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.client.ChunkRequest;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.entities.ItemDrop;
import com.bohdloss.fuckunclejack.components.entities.Player;
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
						System.out.println("Player "+player.getName()+" joined world "+ dimensions.get("world").getName());
						
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
				
				p.getInventory().selected=(int)CMath.limit(buf.getInt(), 27, 35);
				
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
						e_BDE_ISSUER.getWorld().destroyBlock(cause, e_BDE_ISSUER, e_BDE_X, e_BDE_Y, true);
					
						}catch(Exception e) {
							e.printStackTrace();
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
						e_BPE_ISSUER.getWorld().placeBlock(cause, e_BPE_ISSUER, e_BPE_X, e_BPE_Y, genBlockById(e_BPE_ID, e_BPE_ISSUER.getWorld(), e_BPE_X, e_BPE_Y), true);
						
						} catch(Exception e) {
							e.printStackTrace();
						}
						
					break;
					case ItemMovedEvent:
						Player e_IME_ISSUER = input.player;
						int e_IME_origin=buf.getInt();
						int e_IME_dest=buf.getInt();
						Item e_IME_get = e_IME_ISSUER.getInventory().slots[e_IME_origin].getContent();
						e_IME_ISSUER.getInventory().slots[e_IME_origin].setContent(e_IME_ISSUER.getInventory().slots[e_IME_dest].getContent());
						e_IME_ISSUER.getInventory().slots[e_IME_dest].setContent(e_IME_get);
					break;
					}
					
				}
				
			break;
			case CHUNKS:
				int CHUNKS_LENGTH = buf.getInt();
				System.out.println(CHUNKS_LENGTH);
				for(int i=0;i<CHUNKS_LENGTH;i++) {
					int CHUNKS_CUR = buf.getInt();
					Chunk CHUNKS_CALC = input.player.getWorld().getChunk(CHUNKS_CUR, true);
					
					if(!input.calcChunks.contains(CHUNKS_CALC)&input.calcChunks.size()<10) {
						input.calcChunks.add(CHUNKS_CALC);
					}
				}
			break;
			}
		}
		} catch(BufferUnderflowException e) {
			System.out.println("Out of bounds at position "+buf.position()+" with stacktrace: ");
			e.printStackTrace();
		}
	}
	
}
