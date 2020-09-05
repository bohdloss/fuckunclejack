package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class VolatileTexture extends Texture{
	
	public VolatileTexture() {
		super(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
	}
	
	public VolatileTexture(BufferedImage img) {
		super(img);
	}

	public static VolatileTexture generate(BufferedImage img) {
		Function<Object> func = new Function<Object>() {
			public Object execute() throws Throwable{
				return new VolatileTexture(img);
			}
		};
		try {
			return (VolatileTexture) MainEvents.waitReturnValue(MainEvents.queue(func));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static VolatileTexture generate() {
		Function<Object> func = new Function<Object>() {
			public Object execute() throws Throwable{
				return new VolatileTexture();
			}
		};
		try {
			return (VolatileTexture) MainEvents.waitReturnValue(MainEvents.queue(func));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void change(BufferedImage img) {
		Function<Object> func = new Function<Object>() {
			public Object execute() throws Throwable {
				update(img);
				return null;
			}
		};
		long id = MainEvents.queue(func);
		try {
			MainEvents.waitReturnValue(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void update(BufferedImage img) {
		glDeleteTextures(id);
		
		this.img=img;
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
	
}
