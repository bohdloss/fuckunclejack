package com.bohdloss.fuckunclejack.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.render.CMath;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.Texture;
import com.bohdloss.fuckunclejack.render.TileSheet;
import com.bohdloss.fuckunclejack.render.VolatileTexture;

public class Bar {

public float x;
public float y;
public String text="";

private float max;
private float current;
private float percent;

private Color color;
private Texture icon;
private static Shader bar;
private static Model square;
private static TileSheet template;
static final float mulConst=25;

//cache
private Matrix4f res=new Matrix4f();
private Matrix4f translate=new Matrix4f();
private VolatileTexture txt;

public float lerpdest;

public float savedlerp;
public float saved;
public boolean lerping;
public long checkTime=0;
public long checkInterval=100;
public long lerpTime=250;
private float noGClerp;
//end

//debug
int i=0;

static {
	bar=Assets.shaders.get("bar");
	square=Assets.models.get("square");
	template=Assets.sheets.get("menubuttons");
}

public Bar(Texture icon, Color color, float x, float y, float max, float current) {
	this.icon=icon;
	this.x=x;
	this.y=y;
	this.max=max;
	setCurrent(current);
	this.color=color;
	txt=VolatileTexture.generate();
	updateSize();
}

public void move(float x, float y) {
	this.x=x;
	this.y=y;
}

public void updateSize() {
	float w=5;
	float h=1;
		BufferedImage size = new BufferedImage((int)(w*mulConst), (int)(h*mulConst), BufferedImage.TYPE_INT_ARGB);
		Graphics g = size.getGraphics();
		
		BufferedImage[] imgs = template.getTiles();
	
		int bordx=13;
		int bordy=13;
		
		g.drawImage(imgs[0+9*i],0,0,bordx,bordy,null);
		g.drawImage(imgs[1+9*i],(int)(w*mulConst)-bordx, 0,bordx,bordy,null);
		g.drawImage(imgs[2+9*i],0,(int)(h*mulConst)-bordy,bordx,bordy,null);
		g.drawImage(imgs[3+9*i],(int)(w*mulConst)-bordx, (int)(h*mulConst)-bordy,bordx,bordy,null);
		
		g.drawImage(imgs[4+9*i], bordx, 0, (int)(w*mulConst)-2*bordx, bordy, null);
		g.drawImage(imgs[5+9*i], bordx, (int)(h*mulConst)-bordy, (int)(w*mulConst)-2*bordx, bordy, null);
		g.drawImage(imgs[6+9*i], 0, bordy, bordx, (int)(h*mulConst)-2*bordy, null);
		g.drawImage(imgs[7+9*i], (int)(w*mulConst)-bordx, bordy, bordx, (int)(h*mulConst)-2*bordy, null);
		
		g.drawImage(imgs[8+9*i], bordx, bordy, (int)(w*mulConst)-2*bordx, (int)(h*mulConst)-2*bordy, null);
	
		g.setColor(new Color(50, 50, 50, 255));
		g.setFont(new Font("Arial", Font.BOLD,15));
		
		int drawx = ((int)(w*mulConst)-g.getFontMetrics().stringWidth(text))/2;
		int drawy = (((int)(h*mulConst)-g.getFontMetrics().getHeight())/2) + g.getFontMetrics().getAscent();
		
		g.drawString(text, drawx, drawy);
		
		txt.change(size);
}

public void setCurrent(float value) {
	this.current=value;
	lerpdest=(float)CMath.reverseLerp(this.current,  0, max);
}

public void render(Shader s, Matrix4f matrix) {
	
	//Interpolation for a smoother animation
	
	if(System.currentTimeMillis()>=checkTime+checkInterval) {
		checkTime=System.currentTimeMillis();
		saved=percent;
		savedlerp=lerpdest;
		lerping=true;
	}
	if(lerping) {
		double reverse=CMath.reverseLerp(System.currentTimeMillis(), checkTime, checkTime+lerpTime);
		noGClerp=(float)CMath.lerp(reverse, saved, savedlerp);
		percent=noGClerp;
		
	}
	
	/*
	//i++;
	
	translate.identity().translate(vec.x-2.25f, vec.y, 0).scale(1.75f);
	res = matrix.mul(translate, res);
	s.setProjection(res);
	icon.bind(0);
	Assets.models.get("square").render();
	
	//debug
	//current=max*(((float)Math.sin((float)i/100f)+1)/2f);
	
	
	translate.identity().translate(vec.x+1, vec.y, 0).scale(0.75f);
	res = matrix.mul(translate, res);
	s.setProjection(res);
	background.bind(0);
	model.render();
	
	middle.bind(0);
	percent=(float)CMath.reverseLerp(current, 0, max)*0.9025f;
	scale.identity().scale(percent, 1, 1);
	translate.identity().translate((0.25f+vec.x+1+((1f-percent)/2f)*-4f)*(1f/percent), vec.y, 0).scale(0.75f);
	res = matrix.mul(scale, res).mul(translate, res);
	s.setProjection(res);
	midmodel.render();
	*/
	
	//render the icon
	
	i++;
	
	translate.identity().translate(x-2.25f, y, 0).scale(1.75f);
	res = matrix.mul(translate, res);
	s.setProjection(res);
	icon.bind(0);
	square.render();
	
	bar.bind();
	bar.setUniform("colormask", color);
	bar.setUniform("percent", percent);
	bar.setUniform("radius", 0.05f);
	
	translate.identity().translate(x+0.5f, y, 0).scale(3.25f, 0.75f, 1);
	bar.setProjection(matrix.mul(translate, res));
	txt.bind(0);
	square.render();
	
	s.bind();
	
}

}
