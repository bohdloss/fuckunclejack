package com.bohdloss.fuckunclejack.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.ResourceLoader;

import static com.bohdloss.fuckunclejack.render.CMath.*;

public class BlockTexture {

public static final int NOSIDES = 0;
public static final int ALLSIDES = 16843009;

public static final int TOP = 16777216;
public static final int RIGHT = 256;
public static final int BOTTOM = 65536;
public static final int LEFT = 1;

public static final int TOPBOTTOM = 16842752;
public static final int LEFTRIGHT = 257;

public static final int TOPRIGHT = 16777472;
public static final int TOPLEFT = 16777217;

public static final int BOTTOMRIGHT = 65792;
public static final int BOTTOMLEFT = 65537;

public static final int TOPBOTTOMRIGHT = 16843008;
public static final int TOPBOTTOMLEFT = 16842753;

public static final int RIGHTLEFTTOP = 16777473;
public static final int RIGHTLEFTBOTTOM = 65793;

public static final int indexOf(int sides) {
	switch(sides) {
	case ALLSIDES: return 0;
	case TOP: return 1;
	case RIGHT: return 2;
	case BOTTOM: return 3;
	case LEFT: return 4;
	case TOPBOTTOM: return 5;
	case LEFTRIGHT: return 6;
	case TOPRIGHT: return 7;
	case TOPLEFT: return 8;
	case BOTTOMRIGHT: return 9;
	case BOTTOMLEFT: return 10;
	case TOPBOTTOMRIGHT: return 11;
	case TOPBOTTOMLEFT: return 12;
	case RIGHTLEFTTOP: return 13;
	case RIGHTLEFTBOTTOM: return 14;
	}
	return -1;
}

public static TileSheet sheet;
private static Shader gui;

static {
	gui = Assets.shaders.get("gui");
	sheet = Assets.sheets.get("blockborder");
}

public void render(Model m, Shader s, Matrix4f matrix, int sidecode) {
	//s.bind();
	s.setProjection(matrix);
	t.bind(0);
	m.render();
	if(sidecode==NOSIDES) return;
	gui.bind();
	gui.setProjection(matrix);
	sheet.bindTile(gui, indexOf(sidecode));
	m.render();
	s.bind();
}

public void render(Model m, Shader s, Matrix4f matrix) {
	render(m, s, matrix, ALLSIDES);
}

private Texture t;

public BlockTexture(String path) throws Exception{
	BufferedImage img = ResourceLoader.loadImage(path);
	t = new Texture(img);
	
	
}

}
