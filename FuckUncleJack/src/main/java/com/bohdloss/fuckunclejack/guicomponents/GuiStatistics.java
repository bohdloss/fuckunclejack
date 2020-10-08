package com.bohdloss.fuckunclejack.guicomponents;

import java.awt.Color;
import java.awt.Font;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.Label;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class GuiStatistics extends GuiComponent {

public static final GuiStatistics INSTANCE;

private static Matrix4f matBuf;
private static Matrix4f res;
private static Model square;
private static Texture background;
private static Texture reflection;
private static Texture battery;

private static Label displayBattery;

static {
	res = new Matrix4f();
	matBuf = new Matrix4f();
	INSTANCE = new GuiStatistics();
	square = Assets.models.get("square");
	background = Assets.textures.get("stats_bg");
	reflection = Assets.textures.get("stats_reflection");
	battery = Assets.textures.get("battery");
	
	displayBattery=new Label("50 %", new Font("Arial", Font.BOLD, 15), new Color(128,128,128));
}
	
	private GuiStatistics() {
		super(MenuTab.tabs.get("ghost"));
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

	@Override
	public void render(Shader s, Matrix4f matrix) {
		s.bind();
		
		//Game.guiScale=Game.scaleAmount-17.8f;
		
		matBuf.set(matrix);
		
		matBuf.scale(0.75f, 0.65f, 1);
		
		s.setProjection(matBuf.translate(0, 0, 0, res).scale(27, 3, 1, res));
		
		background.bind(0);
		square.render();
		
		displayBattery.setTranslation(-6.4f, 0);
		displayBattery.render(s, matBuf);
		
		s.setProjection(matBuf.translate(0, 0, 0, res).scale(27, 3, 1, res));
		
		reflection.bind(0);
		square.render();
		
		s.setProjection(matBuf.translate(-10.85f, 0.035f, 0, res).scale(2.6f,1.25f,1,res));
		battery.bind(0);
		square.render();
		
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public void finalize() {
		//don't care
	}
	
}
