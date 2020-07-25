package com.bohdloss.fuckunclejack.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;

public class FontManager {

private static final char[] letters=" abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\\|!\"£$%&/()=?'^ì'<>èé[{€+*]}òç@à°#ù§,;.:-_".toCharArray();
	
//GC
private static Matrix4f translation = new Matrix4f();
private static Matrix4f res = new Matrix4f();
//end
	
	public static TileSheet load(Font font, Color color) {
		BufferedImage img = new BufferedImage(64, 64*letters.length, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.setFont(font);
		
		for(int i=0;i<letters.length;i++) {
			int drawx = (64-g.getFontMetrics().stringWidth(""+letters[i]))/2;
			int drawy = i*64 + ((64-g.getFontMetrics().getHeight())/2) + g.getFontMetrics().getAscent();
			g.drawString(""+letters[i], drawx, drawy);
		}
		
		Texture t = new Texture(img);
		TileSheet sheet = new TileSheet(t, letters.length);
		
		return sheet;
	}
	
	public static void renderString(float x, float y, TileSheet sheet, Shader shader, Matrix4f matrix, Model model, String string) {
		shader.bind();
		char[] chars = string.toCharArray();
		for(int i=0;i<chars.length;i++) {
			translation.identity().translate(x+i*(0.25f), y, 0);
			shader.setUniform("projection", matrix.mul(translation, res));
			sheet.bindTile(shader, indexOf(chars[i]));
			model.render();
		}
	}
	
	public static float strWidth(String input) {
		return 0.8f*0.25f*(float)input.length();
	}
	
	private static int indexOf(char c) {
		char[] chars=letters;
		for(int i=0;i<chars.length;i++) {
			if(chars[i]==c) return i;
		}
		return -1;
	}
	
}
