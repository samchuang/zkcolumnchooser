package demo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.mvel2.MVEL;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.lang.Strings;
import org.zkoss.zul.Messagebox;

public class MyViewModel {
	

	ArrayList<Bean> _beans;
	
	HashMap<String, String> _columnType;
	HashMap<String, Serializable> _expressionCache;
	
	LinkedList<String> _columns;
	LinkedList<String> _visibleColumns;
	LinkedList<String> _hiddenColumns;
	
	@Init
	public void init() {
		_columns = new LinkedList<String>();
		_columns.add("a");
		_columns.add("b");
		_columns.add("c");
		_columns.add("d");
		
		_expressionCache = new HashMap<String, Serializable>(_columns.size());
		
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
	
	public List getBeans() {
		return _beans;
	}
	
	public List getVisibleColumns() {
		return new ArrayList<String>(_visibleColumns);
	}
	
	public List getHiddenColumns() {
		return new ArrayList<String>(_hiddenColumns);
	}
	
	public Object getValue(Object bean, String expression) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		System.out.println("bean [" + bean + "], expression [" + expression + "]");
		if (!Strings.isEmpty(expression)) {
			if (_expressionCache.containsKey(expression)) {
				return MVEL.executeExpression(_expressionCache.get(expression), bean);
			} else {
				Serializable s = null;
				_expressionCache.put(expression, s = MVEL.compileExpression(expression));
				return MVEL.executeExpression(s, bean);
			}
		} else {
			//why column is empty?
		}
		return null;
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "beans"})
	public void dropToVisibleColumns(@BindingParam("item") String column) {
		if (_hiddenColumns.contains(column)) {
			_hiddenColumns.remove(column);
			_visibleColumns.add(column);
		}
	} 
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "beans"})
	public void dropToHiddenColumns(@BindingParam("item") String column) {
		if (_visibleColumns.contains(column)) {
			_visibleColumns.remove(column);
			_hiddenColumns.add(column);
		}
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumns", "beans"})
	public void insertToVisibleColumns(@BindingParam("item") String drag, @BindingParam("base") String drop) {
		if (_hiddenColumns.contains(drag)) {
			_hiddenColumns.remove(drag);
		} else {
			_visibleColumns.remove(drag);
		}
		append(drag, drop, _visibleColumns);
//		int index = _visibleColumns.indexOf(drop);
//		if (index >= 0) {
//			_visibleColumns.add(index, _visibleColumns.set(index, drag));
//		} else {
//			_visibleColumns.add(drag);
//		}
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
	@NotifyChange({"visibleColumns", "hiddenColumns", "beans"})
	public void insertToHiddenColumns(@BindingParam("item") String drag, @BindingParam("base") String drop) {
		if (_visibleColumns.contains(drag)) {
			_visibleColumns.remove(drag);
		} else {
			_hiddenColumns.remove(drag);
		}
		append(drag, drop, _hiddenColumns);
//		_hiddenColumns.add(_hiddenColumns.indexOf(drop), drag);
	}
	
	public String getColumnType(@BindingParam("value") String column) {
		String type = _columnType.get(column);
		if (Strings.isEmpty(type)) {
			return "label";//default
		}
		return type;
	}
	
	@Command
	public void doClick(@BindingParam("bean") Bean bean, @BindingParam("column") String column) {
		System.out.println("debug");
		Messagebox.show("value [" + MVEL.executeExpression(_expressionCache.get(column), bean) + "]");
	}
	
	@Command
	public void doCheck(@BindingParam("bean") Bean bean, @BindingParam("column") String column) {
		System.out.println("debug");
		Messagebox.show("value [" + MVEL.executeExpression(_expressionCache.get(column), bean) + "]");
	}
}