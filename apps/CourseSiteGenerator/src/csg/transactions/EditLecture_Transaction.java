/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.Lecture;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class EditLecture_Transaction implements jTPS_Transaction{
    
    Lecture lectureToEdit;
    String oldSection, newSection;
    String oldDays, newDays;
    String oldTime, newTime;
    String oldRoom, newRoom;
    
    public EditLecture_Transaction(Lecture lec, 
            String sec, String d, String t, String rm) {
        lectureToEdit = lec;
        oldSection = lectureToEdit.getSection();
        oldDays = lectureToEdit.getDays();
        oldTime = lectureToEdit.getTime();
        oldRoom = lectureToEdit.getRoom();
        newSection = sec;
        newDays = d;
        newTime = t;
        newRoom = rm;
    }


    @Override
    public void doTransaction() {
        lectureToEdit.setSection(newSection);
        lectureToEdit.setDays(newDays);
        lectureToEdit.setTime(newTime);
        lectureToEdit.setRoom(newRoom);
    }

    @Override
    public void undoTransaction() {
        lectureToEdit.setSection(oldSection);
        lectureToEdit.setDays(oldDays);
        lectureToEdit.setTime(oldTime);
        lectureToEdit.setRoom(oldRoom);
    }
}
