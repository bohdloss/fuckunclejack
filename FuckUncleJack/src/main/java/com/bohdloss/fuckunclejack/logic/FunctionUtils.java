package com.bohdloss.fuckunclejack.logic;

import java.nio.ByteBuffer;
import java.util.Random;

import com.bohdloss.fuckunclejack.components.Block;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.Item;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.blocks.*;
import com.bohdloss.fuckunclejack.components.entities.*;
import com.bohdloss.fuckunclejack.generator.generators.*;
import com.bohdloss.fuckunclejack.components.items.BowItem;
import com.bohdloss.fuckunclejack.components.items.WinnerswordItem;
import com.bohdloss.fuckunclejack.components.items.blocks.*;
import com.bohdloss.fuckunclejack.logic.events.PlayerJoinEvent;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Animation;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.server.Server;

import static com.bohdloss.fuckunclejack.server.CSocketUtils.*;

public class FunctionUtils {

	public static ItemDropEntity genItemEntity(Item item) {
		ItemDropEntity drop = new ItemDropEntity(item);
		Random r = new Random();
		float velx=(r.nextFloat()-0.5f)/2f;
		float vely=(r.nextFloat()/2f)+0.1f;
		drop.setVelocity(velx, vely);
		return drop;
	}

	public static Block genBlockById(int id, World world, int x, int y) {
		int chunk = CMath.fastFloor(((double)x/16d));
		Chunk c = world.getChunk(chunk, false);
		if(c==null) return null;
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
		case 9:
			return new WinnerswordItem();
		case 10:
			return new BowItem();
		}
		return null;
	}
	
	public static Entity genEntityById(int entid, Object[] data) {
		switch(entid) {
		case 1:
			int id = (int) data[0];
			int amount = (int) data[1];
			return new ItemDropEntity(genItemById(id, amount));
		case 2:
			return new DesertHouseEntity();
		case 3:
			return new PropEntity((float)data[0], (float)data[1], (String)data[2], (float)data[3], (float)data[4], (boolean)data[5], (boolean)data[6]);
		case 4:
			return new ProjectileEntity((String)data[0], null);
		case 5:
			return new StaticProjectileEntity((String)data[0], (float)data[1]);
		}
		return null;
	}
	
	public static Object[] genDataById(ByteBuffer buf, int id, Object[] databuffer) {
		databuffer=null;
		switch(id) {
		case 1:
			databuffer=new Object[2];
			databuffer[0]=buf.getInt();
			databuffer[1]=buf.getInt();
			break;
		case 3:
			databuffer=new Object[7];
			databuffer[0]=buf.getFloat();
			databuffer[1]=buf.getFloat();
			databuffer[2]=readString(buf);
			databuffer[3]=buf.getFloat();
			databuffer[4]=buf.getFloat();
			databuffer[5]=(boolean)(buf.get()==(byte)1);
			databuffer[6]=(boolean)(buf.get()==(byte)1);
			break;
		case 4:
			databuffer=new Object[1];
			databuffer[0]=readString(buf);
			break;
		case 5:
			databuffer=new Object[2];
			databuffer[0]=readString(buf);
			databuffer[1]=buf.getFloat();
			break;
		}
		return databuffer;
	}
	
	public static void travel(PlayerEntity p, World w, int x, int y) {
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
	
	public static Animation getLobbyAnimation(int id) {
		switch(id) {
		case 0:
			return Assets.animationSets.get("dad").longIdle;
		}
		return null;
	}
	
}
