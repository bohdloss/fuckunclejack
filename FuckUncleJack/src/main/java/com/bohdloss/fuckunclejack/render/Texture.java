package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class Texture {
	
protected int width=-1;
protected int height=-1;
protected int id=-1;

protected static List<WeakReference<Texture>> instances = new ArrayList<WeakReference<Texture>>();
public static List<WeakReference<Texture>> getInstances() {
	return instances;
}

public Texture disposeAll(boolean gc) {
	glDeleteTextures(id);
	if(gc) System.gc();
	return this;
}

public Texture disposeAll() {
	return disposeAll(false);
}

public Texture(BufferedImage img, boolean keepImg) {
	if(img!=null) {
	int w=img.getWidth();
	int h=img.getHeight();
	width=w;
	height=h;
	int pixels_raw[] = new int[w*h*4];
	pixels_raw=img.getRGB(0, 0, w, h, null, 0, w);
	ByteBuffer pixels = BufferUtils.createByteBuffer(w*h*4);
	for(int i=0;i<pixels_raw.length;i++) {
			int pixel = pixels_raw[i];
			pixels.put((byte)((pixel>>16)&0xFF)); //RED
			pixels.put((byte)((pixel>>8)&0xFF)); //GREEN
			pixels.put((byte)((pixel)&0xFF)); //BLUE
			pixels.put((byte)((pixel>>24)&0xFF)); //ALPHA
	}
	
	pixels.flip();
	
	id = glGenTextures(); //get id for binding
	
	glBindTexture(GL_TEXTURE_2D, id); //bind texture to edit settings
	
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels); //write the texture in video card
	
	}
}

public Texture(String path, boolean keepImage) throws Exception{
	this(path==null?null:ResourceLoader.loadImage(path));
}

public Texture(String path) throws Exception{
	this(path, false);
}

public Texture(BufferedImage img) {
	this(img, false);
}

public void bind(int sampler) {
	if(sampler >=0 && sampler<=31 && id!=-1) {
	glActiveTexture(GL_TEXTURE0+sampler);
	glBindTexture(GL_TEXTURE_2D, id);
	}
}

public int getWidth() {
	return width;
}

public int getHeight() {
	return height;
}

public int getId() {
	return id;
}

public static Texture generate(BufferedImage img) {
	Function<Object> func = new Function<Object>() {
		public Object execute() throws Throwable{
			return new Texture(img);
		}
	};
	try {
		return (Texture) MainEvents.waitReturnValue(MainEvents.queue(func));
	} catch (Throwable e) {
		e.printStackTrace();
	}
	return null;
}

public static Texture generate(String path) {
	Function<Object> func = new Function<Object>() {
		public Object execute() throws Throwable{
			return new Texture(path);
		}
	};
	try {
		return (Texture) MainEvents.waitReturnValue(MainEvents.queue(func));
	} catch (Throwable e) {
		e.printStackTrace();
	}
	return null;
}

}
