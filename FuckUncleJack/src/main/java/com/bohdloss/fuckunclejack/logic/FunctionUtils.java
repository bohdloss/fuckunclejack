package com.bohdloss.fuckunclejack.logic;

import java.util.Random;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.blocks.*;
import com.bohdloss.fuckunclejack.components.entities.*;
import com.bohdloss.fuckunclejack.generator.generators.*;
import com.bohdloss.fuckunclejack.components.items.blocks.*;
import com.bohdloss.fuckunclejack.logic.events.PlayerJoinEvent;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.server.Server;

public class FunctionUtils {

	public static ItemDrop genItemEntity(Item item) {
		ItemDrop drop = new ItemDrop(item);
		Random r = new Random();
		float velx=(r.nextFloat()-0.5f)/2f;
		float vely=(r.nextFloat()/2f)+0.1f;
		drop.setVelocity(velx, vely);
		return drop;
	}

	public static Block genBlockById(int id, World world, int x, int y) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = world.chunks.get(chunk);
		return genBlockByIdChunk(id, c, x-(c.getOffsetx()*16), y);
	}

	public static Block genBlockByIdChunk(int id, Chunk c, int x, int y) {
		switch(id) {
		case 0:
			return new AirBlock(c, x, y);
		case 1:
			return new BedrockBlock(c, x, y);
		case 2:
			return new DirtBlock(c, x, y);
		case 3:
			return new GrassBlock(c, x, y);
		case 4:
			return new LogBlock(c, x, y);
		case 5:
			return new StoneBlock(c, x, y);
		case 6:
			return new SandBlock(c, x, y);
		case 7:
			return new SandstoneBlock(c, x, y);
		case 8:
			return new CactusBlock(c, x, y);
		}
		return null;
	}
	
	public static Item genItemById(int itemid, int amount) {
		switch(itemid) {
		case 0:
			return new AirBlockItem(amount);
		case 1:
			return new BedrockBlockItem(amount);
		case 2:
			return new DirtBlockItem(amount);
		case 3:
			return new GrassBlockItem(amount);
		case 4:
			return new LogBlockItem(amount);
		case 5:
			return new StoneBlockItem(amount);
		case 6:
			return new SandBlockItem(amount);
		case 7:
			return new SandstoneBlockItem(amount);
		case 8:
			return new CactusBlockItem(amount);
		}
		return null;
	}
	
	public static Entity genEntityById(int entid, Object[] data) {
		switch(entid) {
		case 1:
			int id = (int) data[0];
			int amount = (int) data[1];
			return new ItemDrop(genItemById(id, amount));
		case 2:
			return new DesertHouse();
		case 3:
			return new Prop((String)data[0], (String)data[1], (float)data[2], (float)data[3], (boolean)data[4], (boolean)data[4]);
		}
		return null;
	}
	
	public static void travel(Player p, World w, int x, int y) {
		if(GameState.isClient.getValue()) return;
		
		p.getWorld().player.remove(p.getUID());
		
		Server.threads.forEach(v->{
			v.events.add(new PlayerJoinEvent(p, GameEvent.changeDim, w));
		});
		
		w.join(p, x, y);
		
		System.out.println("Player "+p.getName()+" traveled to world "+w.getName());
	}
	
	public static World genWorldById(int id, String name) {
		switch(id) {
		case 0:
			return new OverworldWorld(name);
		case 1:
			return new DeserthouseWorld(name);
		}
		return null;
	}
	
}
