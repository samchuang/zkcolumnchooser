package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.addon.columnchooser.Columnchooser;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;


public class MyViewModel {
	
	ArrayList<Bean> _beans;
	
	HashMap<String, String> _columnType;
	
	LinkedList<String> _columns;
	List<String> _visibleColumns;
	List<String> _hiddenColumns;
	
	@Wire
	Columnchooser columnchooser;
	
	@Init
	public void init() {
		_columns = new LinkedList<String>();
		_columns.add("a");
		_columns.add("b");
		_columns.add("c");
		_columns.add("d");
		
		_columnType = new HashMap<String, String>(_columns.size());
		_columnType.put("a", "checkbox");
		_columnType.put("c", "button");
		
		_visibleColumns = new LinkedList<String>();
		_visibleColumns.add("b");
		_visibleColumns.add("a");
		_visibleColumns.add("c");
		
		_hiddenColumns = new LinkedList<String>();
		_hiddenColumns.add("d");
		
		_beans = new ArrayList<Bean>();
		for (int i = 0; i < 2; i++) {
			_beans.add(new Bean("a" + i, "b" + i, "c" + i, "d" + i));
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void openColumnchooser(@BindingParam("ref") Component ref) {
		columnchooser.open(ref);
	}
	
	public List getBeans() {
		return _beans;
	}
	
	public List<String> getVisibleColumns() {
		return _visibleColumns;
	}
	
	public List<String> getHiddenColumns() {
		return _hiddenColumns;
	}
	
	@Command
	@NotifyChange({"visibleColumns", "beans"})
	public void doColumnVisibilityChange(@BindingParam("visibleColumns") List<String> visibleColumns, @BindingParam("hiddenColumns") List<String> hiddenColumns) {
		//redraw columns
		_visibleColumns = visibleColumns;
		_hiddenColumns = hiddenColumns;
		System.out.println("doVisibleColumnChange");
	}
	
	public String getColumnType(@BindingParam("value") String column) {
		String type = _columnType.get(column);
		if (Strings.isEmpty(type)) {
			return "label";//default
		}
		return type;
	}
	
	@Command
	public void doClick(@BindingParam("cell") Object value) {
//		public void doClick(@BindingParam("bean") Bean bean, @BindingParam("column") String column) {
		System.out.println("debug [" + value + "]");
		
//		Messagebox.show("value [" + MVEL.executeExpression(_expressionCache.get(column), bean) + "]");
	}
	
	@Command
	public void doCheck(@BindingParam("cell") Object value) {
		System.out.println("debug [" + value + "]");
//		Messagebox.show("value [" + MVEL.executeExpression(_expressionCache.get(column), bean) + "]");
	}
}