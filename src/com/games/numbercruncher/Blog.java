package com.games.numbercruncher;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Blog{
	public static void i(Object... objects){
		String tag=getTag();
		Logger log=Logger.getLogger(tag);
		if(objects==null) log.info("null");
		StringBuilder builder=new StringBuilder();
		for(Object o:objects){
			String add=(o==null?"null":o.toString());
			builder.append(add);
		}
		log.logp(Level.INFO,tag,"",builder.toString());
	}
	public static void w(Object... objects){
		String tag=getTag();
		Logger log=Logger.getLogger(tag);
		if(objects==null) log.warning("null");
		StringBuilder builder=new StringBuilder();
		for(Object o:objects){
			String add=(o==null?"null":o.toString());
			builder.append(add);
		}
		log.logp(Level.WARNING,tag,"",builder.toString());
	}	
	private static String getTag() {
		StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
		return stackTraceElements[2].getClassName()+"."+stackTraceElements[2].getMethodName();
	}
	
}

