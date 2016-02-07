package com.panzoto.people;

import java.io.*;
import java.util.*;

import javax.swing.*;

public class Person {

	// windows vaiable
	private JTextArea textArea;
	
	// personal parameters
	private boolean visible = false;
	private int index = 0;
	private String firstName = "";
	private String lastName = "";
	private long birthday = 0;
	private long ageInMs = 0;
	private float age = 0;
	private float lifeSpan = 0;
	private String gender = "";
	private int height = 0;
	private int maxHeight = 0;
	private int intelligence = 0;
	private int cash = 0;
	private int stamina = 100;
	private String job = "none";
	private String status = "";
	private boolean alive = true;
	private boolean married = false;
	private String spouseName = "none";
	private int reproduction = 0;
	private int childrenNumber = 0;
	private Education education = Education.valueOf("NONE");
	private boolean finishedSchool = false;
	
	// location variables
	private int xAxis = 0;
	private int yAxis = 0;
	//private int zAxis = 0;

	// enum for levels of school
	public static enum Education {
		NONE(0), PRE(3), ELEMENTARY(7), MIDDLE(12), HIGH(15), COLLEGE(19), GRADUATE(
				22), MEDICAL(22);
		private final int years;

		Education(int years) {
			this.years = years;
		}

		private int getYears() {
			return years;
		}
	}

	public void setEducation(String setEducation) {
		education = Education.valueOf(setEducation);
	}

	public Education getEducation() {
		return education;
	}

