package org.omega.crawler.test;

import java.math.BigDecimal;

import org.omega.crawler.common.Utils;

public class Misc4Test {

	public static void main(String[] args) {
//		double y = 100.0d;
//		double x = 66.24d;
		double y = 90000000;
		double x = 0.00000006;
		double x2 = x * y;
//		System.out.println(x);
		System.out.println(x2);
		BigDecimal x3 = new BigDecimal(Double.toString(x));
		BigDecimal x4 = x3.multiply(new BigDecimal(Double.toString(y)));
//		System.out.println(x3);
		System.out.println(x4);
		
		System.out.println(Byte.MAX_VALUE);
		System.out.println(Byte.MIN_VALUE);
		
		byte bx = (byte) 129;
		System.out.println(bx);
		
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		
		System.out.println(Utils.UNIT_BILLION);
		System.out.println(Utils.UNIT_TRILLION);
		
	}
	
}
