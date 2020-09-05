package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL20.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import com.bohdloss.fuckunclejack.main.ResourceLoader;

public class Shader {

public static final Color NO_COLOR=new Color(0,0,0,0);
public static final Vector4f NO_VECTOR_COLOR=new Vector4f(-1,0,0,0);
	
private int program;
private int fs;
private int vs;
	
public static final String PROJECTION = "projection";

//Variables to reduce time taken by GC
private int uniformlocation;
private FloatBuffer floatbuffer=BufferUtils.createFloatBuffer(16);

public Shader(String locationvs, String locationfs) throws Exception{
	program = glCreateProgram();
	
	vs = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vs, ResourceLoader.loadText(locationvs));
	glCompileShader(vs);
	if(glGetShaderi(vs, GL_COMPILE_STATUS)==GL_FALSE) {
		throw new Exception("OpenGL error: "+glGetShaderInfoLog(vs));
	}
	
	fs = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fs, ResourceLoader.loadText(locationfs));
	glCompileShader(fs);
	if(glGetShaderi(fs, GL_COMPILE_STATUS)==GL_FALSE) {
		throw new Exception("OpenGL error: "+glGetShaderInfoLog(fs));
	}
	
	glAttachShader(program, vs);
	glAttachShader(program, fs);
	
	glBindAttribLocation(program, 0, "vertices");
	glBindAttribLocation(program, 1, "textures");
	glLinkProgram(program);
	if(glGetProgrami(program, GL_LINK_STATUS)==GL_FALSE) {
		throw new Exception("OpenGL error: "+glGetProgramInfoLog(program));
	}
	
	glValidateProgram(program);
	if(glGetProgrami(program, GL_VALIDATE_STATUS)==GL_FALSE) {
		throw new Exception("OpenGL error: "+glGetProgramInfoLog(program));
	}
}

public void bind() {
	glUseProgram(program);
}

public void setUniform(String name, int value) {
	uniformlocation = glGetUniformLocation(program, name);
	if(uniformlocation!=-1) {
		glUniform1i(uniformlocation, value);
	}
}

public void setUniform(String name, float value) {
	uniformlocation = glGetUniformLocation(program, name);
	if(uniformlocation!=-1) {
		glUniform1f(uniformlocation, value);
	}
}

public void setUniform(String name, Vector4f value) {
	uniformlocation = glGetUniformLocation(program, name);
	if(uniformlocation!=-1) {
		glUniform4f(uniformlocation, value.x, value.y, value.z, value.w);
	}
}

public void setUniform(String name, Matrix4f value) {
	uniformlocation = glGetUniformLocation(program, name);
	value.get(floatbuffer);
	if(uniformlocation!=-1) {
		glUniformMatrix4fv(uniformlocation, false, floatbuffer);
	}
}

public void setUniform(String name, boolean b) {
	uniformlocation = glGetUniformLocation(program, name);
	if(uniformlocation!=-1) {
		glUniform1i(uniformlocation, b?1:0);
	}
}

public void setUniform(String name, Color c) {
	uniformlocation = glGetUniformLocation(program, name);
	if(uniformlocation!=-1) {
		glUniform4f(uniformlocation, c==NO_COLOR?-1:(float)c.getRed()/255f, (float)c.getGreen()/255f, (float)c.getBlue()/255f, (float)c.getAlpha()/255f);
	}
}

public void setProjection(Matrix4f matrix) {
	setUniform(PROJECTION, matrix);
}



}
