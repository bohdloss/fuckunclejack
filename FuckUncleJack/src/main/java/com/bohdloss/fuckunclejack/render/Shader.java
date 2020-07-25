package com.bohdloss.fuckunclejack.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {

private int program;
private int fs;
private int vs;
	
//Variables to reduce time taken by GC
private int uniformlocation;
private FloatBuffer floatbuffer=BufferUtils.createFloatBuffer(16);

public Shader(String location) throws Exception{
	program = glCreateProgram();
	
	vs = glCreateShader(GL_VERTEX_SHADER);
	glShaderSource(vs, read(location+".vs"));
	glCompileShader(vs);
	if(glGetShaderi(GL_COMPILE_STATUS, vs)!=0) {
		throw new Exception(""+glGetShaderInfoLog(vs));
	}
	
	fs = glCreateShader(GL_FRAGMENT_SHADER);
	glShaderSource(fs, read(location+".fs"));
	glCompileShader(fs);
	if(glGetShaderi(GL_COMPILE_STATUS, fs)!=0) {
		throw new Exception(""+glGetShaderInfoLog(fs));
	}
	
	glAttachShader(program, vs);
	glAttachShader(program, fs);
	
	glBindAttribLocation(program, 0, "vertices");
	glBindAttribLocation(program, 1, "textures");
	glLinkProgram(program);
	if(glGetProgrami(program, GL_LINK_STATUS)!=1) {
		throw new Exception(""+glGetProgramInfoLog(program));
	}
	
	glValidateProgram(program);
	if(glGetProgrami(program, GL_VALIDATE_STATUS)!=1) {
		throw new Exception(""+glGetProgramInfoLog(program));
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

private String read(String location) {
	try {
		String res="";
		String line="";
		BufferedReader br = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(location)));
		while((line=br.readLine())!=null) {
			res=res+line+"\n";
		}
		res=res.trim();
		br.close();
		return res;
	} catch(Exception e) {
		e.printStackTrace();
	}
	return null;
}



}
