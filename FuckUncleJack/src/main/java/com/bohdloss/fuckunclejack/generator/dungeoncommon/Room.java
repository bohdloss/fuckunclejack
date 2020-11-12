package com.bohdloss.fuckunclejack.generator.dungeoncommon;

import java.util.ArrayList;
import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.components.World;
import com.bohdloss.fuckunclejack.components.entities.PropEntity;
import com.bohdloss.fuckunclejack.logic.FunctionUtils;
import com.bohdloss.fuckunclejack.logic.structs.CustomEntityData;
import com.bohdloss.fuckunclejack.logic.structs.NormalEntityData;

public class Room {
	
public int id;
private int x, y;
public RoomData data;
public World world;

public ArrayList<Entity> entities = new ArrayList<Entity>();

public Room(RoomData source) {
	this.data=source;
	
	for(int i=0;i<data.customs.size();i++) {
		CustomEntityData info = data.customs.get(i);
		
		PropEntity entity = new PropEntity(info.xscale, info.yscale, info.texture, info.width, info.height, info.collision, info.physics);
		entities.add(entity);
	}
	
	for(int i=0;i<data.normals.size();i++) {
		NormalEntityData info = data.normals.get(i);
		
		Entity entity = FunctionUtils.genEntityById(info.id, info.params);
		entities.add(entity);
	}
}

public void place(int x, int y, World world) {
	this.x=x;
	this.y=y;
	this.world=world;
	
	int e=0;
	for(int i=0;i<data.customs.size();i++) {
		world.join(entities.get(e), data.customs.get(i).x+x, data.customs.get(i).y+y);
		e++;
	}
	for(int i=0;i<data.normals.size();i++) {
		world.join(entities.get(e), data.normals.get(i).x+x, data.normals.get(i).y+y);
		e++;
	}
}

public int getId() {
	return id;
}
public float getX() {
	return x;
}
public float getY() {
	return y;
}

}
