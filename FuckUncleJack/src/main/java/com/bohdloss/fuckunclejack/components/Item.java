package com.bohdloss.fuckunclejack.components;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.BlockTexture;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.render.TileSheet;

public abstract class Item implements Tickable{

protected int uses;
protected int used;

protected float damage=1;
protected float breakspeed=1;

protected int amount;

protected boolean usable=true;
protected boolean available;
public String texture;
	
public ItemSlot owner;

//GC
static Matrix4f translate=new Matrix4f();
static Matrix4f res=new Matrix4f();

static Model slot;
static TileSheet sheet;
static Shader gui;
static Model itemm;
static Model smallrect;
static Model rect;
static Model bigrect;
static Texture smallrectt;
//End

static {
	slot=Assets.models.get("square");
	sheet=Assets.sheets.get("font");
	gui=Assets.shaders.get("gui");
	itemm=Assets.models.get("item");
	smallrect=Assets.models.get("smallrect");
	rect=Assets.models.get("rect");
	bigrect=Assets.models.get("bigrect");
	smallrectt=Assets.textures.get("smallrect");
}

public Item(int uses, int used, int amount, String texture) {
	this.uses=uses;
	this.used=used;
	this.amount=(int)CMath.clamp(amount, 1, getMax());
	this.texture=texture;
	if(amount>0) available=true;
}

public ItemEventProperties properties() {
	return new ItemEventProperties(this).setDamage(damage).setBreakspeed(breakspeed);
}

public static ItemEventProperties defaultProperties() {
	return new ItemEventProperties(null).setDamage(1).setBreakspeed(0);
}

public ItemEventProperties onRightClickBegin(float x, float y, Entity entity) {
	owner.owner.owner.itemRightBegin(this);
	return properties();
}
public ItemEventProperties onRightClickEnd(float x, float y, Entity entity) {
	owner.owner.owner.itemRightEnd(this);
	return properties();
}
public ItemEventProperties onLeftClickBegin(float x, float y, Entity entity) {
	owner.owner.owner.itemLeftBegin(this);
	return properties();
}
public ItemEventProperties onLeftClickEnd(float x, float y, Entity entity) {
	owner.owner.owner.itemLeftEnd(this);
	return properties();
}

public void use(int amount) {
	if(amount>0) used=(int)CMath.clamp(used+amount, 0, uses);
	if(used==uses) destroy();
}

public void repair(int amount) {
	if(amount>0) used=(int)CMath.clamp(used-amount, 0, uses);
}

public void decrease(int x) {
	if(x>0) amount=(int)CMath.clamp(amount-x, 0, getMax());
	if(amount==0) {
		available=false;
		totalDestroy();
	}
}

private void totalDestroy() {
	owner.setContent(null);
}

public void increase(int x) {
	if(x>0) amount=(int)CMath.clamp(amount+x, 0, getMax());
	if(amount>0) available=true;
}

public void destroy() {
	usable=false;
	used=uses;
	decrease(1);
}

public int getAmount() {
	return amount;
}

public int getUsed() {
	return used;
}

public int getUses() {
	return uses;
}

public void setUsed(int used) {
	this.used=used;
}

public void setUses(int uses) {
	this.uses=uses;
}

public void setAmount(int amount) {
	this.amount=amount;
}

public String getTexture() {
	return texture;
}

public void render(Shader s, Matrix4f matrix, float x, float y, boolean showInfo) {
	translate.identity().translate(x, y, 0f);
	res = matrix.mul(translate, res);
	s.setProjection(res);
	
	Texture t = Assets.textures.get(texture);
	BlockTexture bt = Assets.blocks.get(texture);
		
	s.setUniform("gray", grayFactor());
	if(t!=null) {
		t.bind(0);
		itemm.render();
	} else if(bt!=null) {
		bt.render(itemm, s, res);
	}
	s.setUniform("gray", 0f);
		
	if(showInfo&amount>1) {
		char[] chars = (""+amount).toCharArray();
		smallrectt.bind(0);
		float calcx=x;
		if(chars.length==1) {
			smallrect.render();
		} else if(chars.length==2) {
			rect.render();
			calcx-=0.125f;
		} else {
			bigrect.render();
			calcx-=0.25f;
		}
		
		FontManager.renderString(calcx, y, sheet, gui, matrix, slot, ""+amount);
	}
		s.bind();
}

public int getMax() {
	return 100;
}

public float grayFactor() {
	return (uses==0&used==0)?0f:((float)used/(float)uses);
}

public abstract int getId();

}
