/* ColumnchooserViewModel.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.columnchooser.impl;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.addon.columnchooser.ColumnVisibilityChangeEvent;
import org.zkoss.addon.columnchooser.Columnchooser;
import org.zkoss.addon.columnchooser.Columnchooser.ViewModel;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

public class ColumnchooserViewModel implements ViewModel {

	private Columnchooser _columnChooser;
	private boolean modified;
	private List<String> _visibleColumnsClone;
	private List<String> _hiddenColumnsClone;
	
	private String _selectedHiddenColumn;
	private String _selectedVisibleColumn;
	
	@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component component) {
		_columnChooser = (Columnchooser)component.getParent();
		init(new LinkedList<String>(_columnChooser.getVisibleColumns()), new LinkedList<String>(_columnChooser.getHiddenColumns()));
	}
	
	private void init(List<String> visibleColumnsClone, List<String> hiddenColumnsClone) {
		modified = false;
		_selectedHiddenColumn = null;
		_selectedVisibleColumn = null;
		_visibleColumnsClone = visibleColumnsClone;
		_hiddenColumnsClone = hiddenColumnsClone;
	}
	
	public void setVisibleColumns(List<String> visibleColumns) {
		_visibleColumnsClone = new LinkedList<String>(visibleColumns);
	}
	
	public List<String> getVisibleColumns() {
		return _visibleColumnsClone;
	}
	
	public void setHiddenColumns(List<String> hiddenColumns) {
		_hiddenColumnsClone = new LinkedList<String>(hiddenColumns);
	}
	
	public List<String> getHiddenColumns() {
		return _hiddenColumnsClone;
	}
	
	@NotifyChange("addDisabled")
	public void setSelectedHiddenColumn(String column) {
		_selectedHiddenColumn = column;
	}
	
	public String getSelectedHiddenColumn() {
		return _selectedHiddenColumn;
	}
	
	@NotifyChange({"removeDisabled", "moveUpDisabled", "moveDownDisabled"})
	public void setSelectedVisibleColumn(String column) {
		_selectedVisibleColumn = column;
	}
	
	public String getSelectedVisibleColumn() {
		return _selectedVisibleColumn;
	}
	
	public boolean isAddDisabled() {
		return Strings.isEmpty(_selectedHiddenColumn);
	}
	
	public boolean isRemoveDisabled() {
		return Strings.isEmpty(_selectedVisibleColumn);
	}
	
	public boolean isMoveUpDisabled() {
		if (Strings.isEmpty(_selectedVisibleColumn))
			return true;
		int index = _visibleColumnsClone.indexOf(_selectedVisibleColumn);
		return index < 1;
	}
	
	public boolean isMoveDownDisabled() {
		if (Strings.isEmpty(_selectedVisibleColumn))
			return true;
		int index = _visibleColumnsClone.indexOf(_selectedVisibleColumn);
		return index + 1 >= _visibleColumnsClone.size();
	}
	
	@Command
	@NotifyChange({"selectedHiddenColumn", "addDisabled", "visibleColumns", "hiddenColumns"})
	public void addSelectedHiddenColumnToVisibleColumn() {
		if (!Strings.isEmpty(_selectedHiddenColumn)) {
			dropToVisibleColumns(_selectedHiddenColumn);
			_selectedHiddenColumn = null;
		}
	}
	
	@Command
	@NotifyChange({"selectedVisibleColumn", "removeDisabled", "moveUpDisabled", "moveDownDisabled", "visibleColumns", "hiddenColumns"})
	public void removeSelectedVisibleColumnToHiddenColumn() {
		if (!Strings.isEmpty(_selectedVisibleColumn)) {
			dropToHiddenColumns(_selectedVisibleColumn);
			_selectedVisibleColumn = null;
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
	public void moveSelectedVisibleColumnUp() {
		if (!Strings.isEmpty(_selectedVisibleColumn)) {
			int index = _visibleColumnsClone.indexOf(_selectedVisibleColumn);
			if (index - 1 >= 0) {
				insertToVisibleColumns(_visibleColumnsClone.get(index - 1), _selectedVisibleColumn);
			}
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
	public void moveSelectedVisibleColumnDown() {
		if (!Strings.isEmpty(_selectedVisibleColumn)) {
			int index = _visibleColumnsClone.indexOf(_selectedVisibleColumn);
			if (index > -1 & index + 1 < _visibleColumnsClone.size()) {
				insertToVisibleColumns(_selectedVisibleColumn, _visibleColumnsClone.get(index + 1));
			}
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "selectedHiddenColumn", "addDisabled"})
	public void dropToVisibleColumns(@BindingParam("column") String column) {
		if (_hiddenColumnsClone.contains(column)) {
			_hiddenColumnsClone.remove(column);
			_visibleColumnsClone.add(column);
			modified = true;
			
			if (Objects.equals(column, _selectedHiddenColumn)) {
				_selectedHiddenColumn = null;
			}
		}
	} 
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "selectedVisibleColumn", "removeDisabled", "moveUpDisabled", "moveDownDisabled"})
	public void dropToHiddenColumns(@BindingParam("column") String column) {
		if (_visibleColumnsClone.contains(column)) {
			_visibleColumnsClone.remove(column);
			_hiddenColumnsClone.add(column);
			modified = true;
			
			if (Objects.equals(column, _selectedVisibleColumn)) {
				_selectedVisibleColumn = null;
			}
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "selectedHiddenColumn", "addDisabled", "moveUpDisabled", "moveDownDisabled"})
	public void insertToVisibleColumns(@BindingParam("drag") String drag, @BindingParam("drop") String drop) {
		if (_hiddenColumnsClone.contains(drag)) {
			_hiddenColumnsClone.remove(drag);
			if (Objects.equals(drag, _selectedHiddenColumn)) {
				_selectedHiddenColumn = null;
			}
		} else {
			_visibleColumnsClone.remove(drag);
		}
		append(drag, drop, _visibleColumnsClone);
		modified = true;
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "selectedVisibleColumn", "removeDisabled", "moveUpDisabled", "moveDownDisabled"})
	public void insertToHiddenColumns(@BindingParam("drag") String drag, @BindingParam("drop") String drop) {
		if (_visibleColumnsClone.contains(drag)) {
			_visibleColumnsClone.remove(drag);
			if (Objects.equals(drag, _selectedVisibleColumn)) {
				_selectedVisibleColumn = null;
			}
		} else {
			_hiddenColumnsClone.remove(drag);
		}
		append(drag, drop, _hiddenColumnsClone);
		modified = true;
	}
	
	private void append(String src, String after, List<String> list) {
		int index = list.indexOf(after);
		if (index >= 0) {
			list.add(index, list.set(index, src));
		} else {
			list.add(src);
		}
	}
	
	@Command
	public void ok(@ContextParam(ContextType.BINDER) Binder binder) {//confirm columns visibility change
		if (modified) {
			_columnChooser.setVisibleColumns(_visibleColumnsClone, false);
			_columnChooser.setHiddenColumns(_hiddenColumnsClone, false);
			init(new LinkedList<String>(_visibleColumnsClone), new LinkedList<String>(_hiddenColumnsClone));
			binder.postCommand("resetAllProperties", null);
			Events.postEvent(new ColumnVisibilityChangeEvent(_columnChooser, _columnChooser.getVisibleColumns(), _columnChooser.getHiddenColumns()));
			_columnChooser.close();
		}
	}
	
	@Command
	public void cancel(@ContextParam(ContextType.BINDER) Binder binder) {
		_columnChooser.close();
		if (modified) {
			init(new LinkedList<String>(_columnChooser.getVisibleColumns()), new LinkedList<String>(_columnChooser.getHiddenColumns()));//revert properties: columns visibility etc..
			binder.postCommand("resetAllProperties", null);
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "selectedHiddenColumn", "selectedVisibleColumn", "addDisabled", "removeDisabled", "moveUpDisabled", "moveDownDisabled"})
	public void resetAllProperties() {
	}
}
