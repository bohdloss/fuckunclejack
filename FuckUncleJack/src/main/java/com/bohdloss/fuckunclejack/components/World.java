package com.bohdloss.fuckunclejack.components;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.client.ChunkRequest;
import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.entities.PlayerEntity;
import com.bohdloss.fuckunclejack.generator.WorldGenerator;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public abstract class World implements Tickable{

public HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();
public HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
public HashMap<Integer, PlayerEntity> player = new HashMap<Integer, PlayerEntity>();
protected long seed;
protected WorldGenerator generator;
protected static Matrix4f res = new Matrix4f().scale(2);

private String name;

public HashMap<Integer, Chunk> cachedChunks = new HashMap<Integer, Chunk>();

public boolean needsLightmap=true;

//cache
protected List<Integer> used = new ArrayList<Integer>();
protected int getChunk;
protected static Model square;
protected Texture bg;
private ArrayList<Entity> priority = new ArrayList<Entity>();
//

static {
	square=Assets.models.get("square");
}

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
	
	public void tick(float delta) {
		
		//Tick everything
		
		try {
		chunks.forEach((k,v)->v.tick(delta));
		} catch(ConcurrentModificationException e) {}
		try {
		entities.forEach((k,v)->{
			v.tick(delta);
			if(v.getY()<-1) {
				//entities.remove(k);
			}
			});
		} catch(ConcurrentModificationException e) {}
		try {
		player.forEach((k,v)->v.tick(delta));
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
		if(e instanceof PlayerEntity) {
			if(!player.containsKey(e.getUID())) {
				PlayerEntity p = (PlayerEntity)e;
				player.put(e.getUID(), p);
				e.join(this, x, y);
				if(!GameState.isClient.getValue()) {
					System.out.println("Player "+p.getName()+" joined world "+ getName());
				}
			}
		} else {
			if(!entities.containsKey(e.getUID())) {
				entities.put(e.getUID(), e);
				e.join(this, x, y);
			}
		}
	}
	
	protected abstract Texture getTexture();
	
	protected void renderBackground(Shader s, Matrix4f matrix) {
		s.setProjection(res);
		bg.bind(0);
		square.render();
	}
	
	public void render(Shader s, Matrix4f matrix) {
		priority.clear();
		while(true) {
			try {
				renderBackground(s, matrix);
				entities.forEach((k,v)->{
					if(CMath.diff(ClientState.lPlayer.getX(), v.getX()) < ClientState.xdrawDistance && CMath.diff(ClientState.lPlayer.getY(), v.getY()) < ClientState.ydrawDistance) {
						if(!v.prioritizeRender) {
							v.render(s, matrix);
						} else {
						priority.add(v);
						}
					}
				});
				MetaData.calculateStatic();
				chunks.forEach((k,v)->v.render(s, matrix));
				priority.forEach(v->v.render(s, matrix));
				player.forEach((k,v)->v.render(s, matrix));
				
				break;
			} catch(ConcurrentModificationException e) {}
		}
	}
	
	public World(long seed, String name) {
		this(name);
		this.seed=seed;
		generator=new WorldGenerator(this, seed);
	}
	
	//For client copy of the world, the client shouldn't know the seed
	public World(String name) {
		this.name=name;
		bg = getTexture();
	}
	
	public long getSeed() {
		return seed;
	}

	public boolean destroyBlock(byte cause, Entity issuer, int x, int y, boolean background, boolean send) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = getChunk(chunk, false);
		if(c==null) return false;
		return c.destroyBlock(cause, issuer, x, y, background, send);
	}
	
	public boolean placeBlock(byte cause, Entity issuer, int x, int y, Block place, boolean background, boolean send) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = getChunk(chunk, false);
		if(c==null) return false;
		return c.placeBlock(cause, issuer, x, y, place, background, send);
	}
	
	public String getName() {
		return name;
	}
	
	public abstract int getID();
	
}
