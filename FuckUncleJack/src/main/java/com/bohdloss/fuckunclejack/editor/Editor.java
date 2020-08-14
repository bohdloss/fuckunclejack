package com.bohdloss.fuckunclejack.editor;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class Editor {

private static Model square;
private static Shader gui;
private static TileSheet font;
private static int i;
private static Matrix4f res=new Matrix4f();
private static Matrix4f translation=new Matrix4f();
public static float scroll=0f;

static {
	square = Assets.models.get("square");
	gui = Assets.shaders.get("gui");
	font = Assets.sheets.get("font");
}

	public static void render(Shader s, Matrix4f matrix) {
		i=0;
		Assets.textures.forEach((k,v)->{
			translation.identity().translate(-12, 4-i+scroll, 0);
			res=matrix.mul(translation, res);
			s.setUniform("projection", res);
			
			v.bind(0);
			square.render();
			
			FontManager.renderString(-11, 4-i+scroll, font, gui, matrix, square, k);
			
			s.bind();
			
			i++;
		});
	}
	
}
