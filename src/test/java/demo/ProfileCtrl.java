/* ProfileCtrl.java

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
import java.util.List;

import org.zkoss.addon.columnchooser.ColumnVisibilityChangeEvent;
import org.zkoss.addon.columnchooser.Columnchooser;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class ProfileCtrl extends SelectorComposer<Component> {
	
	@Wire
	Button button;
	
	@Wire
	Columnchooser columnchooser;
	
	@Wire
	Listbox listbox;
	
	List<Profile> profiles = Profiles.provideData();
	
	List<String> visibleColumns;
	
	List<String> hiddenColumns;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		visibleColumns = new ArrayList<String>();
		visibleColumns.add("Name");
		visibleColumns.add("Birth");
		
		hiddenColumns = new ArrayList<String>();
		hiddenColumns.add("Marital status");
		hiddenColumns.add("Professional Skill");
		
		columnchooser.setVisibleColumns(visibleColumns);
		columnchooser.setHiddenColumns(hiddenColumns);
		
		Listhead listhead = listbox.getListhead();
		for (String visibleColumn : visibleColumns) {
			listhead.appendChild(new Listheader(visibleColumn));
		}
		
		listbox.setModel(new ListModelList<Profile>(profiles));
		listbox.setItemRenderer(new ListitemRenderer<Profile>() {

			public void render(Listitem item, final Profile profile, int index)
					throws Exception {
				for (String visibleColumn : visibleColumns) {
					if ("Name".equals(visibleColumn)) {
						item.appendChild(new Listcell(profile.getName()));
					} else if ("Birth".equals(visibleColumn)) {
						final Datebox datebox = new Datebox(profile.getBirth());
						datebox.addEventListener(Events.ON_CHANGE, new EventListener<InputEvent>() {

							public void onEvent(InputEvent event)
									throws Exception {
								profile.setBirth(datebox.getValue());
							}
						});
						
						Listcell cell = new Listcell();
						cell.appendChild(datebox);
						item.appendChild(cell);
					} else if ("Marital status".equals(visibleColumn)) {
						Checkbox checkbox = new Checkbox("Married");
						checkbox.setChecked(profile.isMarried());
						checkbox.addEventListener(Events.ON_CHECK, new EventListener<CheckEvent>() {

							public void onEvent(CheckEvent event)
									throws Exception {
								profile.setMarried(event.isChecked());
							}
						});
						
						Listcell cell = new Listcell();
						cell.appendChild(checkbox);
						item.appendChild(cell);
					} else if ("Professional Skill".equals(visibleColumn)) {
						final Chosenbox chosebox = new Chosenbox();
						chosebox.setHflex("true");
						chosebox.setModel(new ListModelList<Skill>(Skill.values()));
						chosebox.setSelectedObjects(profile.getSkills());
						chosebox.addEventListener(Events.ON_SELECT, new EventListener<SelectEvent>() {

							public void onEvent(SelectEvent event)
									throws Exception {
								profile.setSkill(new ArrayList<Skill>(event.getSelectedObjects()));
							}
						});
						
						Listcell cell = new Listcell();
						cell.appendChild(chosebox);
						item.appendChild(cell);
					}
				}
			}
		});
	}
	
	@Listen("onClick=#button")
	public void openColumnChooser() {
		columnchooser.open(button, "end_before");
	}
	
	@Listen("onColumnVisibilityChange=#columnchooser")
	public void doColumnVisibilityChange(ColumnVisibilityChangeEvent event) {
		visibleColumns = event.getVisibleColumns();
		hiddenColumns = event.getHiddenColumns();
		
		Listhead listhead = listbox.getListhead();
		listhead.getChildren().clear();
		for (String visibleColumn : visibleColumns) {
			listhead.appendChild(new Listheader(visibleColumn));
		}
		
		listbox.setModel(new ListModelList<Profile>(profiles));
	}
}