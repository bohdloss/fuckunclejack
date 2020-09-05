package com.bohdloss.fuckunclejack.menutabs;

import java.awt.Color;
import java.util.concurrent.Callable;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.guicomponents.AnimationSystem;
import com.bohdloss.fuckunclejack.guicomponents.GuiButton;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Shader;

public class SettingsTab extends MenuTab {

	public SettingsTab() {
		super("settings");
		
		fade=new AnimationSystem("settings");
		
		new GuiButton(this, "Back", 0f, -1.5f, 4f, 1.25f, Assets.sheets.get("menubuttons"), new Color(100, 0, 0, 0)).setAction(new Callable<Integer>(){
			public Integer call() {
				ClientState.showMenu(true, true, "main");
				return 0;
			}
		});
	}

	@Override
	public void render(Shader s, Matrix4f matrix) {
		
		//Background
		
		translation.identity().translate(0, 0, 0).scale(26.9f, 15.2f, 1);
		setShader(s, translation, matrix);
		renderSprite("mainbackground");
		
		//Nothing's here!
		
		String str = "Nothing's here yet! Go back!";
		FontManager.renderString(-FontManager.strWidth(str)/2, 0, Assets.sheets.get("font"), Assets.shaders.get("gui"), matrix, Assets.models.get("square"), str);
	
		//Button
		
		s.bind();
		components.get(0).render(s, matrix);
		
	}

}
