/* MyMenupopup.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 30, 2013 12:08:17 PM , Created by Sam
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package test;

import java.util.List;

import org.zkoss.zul.Menupopup;

/**
 * @author Sam
 *
 */
public class MyMenupopup extends Menupopup {
	
	private String[] _columns;
	
	public void setColumns(String[] columns) {
		_columns = columns;
	}
	
	public String[] getColumns() {
		return _columns;
	}
}
