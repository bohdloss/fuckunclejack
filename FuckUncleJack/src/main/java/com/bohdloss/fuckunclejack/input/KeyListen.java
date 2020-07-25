package com.bohdloss.fuckunclejack.input;

public interface KeyListen {

abstract void onKeyPressed(int code);
abstract void onKeyReleased(int code);
abstract void onMouseButtonPressed(int code);
abstract void onMouseButtonReleased(int code);
	
}
