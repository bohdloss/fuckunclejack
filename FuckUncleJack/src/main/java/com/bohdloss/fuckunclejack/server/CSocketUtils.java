package com.bohdloss.fuckunclejack.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import com.bohdloss.fuckunclejack.logic.GameState;

public class CSocketUtils {
	
	public static final int bufferSize = 1000000;
	
	public static final byte PLAYERDATA=(byte)1;
	public static final byte CREDENTIALS=(byte)2;
	public static final byte END=(byte)3;
	public static final byte EVENT=(byte)4;
	public static final byte ENTITIES=(byte)5;
	public static final byte CHUNKS=(byte)6;
	public static final byte INVENTORY=(byte)7;
	
	public static final byte DEBUG=(byte)8;
	
	public static void read(Socket s, ByteBuffer buf, ByteBuffer lengthBuf) throws Exception{
		
		
		
		//setup
		
		buf.clear();
		lengthBuf.clear();
		InputStream in;
		in=s.getInputStream();
		
		//reads the length of the byte array that needs to be read
		
		
		for(int i=0;i<4;i++) {
			lengthBuf.put((byte)in.read());
		}
		
		lengthBuf.clear();
		int length = lengthBuf.getInt();
		
		//put the length as a limit to the bytebuffer, to avoid unnecessary processing times
		
		buf.limit(length);
		
		//read from the input stream as much as specified by length
		
		
		
		for(int i=0;i<length;i++) {
			buf.put((byte)in.read());
		}
		
	}
	
	public static void write(Socket s, ByteBuffer buf) throws IOException {
		OutputStream out;
		out=s.getOutputStream();
		int l = buf.limit();
		byte[] b = {(byte)(l>>>24), (byte)(l>>>16), (byte)(l>>>8), (byte)(l)};
		out.write(b);
		out.write(buf.array(), 0, buf.limit());
		out.flush();
	}

	public static String readString(ByteBuffer buf) {
		int length = buf.getInt();
		byte b[] = new byte[length];
		for(int i=0;i<length;i++) {
			b[i]=buf.get();
		}
		
		return new String(b);
	}
	
	public static void putEntityData(ByteBuffer buf, Object[] data) {
		for(int i=0;i<data.length;i++) {
			if(data[i] instanceof Integer) {
				buf.putInt((int)data[i]);
			} else if(data[i] instanceof Float) {
				buf.putFloat((float)data[i]);
			} else if(data[i] instanceof Boolean) {
				buf.put((boolean)data[i]?(byte)1:(byte)0);
			} else if(data[i] instanceof String) {
				writeString(buf, (String)data[i]);
			} else if(data[i] instanceof Byte) {
				buf.put((byte)data[i]);
			}
		}
	}
	
	public static void writeString(ByteBuffer buf, String write) {
		buf.putInt(write.length());
		buf.put(write.getBytes());
	}
	
	public static void putEnd(ByteBuffer buf) {
		buf.put(END);
	}
	
	public static void clearBuf(ByteBuffer buf) {
		buf.clear();
		for(int i=0;i<bufferSize;i++) {
			buf.put((byte)0);
		}
		buf.clear();
	}
	
}
