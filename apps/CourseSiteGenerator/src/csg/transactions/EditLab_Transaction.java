/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.Lab;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class EditLab_Transaction implements jTPS_Transaction{
    
    Lab labToEdit;
    String oldSection, newSection;
    String oldDaysTime, newDaysTime;
    String oldRoom, newRoom;
    String oldTA1, newTA1;
    String oldTA2, newTA2;
    
    
    public EditLab_Transaction(Lab lab, String sec, String daysTime, String room, String ta1, String ta2){
        labToEdit = lab;
        oldSection = labToEdit.getSection();
        oldDaysTime = labToEdit.getDaysAndTime();
        oldRoom = labToEdit.getRoom();
        oldTA1 = labToEdit.getTa1();
        oldTA2 = labToEdit.getTa2();
        newSection = sec;
        newDaysTime = daysTime;
        newRoom = room;
        newTA1 = ta1;
        newTA2 = ta2;
    }
    
    @Override
    public void doTransaction() {
        labToEdit.setSection(newSection);
        labToEdit.setDaysAndTime(newDaysTime);
        labToEdit.setRoom(newRoom);
        labToEdit.setTa1(newTA1);
        labToEdit.setTa2(newTA2);
    }

    @Override
    public void undoTransaction() {
        labToEdit.setSection(oldSection);
        labToEdit.setDaysAndTime(oldDaysTime);
        labToEdit.setRoom(oldRoom);
        labToEdit.setTa1(oldTA1);
        labToEdit.setTa2(oldTA2);
    }
    
}
