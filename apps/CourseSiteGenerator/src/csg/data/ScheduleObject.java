/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import java.time.LocalDate;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author sudippaul
 */
public class ScheduleObject {

    private StringProperty type;
    private LocalDate date;
    private StringProperty title;
    private StringProperty topic;
    private StringProperty link;

    public ScheduleObject(String initType, LocalDate initDate, String initTitle, String initTopic, String initLink) {
        type = new SimpleStringProperty(initType);
        date = initDate;
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);

    }

    public String getType() {
        return type.get();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }

    public String getLink() {
        return link.get();
    }

    public void setType(String s) {
        type.setValue(s);
    }

    public void setDate(LocalDate s) {
        date = s;
    }

    public void setTitle(String s) {
        title.setValue(s);
    }

    public void setTopic(String s) {
        topic.setValue(s);
    }

    public void setLink(String s) {
        link.setValue(s);
    }
}
