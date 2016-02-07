package com.panzoto.people;

import com.panzoto.people.Person.Education;

public class Work {
	
	private String name = "";
	private int requiredIQ = 0;
	private Education requiredEducation = null;
	private int startHour = 0;
	private int endHour = 0;
	private int pay = 0;
	private int stress = 0;
	
	public void setName(String setName) {
		name = setName;
	}

	public String getName() {
		return name;
	}
	
	public void setRequiredIQ(int setRequiredIQ) {
		requiredIQ = setRequiredIQ;
	}

	public int getRequiredIQ() {
		return requiredIQ;
	}
	
	
	public void setRequiredEducation(String setRequiredEducation) {
		requiredEducation = Education.valueOf(setRequiredEducation);
	}

	public Education getRequiredEducation() {
		return requiredEducation;
	}
	
	public void setStartHour(int setStartHour) {
		startHour = setStartHour;
	}

	public int getStartHour() {
		return startHour;
	}
	
	public void setEndHour(int setEndHour) {
		endHour = setEndHour;
	}

	public int getEndHour() {
		return endHour;
	}
	
	public void setPay(int setPay) {
		pay = setPay;
	}

	public int getPay() {
		return pay;
	}
	
	public void setStress(int setStress) {
		stress = setStress;
	}

	public int getStress() {
		return stress;
	}
}
