package com.bohdloss.fuckunclejack.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class Animation {

protected TileSheet frames;
private long last=0;	
private Shader gui;
private static final long wait=250;
protected float speed;
protected int currentIndex=0;
protected boolean looping;
private Vector3f[] hand;
private Vector2f[] scale;

public Animation(TileSheet frames, boolean looping, String json) {
	this.frames=frames;
	this.looping=looping;
	speed=1f;
	gui=Assets.shaders.get("gui");
	
	hand=new Vector3f[frames.getAmount()];
	scale=new Vector2f[frames.getAmount()];
	
	for(int i=0;i<frames.getAmount();i++) {
		hand[i]=new Vector3f();
		scale[i]=new Vector2f();
	}
	
	if(json!=null) {
	
		try {
		
		JSONObject info = (JSONObject) ResourceLoader.loadJSON("/data/animations/"+json+".json");
	
		JSONObject scalej = (JSONObject) info.get("scale");
	
		JSONArray xscale = (JSONArray) scalej.get("x");
		JSONArray yscale = (JSONArray) scalej.get("y");
		
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
	if(currentIndex==frames.getAmount()-1) {
		if(looping) {
		currentIndex=0;
		}
	} else {
		currentIndex++;
	}
}

public Vector3f getHandPosition() {
	return hand[(int)CMath.clamp(currentIndex, 0, frames.getAmount())];
}

public Vector2f getScale() {
	return scale[(int)CMath.clamp(currentIndex, 0, frames.getAmount())];
}

public void bind() {
	float add=(wait*(1/speed));
	long res=last+(long)add;
	long time=System.currentTimeMillis();
	if(time>=res) {
		last=System.currentTimeMillis();
		calcIndex();
	}
	frames.bindTile(gui, currentIndex);
}

public boolean isLooping() {
	return looping;
}

public void setLooping(boolean looping) {
	this.looping = looping;
}

}
