/* Profile.java

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

public class Profile {
	
	String name;
	Date birth;
	boolean married;
	List<Skill> skills;
	
	public Profile(String name, int birthYear, int birthMonth, int birthDayOfMonth, boolean married,
			Skill[] skills) {
		this.name = name;
		this.birth = new GregorianCalendar(birthYear, birthMonth, birthDayOfMonth).getTime();
		this.married = married;
		ArrayList<Skill> list = new ArrayList<Skill>();
		for (Skill e : skills) {
			list.add(e);
		}
		this.skills = list;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getBirth() {
		return birth;
	}
	
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	public boolean isMarried() {
		return married;
	}
	
	public void setMarried(boolean married) {
		this.married = married;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	
	public void setSkill(List<Skill> skills) {
		this.skills = skills;
	}
}
