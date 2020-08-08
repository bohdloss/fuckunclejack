package com.bohdloss.fuckunclejack.hud;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;

public class Bar {

public float x;
public float y;

public float max;
public float current;

private Model model;
private Model midmodel;
private Texture background;
private Texture middle;
private Texture icon;

//faster GC
private Matrix4f res=new Matrix4f();
private Matrix4f translate=new Matrix4f();
private Matrix4f scale=new Matrix4f();
private Vector3f vec;
private float percent;
//end

//debug
int i=0;

public Bar(Texture icon, Texture background, Texture middle, float x, float y, float max, float current) {
	this.icon=icon;
	this.background=background;
	this.middle=middle;
	this.x=x;
	this.y=y;
	this.max=max;
	this.current=current;
	vec=new Vector3f();
	vec.x=x;
	vec.y=y;
	model=Assets.models.get("hud_bar");
	midmodel=Assets.models.get("bar_middle");
}

public void move(float x, float y) {
	this.x=x;
	this.y=y;
	vec.x=x;
	vec.y=y;
}

public void render(Shader s, Matrix4f matrix) {
	
	i++;
	
	translate.identity().translate(vec.x-2.25f, vec.y, 0).scale(1.75f);
	res = matrix.mul(translate, res);
	s.setUniform("projection", res);
	icon.bind(0);
	Assets.models.get("square").render();
	
	//debug
	//current=max*(((float)Math.sin((float)i/100f)+1)/2f);
	
	
	translate.identity().translate(vec.x+1, vec.y, 0).scale(0.75f);
	res = matrix.mul(translate, res);
	s.setUniform("projection", res);
	background.bind(0);
	model.render();
	
	middle.bind(0);
	percent=CMath.limit((current/max), 0, 1)*0.9025f;
	scale.identity().scale(percent, 1, 1);
	translate.identity().translate((0.25f+vec.x+1+((1f-percent)/2f)*-4f)*(1f/percent), vec.y, 0).scale(0.75f);
	res = matrix.mul(scale, res).mul(translate, res);
	s.setUniform("projection", res);
	midmodel.render();
}

}
