package com.bohdloss.fuckunclejack.generator.dungeoncommon;

import java.util.ArrayList;

import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.components.Tickable;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.logic.GameState;

public class Dungeon implements Tickable{

public static RoomData data;
private boolean init=false;	

static {
	init: 
	{
		if(GameState.isClient.getValue()) break init;
		try {
			//Initialization goes here
			
			data = new RoomData("/data/dungeons/Debug2.json");
			
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Initialized dungeons");
	}
}
	
public ArrayList<Room> rooms = new ArrayList<Room>();
public ArrayList<Vector2f> coords = new ArrayList<Vector2f>();
public World source;

public Dungeon(World source) {
	this.source=source;
	rooms.add(new Room(data));
	coords.add(new Vector2f(0, 50));
}

public void place(float x, float y) {
	for(int i=0;i<rooms.size();i++) {
		Vector2f vec = coords.get(i);
		Room room = rooms.get(i);
		
		room.place((int)vec.x, (int)vec.y, source);
	}
}

@Override
public void tick(float delta) {
	if(!init) {
		init=true;
		place(0, 50);
	}
}

}
