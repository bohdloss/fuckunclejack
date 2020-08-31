package com.bohdloss.fuckunclejack.render;

import static com.bohdloss.fuckunclejack.main.Game.window;

import java.awt.Point;
import java.util.Random;

import com.bohdloss.fuckunclejack.main.Game;

public class CMath {	

	public static double remap(double value, double min1, double max1, double min2, double max2) {
		return lerp(reverseLerp(value, min1, max1), min2, max2);
	}
	
	public static double lerp(double alpha, double a, double b) {
		if(alpha>=1) return b;
		if(alpha<=0) return a;
		return a+((b-a)*alpha);
	}
	
	public static double reverseLerp(double lerp, double a, double b) {
		return (lerp-a)/(b-a);
	}
	
	public static double approx(double step, double in) {
		double toMult=1d/step;
		double doMultIn=in*toMult;
		int previous=(int)doMultIn;
		int next=previous+1;
		double toReturn = closest(in, (double)previous/toMult, (double)next/toMult);
		return toReturn;
	}
	
	public static double diff(double a, double b) {
		return fastAbs(a-b);
	}
	
	public static double closest(double in, double min, double max) {
		double diffmin=diff(min, in);
		double diffmax=diff(max, in);
		if(diffmax<diffmin) {
			return max;
		} else {
			return min;
		}
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double lx=diff(x1,x2);
		double ly=diff(y1,y2);
		return Math.sqrt(lx*lx+ly*ly);
	}
	
	public static double distance2(double x1, double y1, double x2, double y2) {
		double lx=diff(x1,x2);
		double ly=diff(y1,y2);
		return lx*lx+ly*ly;
	}
	
	public static float limitMin(float in, float min) {
		return in<min ? min : in;
	}
	
	public static float limitMax(float in, float max) {
		return in>max ? max : in;
	}
	
	public static float limit(float in, float min, float max) {
		if(max>in&in>min) return in;
		if(in>=max) return max;
		if(in<=min) return min;
		return 0f;
	}
	
	public static double fastAbs(double in) {
		return in<0 ? -in : in;
	}
	
	public static int fastFloor(double x) {
		int xi = (int)x;
		return x < xi ? xi - 1 : xi;
	}
	
	public static boolean inrange(float in, float min, float max) {
		return (in>=min&max<=max);
	}

	public static Point2f toGLCoord(float x, float y, float scale) {
			float visiblex, visibley, blockx, blocky;
			Point pos;
		
			visiblex=(float)window.getWidth()/(float)scale;
			visibley=(float)window.getHeight()/(float)scale;
			
			pos = window.getCursorPos();
			blockx=CMath.limit(pos.x, 0, window.getWidth());
			blocky=CMath.limit(pos.y, 0, window.getHeight());
			
			blockx=(blockx/window.getWidth());
			blocky=(blocky/window.getHeight());
			
			blockx/=(1d/(double)visiblex);
			blocky/=-(1d/(double)visibley);
			
			blockx-=visiblex/2f;
			blocky+=visibley/2f;
			
			return new Point2f(blockx, blocky);
	}
	
	public static Point2f mGLCoord(float scale) {
		Point p = Game.window.getCursorPos();
		return toGLCoord(p.x, p.y, scale);
	}
	
	public static boolean random(Random r, int iterations) {
		if(iterations==0) {
			return r.nextBoolean();
		} else {
			return random(r, iterations-1)&r.nextBoolean();
		}
	}
	
	public static float lookAt(Point2f point) {
		return (float)Math.atan2(point.y, point.x);
	}
	
	public static float oppositeTo(Point2f point) {
		return (float)(lookAt(point)+Math.PI);
	}
	
	public static float lookAt(float x, float y) {
		return (float)Math.atan2(y, x);
	}
	
	public static float oppositeTo(float x, float y) {
		return (float)(lookAt(x, y)+Math.PI);
	}
	
}
