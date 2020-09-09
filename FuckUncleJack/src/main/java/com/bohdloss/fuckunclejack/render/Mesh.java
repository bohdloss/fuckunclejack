package com.bohdloss.fuckunclejack.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Mesh {

//cache
protected static Matrix4f transcache=new Matrix4f();
protected static Matrix4f res=new Matrix4f();
//
	
protected Texture texture;
protected Model model;

protected Vector2f translation;
protected Vector2f scale;
protected float rotation;

public Mesh(Texture texture, Model model) {
	super();
	this.texture = texture;
	this.model = model;
	translation=new Vector2f();
	scale=new Vector2f();
}
public Texture getTexture() {
	return texture;
}
public void setTexture(Texture texture) {
	this.texture = texture;
}
public Model getModel() {
	return model;
}
public void setModel(Model model) {
	this.model = model;
}
public Vector2f getTranslation() {
	return translation;
}
public void setTranslation(Vector2f translation) {
	this.translation = translation;
}
public Vector2f getScale() {
	return scale;
}
public void setScale(Vector2f scale) {
	this.scale = scale;
}
public float getRotation() {
	return rotation;
}
public void setRotation(float rotation) {
	this.rotation = rotation;
}

public float getX() {
	return translation.x;
}

public float getY() {
	return translation.y;
}

public void setX(float x) {
	translation.x=x;
}

public void setY(float y) {
	translation.y=y;
}

public void setTranslation(float x, float y) {
	translation.x=x;
	translation.y=y;
}

public float getXScale() {
	return scale.x;
}

public float getYScale() {
	return scale.y;
}

public void setXScale(float x) {
	scale.x=x;
}

public void setYScale(float y) {
	scale.y=y;
}

public void setScale(float x, float y) {
	scale.x=x;
	scale.y=y;
}

public void render(Shader s, Matrix4f matrix) {
	transcache.identity().translate(translation.x, translation.y, 0).rotate(rotation, 0, 0, 1).scale(scale.x, scale.y, 1);
	s.bind();
	s.setProjection(matrix.mul(transcache, res));
	texture.bind(0);
	model.render();
}

}
