package com.bohdloss.fuckunclejack.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class TileSheet {

private Texture texture;
private BufferedImage img;
private Matrix4f scale;
private Matrix4f translation;
private int amount;
	
private BufferedImage[] tiles;

	public TileSheet disposeCache(boolean gc) {
		tiles=null;
		if(gc) System.gc();
		return this;
	}

	public TileSheet disposeAll(boolean gc) {
		disposeCache(false);
		img=null;
		if(gc) System.gc();
		return this;
	}
	
	public TileSheet disposeAll() {
		return disposeAll(false);
	}
	
	public TileSheet disposeCache() {
		disposeCache(false);
		return this;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public TileSheet(String path, int amount) throws Exception{
		this(new Texture(path), amount);
		this.img = ResourceLoader.loadImage(path);
	}
	
	public TileSheet(Texture texture, int amount) throws Exception{
		this.texture=texture;
		this.amount=amount;
		
		scale = new Matrix4f().scale(1f, 1f/(float)amount, 1f);
		translation = new Matrix4f();
	}
	
	public TileSheet(BufferedImage img, int amount) throws Exception{
		this(new Texture(img), amount);
		this.img=img;
	}
	
	public void bindTile(Shader s, int y) {
		scale.translate(0, y, 0, translation);
		
		s.setUniform("sampler", 0);
		s.setUniform("texModifier", translation);
		
		texture.bind(0);
	}
	
	public void test(Shader s, float y) {
		scale.translate(0, y, 0, translation);
		s.setUniform("texModifier", translation);
	}
	
	private void buildCache() {
		tiles=new BufferedImage[amount];
		
		for(int i=0;i<amount;i++) {
			tiles[i]=getPart(img, 0, CMath.fastFloor(((double)i*((double)img.getHeight()/(double)amount))), img.getWidth(), CMath.fastFloor((double)img.getHeight()/(double)amount));
		}
	}
	
	private static BufferedImage getPart(BufferedImage in, int startx, int starty, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(in, -startx, -starty, in.getWidth(), in.getHeight(), null);
		
		return img;
	}
	
	public BufferedImage[] getTiles() {
		if(tiles==null) buildCache();
		return tiles;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