	public void setVisible(boolean setVisible, JTextArea setTextArea) {
		visible = setVisible;
		// System.out.println(firstName+lastName+" is visible? "+visible);
		textArea = setTextArea;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setFirstName(String setFirstName) {
		firstName = setFirstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String setLastName) {
		lastName = setLastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setBirthday(long setBirthday) {
		birthday = setBirthday;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setAgeInMs(long setAgeInMs) {
		ageInMs = setAgeInMs;
	}

	public long getAgeInMs() {
		return ageInMs;
	}

	public void setAge(float setAge) {
		age = setAge;
	}

	public float getAge() {
		return age;
	}

	public float getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(float setLifeSpan) {
		lifeSpan = setLifeSpan;
	}

	public void setIntelligence(int setIntelligence) {
		intelligence = setIntelligence;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setGender(String setGender) {
		gender = setGender;
	}

	public String getGender() {
		return gender;
	}

	public void setHeight(int setHeight) {
		if (setHeight > maxHeight) {
			System.out.println("you cannot be higher than maxHeight");
			setHeight = maxHeight;
		}
		height = setHeight;
	}

	public int getHeight() {
		return height;
	}

	public void setMaxHeight(int setMaxHeight) {
		maxHeight = setMaxHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setCash(int setCash) {
		cash = setCash;
	}

	public int getCash() {
		return cash;
	}

	public void setStamina(int setStamina) {
		stamina = setStamina;
	}

	public long getStamina() {
		return stamina;
	}

	public void setJob(String setJob) {
		job = setJob;
	}

	public String getJob() {
		return job;
	}

	public void setFinishedSchool(boolean setFinishedSchool) {
		finishedSchool = setFinishedSchool;
	}

	public boolean getFinishedSchool() {
		return finishedSchool;
	}

	public void setStatus(String setStatus) {
		status = setStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setAlive(boolean setAlive) {
		alive = setAlive;
	}

	public boolean getAlive() {
		return alive;
	}

	public void setMarried(boolean setMarried) {
		married = setMarried;
	}

	public boolean getMarried() {
		return married;
	}

	public void setSpouseName(String setSpouseName) {
		spouseName = setSpouseName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setReproduction(int setReproduction) {
		reproduction = setReproduction;
	}

	public int getReproduction() {
		return reproduction;
	}

	public void setChildrenNumber(int setChildrenNumber) {
		childrenNumber = setChildrenNumber;
	}

	public int getChildrenNumber() {
		return childrenNumber;
	}

	public void setIndex(int setIndex) {
		index = setIndex;
	}

	public int getIndex() {
		return index;
	}
	
	public int getXAxis() {
		return xAxis;
	}

	public void setXAxis(int setXAxis) {
		xAxis = setXAxis;
	}
	
	public int getYAxis() {
		return yAxis;
	}

	public void setYAxis(int setYAxis) {
		yAxis = setYAxis;
	}
	
	/*
	public int getZAxis() {
		return zAxis;
	}

	public void setZAxis(int setZAxis) {
		zAxis = setZAxis;
	}
	*/

	// give a random number from min to max
	public static int randomNumber(int min, int max) {
		Random rand = new Random();
		int randomNumber = rand.nextInt(max - min + 1) + min;
		return randomNumber;
	}

	// give a random number according to normal distribution
	public static int randomDistribution(float mean, float stdev) {
		int min = 0;
		Random rand = new Random();
		int randomNumber = min + (int) (rand.nextGaussian() * stdev + mean);
		return randomNumber;
	}

	// randomly generate a name
	public String generateName() {
		String name = "";
		int length = randomNumber(3, 9);
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		Random r = new Random();
		// generate random letter from the alphabet
		for (int i = 0; i < length; i++) {
			char letter = alphabet.charAt(r.nextInt(alphabet.length()));
			name = name + letter;
		}
		return name;
	}

	public void determineGender() {
		int randomNumber = randomNumber(0, 1);
		if (randomNumber == 0) {
			gender = "male";
			// System.out.println("gender is " + gender);
		} else {
			gender = "female";
			// System.out.println("gender is " + gender);
		}
	}

	// check alive
	public void checkAlive() {
		if (age > lifeSpan) {
			// System.out.println(firstName + " " + lastName +
			// " died of old age at " + age);

			if (visible) {
				textArea.append(firstName + " " + lastName
						+ " died of old age at " + age);
			}
			alive = false;
		}
	}

	public void findWork() {

		// create a list of professions, each include Work.java class
		List<Work> profession = new ArrayList<Work>();

		profession = loadProfession();

		// create a list of all professions
		List<String> availableJob = new ArrayList<String>();

		// check for available jobs using IQ
		for (int i = 0; i < profession.size(); i++) {
			if (intelligence >= profession.get(i).getRequiredIQ()) {
				// System.out.println(firstName + " job is :" +
				// profession.get(i).getName());
				// System.out.println(firstName + " required IQ: " +
				// profession.get(i).getRequiredIQ());
				availableJob.add(profession.get(i).getName());
			}
		}

		// if there is no available job (low IQ), assign nothing
		// other wise randomize job
		int selector = 0;
		if (availableJob.size() == 0) {
			// System.out.println("No job found for " + firstName);
		} else {
			selector = randomNumber(1, availableJob.size());
			// System.out.println("selector is " + selector);
			job = availableJob.get(selector - 1);
		}

		// change profession to doctor if went to medical school
		if (education.equals(Education.valueOf("MEDICAL"))) {
			job = "doctor";
		}
		// System.out.println(firstName + " available jobs: " + job);
	}

	public List<Work> loadProfession() {

		// create a list of professions, each include Work.java class
		List<Work> profession = new ArrayList<Work>();

		// System.out.println("loading people");
		// Find the the number of jobs available
		Scanner sc = null;
		try {
			sc = new Scanner(new File("work.data"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNext()) {
			String line = sc.nextLine();
			String[] saved = line.split("\\t");

			// new job
			Work job = new Work();
			profession.add(job);
			profession.get(profession.size() - 1).setName(saved[0]);
			profession.get(profession.size() - 1).setRequiredIQ(
					Integer.parseInt(saved[1]));
			profession.get(profession.size() - 1)
					.setRequiredEducation(saved[2]);
			profession.get(profession.size() - 1).setStartHour(
					Integer.parseInt(saved[3]));
			profession.get(profession.size() - 1).setEndHour(
					Integer.parseInt(saved[4]));
			profession.get(profession.size() - 1).setPay(
					Integer.parseInt(saved[5]));
			profession.get(profession.size() - 1).setStress(
					Integer.parseInt(saved[6]));
		}
		sc.close();
		return profession;
	}

	public void goToWork(int hourOfDay) {

		int startWork = 0;
		int endWork = 0;
		int pay = 0;
		int stress = 0;

		Scanner sc = null;
		try {
			sc = new Scanner(new File("work.data"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNext()) {
			String line = sc.nextLine();
			String[] saved = line.split("\\t");
			if (saved[0].equals(job)) {
				startWork = Integer.parseInt(saved[3]);
				endWork = Integer.parseInt(saved[4]);
				pay = Integer.parseInt(saved[5]);
				stress = Integer.parseInt(saved[6]);
			}
		}
		sc.close();

		// if it's time to work, set status to working
		if (hourOfDay > startWork && hourOfDay < endWork
				&& !status.equals("working")) {
			status = "working";
			// System.out.println("Start working");

			// display if focused by user
			if (visible)
				textArea.append("start working\n");
		}

		// if it's time to get off work, get paid, set stress
		if (hourOfDay > endWork && status.equals("working")) {
			status = "offWork";
			cash = cash + pay * intelligence / 100;
			stamina = stamina - stress * 10;
			// System.out.println("finished working. stamina: " + stamina);

			// display if focused by user
			if (visible)
				textArea.append("finished working\n");
		}
	}

	public void sleep(int hourOfDay) {

		// go to sleep at 11 pm
		if ((hourOfDay >= 23 && !status.equals("sleeping"))
				|| (hourOfDay <= 6 && !status.equals("sleeping"))) {
			status = "sleeping";
			stamina = 100;
			// System.out.println("sleeping");

			// display if focused by user
			if (visible) {
				textArea.append("sleeping\n");

				// scroll pane at when sleeping
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		}

		// wake up at 7 am
		if (hourOfDay > 6 && hourOfDay < 23 && status.equals("sleeping")) {
			status = "wake";
			// System.out.println("waked up");

			// display if focused by user
			if (visible)
				textArea.append("waked up\n");
		}
	}

	public void goToSchool(int hourOfDay) {

		// System.out.println("got to school.");
		assignGrade();

		// go to school at 8 am
		if (hourOfDay >= 8 && hourOfDay < 15 && !status.equals("atSchool")) {
			status = "atSchool";
			stamina -= 50;
			// System.out.println("At school");

			// display if focused by user
			if (visible) {
				textArea.append("At " + education.toString().toLowerCase()
						+ " school\n");
				// System.out.println(firstName + " " + lastName +
				// ", value of education: "+education.toString().toLowerCase());
			}
		}

		// get off school at 3 pm
		if (hourOfDay >= 15 && status.equals("atSchool")) {
			status = "offSchool";
			// System.out.println("Off school");

			// display if focused by user
			if (visible)
				textArea.append("Off " + education.toString().toLowerCase()
						+ " school\n");
		}
	}

	public void assignGrade() {
		if (age < Education.PRE.getYears()) {
			education = Education.valueOf("NONE");
		} else if (age >= Education.PRE.getYears()
				&& age < Education.ELEMENTARY.getYears()) {
			education = Education.valueOf("PRE");
		} else if (age >= Education.ELEMENTARY.getYears()
				&& age < Education.MIDDLE.getYears()) {
			education = Education.valueOf("ELEMENTARY");
		} else if (age >= Education.MIDDLE.getYears()
				&& age < Education.HIGH.getYears()) {
			education = Education.valueOf("MIDDLE");
		} else if (age >= Education.HIGH.getYears()
				&& age < Education.COLLEGE.getYears()) {
			education = Education.valueOf("HIGH");
		} else if (age >= Education.COLLEGE.getYears()
				&& age < Education.GRADUATE.getYears()) {
			education = Education.valueOf("COLLEGE");
		} else if (age >= Education.GRADUATE.getYears()
				&& (age <= Education.GRADUATE.getYears() + 6)
				&& intelligence > 130 && intelligence < 140) {
			education = Education.valueOf("GRADUATE");
		} else if (age >= Education.MEDICAL.getYears()
				&& (age <= Education.MEDICAL.getYears() + 4)
				&& intelligence > 140) {
			education = Education.valueOf("MEDICAL");
		} else {
			finishedSchool = true;
		}
	}

}