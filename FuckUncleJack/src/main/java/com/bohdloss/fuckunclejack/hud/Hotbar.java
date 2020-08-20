package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;

public class Hotbar {
	
//GC
private Matrix4f res=new Matrix4f();
private Matrix4f translate=new Matrix4f();

private Model bg;
private Texture bgtxt;

private float calcx;
//end

public Hotbar() {
	
	bg=Assets.models.get("hotbar");
	bgtxt=Assets.textures.get("hotbar_bg");
}
	
public void render(Shader s, Matrix4f matrix) {
	translate.identity().translate(0, -6f, 0);
	res=matrix.mul(translate, res);
	s.setUniform("projection", res);
	bgtxt.bind(0);
	bg.render();
	for(int x=0;x<9;x++) {
		calcx=(float)x*(1.15f)-4.6f;
		
		boolean selected=false;
		
		if(ClientState.sel()==x) {
			selected=true;
		}
		
		lPlayer.getInventory().slots[x].render(s, matrix, calcx, -6f, selected);
		
	}
}

}
