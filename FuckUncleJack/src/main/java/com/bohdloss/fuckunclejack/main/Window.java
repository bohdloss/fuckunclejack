package com.bohdloss.fuckunclejack.main;

import java.nio.DoubleBuffer;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;

import com.bohdloss.fuckunclejack.render.Point2f;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

private long window;

private int width;
private int height;
private boolean destroyed=true;
private boolean fullscreen;

public static final String title="Fuck Uncle Jack";



//faster GC
private DoubleBuffer posx=BufferUtils.createDoubleBuffer(1);
private DoubleBuffer posy=BufferUtils.createDoubleBuffer(1);
private Point2f curpos=new Point2f(0,0);
//

public Window() {
	Dimension2i d = defaultD();
	int w = (int)(d.getWidth()*0.7d);
	int h = (int)(d.getHeight()*0.7d);
	width=w;
	height=h;
	createWindow(w, h);
}

public Dimension2i defaultD() {
	GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	int w = mode.width();
	int h = mode.height();
	return new Dimension2i(w,h);
}

public void toggleFullscreen() {
/*	if(!fullscreen) {
		destroy();
		createFullscreenWindow();
		Dimension d = defaultD();
		int w = (int)d.getWidth();
		int h = (int)d.getHeight();
		width=w;
		height=h;
	} else {
		destroy();
		Dimension d = defaultD();
		int w = (int)(d.getWidth()*0.7d);
		int h = (int)(d.getHeight()*0.7d);
		width=w;
		height=h;
		createWindow(w,h);
	} */
	
	if(!fullscreen) {
	Dimension2i d = defaultD();
	int w = (int)(d.getWidth()*0.7d);
	int h = (int)(d.getHeight()*0.7d);
	width=w;
	height=h;
	} else {
		Dimension2i d = defaultD();
		int w = (int)(d.getWidth());
		int h = (int)(d.getHeight());
		width=w;
		height=h;
	}
	
	glfwSetWindowMonitor(window, !fullscreen ? glfwGetPrimaryMonitor() : 0, 0, 0, width, height, GLFW_DONT_CARE);
	
	fullscreen=!fullscreen;
}

public void createWindow(int width, int height) {
	Dimension2i d = defaultD();
	window = glfwCreateWindow(width, height, title, 0, 0);
	if(window==0) {
		JOptionPane.showMessageDialog(null, "WTF? You broke it! Are you happy now? Do you feel accomplished? Well, i'm happy for you because only god knows how many brain cells died to create this game and make it work the best i could... just for you to come here and ruin my work. If i ever catch you making my game crash again, i swear to god it will be the last time.");
		System.exit(1);
	}
	glfwSetWindowSizeLimits(window, width, height, width, height);
	glfwSetWindowPos(window, (int)((d.getWidth()-width)/2), (int)((d.getHeight()-height)/2));
	
	GLFWImage icon = ImageLoader.load_image("/data/icon.png");
	GLFWImage iconSmall = ImageLoader.load_image("/data/icon_small.png");
	GLFWImage.Buffer imagebf = GLFWImage.malloc(2);
    imagebf.put(0, icon);
    imagebf.put(1, iconSmall);
    glfwSetWindowIcon(window, imagebf);
	
	glfwShowWindow(window);
	glfwMakeContextCurrent(window);
	
	destroyed=false;
}

public Point2f getCursorPos() {
	posx.clear();
	posy.clear();
	glfwGetCursorPos(window, posx, posy);
	curpos.x=(float)posx.get(0);
	curpos.y=(float)posy.get(0);
	return curpos;
}

public void createFullscreenWindow() {
	GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	window = glfwCreateWindow(mode.width(), mode.height(), title, glfwGetPrimaryMonitor(), 0);
	if(window==0) {
		System.exit(1);
	}
	glfwSetWindowPos(window, 0, 0);
	glfwShowWindow(window);
	glfwMakeContextCurrent(window);
	destroyed=false;
}

public void destroy() {
	glfwDestroyWindow(window);
	destroyed=true;
}

public boolean shouldClose() {
	return glfwWindowShouldClose(window);
}

public void close() {
	glfwSetWindowShouldClose(window, true);
}

public void swap() {
	glfwSwapBuffers(window);
}

public boolean isKeyDown(int key) {
	return glfwGetKey(window, key)==GLFW_TRUE;
}

public boolean isMouseButtonDown(int code) {
	return glfwGetMouseButton(window, code)==GLFW_TRUE;
}

public long getWindow() {
	return window;
}
public void setWindow(long window) {
	this.window = window;
}
public int getWidth() {
	return width;
}
public int getHeight() {
	return height;
}

public boolean isDestroyed() {
	return destroyed;
}
	
}
