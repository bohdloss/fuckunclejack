package com.bohdloss.fuckunclejack.main;

import com.bohdloss.fuckunclejack.render.Function;

public abstract class ArgFunction<T> extends Function<T> {

	public abstract T execute(Object...objects) throws Throwable;

	public T execute() {return null;}
	
}
