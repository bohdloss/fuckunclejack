package com.bohdloss.fuckunclejack.main;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.json.simple.parser.JSONParser;

public final class ResourceLoader {

	private ResourceLoader() {}
	
	public static BufferedImage loadImage(String path) throws Exception{
		return ImageIO.read(ResourceLoader.class.getResourceAsStream(path));
	}
	
	public static byte[] loadBytes(String path) throws Exception{
		InputStream is = ResourceLoader.class.getResourceAsStream(path);
		byte[] b = new byte[is.available()];
		is.read(b,0,b.length);
		is.close();
		return b;
	}
	
	public static String loadText(String path) throws Exception{
		return new String(loadBytes(path));
	}
	
	public static Object loadJSON(String path) throws Exception{
		JSONParser parser = new JSONParser();
		return parser.parse(loadText(path));
	}
	
}
