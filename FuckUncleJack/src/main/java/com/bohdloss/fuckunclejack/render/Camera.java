package com.bohdloss.fuckunclejack.render;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
//smooth camera movement
	public float xlerpdest;
	public float ylerpdest;

	public float savedlerpx;
	public float savedlerpy;
	public float savedx;
	public float savedy;
	public boolean lerping;
	public long checkTime=0;
	public long checkInterval=100;
	public long lerpTime=250;
	
	
	private float noGClerpx;
	private float noGClerpy;
	
	public boolean shake=false;
	public float intensity=5;
//end of lerp vars	
	
//vars to make GC faster
	private Vector3f position;
	private Matrix4f projection;
	
	private Matrix4f target=new Matrix4f();
	private Matrix4f pos=new Matrix4f();
	private double reverse;
	
//end of GC vars
	
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void setX(float x) {
		xlerpdest=x;
	}
	
	public void setY(float y) {
		ylerpdest=y;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void addPosition(Vector3f vec) {
		xlerpdest+=vec.x;
		ylerpdest+=vec.y;
	}
	
	public void setPosition(Vector3f vec) {
		xlerpdest=vec.x;
		ylerpdest=vec.y;
	}
	
	public Matrix4f projection() {
		if(System.currentTimeMillis()>=checkTime+checkInterval) {
			checkTime=System.currentTimeMillis();
			savedx=position.x;
			savedy=position.y;
			savedlerpx=xlerpdest;
			savedlerpy=ylerpdest;
			lerping=true;
		}
		if(lerping) {
			reverse=CMath.reverseLerp(System.currentTimeMillis(), checkTime, checkTime+lerpTime);
			noGClerpx=(float)CMath.lerp(reverse, savedx, savedlerpx);
			noGClerpy=(float)CMath.lerp(reverse, savedy, savedlerpy);
			position.x=noGClerpx;
			position.y=noGClerpy;
			if(shake) {
				position.x+=(new Random().nextFloat()-0.5f)*intensity;
				position.y+=(new Random().nextFloat()-0.5f)*intensity;
			}
		}
		
		pos.setTranslation(-position.x, -position.y, position.z);
		
		projection.mul(pos, target);
		
		return target;
	}

	public Matrix4f unTransformedProjection() {
		return projection;
	}
	
}
