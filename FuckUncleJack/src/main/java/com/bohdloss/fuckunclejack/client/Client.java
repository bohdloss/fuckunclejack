package com.bohdloss.fuckunclejack.client;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameEvent;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.main.Main;
import com.bohdloss.fuckunclejack.server.SocketThread;

import static com.bohdloss.fuckunclejack.server.CSocketUtils.*;

import static com.bohdloss.fuckunclejack.logic.ClientState.*;

public class Client extends Thread{

public static List<GameEvent> events = new ArrayList<GameEvent>();
public static List<GameEvent> swapBuf = new ArrayList<GameEvent>();

public static HashMap<Integer, ChunkRequest> chunkrequest = new HashMap<Integer, ChunkRequest>();
public static Socket socket;	
public static boolean auth=false;
public static String upid;

public static boolean sendDebug=false;

public static List<String> invalidMatches = new ArrayList<String>();

//cache
private static ByteBuffer buf = ByteBuffer.allocate(bufferSize);

public ByteBuffer lengthBuf=ByteBuffer.allocate(4);

private static List<Integer> processed = new ArrayList<Integer>();

private static ByteBuffer rec = ByteBuffer.allocate(bufferSize);
//end

	public Client() {
		start();
	}
	
	public void swap() {
		List<GameEvent> save = events;
		events=swapBuf;
		swapBuf=save;
	}
	
	public void run() {
		while(true) {
		try {
			
			if(ClientState.state==ClientState.GAME) {
			
				try {
				
				socket = new Socket(ClientState.IP, ClientState.PORT);
				
				if(invalidMatches.contains(SocketThread.getIP(socket))) {
					System.out.println("disconnecting");
					socket.close();
					ClientState.disconnect();
					
					
				}
				
				while(!socket.isClosed()) {
					
					//try filling the buffer
					
					try {
					fillObject();
					} catch(Exception e) {e.printStackTrace();} finally {
						buf.limit(buf.position());
					}
					
					write(socket, buf);
					read(socket, rec, lengthBuf);
					
					
					
					DataHandler.handleBuffer(rec);
				
					sleep(GameState.replicationDelay);
					
				}
				
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					auth=false;
					invalidMatches.add(SocketThread.getIP(socket));
				}
				
			}
		
		sleep(100);
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		}
	}
	
	public void fillObject() throws Exception{
		
		swap();
		
		//clear buffer
		
		clearBuf(buf);
		
		if(auth) {
		
			
			
			if(sendDebug) {
				buf.put(DEBUG);
				sendDebug=false;
			}
			
			
			try {
				buf.put(EVENT);
				buf.putInt(swapBuf.size());
				swapBuf.forEach(e->{
					buf.put(e.bytes().array());
				});
				swapBuf.clear();
				} catch(ConcurrentModificationException e) {
					clearBuf(buf);
					putEnd(buf);
					return;
				}
			
			//Put playerdata marker
			buf.put(PLAYERDATA);
			
			buf.putFloat(lPlayer.x);
			buf.putFloat(lPlayer.y);
			buf.putFloat(lPlayer.getVelocity().x);
			buf.putFloat(lPlayer.getVelocity().y);
			buf.putInt(lPlayer.getInventory().selected);
			
			try {
				processChunks();
				if(!processed.isEmpty()) {
					buf.put(CHUNKS);
					buf.putInt(processed.size());
					processed.forEach(v->{
						buf.putInt(v);
					});
				}
			} catch(ConcurrentModificationException e) {
				clearBuf(buf);
				putEnd(buf);
			}
			
		} else {
			
			//Put credentials marker
			buf.put(CREDENTIALS);
			
			//Put playername
			writeString(buf, Main.name);
			
			//Put match id
			writeString(buf, "TODO");
			
		}
		putEnd(buf);
	}
	
	public static void processChunks() {
		processed.clear();
		chunkrequest.forEach((k,request)->{
			switch(request.status) {
			case ChunkRequest.UNSENT:
				processed.add(request.x);
				request.status=ChunkRequest.ELABORATING;
				break;
			case ChunkRequest.ELABORATING:
				request.expiration--;
				if(request.expiration<=0) chunkrequest.remove(k);
				break;
			case ChunkRequest.READY:
				lWorld.putChunk(request.chunk);
				chunkrequest.remove(k);
			}
		});
	}
	
}
