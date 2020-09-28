package com.bohdloss.fuckunclejack.main;

import java.nio.DoubleBuffer;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

import com.bohdloss.fuckunclejack.render.Function;
import com.bohdloss.fuckunclejack.render.MainEvents;
import com.bohdloss.fuckunclejack.render.Point2f;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

private long window;

private int width;
private int height;
private boolean destroyed=true;
private boolean fullscreen;

public static final String title="Fuck Uncle Jack";

//cache
private DoubleBuffer posx=BufferUtils.createDoubleBuffer(1);
private DoubleBuffer posy=BufferUtils.createDoubleBuffer(1);
private Point2f curpos=new Point2f(0,0);
//

public Window() {
	GLFWVidMode size = defaultD();
	width=(int)((double)size.width()*0.7d);
	height=(int)((double)size.height()*0.7d);
	createWindow(width, height);
	Window self = this;
	glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback(){
        @Override
        public void invoke(long window, int width, int height) {
            self.width=width;
            self.height=height;

            Game.camera.setOrtho(width, height);
            Game.calcScale();
            
            Function<Object> updview = new Function<Object>() {

				@Override
				public Object execute() throws Throwable {
					GL11.glViewport(0, 0, self.width, self.height);
					return null;
				}
            	
            };
            
            MainEvents.queue(updview, true);
            
        }
    });
}

public GLFWVidMode defaultD() {
	GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	return mode;
}

public void toggleFullscreen() {
	GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	
	if(fullscreen) {
		width=(int)((double)mode.width()*0.7d);
		height=(int)((double)mode.height()*0.7d);
	} else {
		width=mode.width();
		height=mode.height();
	}
	glfwSetWindowMonitor(window, fullscreen ? 0 : glfwGetPrimaryMonitor(), 0, 0, width, height, GLFW_DONT_CARE);
	glfwSetWindowPos(window, (int)((double)mode.width()*0.5-(double)width*0.5), (int)((double)mode.height()*0.5-(double)height*0.5));
	fullscreen=!fullscreen;
}

public void createWindow(int width, int height) {
	GLFWVidMode d = defaultD();
	window = glfwCreateWindow(width, height, title, 0, 0);
	if(window==0) {
		JOptionPane.showMessageDialog(null, "Window could not be initialized ¯\\_(ツ)_/¯");
		System.exit(1);
	}
	glfwSetWindowSizeLimits(window, 100, 100, d.width(), d.height());
	glfwSetWindowPos(window, (int)((d.width()-width)/2), (int)((d.height()-height)/2));
	
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
