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

public abstract class Item {

protected int uses;
protected int used;

protected float damage=1;
protected float breakspeed=1;

protected int amount;

protected boolean usable=true;
protected boolean available;
protected String texture;
	
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
	this.amount=(int)CMath.limit(amount, 1, getMax());
	this.texture=texture;
	if(amount>0) available=true;
}

public ItemEventProperties properties() {
	return new ItemEventProperties(this).setDamage(damage).setBreakspeed(breakspeed);
}

public static ItemEventProperties defaultProperties() {
	return new ItemEventProperties(null).setDamage(1).setBreakspeed(0);
}

abstract public ItemEventProperties onRightClickBegin(int x, int y, Entity entity);
abstract public ItemEventProperties onRightClickEnd(int x, int y, Entity entity);
abstract public ItemEventProperties onLeftClickBegin(int x, int y, Entity entity);
abstract public ItemEventProperties onLeftClickEnd(int x, int y, Entity entity);

public void use(int amount) {
	if(amount>0) used=(int)CMath.limit(used+amount, 0, uses);
	if(used==uses) destroy();
}

public void repair(int amount) {
	if(amount>0) used=(int)CMath.limit(used-amount, 0, uses);
}

public void decrease(int x) {
	if(x>0) amount=(int)CMath.limit(amount-x, 0, getMax());
	if(amount==0) {
		available=false;
		totalDestroy();
	}
}

private void totalDestroy() {
	owner.setContent(null);
}

public void increase(int x) {
	if(x>0) amount=(int)CMath.limit(amount+x, 0, getMax());
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

public void setAmount(int amount) {
	this.amount=amount;
}

public String getTexture() {
	return texture;
}

public void render(Shader s, Matrix4f matrix, float x, float y, boolean showInfo) {
	translate.identity().translate(x, y, 0f);
	res = matrix.mul(translate, res);
	s.setUniform("projection", res);
	
	boolean found=false;
		Texture t = Assets.textures.get(texture);
		if(t!=null) {
			t.bind(0);
			found=true;
		} else {
			BlockTexture load = Assets.blocks.get(texture);
			if(load!=null) {
				load.txt[19].bind(0);
				found=true;
			}
		}
		if(found) {
		itemm.render();
		}
		if(showInfo) {
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

public abstract int getId();

}
