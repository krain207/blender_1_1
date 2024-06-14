package com.imeritz.blender.tran;

public class BTConvert {
	public static String toString(Object obj) {
		if (obj == null)
			return null;
		return obj.toString(); 
	}
	
	public static String toString(int p) {
		return Integer.toString(p);     
	}
	
	public static String toString(double p) {
		return Double.toString(p);
	}

	public static String toString(long p) {
		return Long.toString(p);
	}

	public static String toString(float p) {
		return Float.toString(p);
	}

	public static String toBinary(int p) {
		return Integer.toBinaryString(p);
	}

	
	public static String tohexadecimal(int p) {
		return Integer.toString(p, 16);
	}
	
	public static int toInt(String str) {
		return Integer.parseInt(str);
	}
	
	public static int toInt(int p) {
		return p;
	}
	
	public static int toInt(long p) {
		return (int)p;
	}
	
	public static int toInt(float p) {
		return (int)p;
	}
	
	public static int toInt(double p) {
		return (int)p;
	}
	
	public static int toInt(Object obj) {
		return toInt(obj.toString());
	}
	
	public static long toLong(String str) {
		return Long.parseLong(str);
	}
	
	public static long toLong(int p) {
		return (long)p;
	}
	
	public static long toLong(long p) {
		return (long)p;
	}
	
	public static long toLong(float p) {
		return (long)p;
	}
	
	public static long toLong(double p) {
		return (long)p;
	}
	
	public static long toLong(Object obj) {
		return toLong(obj.toString());
	}
	
	public static short toShort(String str) {
		return Short.parseShort(str);
	}
	
	public static short toShort(int p) {
		return (short)p;
	}
	
	public static short toShort(long p) {
		return (short)p;
	}
	
	public static short toShort(float p) {
		return (short)p;
	}
	
	public static short toShort(double p) {
		return (short)p;
	}
	
	public static short toShort(Object obj) {
		return toShort(obj.toString());
	}
	
	
	public static float toFloat(String str) {
		return Float.parseFloat(str.replaceAll(",", ""));
	}

	public static float toFloat(int p) {
		return (float)p;
	}
	
	public static float toFloat(long p) {
		return (float)p;
	}
	
	public static float toFloat(float p) {
		return (float)p;
	}
	
	public static float toFloat(double p) {
		return (float)p;
	}
	
	public static float toFloat(Object obj) {
		return toFloat(obj.toString());
	}
	
	public static double toDouble(String str) {
		return Double.parseDouble(str.replaceAll(",", ""));
	}
	
	public static double toDouble(int p) {
		return (double)p;
	}
	
	public static double toDouble(long p) {
		return (double)p;
	}
	
	public static double toDouble(float p) {
		return (double)p;
	}
	
	public static double toDouble(double p) {
		return (double)p;
	}
	
	public static double toDouble(Object obj) {
		return toDouble(obj.toString());
	}

}
