package com.bohdloss.fuckunclejack.main;

import java.util.Scanner;

import javax.swing.JOptionPane;

import com.bohdloss.fuckunclejack.client.Client;
import com.bohdloss.fuckunclejack.components.Entity;
import com.bohdloss.fuckunclejack.logic.GameState;
import com.bohdloss.fuckunclejack.server.Server;

public class Main {

public static Game game;

public static String ip=true?"localhost":"95.111.250.134";
public static String name=null;

	public static void main(String[] args) {
		boolean set=false;
		
		if(args.length>=1) {
			if(args[0].equals("server")) {
				GameState.isClient.setValue(false);
				GameState.isClient.lock();
				set=true;
			}
		}
		if(!set) {
			GameState.isClient.setValue(true);
			GameState.isClient.lock();
		}
		
		if(GameState.isClient.getValue()) {
			game = new Game();
			new Client();
			game.begin();
		} else {
			new Server();
		}
	}

}
