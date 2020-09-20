package com.bohdloss.fuckunclejack.render;

import java.awt.Color;
import java.awt.Font;

import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.main.Assets;

public class Label extends Mesh{

protected String text;
protected Font font;
protected Color color;
protected float width;
protected float height;
	
	public Label(String text, Font font, Color color, float width, float height) {
		super(Assets.textures.get("empty"), Assets.models.get("square"));
		this.text=text;
		this.font=font;
		this.color=color;
		this.width=width;
		this.height=height;
		texture = TextTexture.generate(text, font, color);
		setScale(getTexture().getGLWidth(), getTexture().getGLHeight());
	}
	
	public void setText(String text) {
		this.text=text;
		((TextTexture)texture).setText(text).update();
		setScale(getTexture().getGLWidth(), getTexture().getGLHeight());
	}
	
	public float getTextWidth() {
		return getTexture().getGLWidth();
	}
	
	public float getTextHeight() {
		return getTexture().getGLHeight();
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public TextTexture getTexture() {
		return (TextTexture)texture;
	}
}
