/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.CSGPropertyType;
import csg.data.ScheduleObject;
import java.time.LocalDate;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;

/**
 *
 * @author sudippaul
 */
public class EditSchedule_Transaction implements jTPS_Transaction{
    
    ScheduleObject object;
    String oldType, newType;
    String oldTitle, newTitle;
    LocalDate oldDate, newDate;
    String oldTopic, newTopic;
    String oldLink, newLink;
    
    public EditSchedule_Transaction(ScheduleObject o, String type, LocalDate loc, String title, String topic, String link) {
        object  = o;
        oldType = o.getType();
        oldTitle = o.getTitle();
        oldDate = o.getDate();
        oldTopic = o.getTopic();
        oldLink = o.getLink();
        newType = type;
        newTitle = title;
        newTopic = topic;
        newLink = link;
        newDate = loc;
    }
    

    @Override
    public void doTransaction() {
        object.setType(newType);
        object.setTitle(newTitle);
        object.setDate(newDate);
        object.setTopic(newTopic);
        object.setLink(newLink);
    }

    @Override
    public void undoTransaction() {
        object.setType(oldType);
        object.setTitle(oldTitle);
        object.setDate(oldDate);
        object.setTopic(oldTopic);
        object.setLink(oldLink);
    }
    
}
