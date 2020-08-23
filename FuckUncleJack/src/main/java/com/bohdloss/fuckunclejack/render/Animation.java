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

public class Animation {

protected TileSheet frames;
private long last=0;	
private Shader gui;
private static final long wait=250;
protected float speed;
protected int currentIndex=0;
protected boolean looping;
private List<Vector3f> hand;
private List<Vector2f> scale;

public Animation(TileSheet frames, boolean looping, String json) {
	this.frames=frames;
	this.looping=looping;
	speed=1f;
	gui=Assets.shaders.get("gui");
	
	if(json!=null) {
	
		hand = new ArrayList<Vector3f>();
		scale = new ArrayList<Vector2f>();
		
		JSONObject info = parse(json);
	
		JSONObject scalej = (JSONObject) info.get("scale");
	
		JSONArray xscale = (JSONArray) scalej.get("x");
		JSONArray yscale = (JSONArray) scalej.get("y");
		
		for(int i=0;i<xscale.size();i++) {
			scale.add(new Vector2f(Float.parseFloat((String)xscale.get(i)), Float.parseFloat((String)yscale.get(i))));
		}
	
		JSONObject handj = (JSONObject) info.get("hand");
	
		JSONArray xhand = (JSONArray) handj.get("x");
		JSONArray yhand = (JSONArray) handj.get("y");
		JSONArray rothand = (JSONArray) handj.get("rot");
		
		for(int i=0;i<xhand.size();i++) {
			hand.add(new Vector3f(Float.parseFloat((String)xhand.get(i)), Float.parseFloat((String)yhand.get(i)), Float.parseFloat((String)rothand.get(i))));
		}
	
	}
	
}

private JSONObject parse(String json) {
	try {
		JSONObject obj = new JSONObject();
		
		String res="";
		String line="";
		BufferedReader br = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream("/data/animations/"+json+".json")));
		while((line=br.readLine())!=null) {
			res=res+line+"\n";
		}
		res=res.trim();
		br.close();
		
		JSONParser parser = new JSONParser();
		obj = (JSONObject) parser.parse(res);
		return obj;
		
	} catch(Exception e) {
		e.printStackTrace();
	}
	throw new IllegalStateException();
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
	if(hand==null) return null;
	return hand.get(currentIndex);
}

public Vector2f getScale() {
	if(scale==null) return null;
	return scale.get(currentIndex);
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
