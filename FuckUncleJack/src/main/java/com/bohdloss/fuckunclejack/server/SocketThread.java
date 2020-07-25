package com.bohdloss.fuckunclejack.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.bohdloss.fuckunclejack.components.BlockLayer;
import com.bohdloss.fuckunclejack.components.Chunk;
import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.components.ItemSlot;
import com.bohdloss.fuckunclejack.components.entities.*;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.logic.events.PlayerLeaveEvent;

import static com.bohdloss.fuckunclejack.server.CSocketUtils.*;

public class SocketThread extends Thread {

public Socket socket;
public String ip;
public boolean auth=false;
public boolean shouldStop=false;
public int UPID;
public boolean writeAuth=false;
public Player player;

//in case inventory is edited
public boolean sendInventory=false;

public List<Integer> done = new ArrayList<Integer>();

public List<GameEvent> events = new ArrayList<GameEvent>();

//CACHE
private ByteBuffer buf = ByteBuffer.allocate(bufferSize);

public ByteBuffer lengthBuf=ByteBuffer.allocate(4);

private ByteBuffer rec = ByteBuffer.allocate(bufferSize);

public List<Chunk> pregen = new ArrayList<Chunk>();
//END

//Important: check last time a whole package has been received,
//safety measure in case the connection gets stuck

public static final long timeoutTime=5000;
public long lastReceivedTime=System.currentTimeMillis();

public SocketThread(Socket socket) {
	this.socket=socket;
	ip=socket.getRemoteSocketAddress().toString();
	Server.threads.add(this);
}
	
public void run() {
	try {
	while(!socket.isClosed()) {
		
		// To avoid calling the deprecated Thread.stop()
		
		if(shouldStop)break;
		
		//Fill the buffer, read and try auth, then write
		
		try {
		fillObject();
		} catch(Exception e) {e.printStackTrace();System.out.println(buf.position());} finally {
			buf.limit(buf.position());
		}
		
		write(socket, buf);
		read(socket, rec, lengthBuf);
		
/*		byte[] b = rec.array();
		
		for(int i=0;i<b.length;i++) {
			System.out.print((b[i]&0xff)+".");
		}
		System.out.println(); */
		
		//Write down the time it is done reading
		lastReceivedTime=System.currentTimeMillis();
		
		DataHandler.handleBuffer(rec, this);
		
/*		if(!auth) {
			socket.close();
			break;
		} */
		
	}
	} catch(Exception e) {
		e.printStackTrace();
	}
	Server.threads.forEach(v->{
		v.events.add(new PlayerLeaveEvent(player, GameEvent.leave));
	});
	GameState.dimensions.forEach((k,v)->{
		System.out.println(v.player.remove(UPID));
		});
	GameState.players.remove(UPID);
	Server.threads.remove(this);
	try {
		socket.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

public void fillObject() {
	clearBuf(buf);
	
	if(writeAuth) {
		writeAuth=false;
		
		//Credentials marker AKA login result 
		
		buf.put(CREDENTIALS);
		buf.put(auth?(byte)1:(byte)0);
		buf.putInt(UPID);
	} 
	if(auth) {
		
		//put player data marker
		buf.put(PLAYERDATA);
		
		int length=player.getWorld().player.size()-1;
		buf.putInt(length);
		player.getWorld().player.forEach((k,v)->{
			if(!k.equals(UPID)) {
				writeString(buf, v.getName());
				buf.putInt(v.getUID());
				buf.putFloat(v.getX());
				buf.putFloat(v.getY());
				buf.putFloat(v.getVelocity().x);
				buf.putFloat(v.getVelocity().y);
			}
		}); 
		
		//put Event array marker and write length
		
		buf.put(EVENT);
		
		try {
		buf.putInt(events.size());
		events.forEach(e->{
			
			buf.put(e.bytes().array());
		});
		events.clear();
		
		//in case of exception, clear the buffer, to avoid corrupt data
		
		} catch(ConcurrentModificationException e) {
			clearBuf(buf);
			putEnd(buf);
			return;
		}
		
		//put entities marker (works just like playerdata, but its an array)
		
		buf.put(ENTITIES);
		
		try {
			
			buf.putInt(player.getWorld().entities.size());
			
			player.getWorld().entities.forEach((k,v)->{
				buf.putInt(k);
				buf.putFloat(v.x);
				buf.putFloat(v.y);
				buf.putFloat(v.getVelocity().x);
				buf.putFloat(v.getVelocity().y);
				buf.putInt(v.getId());
				
				switch(v.getId()) {
				case 1:
					ItemDrop d = (ItemDrop) v;
					buf.putInt(d.getItem().getId());
					buf.putInt(d.getItem().getAmount());
				break;
				}
				
			});
			
		} catch(ConcurrentModificationException e) {
			clearBuf(buf);
			putEnd(buf);
			return;
		}
		
		if(!pregen.isEmpty()) {
			buf.put(CHUNKS);
			buf.putInt(pregen.size());
			pregen.forEach(chunk->{
				buf.putInt(chunk.getOffsetx());
				BlockLayer[][] layers = chunk.blocks;
				for(int x=0;x<16;x++) {
					for(int y=0;y<256;y++) {
						BlockLayer layer = layers[x][y];
						buf.putInt(layer.getTop().getId());
						buf.putInt(layer.getBackground().getId());
					}
				}
			});
			pregen.clear();
		}
		
		if(sendInventory) {
			sendInventory=false;
			buf.put(INVENTORY);
			Inventory inv = player.getInventory();
			for(int i=0;i<inv.slots.length;i++) {
				ItemSlot s = inv.slots[i];
				if(s.getContent()==null) {
					buf.putInt(-1);
					buf.putInt(0);
				} else {
					buf.putInt(s.getContent().getId());
					buf.putInt(s.getContent().getAmount());
				}
			}
		}
	}
	putEnd(buf);
}

}
