/* ColumnVisibilityChangeEvent.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.columnchooser;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class ColumnVisibilityChangeEvent extends Event {

	public final static String NAME = "onColumnVisibilityChange";
	
	private final List<String> _visibleColumns;
	private final List<String> _hiddenColumns;
	
	public ColumnVisibilityChangeEvent(Component target, List<String> visibleColumns, List<String> hiddenColumns) {
		super(NAME, target);
		_visibleColumns = visibleColumns;
		_hiddenColumns = hiddenColumns;
	}

	public List<String> getVisibleColumns() {
		return _visibleColumns;
	}
	
	public List<String> getHiddenColumns() {
		return _hiddenColumns;
	}
}
