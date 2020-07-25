package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Inventory;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Point2f;
import com.bohdloss.fuckunclejack.render.Shader;

public class HUD {
private Inventory inventory;

public Bar health;
public Bar armour;
public InvDisplay invdisplay;
public Hotbar hotbar;

private boolean invopen;

//faster GC
private Point2f mpoint;
//end

	public HUD() {
		health=new Bar(Assets.textures.get("icon_health"), Assets.textures.get("bar_bg"), Assets.textures.get("bar_health"), -10.5f, -6.6f, 20, 20);
		armour=new Bar(Assets.textures.get("icon_health"), Assets.textures.get("bar_bg"), Assets.textures.get("bar_armour"), -10.5f, -5.4f, 100, 25);
		invdisplay=new InvDisplay();
		hotbar=new Hotbar();
	}
	
	public void toggleInventory() {
		invopen=!invopen;
	}
	
	public boolean isInvOpen() {
		return invopen;
	}
	
	public void render(Shader s, Matrix4f matrix) {
		health.current=ClientState.lPlayer.getHealth();
		health.render(s, matrix);
		armour.render(s, matrix);
		if(invopen) {
			invdisplay.render(s, matrix);
		} else {
			hotbar.render(s, matrix);
		}
		if(ClientState.grabbed!=null) {
			mpoint=CMath.mGLCoord();
			ClientState.grabbed.render(s, matrix, mpoint.x, mpoint.y);
		}
	}
	
}
