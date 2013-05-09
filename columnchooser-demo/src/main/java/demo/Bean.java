/* Bean.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 30, 2013 12:27:30 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package demo;

/**
 * @author Sam
 *
 */
public class Bean {
	String a;
	String b;
	String c;
	String d;

	public Bean(String a, String b, String c, String d) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	/**
	 * @return the d
	 */
	public String getD() {
		return d;
	}

	/**
	 * @param d the d to set
	 */
	public void setD(String d) {
		this.d = d;
	}
}
