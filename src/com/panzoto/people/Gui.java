/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panzoto.people;

import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Yang
 */
public class Gui extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    // create a list of people, each include Person.java class
    private List<Person> people = new ArrayList<>();

    private String peopleLabel = "";
    private String mapLabel = "";

    // properties of time
    private int lengthOfDay = 100;
    private int timeCounterPause = lengthOfDay / 24;
    private int timeOfDay = 0;
    private int hourOfDay = 0;
    private int lengthOfYear = lengthOfDay * 365;
    private int dayOfWeek = 1; // day starts monday
    private String dayOfWeekS = "";

    // properties of population
    private int maxPopulation = 1000000; // 1,000,000

    // location variables
    private final int MAX_X_AXIS = 300;
    private final int MAX_Y_AXIS = 300;
    // private final int MAX_Z_AXIS = 300;
    private final int PARAMETERS = 4;
    private final String[][][] map = new String[MAX_X_AXIS][MAX_Y_AXIS][PARAMETERS];

    // boolean for stop Runnable
    private volatile boolean stopRequested = false;

    // daily runnable
    Runnable dailyTask = new Runnable() {
        @Override
        public void run() {
            try {
                while (!stopRequested) {

                    // determine time of day
                    timeOfDay = timeOfDay + timeCounterPause;
                    if (timeOfDay > lengthOfDay) {
                        timeOfDay = timeOfDay % lengthOfDay;

                        // assign int to string dayOfWeekS
                        assignDay();
                    }

                    hourOfDay = timeOfDay / (lengthOfDay / 24);

                    for (int i = 0; i < people.size(); i++) {

                        // display day of week if set visible (focused)
                        if (people.get(i).getVisible() && hourOfDay < 1) {
                            textArea.append(dayOfWeekS + "\n");
                            // scroll pane at when sleeping
                            textArea.setCaretPosition(textArea.getDocument()
                                    .getLength());
                        }

                        // System.out.println("hourOfDay: "+hourOfDay);
                        people.get(i)
                                .setAge(people.get(i).getAgeInMs() / (float) lengthOfYear);
						// System.out.println("ageInMs is "+people.get(i).getAgeInMs());
                        // System.out.println("age is "+people.get(i).getAge());
                        // System.out.println("length of year is "+lengthOfYear);
                        // System.out.println("visible is : "+people.get(i).getVisible());

                        // System.out.println("clock: " + timeOfDay / 3600 +
                        // ":"+
                        // timeOfDay % 3600 / 60);
                        // show time if focused
                        if (people.get(i).getVisible()) {
                            // textArea.append(hourOfDay +" o'clock\n");
                        }

                        // go to school
                        if (people.get(i).getFinishedSchool() == false
                                && dayOfWeek != 6 && dayOfWeek != 7) {
                            people.get(i).goToSchool(hourOfDay);
                        }

                        // go to work if has work, and not on weekends
                        if (!people.get(i).getJob().equals("none")
                                && dayOfWeek != 6 && dayOfWeek != 7) {
                            people.get(i).goToWork(hourOfDay);
                        }

                        // sleep at night
                        people.get(i).sleep(hourOfDay);

                        // adjust ageInMs
                        people.get(i).setAgeInMs(
                                people.get(i).getAgeInMs() + timeCounterPause);

                        // check alive
                        people.get(i).checkAlive();

                        // if alive is false, then remove the person
                        // remove person as the last thing to run
                        if (!people.get(i).getAlive()) {
                            people.remove(i);
                        }
                    }

                    // interval between each time status are checked
                    Thread.sleep(timeCounterPause);
                }
            } catch (InterruptedException iex) {
            }
        }
    };

    // yearly runnable
    Runnable yearlyTask = new Runnable() {
        @Override
		public void run() {
            try {
                while (!stopRequested) {

                    for (int i = 0; i < people.size(); i++) {

                        // find job
                        if (people.get(i).getFinishedSchool() == true
                                && people.get(i).getJob().equals("none")) {
                            // System.out.println("finding work");
                            people.get(i).findWork();
                        }

                        // get married, need a decision (0/1)
                        if (people.get(i).getAge() > 18
                                && people.get(i).getMarried() == false
                                && randomNumber(0, 1) == 1) {
                            getMarried(i);
                        }

                        // give birth, need a decision (0/1)
                        if (people.size() < maxPopulation
                                && people.get(i).getMarried() == true
                                && people.get(i).getGender().equals("female")
                                && people.get(i).getAge() < 45
                                && randomNumber(0, 1) == 1) {
                            giveBirth(i);
                        }
                    }

                    // interval between each time status is checked
                    Thread.sleep(lengthOfYear);
                }
            } catch (InterruptedException iex) {
            }
        }
    };

    // method to stop Runnable
    public void requestStop() {
        stopRequested = true;
    }

    // assign int to string dayOfWeekS
    public void assignDay() {

        // increase the day if it's over a day length
        dayOfWeek++;

        // go to sunday after monday
        if (dayOfWeek > 7) {
            dayOfWeek = dayOfWeek % 7;
        }

        switch (dayOfWeek) {
            case 1:
                dayOfWeekS = "Monday";
                break;
            case 2:
                dayOfWeekS = "Tuesday";
                break;
            case 3:
                dayOfWeekS = "Wednesday";
                break;
            case 4:
                dayOfWeekS = "Thursday";
                break;
            case 5:
                dayOfWeekS = "Friday";
                break;
            case 6:
                dayOfWeekS = "Saturday";
                break;
            case 7:
                dayOfWeekS = "Sunday";
                break;
            default:
                break;
        }
    }

    /**
     * Creates new form gui
     */
    public Gui() {
        super("Panzoto");
        initComponents();

        // initialization actions
        loadSimulation();
        // load all data of each person
        loadPeople();

        // initialize map
        loadMap();

        statusBar.setText("loading");

        // start running dailytask thread when window is loaded
        Thread thr1 = new Thread(dailyTask);
        thr1.start();

        // start running yearly thread when window is loaded
        Thread thr2 = new Thread(yearlyTask);
        thr2.start();        // TODO add your handling code here:
        statusBar.setText("loading");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        textField = new javax.swing.JTextField();
        commandList = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        statusBar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        explainArea = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        loadData = new javax.swing.JMenuItem();
        saveData = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textField.setText("Enter your commands here.");
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldActionPerformed(evt);
            }
        });

        commandList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "List of commands", "create person 3", "topAge 3", "topIQ 3", "topCash 3", "focus 3", "focus firstName lastName", "unfocus" }));
        commandList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commandListActionPerformed(evt);
            }
        });

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        statusBar.setEditable(false);

        explainArea.setColumns(20);
        explainArea.setLineWrap(true);
        explainArea.setRows(5);
        explainArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(explainArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textField)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(commandList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textField)
                    .addComponent(commandList))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        file.setText("File");
        file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileActionPerformed(evt);
            }
        });

        loadData.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadData.setText("Load data");
        loadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDataActionPerformed(evt);
            }
        });
        file.add(loadData);

        saveData.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveData.setText("Save data");
        saveData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDataActionPerformed(evt);
            }
        });
        file.add(saveData);

        jMenuBar1.add(file);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void commandListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandListActionPerformed

        JComboBox<String> cb = (JComboBox) evt.getSource();
        String setText = (String) cb.getSelectedItem();
        // System.out.println("setText is "+setText);
        textField.setText(setText);
        textField.requestFocus();        // TODO add your handling code here:
        
        // change text in explaination field

        switch (setText) {
            case "create person 3":
                explainArea.setText("Create 3 people. Or change the number to whatever number of people you want to create.");
                break;
            case "topAge 3":
                explainArea.setText("List the top 3 oldest people. Or change the number to however many top people you want to list.");
                break;
            case "topIQ 3":
                explainArea.setText("List the top 3 smartest people. Or change the number to however many top people you want to list.");
                break;
            case "topCash 3":
                explainArea.setText("List the top 3 richest people. Or change the number to however many top people you want to list.");
                break;
            case "focus 3":
                explainArea.setText("Focus on the daily activity of the person with index of 3. Or change to whoever you want to focus.");
                break;
            case "focus firstName lastName":
                explainArea.setText("Focus on the daily activity of the person with the specific first name and last name. Or change to whoever you want to focus.");
                break;
            case "unfocus":
                explainArea.setText("Unfocus on all people's activity.");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_commandListActionPerformed

    private void loadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDataActionPerformed

    }//GEN-LAST:event_loadDataActionPerformed

    private void saveDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDataActionPerformed
        savePeople();
        saveSimulation();
        saveMap();        // TODO add your handling code here:

        statusBar.setText("saving");
    }//GEN-LAST:event_saveDataActionPerformed

    private void fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileActionPerformed

    private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldActionPerformed

        // get from testField
        String prompt = textField.getText();
        String[] words = prompt.split("\\s+");

        // respond to commands
        switch (words[0]) {
            case "create":
                if (words[1].equals("person")) {
                    createPerson(Integer.parseInt(words[2]));
                } else {
                    textArea.append("Create what?\n");
                }
                break;
            case "focus":
                if (words.length == 3) {
                    focus(words[1], words[2]);
                } else if (words.length == 2) {
                    focus(Integer.parseInt(words[1]));
                } else {
                    textArea.append("Who do you want to focus (focus <first name> <last name>)");
                }
                break;
            case "unfocus":
                unfocus();
                break;
            case "topCash":
                if (words.length == 2) {
                    topCash(Integer.parseInt(words[1]));
                } else {
                    textArea.append("How many person do you want to list? (topCash <number>)");
                }
                break;
            case "topAge":
                if (words.length == 2) {
                    topAge(Integer.parseInt(words[1]));
                } else {
                    textArea.append("How many person do you want to list? (topAge <number>)");
                }
                break;
            case "topIQ":
                if (words.length == 2) {
                    topIQ(Integer.parseInt(words[1]));
                } else {
                    textArea.append("How many person do you want to list? (topIQ <number>)");
                }
                break;
            default:
                textArea.append("I don't know what you are saying.\n");
                break;
        }
        textField.selectAll();        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new Gui().setVisible(true);
            }
        });

    }

    public void createPerson(int number) {

        for (int i = 0; i < number; i++) {
            // create a person
            Person person = new Person();

            /*
             * birthDate was set at the time of class creation maximum height is
             * generated at creation each person is also a Runnable, used by
             * Threads
             */
            // System.out.println("Birthday is :"+person.getBirthDate().getTime());
            person.setFirstName(person.generateName());
            person.setLastName(person.generateName());
            person.setBirthday(System.currentTimeMillis());
            person.setLifeSpan(randomDistribution(77.67f, 16.51f));
            person.determineGender();
            person.setMaxHeight(randomDistribution(175, 7));
            person.setIntelligence(randomDistribution(100, 15));
            person.setJob("none");
            person.setStatus("born");
            // textArea.append(""+person.getAgeInMs()/person.getLengthOfYear()+"\n");

            person.setEducation("NONE");
            person.setReproduction(randomDistribution(2, 1));

            // add the new person to a list of people
            people.add(person);

            // assign person to an empty location
            assignLocation(people.size() - 1);

            // System.out.println("One person added");
            // System.out.println("people size is: " + people.size());
        }
        // System.out.println("people size is: " + people.size());
    }

    public void saveSimulation() {
        BufferedWriter writer = null;
        try {
            // read people data, tab for each property, newline for another
            // person
            writer = new BufferedWriter(
                    new FileWriter("simulation.data", false));
            if (!people.isEmpty()) {
                writer.write(timeOfDay + "\t" + dayOfWeek + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void loadSimulation() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("simulation.data"));
        } catch (FileNotFoundException e) {
        }

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] saved = line.split("\\t");
            timeOfDay = Integer.parseInt(saved[0]);
            dayOfWeek = Integer.parseInt(saved[1]);

            // System.out.println("Time of day: "+timeOfDay);
        }
        sc.close();
    }

    public void savePeople() {
        System.out.println("saving people");
        // write data for person
        BufferedWriter writer = null;
        try {
            // read people data, tab for each property, newline for another
            // person
            writer = new BufferedWriter(new FileWriter("people.data", false));

            // print label for first line
            writer.write(peopleLabel + "\n");

            for (int i = 0; i < people.size(); i++) {

                writer.write((i)
                        + "\t"
                        + people.get(i).getFirstName()
                        + "\t"
                        + people.get(i).getLastName()
                        + "\t"
                        + people.get(i).getBirthday()
                        + "\t"
                        + ((float) (people.get(i).getAgeInMs()) / (float) lengthOfYear)
                        + "\t" + people.get(i).getGender() + "\t"
                        + people.get(i).getHeight() + "\t"
                        + people.get(i).getMaxHeight() + "\t"
                        + people.get(i).getIntelligence() + "\t"
                        + people.get(i).getEducation() + "\t"
                        + people.get(i).getCash() + "\t"
                        + people.get(i).getStamina() + "\t"
                        + people.get(i).getJob() + "\t"
                        + people.get(i).getStatus() + "\t"
                        + +people.get(i).getLifeSpan() + "\t"
                        + +people.get(i).getReproduction() + "\t"
                        + people.get(i).getSpouseName() + "\t"
                        + people.get(i).getMarried() + "\t"
                        + people.get(i).getChildrenNumber() + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void loadPeople() {
        System.out.println("loading people");
        // Find the population
        Scanner sc = null;
        try {
            sc = new Scanner(new File("people.data"));
        } catch (FileNotFoundException e) {
        }

        // load first line as label
        peopleLabel = sc.nextLine();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] saved = line.split("\\t");

            // read each person
            Person person = new Person();
            people.add(person);
            people.get(people.size() - 1).setIndex(Integer.parseInt(saved[0]));
            people.get(people.size() - 1).setFirstName(saved[1]);
            people.get(people.size() - 1).setLastName(saved[2]);
            people.get(people.size() - 1).setBirthday(Long.parseLong(saved[3]));
            people.get(people.size() - 1).setAge(Float.parseFloat(saved[4]));
            people.get(people.size() - 1)
                    .setAgeInMs(
                            (long) (people.get(people.size() - 1).getAge() * lengthOfYear));
            people.get(people.size() - 1).setGender(saved[5]);
            people.get(people.size() - 1).setHeight(Integer.parseInt(saved[6]));
            people.get(people.size() - 1).setMaxHeight(
                    Integer.parseInt(saved[7]));
            people.get(people.size() - 1).setIntelligence(
                    Integer.parseInt(saved[8]));
            people.get(people.size() - 1).setEducation(saved[9]);
            people.get(people.size() - 1).setCash(Integer.parseInt(saved[10]));
            people.get(people.size() - 1).setStamina(
                    Integer.parseInt(saved[11]));
            people.get(people.size() - 1).setJob(saved[12]);
            people.get(people.size() - 1).setStatus(saved[13]);
            people.get(people.size() - 1).setLifeSpan(
                    Float.parseFloat(saved[14]));
            people.get(people.size() - 1).setReproduction(
                    Integer.parseInt(saved[15]));
            people.get(people.size() - 1).setSpouseName(saved[16]);
            people.get(people.size() - 1).setMarried(
                    Boolean.parseBoolean(saved[17]));
            people.get(people.size() - 1).setChildrenNumber(
                    Integer.parseInt(saved[18]));
            // initialize variables
        }
        sc.close();
    }

    // give a random number from min to max
    public static int randomNumber(int min, int max) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(max - min + 1) + min;
        return randomNumber;
    }

    // give a random number according to normal distribution
    public static int randomDistribution(int mean, int stdev) {
        int min = 0;
        Random rand = new Random();
        int randomNumber = min + (int) (rand.nextGaussian() * stdev + mean);
        return randomNumber;
    }

    // give a random number according to normal distribution
    public static int randomDistribution(float mean, float stdev) {
        int min = 0;
        Random rand = new Random();
        int randomNumber = min + (int) (rand.nextGaussian() * stdev + mean);
        return randomNumber;
    }

    // focus by first and last name
    public void focus(String firstName, String lastName) {
        for (Person people1 : people) {
            // if name matches, then set printout visible
            // System.out.println("looking " + people.get(i).getFirstName() +
            // " " + people.get(i).getLastName());
            if (firstName.equals(people1.getFirstName()) && lastName.equals(people1.getLastName())) {
                // System.out.println(firstName + " " + lastName
                // +" is "+people.get(i).getStatus());
                textArea.append(firstName + " " + lastName + " is " + people1.getStatus() + "\n");
                people1.setVisible(true, textArea);
            }
        }
    }

    // focus by index number
    public void focus(int personIndex) {
        people.get(personIndex).setVisible(true, textArea);
        String firstName = people.get(personIndex).getFirstName();
        String lastName = people.get(personIndex).getLastName();
        textArea.append(firstName + " " + lastName + " is "
                + people.get(personIndex).getStatus() + "\n");
    }

    public void unfocus() {
        for (Person people1 : people) {
            // System.out.println(people.get(i).getFirstName() + " " +
            // people.get(i).getLastName()
            // +" set visible is "+people.get(i).getVisible());
            if (people1.getVisible() == true) {
                people1.setVisible(false, textArea);
                textArea.append("unfocused");
            }
        }
    }

    public void topCash(int number) {
        float[] cash = new float[people.size()];
        int[] index = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            cash[i] = people.get(i).getCash();
            index[i] = i;
        }
        quicksort(cash, index);

        // for showing data
		/*
         * for (int i = 0; i<people.size(); i++){
         * System.out.println(cash[i]+" "+index[i]); }
         */
        textArea.append("\nTop cash earner:\n");
        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (int i = 1; i < number + 1; i++) {
            int place = index[index.length - i];
            textArea.append(place + "\t" + people.get(place).getFirstName()
                    + "\t" + people.get(place).getLastName() + "\t" + "IQ:"
                    + people.get(place).getIntelligence() + "\t"
                    + people.get(place).getJob() + "\t"
                    + people.get(place).getAgeInMs() / lengthOfYear
                    + " yrs old" + "\t$" + people.get(place).getCash() + "\n");
        }

        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        // scroll pane at when sleeping
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    // beginning of the modified quicksort methods
    public static void quicksort(float[] main, int[] index) {
        quicksort(main, index, 0, index.length - 1);
    }

    // quicksort a[left] to a[right]
    public static void quicksort(float[] a, int[] index, int left, int right) {
        if (right <= left) {
            return;
        }
        int i = partition(a, index, left, right);
        quicksort(a, index, left, i - 1);
        quicksort(a, index, i + 1, right);
    }

    // partition a[left] to a[right], assumes left < right
    private static int partition(float[] a, int[] index, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (less(a[++i], a[right]))
				// find item on left to swap
				; // a[right] acts as sentinel
            while (less(a[right], a[--j])) // find item on right to swap
            {
                if (j == left) {
                    break; // don't go out-of-bounds
                }
            }
            if (i >= j) {
                break; // check if pointers cross
            }
            exch(a, index, i, j); // swap two elements into place
        }
        exch(a, index, i, right); // swap with partition element
        return i;
    }

    // is x < y ?
    private static boolean less(float x, float y) {
        return (x < y);
    }

    // exchange a[i] and a[j]
    private static void exch(float[] a, int[] index, int i, int j) {
        float swap = a[i];
        a[i] = a[j];
        a[j] = swap;
        int b = index[i];
        index[i] = index[j];
        index[j] = b;
    }

    // end of the modified quicksort method
    public void topIQ(int number) {
        float[] IQ = new float[people.size()];
        int[] index = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            IQ[i] = people.get(i).getIntelligence();
            index[i] = i;
        }
        quicksort(IQ, index);

        // for showing value
		/*
         * for (int i = 0; i<people.size(); i++){
         * System.out.println(IQ[i]+" "+index[i]); }
         */
        textArea.append("\nTop smart people:\n");
        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (int i = 1; i < number + 1; i++) {
            int place = index[index.length - i];
            textArea.append(place + "\t" + people.get(place).getFirstName()
                    + "\t" + people.get(place).getLastName() + "\t"
                    + people.get(place).getAgeInMs() / lengthOfYear
                    + " yrs old" + "\t$" + people.get(place).getCash()
                    + "\tIQ:" + people.get(place).getIntelligence() + "\n");
        }

        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        // scroll pane at when sleeping
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void topAge(int number) {
        float[] age = new float[people.size()];
        int[] index = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            age[i] = people.get(i).getAgeInMs();
            index[i] = i;
        }
        quicksort(age, index);

        // for showing data
		/*
         * for (int i = 0; i<people.size(); i++){
         * System.out.println(age[i]+" "+index[i]); }
         */
        textArea.append("\nTop old people:\n");
        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (int i = 1; i < number + 1; i++) {
            int place = index[index.length - i];
            textArea.append(place + "\t" + people.get(place).getFirstName()
                    + "\t" + people.get(place).getLastName() + "\t"
                    + people.get(place).getAgeInMs() / lengthOfYear + " yrs "
                    + "\t$" + people.get(place).getCash() + "\tIQ:"
                    + people.get(place).getIntelligence() + "\n");
        }

        textArea.append("---------------------------------------------------------------------------------------------------------------------------------------------\n");

        // scroll pane at when sleeping
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void getMarried(int personIndex) {
        // System.out.println("person getting married "+(personIndex+1));
        // System.out.println("people.size() is "+people.size());
        String spouseGender = null;
        if (people.get(personIndex).getGender().equals("female")) {
            // System.out.println("person is: "+people.get(personIndex).getGender());
            spouseGender = "male";
        } else {
            spouseGender = "female";
        }
        // System.out.println("spouseGender is "+spouseGender);
        int rand = 0;
        // keep searching until spouse have the opposite gender
        while (people.get(rand).getGender().equals(spouseGender) == false) {
            // set the spouse index to a random number
            rand = randomNumber(0, people.size() - 1);
            String spouseName = people.get(rand).getLastName();
            String lastName = people.get(personIndex).getLastName();
            float ageDifference = Math.abs(people.get(personIndex).getAge()
                    - people.get(rand).getAge());
            if (people.get(rand).getGender().equals(spouseGender) == true
                    && people.get(rand).getAge() >= 18 && ageDifference <= 20) {
                // System.out.println("selected spouseGender "+(rand+1)+" is "+people.get(rand).getGender());
                people.get(personIndex).setSpouseName(spouseName);
                people.get(personIndex).setMarried(true);
                people.get(rand).setSpouseName(lastName);
                people.get(rand).setMarried(true);

                // print message if focused
                if (people.get(personIndex).getVisible()) {
                    textArea.append("Got married to "
                            + people.get(rand).getLastName());
                }
                if (people.get(rand).getVisible()) {
                    textArea.append("Got married!"
                            + people.get(personIndex).getLastName());
                }
            } else if (people.get(rand).getGender().equals(spouseGender) == false) {
                // System.out.println("selected person "+(rand+1)+" is not the right gender");
            } else if (people.get(rand).getAge() < 18) {
                // System.out.println("selected person "+(rand+1)+" is too young");
            } else if (ageDifference > 20) {
                // System.out.println("The age difference is too great");
            }
        }
    }

    public void giveBirth(int personIndex) {
        // int spouseIndex = people.get(personIndex).getSpouseIndex();
        int childrenNumber = people.get(personIndex).getChildrenNumber();
        boolean capable = people.get(personIndex).getChildrenNumber() < people
                .get(personIndex).getReproduction() + 1;
        if (capable) {
            people.get(personIndex).setChildrenNumber(childrenNumber + 1);
            // people.get(spouseIndex).setChildrenNumber(childrenNumber+1);

            createPerson(1);

            if (people.get(personIndex).getVisible()) {
                textArea.append("Gave birth to "
                        + people.get(people.size()).getFirstName());
            }
        }
    }

    public void assignLocation(int personIndex) {

        int x = randomNumber(0, MAX_X_AXIS - 1);
        int y = randomNumber(0, MAX_Y_AXIS - 1);
		// int z = randomNumber(0, MAX_Z_AXIS - 1);

        // System.out.println("x y z is:"+x+"\t"+y+"\t"+z+"\t");
        // System.out.println("map[x][y][z] is "+map[x][y][z]);
        // assign random position to person if it is empty
        if (map[x][y][0] == null) {
            people.get(personIndex).setXAxis(x);
            people.get(personIndex).setYAxis(y);
            // people.get(personIndex).setZAxis(z);
            map[x][y][0] = people.get(personIndex).getFirstName();
            map[x][y][1] = people.get(personIndex).getLastName();
            map[x][y][2] = "person";
            // System.out.println("x y z is:"+x+"\t"+y+"\t"+z+"\t");
        }
    }

    public void saveMap() {
        System.out.println("saving map");
        BufferedWriter writer = null;
        try {
            // read map data, tab for each property, newline for another
            // location
            writer = new BufferedWriter(new FileWriter("map.data", false));
            writer.write(mapLabel + "\n");
            for (int i = 0; i < MAX_X_AXIS; i++) {
                for (int j = 0; j < MAX_Y_AXIS; j++) {
                    if (map[i][j][0] != null) {
                        writer.write(i + "\t" + j + "\t" + map[i][j][2] + "\t"
                                + map[i][j][0] + "\t" + map[i][j][1] + "\n");
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public void loadMap() {
        System.out.println("loading map");
        // Find the population
        Scanner sc = null;
        try {
            sc = new Scanner(new File("map.data"));
        } catch (FileNotFoundException e) {
        }

        // load first line as label
        mapLabel = sc.nextLine();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] saved = line.split("\\t");

            // assign the location to a person or object
            int x = Integer.parseInt(saved[0]);
            int y = Integer.parseInt(saved[1]);
            // int z = Integer.parseInt(saved[2]);
            map[x][y][2] = saved[2]; // category
            map[x][y][0] = saved[3]; // first name
            map[x][y][1] = saved[4]; // last name
        }
        sc.close();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox commandList;
    private javax.swing.JTextArea explainArea;
    private javax.swing.JMenu file;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem loadData;
    private javax.swing.JMenuItem saveData;
    private javax.swing.JTextField statusBar;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables
}
