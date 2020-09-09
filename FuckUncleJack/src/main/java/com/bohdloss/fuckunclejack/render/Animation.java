package com.bohdloss.fuckunclejack.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class Animation {

private int total;
private long last=0;	
private static Shader gui;
private static final long wait=250;
protected float speed=1f;
protected int currentIndex=0;
protected boolean looping;
private Vector3f[] hand;
private Vector2f[] scale;
protected TileSheet[] split;
private static int statid=0;
private static final double MAXFRAMES=10d;

static {
	gui=Assets.shaders.get("gui");
}

private void construct(int offset, TileSheet frames, int index) throws Exception{
	BufferedImage output = new BufferedImage(frames.getTiles()[0].getWidth(), (int)((double)frames.getTiles()[0].getHeight()*(CMath.clampMax((double)offset+MAXFRAMES, (double)frames.getTiles().length)-(double)offset)), BufferedImage.TYPE_INT_ARGB);
	Graphics g = output.getGraphics();
	
	int amo=0;
	for(int i=offset;i<CMath.clampMax(frames.getTiles().length, offset+MAXFRAMES);i++) {
		g.drawImage(frames.getTiles()[i], 0, frames.getTiles()[i].getHeight()*(i-offset), null);
		amo++;
	}
	
	//ImageIO.write(output, "png", new File("C:/Users/Antonio/Desktop/tilesheet"+statid+"i"+index+".png"));
	
	split[index]=new TileSheet(output, amo);
}

public Animation(TileSheet frames, boolean looping, String json) throws Exception{
	this.looping=looping;
	this.total=frames.getAmount();
	parseJson(json);
	
	split=new TileSheet[((int)((double)frames.getAmount()/MAXFRAMES))+1];
	
	int remaining=frames.getAmount();
	int offset=0;
	int index=0;
	
	while(true) {
		int done = remaining-(int)CMath.clampMin(remaining-MAXFRAMES, 0);
		
		if(done==0) break;
		construct(offset, frames, index);
		offset+=done;
		remaining-=done;
		index++;
	}
	statid++;
}

public Animation(String path, int amount, boolean looping, String json) throws Exception{
	this(new TileSheet(path, amount), looping, json);
}

public Animation(BufferedImage img, int amount, boolean looping, String json) throws Exception{
	this(new TileSheet(img, amount), looping, json);
}

public Animation(Texture txt, int amount, boolean looping, String json) throws Exception{
	this(new TileSheet(txt, amount), looping, json);
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
	int pick = CMath.fastFloor((double)currentIndex/MAXFRAMES);
	split[pick].bindTile(gui, (int)(currentIndex-MAXFRAMES*pick));
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
