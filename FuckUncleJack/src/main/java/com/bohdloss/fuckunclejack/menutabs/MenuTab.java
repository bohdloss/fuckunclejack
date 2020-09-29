package com.bohdloss.fuckunclejack.menutabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Tickable;
import com.bohdloss.fuckunclejack.guicomponents.AnimationPacket;
import com.bohdloss.fuckunclejack.guicomponents.AnimationSystem;
import com.bohdloss.fuckunclejack.guicomponents.GuiComponent;
import com.bohdloss.fuckunclejack.guicomponents.MainAnimationPacket;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Point2f;
import com.bohdloss.fuckunclejack.render.Shader;

public class MenuTab implements Tickable{
	
public static Point2f mouse=new Point2f(0,0);
public static MenuTab active;
public static HashMap<String, MenuTab> tabs = new HashMap<String, MenuTab>();

protected AnimationSystem fade;
protected static Matrix4f translation=new Matrix4f();
protected static Matrix4f res=new Matrix4f();
protected String name;
public List<GuiComponent> components = new ArrayList<GuiComponent>();
protected static Model square;	
public boolean ignoreInput=false;

public static void init() {
	square=Assets.models.get("square");
	new MenuTab("ghost");
	new MainTab();
	new SettingsTab();
	new CharacterTab();
}

private static void transition(boolean withstart, boolean withend, MenuTab tab) {
	if(withstart) {
		ClientState.fadeToBlack();
	} else {
		Game.fadeVal=1;
	}
	ClientState.state=ClientState.MENU;
	tab.onActivate();
	active=tab;
	if(withend) {
		ClientState.fadeFromBlack();
	} else {
		Game.fadeVal=0;
	}
}

public static void bindTab(boolean start, boolean end, String name) {
	if(!tabs.containsKey(name)) return;
	transition(start, end, tabs.get(name));
}

public MenuTab(String name) {
	this.name=name;
	tabs.put(name, this);
}

public String getName() {
	return name;
}

public void onActivate() {
	fade.reset();
	fade.start();
	ignoreInput=true;
}

public void renderComponents(Shader s, Matrix4f matrix) {
	components.forEach(v->{
		v.render(s, matrix);
	});
}

public void render(Shader s, Matrix4f matrix) {}

public void calcMatrix(AnimationPacket packet) {
	translation.identity().translate(packet.getX(), packet.getY(), 0).rotate(packet.getRot(), 0, 0, 1).scale(packet.getXscale(), packet.getYscale(), 1);
}
public void setShader(Shader s, Matrix4f trans, Matrix4f mat) {
	s.setProjection(mat.mul(trans, res));
}

public void renderSprite(String name) {
	Assets.textures.get(name).bind(0);
	square.render();
}

@Override
public void tick(float delta) {
	if(ignoreInput&&fade.over()) {
		ignoreInput=false;
	}
	if(ignoreInput) return;
	
	mouse = CMath.mGLCoord(Game.guiScale);
	
	components.forEach(component->{
		component.tick(0);
	});
	
}

}
