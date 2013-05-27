package demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

public class Profile {
	
	String name;
//	int age;
	Date birth;
	boolean married;
	List<Skill> skills;
	
	public Profile(String name, int birthYear, int birthMonth, int birthDayOfMonth, boolean married,
			Skill[] skills) {
		this.name = name;
//		this.age = Calendar.getInstance().get(Calendar.YEAR) - birthYear;
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
	
//	public int getAge() {
//		return age;
//	}
//	
//	public void setAge(int age) {
//		this.age = age;
//	}
	
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
