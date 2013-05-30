/* Columnchooser.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.columnchooser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.lang.Library;
import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.impl.Utils;
import org.zkoss.zk.ui.metainfo.Property;
import org.zkoss.zul.Popup;

/**
 * 
 * @author sam
 *
 */
public class Columnchooser extends Popup {
	
	public static final String TEMPLATE = "org.zkoss.addon.columnchooser.template";
	
	private static final String DEFAULT_TEMPLATE = "~./zul/columnchooser.zul";
	private String _templ = Library.getProperty(TEMPLATE, DEFAULT_TEMPLATE);
	
	private Component _cave;
	private Binder _caveBinder;
	private ViewModel _caveViewModel;
	
	private List<String> _visibleColumns;
	private List<String> _hiddenColumns;
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		
		boolean setTemplate = false; 
		List<Property> properties = Utils.getComponentInfo(this).getProperties();
		for (Property property : properties) {
			if ("template".equals(property.getName())) {
				setTemplate = true;
				break;
			}
		}
		
		if (!setTemplate && _cave == null) {
			initCave();
		}
	}
	
	protected void initCave() {
		getChildren().clear();
		_cave = Executions.createComponents(_templ, this, null);
		_caveBinder = (Binder)_cave.getAttribute(BinderImpl.BINDER);
		if (_caveBinder == null) {
			throw new NullPointerException("binder is null, [" + _templ + "] should contains binder and view model");
		}
		_caveViewModel = (ViewModel) _caveBinder.getViewModel();
	}

	public void setVisibleColumns(List<String> columns) {
		setVisibleColumns(columns, true);
	}
	
	public void setVisibleColumns(List<String> columns, boolean reload) {
		if (!Objects.equals(_visibleColumns, columns)) {
			_visibleColumns = columns != null ? new LinkedList<String>(columns) : null;
			
			if (reload) {
				_caveViewModel.setVisibleColumns(columns);
				_caveBinder.postCommand("resetAllProperties", null);
			}
		}
	}
	
	public List<String> getVisibleColumns() {
		return _visibleColumns != null ?
				Collections.unmodifiableList(_visibleColumns) : Collections.EMPTY_LIST;
	}
	
	public void setHiddenColumns(List<String> columns) {
		setHiddenColumns(columns, true);
	}
	
	public void setHiddenColumns(List<String> columns, boolean reload) {
		if (!Objects.equals(_hiddenColumns, columns)) {
			_hiddenColumns = columns != null ? new LinkedList<String>(columns) : null;
			
			if (reload) {
				_caveViewModel.setHiddenColumns(columns);
				_caveBinder.postCommand("resetAllProperties", null);
			}
		}
	}
	
	public List<String> getHiddenColumns() {
		return _hiddenColumns != null ?
				Collections.unmodifiableList(_hiddenColumns) : Collections.EMPTY_LIST;
	}
	
	public void setTemplate(String uri) {
		if (Strings.isEmpty(uri))
			throw new IllegalArgumentException("empty template");
		
		if (!Objects.equals(_templ, uri)) {
			_templ = uri;
			initCave();
		}
	}

	public String getTemplate() {
		return _templ;
	}
	
	public void onOpen(OpenEvent event) {
		if (_caveBinder != null && !event.isOpen())
			_caveBinder.postCommand("cancel", null);
	}
	
	/**
	 * The ViewModel of the template zul page
	 * 
	 * @author sam
	 */
	public interface ViewModel {
		
		/**
		 * Sets visible columns
		 */
		public void setVisibleColumns(List<String> visibleColumns);
		
		/**
		 * Used with binding: @load(vm.hiddenColumns)
		 */
		public List<String> getVisibleColumns();
		
		/**
		 * Sets hidden columns
		 */
		public void setHiddenColumns(List<String> hiddenColumns);
		
		/**
		 * Used with binding: @load(vm.visibleColumns)
		 */
		public List<String> getHiddenColumns();
		
		/**
		 * Used with binding: @bind(vm.selectedHiddenColumn)
		 * 
		 * The implementation should annotate with:
		 * @NotifyChange("addDisabled")
		 */
		@NotifyChange("addDisabled")
		public void setSelectedHiddenColumn(String column);
		
		/**
		 * Used with binding: @bind(vm.selectedHiddenColumn)
		 */
		public String getSelectedHiddenColumn();
		
		/**
		 * Used with binding: @bind(vm.selectedVisibleColumn)
		 * 
		 * The implementation should annotate with:
		 * @NotifyChange({"removeDisabled", "moveUpDisabled", "moveDownDisabled"})
		 */
		@NotifyChange({"removeDisabled", "moveUpDisabled", "moveDownDisabled"})
		public void setSelectedVisibleColumn(String column);
		
		/**
		 * Used with binding: @bind(vm.selectedVisibleColumn)
		 */
		public String getSelectedVisibleColumn();
		
		/**
		 * Used with binding: @load(vm.addDisabled)
		 */
		public boolean isAddDisabled();
		
		/**
		 * Used with binding: @load(vm.removeDisabled)
		 */
		public boolean isRemoveDisabled();
		
		/**
		 * Used with binding: @load(vm.moveUpDisabled)
		 */
		public boolean isMoveUpDisabled();
		
		/**
		 * Used with binding: @load(vm.moveDownDisabled)
		 */
		public boolean isMoveDownDisabled();
		
		/**
		 * Used with binding: @command('addSelectedHiddenColumnToVisibleColumn')
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"selectedHiddenColumn", "addDisabled", "visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"selectedHiddenColumn", "addDisabled", "visibleColumns", "hiddenColumns"})
		public void addSelectedHiddenColumnToVisibleColumn();
		
		/**
		 * Used with binding: @command('removeSelectedVisibleColumnToHiddenColumn')
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"selectedVisibleColumn", "removeDisabled", "moveUpDisabled", "moveDownDisabled", "visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"selectedVisibleColumn", "removeDisabled", "moveUpDisabled", "moveDownDisabled", "visibleColumns", "hiddenColumns"})
		public void removeSelectedVisibleColumnToHiddenColumn();
		
		/**
		 * Used with binding: @command('moveSelectedVisibleColumnUp')
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
		public void moveSelectedVisibleColumnUp();
		
		/**
		 * Used with binding: @command('moveSelectedVisibleColumnDown')
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "moveUpDisabled", "moveDownDisabled"})
		public void moveSelectedVisibleColumnDown();
		
		/**
		 * Used with binding: @command('dropToVisibleColumns', column=event.dragged.value)
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "hiddenColumns"})
		public void dropToVisibleColumns(@BindingParam("column") String column);
		
		/**
		 * Used with binding: @command('dropToHiddenColumns', column=event.dragged.value)
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "hiddenColumns"})
		public void dropToHiddenColumns(@BindingParam("column") String column);
		
		/**
		 * Used with binding: @command('insertToVisibleColumns', drag=event.dragged.value, drop=each)
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "hiddenColumns"})
		public void insertToVisibleColumns(@BindingParam("drag") String drag, @BindingParam("drop") String drop);
		
		/**
		 * Used with binding: @command('insertToHiddenColumns', drag=event.dragged.value, drop=each)
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "hiddenColumns"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "hiddenColumns"})
		public void insertToHiddenColumns(@BindingParam("drag") String drag, @BindingParam("drop") String drop);
		
		/**
		 * Used with binding: @command('ok')
		 * 
		 * The implementation should annotate with:
		 * @Command
		 */
		@Command
		public void ok(@ContextParam(ContextType.BINDER) Binder binder);

		/**
		 * Used with binding: @command('cancel')
		 * 
		 * The implementation should annotate with:
		 * @Command
		 */
		@Command
		public void cancel(@ContextParam(ContextType.BINDER) Binder binder);
		
		/**
		 * Used by "ok" and "cancel" command
		 * 
		 * The implementation should annotate with:
		 * @Command and @NotifyChange({"visibleColumns", "hiddenColumns", "selectedHiddenColumn", "selectedVisibleColumn", "addDisabled", "removeDisabled", "moveUpDisabled", "moveDownDisabled"})
		 */
		@Command
		@NotifyChange({"visibleColumns", "hiddenColumns", "selectedHiddenColumn", "selectedVisibleColumn", "addDisabled", "removeDisabled", "moveUpDisabled", "moveDownDisabled"})
		public void resetAllProperties();
	}
}