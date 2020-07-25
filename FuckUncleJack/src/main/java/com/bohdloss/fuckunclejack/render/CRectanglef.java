package com.bohdloss.fuckunclejack.render;

public class CRectanglef {

public float x, y, width, height;

public CRectanglef(float x, float y, float width, float height) {
	this.x=x;
	this.y=y;
	this.width=width;
	this.height=height;
}
	
public boolean intersects(CRectanglef in) {
    if (x >= in.x+in.width || in.x >= x+width) { 
        return false; 
    } 

    if (y >= in.y+in.height || in.y >= y+height) { 
        return false; 
    } 
	
	return true;
}

public double distance(CRectanglef in) {
	return CMath.distance(x+width/2f, y+height/2f, in.x+in.width/2f, in.y/in.height/2f);
}

public CRectanglef setFrame(float x, float y, float width, float height) {
	this.x=x;
	this.y=y;
	this.width=width;
	this.height=height;
	return this;
}

@Override
public String toString() {
	return "x: "+x+" y: "+y+" width: "+width+" height:"+height;
}

}
