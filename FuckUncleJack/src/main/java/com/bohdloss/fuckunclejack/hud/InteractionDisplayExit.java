package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;
import static com.bohdloss.fuckunclejack.main.Assets.*;

import com.bohdloss.fuckunclejack.generator.generators.DeserthouseWorld;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.render.*;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;

public class InteractionDisplayExit {

//cache
static Model background;
static Model slot;
static Texture txt;
static Shader gui;
static TileSheet font;

static Matrix4f translation;
static Matrix4f res;
//end
	
static {
	background=models.get("interaction");
	slot=models.get("item");
	txt=textures.get("interaction");
	gui=shaders.get("gui");
	font=sheets.get("font");
	
	translation=new Matrix4f();
	res=new Matrix4f();
}

public void render(Shader s, Matrix4f mat) {
	if(ClientState.lPlayer.getWorld() instanceof DeserthouseWorld) {
		translation.identity().translate(0, 2, 0);
		res=mat.mul(translation, res);
		s.setProjection(res);
		
		txt.bind(0);
		background.render();
		
		FontManager.renderString(-FontManager.strWidth("F: Exit house")/2-(0.8f*0.25f), 2, font, gui, mat, slot, "F: Exit house");
		
	}
}

}
