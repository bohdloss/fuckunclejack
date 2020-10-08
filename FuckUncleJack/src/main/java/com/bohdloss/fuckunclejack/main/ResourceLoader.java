package com.bohdloss.fuckunclejack.main;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import com.bohdloss.fuckunclejack.render.TileSheet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

	public static TileSheet[] sheetArray(String json) throws Exception{
		JSONObject parsed = (JSONObject) loadJSON(json);
		String prefix = parsed.get("prefix").toString();
		String suffix = parsed.get("suffix").toString();
		
		JSONArray ar = (JSONArray) parsed.get("data");
		TileSheet[] res = new TileSheet[ar.size()];
		
		for(int i=0;i<ar.size();i++) {
			JSONObject obj = (JSONObject) ar.get(i);
			int tiles = Integer.parseInt(obj.get("tiles").toString());
			res[i] = new TileSheet(loadImage(prefix+i+suffix), tiles);
		}
		
		return res;
	}
	
}
