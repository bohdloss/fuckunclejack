package com.bohdloss.fuckunclejack.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

public class ImageLoader {
	
    public static GLFWImage load_image(String path) {
        BufferedImage originalImage=null;
        try {
            originalImage = ImageIO.read(ImageLoader.class.getResourceAsStream(path));
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return imageToGLFWImage(originalImage);
    }
    
    private static GLFWImage imageToGLFWImage(BufferedImage image) {
    	  if (image.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
    	    final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
    	    final Graphics2D graphics = convertedImage.createGraphics();
    	    final int targetWidth = image.getWidth();
    	    final int targetHeight = image.getHeight();
    	    graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
    	    graphics.dispose();
    	    image = convertedImage;
    	  }
    	  final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
    	  for (int i = 0; i < image.getHeight(); i++) {
    	    for (int j = 0; j < image.getWidth(); j++) {
    	      int colorSpace = image.getRGB(j, i);
    	      buffer.put((byte) ((colorSpace << 8) >> 24));
    	      buffer.put((byte) ((colorSpace << 16) >> 24));
    	      buffer.put((byte) ((colorSpace << 24) >> 24));
    	      buffer.put((byte) (colorSpace >> 24));
    	    }
    	  }
    	  buffer.flip();
    	  final GLFWImage result = GLFWImage.create();
    	  result.set(image.getWidth(), image.getHeight(), buffer);
    	  return result;
    	}

    
}