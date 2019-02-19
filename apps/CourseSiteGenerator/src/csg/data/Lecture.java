/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 *
 * @author sudippaul
 */
public class Lecture {
    private final StringProperty section;
    private final StringProperty days;
    private final StringProperty time;
    private final StringProperty room;
    
    public void setSection(String sec){
        section.setValue(sec);
    }
    
    public void setDays(String d){
        days.setValue(d);
    }
    
    public void setTime(String t){
        time.setValue(t);
    }
    
    public void setRoom(String r){
        room.setValue(r);
    }
    
    public StringProperty sectionProperty(){
        return section;
    }
    
    public StringProperty daysProperty(){
        return days;
    }
    
    public StringProperty timeProperty(){
        return time;
    }
    
    public StringProperty roomProperty(){
        return room;
    }

    public String getSection() {
        return section.get();
    }

    public String getDays() {
        return days.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getRoom() {
        return room.get();
    }
    
    /**
     * Constructor for Lecture data
     * @param initSec
     * @param initDays
     * @param initTime
     * @param initRoom 
     */
    public Lecture(String initSec, String initDays, String initTime, String initRoom){
        section = new SimpleStringProperty(initSec);
        days = new SimpleStringProperty(initDays);
        time = new SimpleStringProperty(initTime);
        room = new SimpleStringProperty(initRoom);
                
    }
    
    
}
