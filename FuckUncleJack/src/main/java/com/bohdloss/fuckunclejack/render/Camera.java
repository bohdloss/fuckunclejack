package com.bohdloss.fuckunclejack.render;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
//smooth camera movement
	//should camera be smooth?
	
	public boolean smooth=true;
	
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
	
	private boolean shake=false;
	private long shakeStart=0;
	private long shakeDuration=0;
	private float lerpIntensity;
	private float intensity=10;
//end of lerp vars	
	
//cache
	private Vector3f position;
	private Vector3f position2;
	private Matrix4f projection;
	
	private Matrix4f target=new Matrix4f();
	private Matrix4f pos=new Matrix4f();
	private double reverse;
	
	private Random random=new Random();
//
	
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
		position2 = new Vector3f(0,0,0);
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void forcePosition(float x, float y) {
		position.x=x;
		position.y=y;
		savedx=x;
		savedy=y;
		savedlerpx=x;
		savedlerpy=y;
		noGClerpx=x;
		noGClerpy=y;
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
		if(smooth) {
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
				
			}
		}
		
		pos.setTranslation(-position.x, -position.y, position.z);
		shake(position);
		projection.mul(pos, target);
		
		return target;
	}

	public void shake(long time, float intensity) {
		shake=true;
		this.intensity=intensity;
		this.shakeStart=System.currentTimeMillis();
		this.shakeDuration=time;
	}
	
	private void shake(Vector3f edit) {
		if(!shake) {
			pos.setRotationXYZ(0, 0, 0);
			return;
		}
		if(System.currentTimeMillis()>=shakeStart+shakeDuration) {
			shake=false;
			return;
		}
		lerpIntensity=(float)CMath.clamp(CMath.remap(System.currentTimeMillis(), shakeStart, shakeStart+shakeDuration, intensity, 0), 0, intensity);
		
		edit.x+=(random.nextFloat()-0.5f)*lerpIntensity;
		edit.y+=(random.nextFloat()-0.5f)*lerpIntensity;
		
		pos.setTranslation(-edit.x, -edit.y, 0);
		pos.setRotationXYZ(0,0,(float)CMath.remap(random.nextDouble(), 0, 1, -lerpIntensity/1000d, lerpIntensity/1000d));
	}
	
	public Matrix4f unTransformedProjection() {
		position2.zero();
		pos.identity();
		shake(position2);
		projection.mul(pos, target);
		return target;
	}
	
}
