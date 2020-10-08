package com.bohdloss.fuckunclejack.render;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class Animation {

private int total=0;
private long last=0;	
private static Shader gui;
private static final long wait=250;
protected float speed=1f;
protected int currentIndex=0;
protected boolean looping;
private Vector3f[] hand;
private Vector2f[] scale;
protected TileSheet sheet;

static {
	gui=Assets.shaders.get("gui");
}

public Animation(TileSheet sheet, boolean looping, String json) throws Exception{
	this.looping=looping;
	parseJson(json);
	this.sheet=sheet.disposeAll();
}

public Animation(String sheet, int amount, boolean looping, String json) throws Exception{
	this(new TileSheet(sheet, amount).disposeAll(), looping, json);
}

public void setSpeed(float speed) {
	this.speed=speed;
}

public float getSpeed() {
	return speed;
}

public void clear() {
	last=System.currentTimeMillis();
	currentIndex=0;
}

private void calcIndex() {
	if(currentIndex==total-1) {
		if(looping) {
		currentIndex=0;
		}
	} else {
		currentIndex++;
	}
}

public Vector3f getHandPosition() {
	return hand[(int)CMath.clamp(currentIndex, 0, total)];
}

public Vector2f getScale() {
	return scale[(int)CMath.clamp(currentIndex, 0, total)];
}

public void bind() {
	float add=(wait*(1/speed));
	long res=last+(long)add;
	long time=System.currentTimeMillis();
	if(time>=res) {
		last=System.currentTimeMillis();
		calcIndex();
	}
	sheet.bindTile(gui, currentIndex);
}

public boolean isLooping() {
	return looping;
}

public void setLooping(boolean looping) {
	this.looping = looping;
}

private void parseJson(String json) {
	if(json!=null) {
		
		try {
		
		JSONObject info = (JSONObject) ResourceLoader.loadJSON("/data/animations/"+json+".json");
	
		JSONObject scalej = (JSONObject) info.get("scale");
	
		JSONArray xscale = (JSONArray) scalej.get("x");
		JSONArray yscale = (JSONArray) scalej.get("y");
		
		//initialize array
		hand=new Vector3f[xscale.size()];
		scale=new Vector2f[xscale.size()];
		for(int i=0;i<xscale.size();i++) {
			hand[i]=new Vector3f();
			scale[i]=new Vector2f();
		}
		
		for(int i=0;i<xscale.size();i++) {
			scale[i].x=Float.parseFloat((String)xscale.get(i));
			scale[i].y=Float.parseFloat((String)yscale.get(i));
		}
	
		JSONObject handj = (JSONObject) info.get("hand");
	
		JSONArray xhand = (JSONArray) handj.get("x");
		JSONArray yhand = (JSONArray) handj.get("y");
		JSONArray rothand = (JSONArray) handj.get("rot");
		
		for(int i=0;i<xhand.size();i++) {
			hand[i].x=Float.parseFloat((String)xhand.get(i));
			hand[i].y=Float.parseFloat((String)yhand.get(i));
			hand[i].z=Float.parseFloat((String)rothand.get(i));
		}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't load additional animation data");
		}
		
	}
}

}
