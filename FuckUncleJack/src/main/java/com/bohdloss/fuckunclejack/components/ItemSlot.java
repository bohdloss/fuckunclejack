package com.bohdloss.fuckunclejack.components;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.render.TileSheet;

public class ItemSlot {

private Item content;
public Inventory owner;

//GC
private Matrix4f res=new Matrix4f();
private Matrix4f translate=new Matrix4f();

private static Texture slotbg;
private static Texture slotbghov;
private static Texture smallrectt;
private static Model slot;
private static Model itemm;
private static Model smallrect;
private static Model rect;
private static Model bigrect;

private static TileSheet sheet;
private static Shader gui;
//end

static {
	slotbg=Assets.textures.get("slot_bg");
	slotbghov=Assets.textures.get("slot_bg_hovered");
	slot=Assets.models.get("square");
	itemm=Assets.models.get("item");
	sheet=Assets.sheets.get("font");
	gui=Assets.shaders.get("gui");
	smallrect=Assets.models.get("smallrect");
	smallrectt=Assets.textures.get("smallrect");
	rect=Assets.models.get("rect");
	bigrect=Assets.models.get("bigrect");
}

public ItemSlot(Inventory owner) {
	this.owner=owner;
}
	
public Item getContent() {
	return content;
}

public boolean isEmpty() {
	return content==null;
}

public void setContent(Item content) {
	this.content=content;
	if(content!=null) {
	this.content.owner=this;
	}
}

public int getId() {
	return content==null?-1:content.getId();
}

public void render(Shader s, Matrix4f matrix, float x, float y, boolean selected) {
	translate.identity().translate(x, y, 0f);
	res = matrix.mul(translate, res);
	s.setUniform("projection", res);
	slotbg.bind(0);
	slot.render();
	
	if(content!=null) {
		content.render(s, matrix, x, y, true);
	}
	
	if(selected) {
		slotbghov.bind(0);
		slot.render();
	}
}

}
