package com.bohdloss.fuckunclejack.guicomponents;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.Shader;

public class GuiStatistics extends GuiComponent {

public static final GuiStatistics INSTANCE;

private static Matrix4f res;

static {
	res=new Matrix4f();
	INSTANCE = new GuiStatistics();
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
		s.setProjection(matrix.scale(10, 1, 1, res));
		Assets.textures.get("empty").bind(0);
		Assets.models.get("square").render();
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
