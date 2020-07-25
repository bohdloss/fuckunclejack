package com.bohdloss.fuckunclejack.render;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ModelLoader {

	public static Model load(String location) throws Exception {
		Model res=null;
		String file = read(location);
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(file);
		
		JSONArray vertices = (JSONArray) obj.get("vertices");
		JSONArray tex_coords = (JSONArray) obj.get("tex_coords");
		JSONArray indices = (JSONArray) obj.get("indices");
		
		float[] vert=new float[vertices.size()];
		float[] tex=new float[tex_coords.size()];
		int[] ind=new int[indices.size()];
		
		for(int i=0;i<vert.length;i++) {
			vert[i]=Float.valueOf((String)vertices.get(i));
		}
		for(int i=0;i<tex.length;i++) {
			tex[i]=Float.valueOf((String)tex_coords.get(i));
		}
		for(int i=0;i<ind.length;i++) {
			ind[i]=Integer.valueOf((String)indices.get(i));
		}
		
		res = new Model(vert, tex, ind);
		
		return res;
	}
	
	private static String read(String location) {
		try {
			String res="";
			String line="";
			BufferedReader br = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(location)));
			while((line=br.readLine())!=null) {
				res=res+line+"\n";
			}
			res=res.trim();
			br.close();
			return res;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
