package com.bohdloss.fuckunclejack.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

public static int port=17657;
	
public static TickThread tick;
public ServerSocket server;	

public static List<SocketThread> threads = new ArrayList<SocketThread>();

	public Server() {
		tick=new TickThread();
		start();
	}
	
	public void run() {
		try {
			server=new ServerSocket(port);
			System.out.println("READY!");
			while(true) {
				try {
					Socket socket = server.accept();
					
					SocketThread thread = new SocketThread(socket);
					thread.start();
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
