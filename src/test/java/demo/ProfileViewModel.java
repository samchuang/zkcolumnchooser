/* ProfileViewModel.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.addon.columnchooser.Columnchooser;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;


public class ProfileViewModel {
	
	ArrayList<Profile> _data;
	
	ArrayList<ColumnInfo> _columns;
	
	@Wire
	Columnchooser columnchooser;
	
	@Init
	public void init() {
		_columns = new ArrayList<ColumnInfo>();
		_columns.add(new ColumnInfo("name", "Name", true, "label"));
		_columns.add(new ColumnInfo("birth", "Birth", true, "birth"));
		_columns.add(new ColumnInfo("married", "Marital status", false, "married"));
		_columns.add(new ColumnInfo("skills", "Professional Skill", false, "skills"));
		
		_data = provideData();
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void openDefaultColumnChooser(@BindingParam("ref") Component ref) {
		columnchooser.open(ref, "after_end");
	}
	
	public List<Profile> getProfiles() {
		return _data;
	}
	
	public ArrayList<ColumnInfo> getVisibleColumns() {
		return getColumns(Filter.VISIBLE);
	}
	
	public ArrayList<String> getVisibleColumnLabels() {
		return transform(getVisibleColumns(), Transformer.TO_LABEL);
	}
	
	public ArrayList<ColumnInfo> getHiddenColumns() {
		return getColumns(Filter.HIDDEN);
	}
	
	public ArrayList<String> getHiddenColumnLabels() {
		return transform(getHiddenColumns(), Transformer.TO_LABEL);
	}
	
	@Command
	@NotifyChange({"visibleColumns", "hiddenColumnLabels", "profiles", "visibleColumnLabels", "hiddenColumnLabels"})
	public void doColumnVisibilityChange(@BindingParam("visibleColumns") List<String> visibleColumns, @BindingParam("hiddenColumns") List<String> hiddenColumns) {
		HashMap<String, ColumnInfo> map = new HashMap<String, ColumnInfo>();
		for (ColumnInfo info : _columns) {
			map.put(info.getLabel(), info);
		}
		
		_columns = new ArrayList<ColumnInfo>();
		for (String visibleColumn : visibleColumns) {
			ColumnInfo columnInfo = map.get(visibleColumn);
			columnInfo.setVisible(true);
			_columns.add(columnInfo);
		}
		for (String hiddenColumn : hiddenColumns) {
			ColumnInfo columnInfo = map.get(hiddenColumn);
			columnInfo.setVisible(false);
			_columns.add(columnInfo);
		}
	}
	
	@Command
	public void setBirth(@BindingParam("profile") Profile profile, @BindingParam("birth") Date birth) {
		profile.setBirth(birth);
	}
	
	@Command
	public void setMarried(@BindingParam("profile") Profile profile, @BindingParam("married") Boolean married) {
		profile.setMarried(married);
	}
	
	@Command
	public void setSkills(@BindingParam("profile") Profile profile, @BindingParam("skills") Collection<Skill> skills) {
		profile.setSkill(new ArrayList<Skill>(skills));
	}
	
	public Skill[] getAllSkills() {
		return Skill.values();
	}
	
	private ArrayList<Profile> provideData() {
		ArrayList<Profile> data = new ArrayList<Profile>();
		
		data.add(new Profile("John", 1980, 1, 20, true, new Skill[]{Skill.AJAX}));
		data.add(new Profile("Mary", 1982, 2, 11, true, new Skill[]{Skill.Java}));
		data.add(new Profile("David", 1979, 3, 21, true, new Skill[]{Skill.Erlang, Skill.Haskell}));
		data.add(new Profile("Peter", 1977, 2, 15, true, new Skill[]{Skill.HTML, Skill.JavaScript}));
		data.add(new Profile("Michael", 1989, 5, 15, false, new Skill[]{Skill.Dart, Skill.JavaScript, Skill.HTML, Skill.CSS}));
		data.add(new Profile("Andy", 1981, 4, 5, true, new Skill[]{Skill.C, Skill.Java, Skill.JavaScript}));
		data.add(new Profile("Kevin", 1988, 10, 9, false, new Skill[]{Skill.Ruby, Skill.Lisp}));
		data.add(new Profile("Alex", 1983, 3, 20, true, new Skill[]{Skill.Haskell}));
		data.add(new Profile("Allen", 1985, 5, 20, false, new Skill[]{Skill.CSS, Skill.HTML}));
		data.add(new Profile("Amy", 1984, 4, 10, false, new Skill[]{Skill.CSS}));
		data.add(new Profile("Lisa", 1987, 7, 17, false, new Skill[]{Skill.CSS, Skill.HTML}));
		data.add(new Profile("Vicky", 1986, 6, 5, false, new Skill[]{Skill.Java, Skill.JavaScript, Skill.HTML}));
		data.add(new Profile("Emily", 1983, 3, 3, false, new Skill[]{Skill.Dart, Skill.JavaScript, Skill.HTML, Skill.CSS}));
		data.add(new Profile("Angela", 1986, 6, 3, false, new Skill[]{Skill.Erlang, Skill.Haskell, Skill.Lisp}));
		data.add(new Profile("Amanda", 1984, 5, 8, false, new Skill[]{Skill.Java, Skill.AJAX}));
		data.add(new Profile("Peggy", 1983, 6, 8, true, new Skill[]{Skill.JavaScript, Skill.HTML}));
		
		return data;
	}
	
	private ArrayList<ColumnInfo> getColumns(Filter filter) {
		ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
		for (ColumnInfo info : _columns) {
			if (filter.accept(info))
				list.add(info);
		}
		return list;
	}
	
	private ArrayList transform(List<ColumnInfo> source, Transformer transformer) {
		ArrayList list = new ArrayList();
		for (ColumnInfo info : source) {
			list.add(transformer.transform(info));
		}
		return list;
	}
	
	interface Filter {
		public boolean accept(ColumnInfo info);
		
		Filter VISIBLE = new Filter() {

			public boolean accept(ColumnInfo info) {
				return info.isVisible();
			}
		};
		
		Filter HIDDEN = new Filter() {

			public boolean accept(ColumnInfo info) {
				return !info.isVisible();
			}
		};
	}
	
	interface Transformer {
		public Object transform(ColumnInfo from);
		
		Transformer TO_LABEL = new Transformer() {

			public Object transform(ColumnInfo from) {
				return from.getLabel();
			}
		};
	}
}