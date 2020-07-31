package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.client.ChunkRequest;
import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.entities.Player;
import com.bohdloss.fuckunclejack.generator.WorldGenerator;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Shader;

public class World implements Tickable{

public HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();
public HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
public HashMap<Integer, Player> player = new HashMap<Integer, Player>();
protected long seed;
protected WorldGenerator generator;
private static Matrix4f res = new Matrix4f().scale(2);

private String name;

public HashMap<Integer, Chunk> cachedChunks = new HashMap<Integer, Chunk>();

//faster GC
private List<Integer> used = new ArrayList<Integer>();
private int getChunk;
//end

	public void putChunk(Chunk c) {
		chunks.put(c.getOffsetx(), c);
		cachedChunks.put(c.getOffsetx(), c);
	}

	public Chunk getChunk(int x, boolean queue) {
		Chunk toRet=null;
		if((toRet=chunks.get(x))!=null) return toRet;
		if(GameState.isClient.getValue()) {
			if(queue) {
				if(Client.chunkrequest.get(x)==null) {
					Client.chunkrequest.put(x, new ChunkRequest(x));
				}
			}
			return null;
		}
		if((toRet=cachedChunks.get(x))!=null) return toRet;
		if(queue) {
				toRet = generator.generateChunk(x);
				putChunk(toRet);
				return toRet;
		}
		return toRet;
	}
	
	public void tick() {
		
		//Tick everything
		
		try {
		chunks.forEach((k,v)->v.tick());
		} catch(ConcurrentModificationException e) {}
		try {
		entities.forEach((k,v)->{
			v.tick();
			if(v.getY()<-1) {
				entities.remove(k);
			}
			});
		} catch(ConcurrentModificationException e) {}
		try {
		player.forEach((k,v)->v.tick());
		} catch(ConcurrentModificationException e) {}
		
		//removed GameState.isClient.getValue() check here
			
		used.clear();
		try {
		player.forEach((k,v)->{
			getChunk=v.getChunk();
			used.add(getChunk-1);
			used.add(getChunk);
			used.add(getChunk+1);
		});
		} catch(ConcurrentModificationException e) {}
		
		try {
		chunks.forEach((k,v)->{
			if(!used.contains(k)) {
				chunks.remove(k);
			}
		});
		} catch(ConcurrentModificationException e) {}
	}
	
	public Entity getEntity(int uid) {
		if(player.containsKey(uid)) {
			return player.get(uid);
		}
		return entities.get(uid);
	}
	
	public Block getBlock(int x, int y) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = getChunk(chunk, false);
		if(c==null) return null;
		return c.getBlock(x, y);
	}
	
	public void join(Entity e, float x, float y) {
		if(e instanceof Player) {
			if(!player.containsKey(e.getUID())) {
				player.put(e.getUID(), (Player)e);
				e.join(this, x, y);
			}
		} else {
			if(!entities.containsKey(e.getUID())) {
				entities.put(e.getUID(), e);
				e.join(this, x, y);
			}
		}
	}
	
	public void render(Shader s, Matrix4f matrix) {
		s.setUniform("projection", res);
		Assets.textures.get("sky").bind(0);
		Assets.models.get("square").render();
		try {
			entities.forEach((k,v)->{
				if(CMath.distance2((double)v.getX(), (double)v.getY(), ClientState.lPlayer.getX(), ClientState.lPlayer.getY())<ClientState.drawDistance) {
				v.render(s, matrix);
				}
			
			});
			} catch(ConcurrentModificationException e) {}
		while(true) {
			try {
					chunks.forEach((k,v)->v.render(s, matrix));
					break;
			} catch(ConcurrentModificationException e) {}
		}
		try {
		player.forEach((k,v)->v.render(s, matrix));
		} catch(ConcurrentModificationException e) {}
		
	}
	
	public World(long seed, String name) {
		this.seed=seed;
		this.name=name;
		generator=new WorldGenerator(this, seed);
	}
	
	//For client copy of the world, the client shouldn't know the seed
	public World(String name) {
		this.seed=0;
		this.name=name;
		generator=null;
	}
	
	public long getSeed() {
		return seed;
	}

	public boolean destroyBlock(byte cause, Entity issuer, int x, int y, boolean send) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = getChunk(chunk, false);
		if(c==null) return false;
		return c.destroyBlock(cause, issuer, x, y, send);
	}
	
	public boolean placeBlock(byte cause, Entity issuer, int x, int y, Block place, boolean send) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = getChunk(chunk, false);
		if(c==null) return false;
		return c.placeBlock(cause, issuer, x, y, place, send);
	}
	
	public String getName() {
		return name;
	}
	
}
