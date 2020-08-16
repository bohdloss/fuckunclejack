package com.bohdloss.fuckunclejack.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class Button {

public static List<Button> buttons = new ArrayList<Button>();
	
public String text;
public float x;
public float y;

public static final int DISABLED=0;
public static final int PRESSED=1;
public static final int IDLE=2;
public static final int HOVERED=3;

public int status=IDLE;

private Callable<Integer> onClick;

public boolean visible=true;

//cache
static Matrix4f res=new Matrix4f();
static Matrix4f translate=new Matrix4f();

static Model square;
static TileSheet button;
static Shader gui;
static TileSheet font;

public CRectanglef bounds=new CRectanglef(0,0,0,0);

static {
	square=Assets.models.get("square");
	button=Assets.sheets.get("buttons");
	gui=Assets.shaders.get("gui");
	font=Assets.sheets.get("font");
}

	public Button(String text, float x, float y) {
		this.text=text;
		this.x=x;
		this.y=y;
		buttons.add(this);
	}
	
	
	public void render(Shader s, Matrix4f matrix) {
		if(!visible) return;
		float width=2;
		float scale=width+0.8f;
		translate.identity().translate(x, y, 0).scale(scale, 1, 1);
		bounds.setFrame(x-0.5f*scale, y-0.5f, scale, 1);
		res=matrix.mul(translate, res);
		gui.bind();
		gui.setUniform("projection", res);
		
		button.bindTile(gui, status);
		square.render();
		FontManager.renderString(x-width/2f, y, font, gui, matrix, square, text);
		s.bind();
	}
	
	public void setAction(Callable<Integer> callable) {
		onClick=callable;
	}
	
	public int click() {
		if(!visible) return 1;
		if(onClick!=null) {
			try {
				return onClick.call();
			} catch(Exception e) {
				e.printStackTrace();
				return 1;
			}
		} else {
			return 1;
		}
	}
	
	@Override
	public void finalize() {
		buttons.remove(this);
	}
	
}
