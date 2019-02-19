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
public class Recitation {
    
    private final StringProperty section;
    private final StringProperty daysAndTime;
    private final StringProperty room;
    private final StringProperty ta1;
    private final StringProperty ta2;

    public StringProperty sectionProperty() {
        return section;
    }

    public StringProperty daysAndTimeProperty() {
        return daysAndTime;
    }

    public StringProperty roomProperty() {
        return room;
    }

    public StringProperty ta1Property() {
        return ta1;
    }

    public StringProperty ta2Property() {
        return ta2;
    }
    
    public String getSection() {
        return section.get();
    }

    public String getDaysAndTime() {
        return daysAndTime.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getTa1() {
        return ta1.get();
    }

    public String getTa2() {
        return ta2.get();
    }
    
    public void setSection(String initSection){
        section.setValue(initSection);
    }
    
    public void setDaysAndTime(String d){
        daysAndTime.setValue(d);
    }
    
    public void setRoom(String initRoom){
        room.setValue(initRoom);
    }
    
    public void setTa1(String initTA1){
        ta1.setValue(initTA1);
    }
    
    public void setTa2(String initTA2){
        ta2.setValue(initTA2);
    }

    
    public Recitation(String initSec, String initDaysTime, String initRoom, String ta_1, String ta_2){
        
        section = new SimpleStringProperty(initSec);
        daysAndTime = new SimpleStringProperty(initDaysTime);
        room = new SimpleStringProperty(initRoom);
        ta1 = new SimpleStringProperty(ta_1);
        ta2 = new SimpleStringProperty(ta_2);
    }
    
    
    
    
}
