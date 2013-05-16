package org.zkoss.addon.columnchooser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.zkoss.addon.columnchooser.impl.ColumnchooserViewModel;
import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Popup;

/**
 * 
 * @author sam
 *
 */
public class Columnchooser extends Popup {
	
	private static String _templ = "~./zul/columnchooser.zul";
	
	private AnnotateBinder _binder;
	private List<String> _visibleColumns;
	private List<String> _hiddenColumns;
	
	public void setVisibleColumns(List<String> columns) {
		setVisibleColumns(columns, true);
	}
	
	public void setVisibleColumns(List<String> columns, boolean reload) {
		if (!Objects.equals(_visibleColumns, columns)) {
			_visibleColumns = columns != null ? new LinkedList<String>(columns) : null;
			
			if (reload && _binder != null) {
				_binder.loadComponent(this, true);
			}
		}
	}
	
	public List<String> getVisibleColumns() {
		return _visibleColumns != null ?
				Collections.unmodifiableList(_visibleColumns) : null;
	}
	
	public void setHiddenColumns(List<String> columns) {
		setHiddenColumns(columns, true);
	}
	
	public void setHiddenColumns(List<String> columns, boolean reload) {
		if (!Objects.equals(_hiddenColumns, columns)) {
			_hiddenColumns = columns != null ? new LinkedList<String>(columns) : null;
			
			if (_binder != null) {
				_binder.loadComponent(this, true);
			}
		}
	}
	
	public List<String> getHiddenColumns() {
		return _hiddenColumns != null ?
				Collections.unmodifiableList(_hiddenColumns) : null;
	}
	
	public static void setTemplate(String uri) {
		if (Strings.isEmpty(uri))
			throw new IllegalArgumentException("empty template");
		_templ = uri;
	}

	public static String getTemplate() {
		return _templ;
	}
	
	public void onCreate() {
		if (_binder == null) {
			//TODO: check if apply BindComposer, if yes, get binder and reload binding
			Executions.createComponents(_templ, this, null);
			_binder = new AnnotateBinder();
			_binder.init(this, new ColumnchooserViewModel(), null);
			setAttribute("vm", _binder.getViewModel());
			_binder.loadComponent(this, true);
			_binder.initAnnotatedBindings();
		}
	}
	
	/**
	 * The ViewModel of the template zul page
	 * 
	 * @author sam
	 */
	public interface ViewModel {
		
		/**
		 * Used with binding: @load(vm.hiddenColumns)
		 */
		public List<String> getVisibleColumns();
		
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