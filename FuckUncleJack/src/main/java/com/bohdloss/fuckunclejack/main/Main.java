package com.bohdloss.fuckunclejack.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.logic.ClientState;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.server.Server;

public class Main {

public static String ip=true?"localhost":"95.111.250.134";
public static String name=null;

	public static void main(String[] args) {
		try {
			//new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch(Exception e) {}
		PrintListener.setListener(new ArgFunction<Object>(){

			@Override
			public Object execute(Object... objects) throws Throwable {
				return null;
			}
			
		});
		
		
		if(args.length>=1) {
			if(args[0].equals("server")) {
				GameState.isClient.setValue(false);
				GameState.isClient.lock();
			}
		}
			GameState.isClient.setValue(true);
			GameState.isClient.lock();
		
		if(GameState.isClient.getValue()) {
			if(args.length>=1 && args[0].equals("debug")) ClientState.debug=true;
			new Client();
			Game.init();
			Game.renderLoop();
		} else {
			new Server();
		}
	}

}
