package com.cred.util;

public class Log {

	private static boolean log = false;

	public static boolean isLog() {
		return log;
	}

	public static void setLog(boolean log) {
		Log.log = log;
	}
	
	public static void show(String msg) {
		
		if (!log)
			return;
		
		System.out.println(msg);
	}
}