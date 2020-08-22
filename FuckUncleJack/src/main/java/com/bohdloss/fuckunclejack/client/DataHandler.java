package com.bohdloss.fuckunclejack.client;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.generator.generators.OverworldWorld;
import com.bohdloss.fuckunclejack.logic.ClientState;

import static com.bohdloss.fuckunclejack.logic.GameState.*;
import static com.bohdloss.fuckunclejack.logic.GameEvent.*;
import static com.bohdloss.fuckunclejack.server.CSocketUtils.*;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import static com.bohdloss.fuckunclejack.logic.FunctionUtils.*;

public class DataHandler {
	
private static List<Integer> uids = new ArrayList<Integer>();
	
	public static void print(byte[] b) {
		for(int i=0;i<b.length;i++) {
			System.out.print((b[i]&0xff)+".");
		}
		System.out.println();
	}
	
	public static void handleBuffer(ByteBuffer buf) {
		try {
		buf.clear();
		while(buf.hasRemaining()) {
			byte operation = buf.get();
			switch(operation) {
			default: 
				
				return;
			case CREDENTIALS:
				
				if(buf.get()==(byte)1) Client.auth=true;
				
				int CRED_UID = buf.getInt();
				
				lPlayer.setUID(CRED_UID);
				
				lWorld.join(lPlayer, 0, 100);
				
				System.out.println("login successful");
				
			break;
			case PLAYERDATA:
				int length = buf.getInt();
				
				for(int i=0;i<length;i++) {
					String name=readString(buf);
					int curUid=buf.getInt();
					
					if(!lWorld.player.containsKey(curUid)) {
						Player p = new Player(name);
						p.setUID(curUid);
						lWorld.join(p, 0, 0);
					}
					
					float x = buf.getFloat();
					float y = buf.getFloat();
					float velx = buf.getFloat();
					float vely = buf.getFloat();
					
					Player toEdit = lWorld.player.get(curUid);
					toEdit.x=x;
					toEdit.y=y;
					toEdit.setVelocity(velx, vely);
				}
				
			break;
			case EVENT:
				
				int eventL = buf.getInt();
				
				for(int i=0;i<eventL;i++) {
					byte event=buf.get();
					byte cause = buf.get();
					
					switch(event) {
					case BlockDestroyedEvent:
						int e_BDE_UID=-1;
						if(cause!=tickDestroy) {e_BDE_UID = buf.getInt();}
						int e_BDE_X = buf.getInt();
						int e_BDE_Y = buf.getInt();
						boolean e_BDE_BG = buf.get()==(byte)1;
						
						Entity e_BDE_ISSUER = lWorld.getEntity(e_BDE_UID);
						lWorld.destroyBlock(cause, e_BDE_ISSUER, e_BDE_X, e_BDE_Y, e_BDE_BG, false);
						
					break;
					case PlayerLeaveEvent:
						int e_PLE_UID = buf.getInt();
						players.remove(e_PLE_UID);
						lWorld.player.remove(e_PLE_UID);
					break;
					case PlayerJoinEvent:
						int e_PJE_UID = buf.getInt();
						int e_PJE_ID = buf.getInt();
						String dimension = readString(buf);
						
						if(cause==changeDim) {
							if(e_PJE_UID==lPlayer.getUID()) {
								lWorld=null;
								System.gc();
								lWorld=genWorldById(e_PJE_ID, dimension);
								lWorld.join(lPlayer, 0, 100);
							} else {
								if(!dimension.equals(lPlayer.getWorld().getName())) {
									players.remove(e_PJE_UID);
									lWorld.player.remove(e_PJE_UID);
								}
							}
						}
						
					break;
					case BlockPlacedEvent:
						int e_BPE_UID = buf.getInt();
						int e_BPE_X = buf.getInt();
						int e_BPE_Y = buf.getInt();
						boolean e_BPE_BG = buf.get()==(byte)1;
						int e_BPE_ID = buf.getInt();
						
						Player e_BPE_ISSUER = lWorld.player.get(e_BPE_UID);
						lWorld.placeBlock(cause, e_BPE_ISSUER, e_BPE_X, e_BPE_Y, genBlockById(e_BPE_ID, lWorld, e_BPE_X, e_BPE_Y), e_BPE_BG, false);
						
					break;
					case DamageEvent:
						
						int e_DE_VICTIM=buf.getInt();
						int e_DE_ISSUER=buf.getInt();
						
						Entity e_DE_VICTIM_ENT = lWorld.getEntity(e_DE_VICTIM);
						Entity e_DE_ISSUER_ENT = lWorld.getEntity(e_DE_ISSUER);
						
						if(e_DE_VICTIM_ENT!=null) {
							e_DE_VICTIM_ENT.red=true;
							TimerTask task = new TimerTask() {
								@Override
								public void run() {
									e_DE_VICTIM_ENT.red=false;
								}
							};
							Entity.timer.schedule(task, 500);
						}
						
					break;
					}
					
				}
				
			break;
			case ENTITIES:
				int ENT_length = buf.getInt();
				
				uids.clear();
				
				for(int i=0;i<ENT_length;i++) {
					int ENT_UID = buf.getInt();
					float ENT_X = buf.getFloat();
					float ENT_Y = buf.getFloat();
					float ENT_VELX = buf.getFloat();
					float ENT_VELY = buf.getFloat();
					
					uids.add(ENT_UID);
					
					int ENT_ID = buf.getInt();
					
					Object[] data=genDataById(buf, ENT_ID);
					
					if(lWorld.entities.get(ENT_UID)==null) {
						Entity ENT_create = genEntityById(ENT_ID, data);
						ENT_create.setUID(ENT_UID);
						lWorld.join(ENT_create, 0, 0);
					}
					
					Entity ENT_edit = lWorld.entities.get(ENT_UID);
					
					ENT_edit.x=ENT_X;
					ENT_edit.y=ENT_Y;
					ENT_edit.setVelocity(ENT_VELX, ENT_VELY);
					
				}
				Object[] val=lWorld.entities.entrySet().toArray();
				for(int i=0;i<val.length;i++) {
					Entry<Integer, Entity> cur = (Entry<Integer, Entity>) val[i];
					if(state==GAME) {
						if(!uids.contains(cur.getKey())) lWorld.entities.remove(cur.getKey());
					}
				}
				
			break;
			case CHUNKS:
				int CHUNKS_LENGTH=buf.getInt();
				for(int i=0;i<CHUNKS_LENGTH;i++) {
					int CH_CUR = buf.getInt();
					boolean CH_OK = buf.get()==(byte)1;
					Chunk CH_GEN=null;
					if(CH_OK) {
					CH_GEN = new Chunk(lWorld, CH_CUR);
						for(int x=0;x<16;x++) {
							for(int y=0;y<256;y++) {
								int CH_BLOCK_ID = buf.getInt();
								int CH_BLOCK_BG_ID = buf.getInt();
								CH_GEN.blocks[x][y].setTop(genBlockByIdChunk(CH_BLOCK_ID, CH_GEN, x, y));
								CH_GEN.blocks[x][y].setBackground(genBlockByIdChunk(CH_BLOCK_BG_ID, CH_GEN, x, y));
							}
						}
					}
					if(CH_OK) {
					ChunkRequest CH_REQUEST = Client.chunkrequest.get(CH_CUR);
					CH_REQUEST.status=ChunkRequest.READY;
					CH_REQUEST.chunk=CH_GEN;
					} else {
						Client.chunkrequest.remove(CH_CUR);
					}
				}
			break;
			case INVENTORY:
				for(int i=0;i<lPlayer.getInventory().slots.length;i++) {
					ItemSlot INV_SLOT = lPlayer.getInventory().slots[i];
					int INV_ID = buf.getInt();
					int INV_AMOUNT = buf.getInt();
					Item INV_ADD = genItemById(INV_ID, INV_AMOUNT);
					INV_SLOT.setContent(INV_ADD);
				}
			break;
			case STATS:
				
				ClientState.locked = buf.get()==(byte)1;
				float STAT_HEALTH = buf.getFloat();
				
				lPlayer.setHealth(STAT_HEALTH);
				
			break;
			}
		}
		} catch(BufferUnderflowException e) {
			System.out.println("Out of bounds at position "+buf.position()+" with stacktrace: ");
			e.printStackTrace();
		}
	}
	
}
