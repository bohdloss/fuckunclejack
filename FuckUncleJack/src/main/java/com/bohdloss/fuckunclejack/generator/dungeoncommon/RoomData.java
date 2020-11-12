package com.bohdloss.fuckunclejack.generator.dungeoncommon;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.logic.structs.BlockData;
import com.bohdloss.fuckunclejack.logic.structs.CustomEntityData;
import com.bohdloss.fuckunclejack.logic.structs.NormalEntityData;
import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class RoomData {

public float width, height;
	
public ArrayList<CustomEntityData> customs = new ArrayList<CustomEntityData>();
public ArrayList<NormalEntityData> normals = new ArrayList<NormalEntityData>();
public ArrayList<BlockData> blocks = new ArrayList<BlockData>();
	
public RoomData(String path) throws Exception{
	this((JSONObject)ResourceLoader.loadJSON(path));
}

public RoomData(JSONObject obj) {
	JSONObject info = (JSONObject) obj.get("info");
	width = Float.parseFloat(info.get("width").toString());
	height = Float.parseFloat(info.get("height").toString());
		
	JSONArray cust = (JSONArray) obj.get("custom");
	JSONArray norm = (JSONArray) obj.get("entities");
	JSONArray bl = (JSONArray) obj.get("blocks");
	
	for(int i=0;i<cust.size();i++) {
		JSONObject current = (JSONObject) cust.get(i);
		
		//Physics related
		boolean physics = Boolean.parseBoolean(current.get("physics").toString());
		boolean collision = Boolean.parseBoolean(current.get("collision").toString());
		
		//Rendering related
		String texture = current.get("texture").toString();
		float xscale = Float.parseFloat(current.get("xscale").toString());
		float yscale = Float.parseFloat(current.get("yscale").toString());
		
		//World related
		float x = Float.parseFloat(current.get("x").toString());
		float y = Float.parseFloat(current.get("y").toString());
		float width = Float.parseFloat(current.get("width").toString());
		float height = Float.parseFloat(current.get("height").toString());
		
		//Construct object
		CustomEntityData data = new CustomEntityData();
		data.physics=physics;
		data.collision=collision;
		data.texture=texture;
		data.xscale=xscale;
		data.yscale=yscale;
		data.x=x;
		data.y=y;
		data.width=width;
		data.height=height;
		
		customs.add(data);
	}
	
	for(int i=0;i<norm.size();i++) {
		JSONObject current = (JSONObject) norm.get(i);
		
		//Gather data
		int id = Integer.parseInt(current.get("id").toString());
		float x = Float.parseFloat(current.get("x").toString());
		float y = Float.parseFloat(current.get("y").toString());
		
		//Construct object
		NormalEntityData data = new NormalEntityData();
		data.id=id;
		data.x=x;
		data.y=y;
		
		normals.add(data);
	}
	
	for(int i=0;i<bl.size();i++) {
		JSONObject current = (JSONObject) bl.get(i);
		
		//Gather data
		int id = Integer.parseInt(current.get("id").toString());
		int x = Integer.parseInt(current.get("x").toString());
		int y = Integer.parseInt(current.get("y").toString());
		boolean background = Boolean.parseBoolean(current.get("background").toString());
		
		//Construct object
		BlockData data = new BlockData();
		data.id=id;
		data.x=x;
		data.y=y;
		data.background=background;
		
		blocks.add(data);
	}
}

}
