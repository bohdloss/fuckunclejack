package com.bohdloss.fuckunclejack.menutabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Tickable;
import com.bohdloss.fuckunclejack.guicomponents.GuiComponent;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Point2f;
import com.bohdloss.fuckunclejack.render.Shader;

public abstract class MenuTab implements Tickable{
	
public static Point2f mouse=new Point2f(0,0);
public static MenuTab active;
public static HashMap<String, MenuTab> tabs = new HashMap<String, MenuTab>();
	
protected String name;
public List<GuiComponent> components = new ArrayList<GuiComponent>();
	
static {
	new MainTab();
}

public MenuTab(String name) {
	this.name=name;
	tabs.put(name, this);
}

public String getName() {
	return name;
}

public abstract void render(Shader s, Matrix4f matrix);

@Override
public void tick() {
	mouse = CMath.mGLCoord(Game.guiScale);
	
	components.forEach(component->{
		component.tick();
	});
	
}

}
