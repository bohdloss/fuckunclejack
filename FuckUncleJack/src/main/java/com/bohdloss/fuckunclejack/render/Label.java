package com.bohdloss.fuckunclejack.render;

import java.awt.Color;
import java.awt.Font;

import org.joml.Vector2f;

import com.bohdloss.fuckunclejack.main.Assets;

public class Label extends Mesh{

protected String text;
protected Font font;
protected Color color;
	
	public Label(String text, Font font, Color color) {
		super(Assets.textures.get("empty"), Assets.models.get("square"));
		this.text=text;
		this.font=font;
		this.color=color;
		texture = TextTexture.generate(text, font, color);
		setScale(getTexture().getGLWidth(), getTexture().getGLHeight());
	}
	
	public void setText(String text) {
		this.text=text;
		((TextTexture)texture).setText(text).update();
		updateSize();
	}
	
	private void updateSize() {
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
	
	public Font getFont() {
		return font;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setFont(Font font) {
		this.font=font;
		getTexture().setFont(font).update();
		updateSize();
	}
	
	public void setColor(Color color) {
		this.color=color;
		getTexture().setColor(color).update();
		updateSize();
	}
	
	@Override
	public TextTexture getTexture() {
		return (TextTexture)texture;
	}
}
