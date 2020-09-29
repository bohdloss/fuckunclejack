package com.bohdloss.fuckunclejack.guicomponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;

import com.bohdloss.fuckunclejack.main.Assets;
import com.bohdloss.fuckunclejack.menutabs.MenuTab;
import com.bohdloss.fuckunclejack.render.FontManager;
import com.bohdloss.fuckunclejack.render.Model;
import com.bohdloss.fuckunclejack.render.Shader;
import com.bohdloss.fuckunclejack.render.TileSheet;
import com.bohdloss.fuckunclejack.render.VolatileTexture;

public class GuiButton extends GuiComponent{

//cache
static Matrix4f res=new Matrix4f();
static Matrix4f translate=new Matrix4f();

static final float mulConst=25;
static Model square;
TileSheet button;
static Shader gui;
static TileSheet font;

public float width=1;
public float height=1;
public Color color;

float i=1;

public VolatileTexture[] changeable = new VolatileTexture[3];

private BufferedImage[] states = new BufferedImage[3];

static {
	square=Assets.models.get("square");
	gui=Assets.shaders.get("gui");
	font=Assets.sheets.get("font");
}

	public GuiButton(MenuTab tab, String text, float x, float y, float width, float height, TileSheet sheet, Color color) {
		super(tab);
		this.text=text;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.button=sheet;
		changeable[0]=VolatileTexture.generate();
		changeable[1]=VolatileTexture.generate();
		changeable[2]=VolatileTexture.generate();
		updateSize();
		this.color=color;
	}
	
	public void updateSize() {
		float w=getWidth();
		float h=getHeight();
		for(int i=0;i<3;i++) {
			BufferedImage size = new BufferedImage((int)(w*mulConst), (int)(h*mulConst), BufferedImage.TYPE_INT_ARGB);
			Graphics g = size.getGraphics();
		
			BufferedImage[] imgs = button.getTiles();
		
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
			
			states[i]=size;
			
			changeable[i].change(states[i]);
		}
	}
	
	@Override
	public void tick(float delta) {
		super.tick(delta);
	}
	
	@Override
	public void render(Shader s, Matrix4f matrix) {
		if(!visible) return;
		
		s.bind();
		translate.identity().translate(x, y, 0).rotate(rot, 0, 0, 1).scale(getWidth(), getHeight(), 1);
		s.setProjection(matrix.mul(translate, res));
		
		renderNoTransform(s);
	}
	
	public void renderNoTransform(Shader s) {
		if(status!=DISABLED) {
			s.setUniform("colormask", color);
		}
		changeable[status==DISABLED?0:status-1].bind(0);
		square.render();
		
		s.setUniform("colormask", Shader.NO_COLOR);
	}
	
	@Override
	public float getY() {
		return y-getHeight()/2;
	}
	
	@Override
	public float getX() {
		return x-getWidth()/2;
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public void pressed() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void hovered() {
		
	}
	
}
