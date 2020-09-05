package com.bohdloss.fuckunclejack.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;

public class ModelLoader {

	public static Model load(String location) throws Exception {
		Model res=null;
		String file = ResourceLoader.loadText(location);
		return loadString(file);
	}
	
	public static Model loadString(String in) throws Exception {
		Model res=null;
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(in);
		
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
	
}
