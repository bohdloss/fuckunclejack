package com.bohdloss.fuckunclejack.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bohdloss.fuckunclejack.render.Shader;

public class LibraryUtils {

private static final int WINDOWS=0;
private static final int LINUX=1;
private static final int MACINTOSH=2;

private static int OS=-1;

private static final int UNKNOWN=-1;
	
private static final String userpath = System.getProperty("user.home");
private static final String nativeDirName = "fuckunclejack_natives";

	private LibraryUtils() {}
	
	public static void prepareLWJGL() {
		try {
		OS=getOS();
		File nativeFolder = new File(userpath+File.separator+nativeDirName);
		if(!nativeFolder.exists()) nativeFolder.mkdir();
		
		String natives = read("/natives/natives.txt");
		
		StringReader sr = new StringReader(natives);
		BufferedReader br = new BufferedReader(sr);
		
		String line;
		
		while((line=br.readLine())!=null) {
			switch(OS) {
			default:
				throw new IllegalStateException("This operating system is not supported");
			case WINDOWS:
				if(line.endsWith(".dll")) copyFile("/natives/"+line, line);
			break;
			case LINUX:
				if(line.endsWith(".so")) copyFile("/natives/"+line, line);
			break;
			case MACINTOSH:
				if(line.endsWith(".dylib")) copyFile("/natives/"+line, line);
			break;
			}
		}
		
		addLibraryPath(userpath+File.separator+nativeDirName);
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("A fatal exception occurred, program will shut down");
			System.exit(1);
		}
	}
	
	private static void copyFile(String path, String name) throws Exception{
		InputStream stream = LibraryUtils.class.getResourceAsStream(path);
		File dest = new File(userpath+File.separator+nativeDirName+File.separator+name);
		if(dest.exists()) return;
		OutputStream destination = new FileOutputStream(dest);
		
		byte[] read = new byte[stream.available()];
		stream.read(read, 0, read.length);
		stream.close();
		
		destination.write(read);
		destination.flush();
		destination.close();
	}
	
	private static String read(String location) {
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
	
	private static void addLibraryPath(String pathToAdd) throws Exception{
		
		System.setProperty("java.library.path", pathToAdd);
		/*
	    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);

	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);

	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return;
	        }
	    }

	    //add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    usrPathsField.set(null, newPaths);*/
	}
	
	public static int getOS() {
		String osname = System.getProperty("os.name").toLowerCase();
		if(osname.contains("win")) return WINDOWS;
		if(osname.contains("nix")|osname.contains("nux")|osname.contains("aix")) return LINUX;
		if(osname.contains("mac")) return MACINTOSH;
		return UNKNOWN;
	}
	
}
