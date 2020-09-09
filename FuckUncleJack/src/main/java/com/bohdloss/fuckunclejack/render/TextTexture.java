package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class TextTexture extends Texture{

private String text;
private Font font;
private Color color;
private float strwidth;
private float strheight;
private static final float mulConst=25;

	public TextTexture(String text, Font font, Color color) throws Exception {
		super((String)null);
		this.text=text;
		this.font=font;
		this.color=color;
		update();
	}

	public TextTexture setText(String text) {
		this.text=text;
		return this;
	}
	
	private void change(boolean delete) {
		
		if(delete)glDeleteTextures(id);
		
		int w=img.getWidth();
		int h=img.getHeight();
		width=w;
		height=h;
		int pixels_raw[] = new int[w*h*4];
		pixels_raw=img.getRGB(0, 0, w, h, null, 0, w);
		ByteBuffer pixels = BufferUtils.createByteBuffer(w*h*4);
		for(int i=0;i<pixels_raw.length;i++) {
				int pixel = pixels_raw[i];
				pixels.put((byte)((pixel>>16)&0xFF));
				pixels.put((byte)((pixel>>8)&0xFF));
				pixels.put((byte)((pixel)&0xFF));
				pixels.put((byte)((pixel>>24)&0xFF));
		}
		
		pixels.flip();
		
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
	
	public void update() {
		if(Thread.currentThread().getId()!=1) {
			queueUpdate();
			return;
		}
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true); 
		int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		int textheight = (int)(font.getStringBounds(text, frc).getHeight());
		strwidth=textwidth;
		strheight=textheight;
		BufferedImage image = new BufferedImage((int)(textwidth), (int)(textheight), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.setFont(font);
		
		int drawx = (textwidth-g.getFontMetrics().stringWidth(text))/2;
		int drawy = ((textheight-g.getFontMetrics().getHeight())/2) + g.getFontMetrics().getAscent();
		
		g.drawString(text, drawx, drawy);
		
		img=image;
		
		change(id!=-1);
	}
	
	public void queueUpdate() {
		Function<Object> updateFunc = new Function<Object>() {
			@Override
			public Object execute() {
				update();
				return null;
			}
		};
		try {
			MainEvents.waitReturnValue(MainEvents.queue(updateFunc));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public String getText() {
		return text;
	}
	
	public TextTexture setFont(Font font) {
		this.font=font;
		return this;
	}
	
	public Font getFont() {
		return font;
	}
	
	public Color getColor() {
		return color;
	}
	
	public TextTexture setColor(Color color) {
		this.color=color;
		return this;
	}
	
	public static TextTexture generate(String text, Font font, Color color) {
		Function<Object> func = new Function<Object>() {
			public Object execute() throws Throwable{
				return new TextTexture(text, font, color);
			}
		};
		try {
			return (TextTexture) MainEvents.waitReturnValue(MainEvents.queue(func));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public float getGLWidth() {
		return strwidth/mulConst;
	}
	
	public float getGLHeight() {
		return strheight/mulConst;
	}
}
