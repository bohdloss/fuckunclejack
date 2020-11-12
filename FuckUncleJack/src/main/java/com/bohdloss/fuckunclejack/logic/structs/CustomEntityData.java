package com.bohdloss.fuckunclejack.logic.structs;

import org.json.simple.JSONObject;

public class CustomEntityData {

public String texture;
public float xscale;
public float yscale;
public float width;
public float height;
public float x;
public float y;
public boolean collision;
public boolean physics;
	
public JSONObject getData(float offsetx, float offsety) {
	JSONObject obj = new JSONObject();
	obj.put("xscale", xscale+"f");
	obj.put("yscale", yscale+"f");
	obj.put("texture", texture);
	obj.put("x", (x-offsetx)+"f");
	obj.put("y", (y-offsety)+"f");
	obj.put("width", width+"f");
	obj.put("height", xscale+"f");
	obj.put("collision", collision+"");
	obj.put("physics", physics+"");
	
	return obj;
}

}
