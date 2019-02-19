/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.Recitation;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class EditRecitation_Transaction implements jTPS_Transaction{
    
    Recitation recitation;
    String oldSection, newSection;
    String oldDaysTime, newDaysTime;
    String oldRoom, newRoom;
    String oldTA1, newTA1;
    String oldTA2, newTA2;
    
    public EditRecitation_Transaction(Recitation recitation, String sec, String daysTime, String room, String ta1, String ta2){
        this.recitation = recitation;
        oldSection = this.recitation.getSection();
        oldDaysTime = this.recitation.getDaysAndTime();
        oldRoom = this.recitation.getRoom();
        oldTA1 = this.recitation.getTa1();
        oldTA2 = this.recitation.getTa2();
        newSection = sec;
        newDaysTime = daysTime;
        newRoom = room;
        newTA1 = ta1;
        newTA2 = ta2;
    }

    @Override
    public void doTransaction() {
        recitation.setSection(newSection);
        recitation.setDaysAndTime(newDaysTime);
        recitation.setRoom(newRoom);
        recitation.setTa1(newTA1);
        recitation.setTa2(newTA2);
    }

    @Override
    public void undoTransaction() {
        recitation.setSection(oldSection);
        recitation.setDaysAndTime(oldDaysTime);
        recitation.setRoom(oldRoom);
        recitation.setTa1(oldTA1);
        recitation.setTa2(oldTA2);
    }
    
    
    
}
