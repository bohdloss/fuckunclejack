package com.bohdloss.fuckunclejack.guicomponents;

import java.util.concurrent.Callable;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.bohdloss.fuckunclejack.components.Tickable;
import com.bohdloss.fuckunclejack.main.Game;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.CRectanglef;
import com.bohdloss.fuckunclejack.render.Shader;

public abstract class GuiComponent implements Tickable{

public String text;
public float x;
public float y;
public float rot;

public static final int DISABLED=0;

public static final int IDLE=1;
public static final int HOVERED=2;
public static final int PRESSED=3;

public int status=IDLE;

private Callable<Integer> onClick;

public CRectanglef bounds=new CRectanglef(0,0,0,0);

public boolean visible=true;
	
protected MenuTab owner;
	
	public GuiComponent(MenuTab owner) {
		this.owner=owner;
		owner.components.add(this);
	}
	
	public MenuTab getOwner() {
		return owner;
	}
	
	public abstract void pressed();
	public abstract void reset();
	public abstract void hovered();
	
	public int executeAction() {
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
	
	public abstract void render(Shader s, Matrix4f matrix);
	
	@Override
	public void tick(float delta) {
		if(!visible|status==DISABLED) return;
		bounds.setFrame(getX(), getY(), getWidth(), getHeight());
		boolean intersects = bounds.pIntersects(MenuTab.mouse);
		
		switch(status) {
		case DISABLED:
			return;
		case PRESSED:
			if(!Game.window.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				if(intersects) {
					reset();
					executeAction();
				}
				status=HOVERED;
			}
		break;
		case IDLE:
			if(intersects) {
				hovered();
				status=HOVERED;
			}
		break;
		case HOVERED:
			if(Game.window.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				pressed();
				status=PRESSED;
			} else if(!intersects) {
				status=IDLE;
			}
		break;
		}
	}

	public void setAction(Callable<Integer> callable) {
		onClick=callable;
	}
	
	@Override
	public void finalize() {
		owner.components.remove(this);
	}
	
	public abstract float getX();
	public abstract float getY();
	public abstract float getWidth();
	public abstract float getHeight();
	
}
