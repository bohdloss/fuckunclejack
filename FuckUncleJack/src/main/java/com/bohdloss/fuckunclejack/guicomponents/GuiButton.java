package com.bohdloss.fuckunclejack.guicomponents;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class GuiButton extends GuiComponent{

//cache
static Matrix4f res=new Matrix4f();
static Matrix4f translate=new Matrix4f();

static Model square;
static TileSheet button;
static Shader gui;
static TileSheet font;

static {
	square=Assets.models.get("square");
	button=Assets.sheets.get("buttons");
	gui=Assets.shaders.get("gui");
	font=Assets.sheets.get("font");
}

	public GuiButton(MenuTab tab, String text, float x, float y) {
		super(tab);
		this.text=text;
		this.x=x;
		this.y=y;
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		if(!visible) return;
		float width=2;
		float scale=width+0.8f;
		translate.identity().translate(x, y, 0).scale(scale, 1, 1);
		res=matrix.mul(translate, res);
		gui.bind();
		gui.setUniform("projection", res);
		
		button.bindTile(gui, status);
		square.render();
		FontManager.renderString(x-width/2f, y, font, gui, matrix, square, text);
		s.bind();
	}
	
	@Override
	public float getY() {
		return y-0.5f;
	}
	
	@Override
	public float getX() {
		float width=2;
		float scale=width+0.8f;
		return x-0.5f*scale;
	}
	
	@Override
	public float getWidth() {
		float width=2;
		float scale=width+0.8f;
		return scale;
	}
	
	@Override
	public float getHeight() {
		return 1;
	}

	@Override
	public void pressed() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void hovered() {
		
	}
	
}
