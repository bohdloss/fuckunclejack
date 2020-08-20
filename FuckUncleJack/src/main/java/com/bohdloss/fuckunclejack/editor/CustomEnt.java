package com.bohdloss.fuckunclejack.editor;

import java.awt.image.BufferedImage;

import org.json.simple.JSONObject;

public class CustomEnt {

public BufferedImage txt;
public String texture;
public float xscale;
public float yscale;
public float width;
public float height;
public float x;
public float y;
public boolean collision;
public boolean physics;
	
public JSONObject getData() {
	JSONObject obj = new JSONObject();
	obj.put("xscale", xscale+"f");
	obj.put("yscale", yscale+"f");
	obj.put("texture", texture);
	obj.put("x", x+"f");
	obj.put("y", y+"f");
	obj.put("width", width+"f");
	obj.put("height", xscale+"f");
	obj.put("collision", collision+"");
	obj.put("physics", physics+"");
	
	return obj;
}

}
