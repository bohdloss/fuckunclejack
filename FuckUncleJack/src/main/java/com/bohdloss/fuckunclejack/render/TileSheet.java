package com.bohdloss.fuckunclejack.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;

public class TileSheet {

private Texture texture;

private Matrix4f scale;
private Matrix4f translation;
private int amount;
	
private BufferedImage[] tiles;

	public TileSheet(String path, int amount) throws Exception{
		this(new Texture(path), amount);
	}

	public TileSheet(Texture texture, int amount) throws Exception{
		this.texture=texture;
		this.amount=amount;
		
		scale = new Matrix4f().scale(1f, 1f/(float)amount, 1f);
		translation = new Matrix4f();
	}
	
	public TileSheet(BufferedImage img, int amount) throws Exception{
		this(new Texture(img), amount);
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
		BufferedImage img = texture.getImg();
		
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
