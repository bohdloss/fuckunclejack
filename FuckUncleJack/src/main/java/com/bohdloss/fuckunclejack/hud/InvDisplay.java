package com.bohdloss.fuckunclejack.hud;

import static com.bohdloss.fuckunclejack.main.Game.*;

import java.awt.Point;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.components.Inventory;
import static com.bohdloss.fuckunclejack.logic.ClientState.*;
import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class InvDisplay {
	
public int hovIndex=-1;

//Assets
private Texture background;
private Model bgmodel;

//faster GC
private Matrix4f translate=new Matrix4f();
private Matrix4f res=new Matrix4f();

private float hovx;
private float hovy;
private float visiblex;
private float visibley;
private Point pos;

private int savedi;

private float calcx;
private float calcy;

private boolean calculated=false;
//end

public InvDisplay() {
	background=Assets.textures.get("inventory_bg");
	bgmodel=Assets.models.get("inventory_bg");
}

public void render(Shader s, Matrix4f matrix) {
	//background
	
	translate.identity().scale(1.1f);
	res = matrix.mul(translate, res);
	s.setUniform("projection", res);
	background.bind(0);
	bgmodel.render();
	
	//slots
	
	int i=0;
	
	calculated=false;
	for(int iy=0;iy<4;iy++) {
		for(int ix=0;ix<9;ix++) {
			
			calcx=(float)ix*(1.15f)-4.6f;
			calcy=(iy==3?-1f:0f)-(float)iy*(1.15f);
			
			translate.identity().translate(calcx, calcy, 0f);
			
			savedi=inventoryCalc(i, calcx, calcy);
			if(savedi!=-1) hovIndex=savedi;
			
			boolean selected=false;
			if(hovIndex==i) {
				selected=true;
			}
			
			lPlayer.getInventory().slots[i].render(s, matrix, calcx, calcy, selected);
			
			i++;
		}
	}
	
	if(!calculated) {
		hovIndex=-1;
	}
}

public int inventoryCalc(int i, float calcx, float calcy) {
	if(calculated) return -1;
	visiblex=(float)window.getWidth()/(float)guiScale;
	visibley=(float)window.getHeight()/(float)guiScale;
	
	pos = window.getCursorPos();
	hovx=CMath.limit(pos.x, 0, window.getWidth());
	hovy=CMath.limit(pos.y, 0, window.getHeight());
	
	hovx=(hovx/window.getWidth());
	hovy=(hovy/window.getHeight());
	
	hovx/=(1d/(double)visiblex);
	hovy/=-(1d/(double)visibley);
	
	hovx-=visiblex/2f;
	hovy+=visibley/2f;
	
	if(CMath.distance(hovx, hovy, calcx, calcy)<0.5f) {
		calculated=true;
		return i;
	}
	return -1;
}

}
