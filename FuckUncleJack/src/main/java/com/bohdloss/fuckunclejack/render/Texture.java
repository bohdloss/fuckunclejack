package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {

private BufferedImage img;
private int width;
private int height;
private int id;

public Texture(BufferedImage img) {
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

public Texture(String path) throws Exception{
	this(ImageIO.read(Texture.class.getResourceAsStream(path)));
}
	
public void bind(int sampler) {
	if(sampler >=0 & sampler<=31) { //why????
	glActiveTexture(GL_TEXTURE0+sampler); //why did i even implement this what does it do???
	glBindTexture(GL_TEXTURE_2D, id); //actual bind command
	}
}

public BufferedImage getImg() {
	return img; //just in case
}

public int getWidth() {
	return width; //you could getImg().getWidth() but still, just in case
}

public int getHeight() {
	return height; //same as other method
}

public int getId() {
	return id; //idk may be useful
}

}
