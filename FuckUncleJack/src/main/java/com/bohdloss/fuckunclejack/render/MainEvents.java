package com.bohdloss.fuckunclejack.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class MainEvents {

	private MainEvents() {}
	
private static HashMap<Long, Function<Object>> calls = new HashMap<Long, Function<Object>>();
private static HashMap<Long, Object> returnValues = new HashMap<Long, Object>();
private static List<Long> hasReturn = new ArrayList<Long>();
private static HashMap<Long, Throwable> exceptions = new HashMap<Long, Throwable>();
private static List<Long> hasException = new ArrayList<Long>();
private static List<Long> ignoreReturn = new ArrayList<Long>();

public static synchronized long queue(Function<Object> function) {
	return queue(function, false);
}

public static synchronized long queue(Function<Object> function, boolean ignoreReturnValue) {
	calls.put(function.getId(), function);
	if(ignoreReturnValue) ignoreReturn.add(function.getId());
	return function.getId();
}

public static Object waitReturnValue(long funcId) throws Throwable{
	while(!hasReturnValue(funcId)&&!hasException(funcId)) {
		try {
			Thread.sleep(0);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	return getReturnValue(funcId);
}

public static synchronized boolean hasReturnValue(long funcId) {
	return hasReturn.contains((Long)funcId);
}

public static synchronized boolean hasException(long funcId) {
	return hasException.contains((Long)funcId);
}

public static synchronized Object getReturnValue(long funcId) throws Throwable{
	if(hasException(funcId)) {
		hasException.remove((Long)funcId);
		throw exceptions.remove(funcId);
	} else if(hasReturnValue(funcId)){
		hasReturn.remove((Long)funcId);
		return returnValues.remove(funcId);
	} else {
		return null;
	}
}

public static synchronized void computeEvents() {
	calls.forEach((id, function)->{
		try {
			Object ret = function.execute();
			if(!ignoreReturn.contains(id)) {
				returnValues.put(id, ret);
				hasReturn.add(id);
			} else {
				ignoreReturn.remove((Long)id);
			}
		} catch(Throwable e) {
			hasException.add(id);
			exceptions.put(id, e);
		}
	});
	calls.clear();
}

}
