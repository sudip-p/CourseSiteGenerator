package csg.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import static csg.CSGPropertyType.*;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.ScheduleObject;
import csg.workspace.CSGWorkspace;
import static djf.AppPropertyType.APP_EXPORT_PAGE;
import static djf.AppPropertyType.SAVE_BUTTON;
import djf.ui.dialogs.AppWebDialog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import static javafx.scene.AccessibleRole.BUTTON;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
//import static csg.CSGPropertyType.BANNER_SUBJECT_CBOX;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CSGFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CSGApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    
    //FOR SITE TAB
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_PAGES = "pages";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_INSTRUCTOR_NAME = "name";
    static final String JSON_INSTRUCTOR_ROOM = "room";
    static final String JSON_INSTRUCTOR_EMAIL = "email";
    static final String JSON_INSTRUCTOR_HOMEPAGE = "homepage";
    static final String JSON_INSTRUCTOR_HOURS = "hours";

    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_HREF_IMAGEVIEW = "href";
    static final String JSON_SRC_IMAGEVIEW = "src";
    static final String JSON_PAGES_NAME = "name";
    static final String JSON_PAGES_LINK = "link";
    
    static final String HOME = "Home";
    static final String SYLLABUS = "Syllabus";
    static final String SCHEDULE = "Schedule";
    static final String HOMEWORKS = "HWs";
    
    //SYLLABUS TAB
    static final String JSON_SYLLABUS = "syllabus";
    static final String JSON_SYL_DESCRIPTION = "description";
    static final String JSON_SYL_TOPICS = "topics";
    static final String JSON_SYL_PREREQS= "prerequsities";
    static final String JSON_SYL_OUTCOMES = "outcomes";
    static final String JSON_SYL_TEXTBOOKS = "textbooks";
    static final String JSON_SYL_GRADEDCOMP = "gradedComponents";
    static final String JSON_SYL_GRADINGNOTE = "gradingNote";
    static final String JSON_SYL_ACADIS = "academicDishonesty";
    static final String JSON_SYL_SPECAS = "specialAssistance";
    static final String JSON_LECTURE = "lectures";
    static final String JSON_LABS = "labs";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_SECTION = "section";
    
    static final String JSON_DAYS = "days";
    static final String JSON_TIME = "time";
    static final String JSON_ROOM = "room";
    
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";

    static final String JSON_STARTING_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_STARTING_MONDAY_DAY = "startingMondayDay";
    static final String JSON_ENDING_FRIDAY_MONTH = "startingFridayMonth";
    static final String JSON_ENDING_FRIDAY_DAY = "startingFridayDay";
    
    static final String JSON_MONTH = "month";
    static final String JSON_DAY = "day";
    static final String JSON_SCHEDULE_TITLE = "title";
    static final String JSON_TOPIC = "topic";
    static final String JSON_LINK= "link";
    
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_LECTURES = "lectures";
    static final String JSON_REFERENCES = "references";
    static final String JSON_HWS = "hws";
    
        static final String JSON_MEETING_TIMES = "meeting_times";
        static final String JSON_SCHEDULE = "schedule";


    
    static final String JSON_OPTION = "option";
    
    static final String OPTIONS_SAVE_DIRECTORY = "./cboxoptions.json";
    

//    private static JsonObject meetingTimesObject;
//    private static JsonObject scheduleObject;
//    private static JsonObject sectionsData;
//    private static JsonObject pageData;
//    


    public CSGFiles(CSGApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	CSGData dataManager = (CSGData)data;
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
        
        loadSite(filePath);
        loadSyllabus(filePath);
    }
    
    private void loadTAs(CSGData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }
        
    }
    
    private void loadSite(String filePath) throws IOException{
        
        JsonObject json = loadJSONFile(filePath);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        ComboBox subjectBox = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SUBJECT_CBOX));
        ComboBox numberBox = ((ComboBox)app.getGUIModule().getGUINode(BANNER_NUMBER_CBOX));
        ComboBox yearBox = ((ComboBox)app.getGUIModule().getGUINode(BANNER_YEAR_CBOX));
        ComboBox semesterBox = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SEMESTER_CBOX));
        TextField titleTF = ((TextField)app.getGUIModule().getGUINode(BANNER_TITLE_TEXTFIELD));
        
        //GET THE SUBJECT, NUMBER, YEAR, SEMESTER AND TITLE
        String subjectChoice = json.getString(JSON_SUBJECT);
        String numberChoice = json.getString(JSON_NUMBER);
        String yearChoice = json.getString(JSON_YEAR);
        String semesterChoice = json.getString(JSON_SEMESTER);
        String courseTitle = json.getString(JSON_TITLE);
        
        subjectBox.getEditor().setText(subjectChoice);
        numberBox.getEditor().setText(numberChoice);
        yearBox.getEditor().setText(yearChoice);
        semesterBox.getEditor().setText(semesterChoice);
        titleTF.setText(courseTitle);
        
        // NOW LOAD THE IMAGEVIEWS
        ImageView favicon = ((ImageView)app.getGUIModule().getGUINode(FAVICON_IV));
        ImageView navbar = ((ImageView)app.getGUIModule().getGUINode(NAVBAR_IV));
        ImageView bottom_left = ((ImageView)app.getGUIModule().getGUINode(LEFT_IV));
        ImageView bottom_right = ((ImageView)app.getGUIModule().getGUINode(RIGHT_IV));
        
        //EXTRACT THE OBJECTS
        JsonObject logosObject = json.getJsonObject(JSON_LOGOS);
        JsonObject faviconObject = logosObject.getJsonObject(JSON_FAVICON);
        String faviconLink = faviconObject.getString(JSON_HREF_IMAGEVIEW);
        JsonObject navbarObject = logosObject.getJsonObject(JSON_NAVBAR);
        String navbarLink = navbarObject.getString(JSON_SRC_IMAGEVIEW);
        String bottomLeftLink = logosObject.getJsonObject(JSON_BOTTOM_LEFT).getString(JSON_SRC_IMAGEVIEW);
        String bottomRightLink = logosObject.getJsonObject(JSON_BOTTOM_RIGHT).getString(JSON_SRC_IMAGEVIEW);
        
        try{
            favicon.setImage(new Image(faviconLink));
        }catch(Exception ex){
            favicon.setImage(new Image("file:./images/white_shield.png"));
        }
        try{
            navbar.setImage(new Image(navbarLink));
        }catch(Exception ex){
            navbar.setImage(new Image("file:./images/SBUDarkRedShieldLogo.png"));
        }
        try{
            bottom_left.setImage(new Image(bottomLeftLink));
        }catch(Exception ex){
            bottom_left.setImage(new Image("file:./images/SBUWhiteShieldLogo.jpg"));
        }
        try{
            bottom_right.setImage(new Image(bottomRightLink));
        }catch(Exception ex){
            bottom_right.setImage(new Image("file:./images/SBUCSLogo.png"));            
        }
        
        //LOAD THE PAGE PREFERENCES
        CheckBox home = (CheckBox)(app.getGUIModule().getGUINode(PAGES_HOME));
        CheckBox syllabus = (CheckBox)(app.getGUIModule().getGUINode(PAGES_SYLLABUS));
        CheckBox schedule = (CheckBox)(app.getGUIModule().getGUINode(PAGES_SCHEDULE));
        CheckBox hws = (CheckBox)(app.getGUIModule().getGUINode(PAGES_HOMEWORK));
        //BY DEFAULT, SET THESE SELECTED AS FALSE
        home.setSelected(false);
        syllabus.setSelected(false);
        schedule.setSelected(false);
        hws.setSelected(false);
        JsonArray pagesArray = json.getJsonArray(JSON_PAGES);
        //ITERATE THROUGH THE PAGES JSONARRAY, SETTING THINGS TRUE AS NEEDED.
        for(int i = 0; i < pagesArray.size(); i++){
            JsonObject jsonPage = pagesArray.getJsonObject(i);
            String pageName = jsonPage.getString(JSON_PAGES_NAME);
            if(!(pageName.equals(""))){
               if(pageName.equals(HOME)){
                   home.setSelected(true);
               }
               else if(pageName.equals(SYLLABUS)){
                   syllabus.setSelected(true);
               }
               else if(pageName.equals(SCHEDULE)){
                   schedule.setSelected(true);
               }
               else if(pageName.equals(HOMEWORKS)){
                   hws.setSelected(true);
               }
            }
        }
        
        //LOAD INSTRUCTOR PREFERENCES
        TextField instructorName = ((TextField)app.getGUIModule().getGUINode(INS_NAME_TF));
        TextField instructorRoom = ((TextField)app.getGUIModule().getGUINode(INS_ROOM_TF));
        TextField instructorEmail = ((TextField)app.getGUIModule().getGUINode(INS_EMAIL_TF));
        TextField instructorHomePage = ((TextField)app.getGUIModule().getGUINode(INS_HOMEPAGE_TF));
        TextArea instructorOfficeHours = ((TextArea)app.getGUIModule().getGUINode(INSTRUCTOR_OH_TEXTAREA));
        
        JsonObject instructorObject = json.getJsonObject(JSON_INSTRUCTOR);
        instructorName.setText(instructorObject.getString(JSON_INSTRUCTOR_NAME));
        instructorRoom.setText(instructorObject.getString(JSON_INSTRUCTOR_ROOM));
        instructorEmail.setText(instructorObject.getString(JSON_INSTRUCTOR_EMAIL));
        instructorHomePage.setText(instructorObject.getString(JSON_INSTRUCTOR_HOMEPAGE));
        
        // INSTRUCTOR OFFICE HOURS IN JSON FORMAT (PRETTY PRINTED)  
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter ohWriter = writerFactory.createWriter(sw);
        JsonArray officeHoursArray = json.getJsonObject(JSON_INSTRUCTOR).getJsonArray(JSON_INSTRUCTOR_HOURS);
        ohWriter.writeArray(officeHoursArray);
        instructorOfficeHours.setText(sw.toString().substring(1));
        
        //LOAD THE OPTIONS ONTO THE CBOXES
        JsonObject optionsArray = loadJSONFile(OPTIONS_SAVE_DIRECTORY);
        JsonArray subjectArray = optionsArray.getJsonArray(JSON_SUBJECT);
        JsonArray numberArray = optionsArray.getJsonArray(JSON_NUMBER);
        JsonArray yearArray = optionsArray.getJsonArray(JSON_YEAR);
        JsonArray semesterArray = optionsArray.getJsonArray(JSON_SEMESTER);
        
        ArrayList<String> yearList = iterateJSONArray(yearArray);
        ArrayList<String> numberList = iterateJSONArray(numberArray);
        ArrayList<String> subjectList = iterateJSONArray(subjectArray);
        ArrayList<String> semesterList = iterateJSONArray(semesterArray);
        
        subjectBox.getItems().clear();
        subjectBox.getItems().addAll(subjectList);
        yearBox.getItems().clear();
        yearBox.getItems().addAll(yearList);
        numberBox.getItems().clear();
        numberBox.getItems().addAll(numberList);
        semesterBox.getItems().clear();
        semesterBox.getItems().addAll(semesterList);
 }
    
    
    private ArrayList<String> iterateJSONArray(JsonArray array){
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            String str = array.getJsonObject(i).getString(JSON_OPTION);   
            stringList.add(str);
        }
        return stringList;
    }
    
    private void loadSyllabus(String filePath) throws IOException{
        JsonObject json = loadJSONFile(filePath);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        TextArea description = (TextArea)app.getGUIModule().getGUINode(DESCRIPTION_TEXTAREA);   //string
        TextArea topics = (TextArea)app.getGUIModule().getGUINode(TOPICS_TEXTAREA); // array
        TextArea prereqs = (TextArea)app.getGUIModule().getGUINode(PREREQ_TEXTAREA);    // string   
        TextArea outcomes = (TextArea)app.getGUIModule().getGUINode(OUTCOME_TEXTAREA);  // array
        TextArea textbooks = (TextArea)app.getGUIModule().getGUINode(TEXTBOOK_TEXTAREA);    // array
        TextArea gradedComponents = (TextArea)app.getGUIModule().getGUINode(GRADED_COMP_TEXTAREA);  // array
        TextArea gradingNote = (TextArea)app.getGUIModule().getGUINode(GRADING_NOTE_TEXTAREA);  // string
        TextArea acaDis = (TextArea)app.getGUIModule().getGUINode(ACADEMIC_DISHONESTY_TEXTAREA);    // string
        TextArea specAs = (TextArea)app.getGUIModule().getGUINode(SPECIAL_ASSISTANCE_TEXTAREA);     // string
        
        // load the strings first
        String descText = json.getJsonObject(JSON_SYLLABUS).getString(JSON_SYL_DESCRIPTION);
        String prereqsText = json.getJsonObject(JSON_SYLLABUS).getString(JSON_SYL_PREREQS);
        String gradingNoteText = json.getJsonObject(JSON_SYLLABUS).getString(JSON_SYL_GRADINGNOTE);
        String acaDisText = json.getJsonObject(JSON_SYLLABUS).getString(JSON_SYL_ACADIS);
        String specAsText = json.getJsonObject(JSON_SYLLABUS).getString(JSON_SYL_SPECAS);
        
        description.setText(descText);
        prereqs.setText(prereqsText);
        gradingNote.setText(gradingNoteText);
        acaDis.setText(acaDisText);
        specAs.setText(specAsText);
        
        // now load the arrays
        JsonArray topicsArray = json.getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_SYL_TOPICS);
        JsonArray outcomesArray = json.getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_SYL_OUTCOMES);
        JsonArray gradedCompArray = json.getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_SYL_GRADEDCOMP);
        JsonArray textbooksArray = json.getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_SYL_TEXTBOOKS);
        prettyPrinter(json, topics, topicsArray);
        prettyPrinter(json, outcomes, outcomesArray);
        prettyPrinter(json, gradedComponents, gradedCompArray);
        prettyPrinter(json, textbooks, textbooksArray);
        
        
        
        
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    public void prettyPrinter(JsonObject json, TextArea ta, JsonArray jsonArray){
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter writer = writerFactory.createWriter(sw);
        writer.writeArray(jsonArray);
        ta.setText(sw.toString().substring(1));
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CSGData dataManager = (CSGData)data;
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        //BUILD LECTURE OBJECTS TO SAVE
        JsonArrayBuilder lectureArrayBuilder = Json.createArrayBuilder();
        Iterator<Lecture> lectureIterator = dataManager.lectureIterator();
        while(lectureIterator.hasNext()){
            Lecture lecture = lectureIterator.next();
            JsonObject taLecture = Json.createObjectBuilder()
                    .add(JSON_SECTION, lecture.getSection())
                    .add(JSON_DAYS, lecture.getDays())
                    .add(JSON_TIME, lecture.getTime())
                    .add(JSON_ROOM, lecture.getRoom()).build();
                    lectureArrayBuilder.add(taLecture);
        }
        JsonArray lectureArray = lectureArrayBuilder.build();
        
        //BUILD RECITATION OBJECTS TO SAVE
        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        Iterator<Recitation> recitationIterator = dataManager.recitationIterator();
        while(recitationIterator.hasNext()){
            Recitation rec = recitationIterator.next();
            JsonObject taRec = Json.createObjectBuilder()
                    .add(JSON_SECTION, rec.getSection())
                    .add(JSON_DAY_TIME, rec.getDaysAndTime())
                    .add(JSON_LOCATION, rec.getRoom())
                    .add(JSON_TA1, rec.getTa1())
                    .add(JSON_TA2, rec.getTa2()).build();
                    recitationArrayBuilder.add(taRec);
        }
        JsonArray recitationArray = recitationArrayBuilder.build();
        
        //BUILD LABS
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
        Iterator<Lab> labIterator = dataManager.labIterator();
        while(recitationIterator.hasNext()){
            Lab lab = labIterator.next();
            JsonObject taLab = Json.createObjectBuilder()
                    .add(JSON_SECTION, lab.getSection())
                    .add(JSON_DAY_TIME, lab.getDaysAndTime())
                    .add(JSON_LOCATION, lab.getRoom())
                    .add(JSON_TA1, lab.getTa1())
                    .add(JSON_TA2, lab.getTa2()).build();
                    recitationArrayBuilder.add(taLab);
        }
        JsonArray labArray = labsArrayBuilder.build();
        
        //NOW GET SCHEDULE OBJECTS
        JsonArrayBuilder holidayArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecture_scheduleArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        
        
        Iterator<ScheduleObject> scheduleIterator = dataManager.scheduleIterator();
        while(scheduleIterator.hasNext()){
            ScheduleObject sched = scheduleIterator.next();
            
            if(sched.getType().equals("Recitations")){
                JsonObject taSched = Json.createObjectBuilder()
                        .add(JSON_MONTH, Integer.toString(sched.getDate().getMonthValue()))
                        .add(JSON_DAY, Integer.toString(sched.getDate().getDayOfMonth()))
                        .add(JSON_YEAR, Integer.toString(sched.getDate().getYear()))
                        .add(JSON_TITLE, sched.getTitle())
                        .add(JSON_TOPIC, sched.getTopic())
                        .add(JSON_LINK, sched.getLink()).build();
                recitationsArrayBuilder.add(taSched);
            }
            else if (sched.getType().equals("Holidays")){
                JsonObject taSched = Json.createObjectBuilder()
                        .add(JSON_MONTH, Integer.toString(sched.getDate().getMonthValue()))
                        .add(JSON_DAY, Integer.toString(sched.getDate().getDayOfMonth()))
                        .add(JSON_YEAR, Integer.toString(sched.getDate().getYear()))
                        .add(JSON_TITLE, sched.getTitle())
                        .add(JSON_LINK, sched.getLink()).build();
                holidayArrayBuilder.add(taSched);
            }
            else if (sched.getType().equals("Lectures")){
                JsonObject taSched = Json.createObjectBuilder()
                        .add(JSON_MONTH, Integer.toString(sched.getDate().getMonthValue()))
                        .add(JSON_DAY, Integer.toString(sched.getDate().getDayOfMonth()))
                        .add(JSON_YEAR, Integer.toString(sched.getDate().getYear()))
                        .add(JSON_TITLE, sched.getTitle())
                        .add(JSON_TOPIC, sched.getTopic())
                        .add(JSON_LINK, sched.getLink()).build();
                lecture_scheduleArrayBuilder.add(taSched);
            }
            else if (sched.getType().equals("References")){
                JsonObject taSched = Json.createObjectBuilder()
                        .add(JSON_MONTH, Integer.toString(sched.getDate().getMonthValue()))
                        .add(JSON_DAY, Integer.toString(sched.getDate().getDayOfMonth()))
                        .add(JSON_YEAR, Integer.toString(sched.getDate().getYear()))
                        .add(JSON_TITLE, sched.getTitle())
                        .add(JSON_TOPIC, sched.getTopic())
                        .add(JSON_LINK, sched.getLink()).build();
                referencesArrayBuilder.add(taSched);
            }
            else if(sched.getType().equals("HWs")){
                JsonObject taSched = Json.createObjectBuilder()
                        .add(JSON_MONTH, Integer.toString(sched.getDate().getMonthValue()))
                        .add(JSON_DAY, Integer.toString(sched.getDate().getDayOfMonth()))
                        .add(JSON_YEAR, Integer.toString(sched.getDate().getYear()))
                        .add(JSON_TITLE, sched.getTitle())
                        .add(JSON_TOPIC, sched.getTopic())
                        .add(JSON_LINK, sched.getLink()).build();
                hwsArrayBuilder.add(taSched);
            }
        }
        
        // BUILD THE JSON ARRAYS
        JsonArray lectureScheduleArray = lecture_scheduleArrayBuilder.build();
        JsonArray recitationScheduleArray = recitationsArrayBuilder.build();
        JsonArray referencesArray = referencesArrayBuilder.build();
        JsonArray hwsArray = hwsArrayBuilder.build();
        JsonArray holidayArray = holidayArrayBuilder.build();
        
         
        String subjectString = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SUBJECT_CBOX)).getEditor().getText();
        String numberString = ((ComboBox)app.getGUIModule().getGUINode(BANNER_NUMBER_CBOX)).getEditor().getText();
        String yearString = ((ComboBox)app.getGUIModule().getGUINode(BANNER_YEAR_CBOX)).getEditor().getText();
        String semesterString = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SEMESTER_CBOX)).getEditor().getText();
        String titleString = ((TextField)app.getGUIModule().getGUINode(BANNER_TITLE_TEXTFIELD)).getText();
        String instructorName = ((TextField)app.getGUIModule().getGUINode(INS_NAME_TF)).getText();
        String instructorRoom = ((TextField)app.getGUIModule().getGUINode(INS_ROOM_TF)).getText();
        String instructorEmail = ((TextField)app.getGUIModule().getGUINode(INS_EMAIL_TF)).getText();
        String instructorHomePage = ((TextField)app.getGUIModule().getGUINode(INS_HOMEPAGE_TF)).getText();
        String instructorOfficeHours = ((TextArea)app.getGUIModule().getGUINode(INSTRUCTOR_OH_TEXTAREA)).getText();
        CheckBox home = (CheckBox)(app.getGUIModule().getGUINode(PAGES_HOME));
        CheckBox syllabus = (CheckBox)(app.getGUIModule().getGUINode(PAGES_SYLLABUS));
        CheckBox schedule = (CheckBox)(app.getGUIModule().getGUINode(PAGES_SCHEDULE));
        CheckBox hws = (CheckBox)(app.getGUIModule().getGUINode(PAGES_HOMEWORK));
        
        DatePicker dp1 = (DatePicker)(app.getGUIModule().getGUINode(CB_SM_DATEPICKER));
        DatePicker dp2 = (DatePicker)(app.getGUIModule().getGUINode(CB_EF_DATEPICKER));
        LocalDate date1 = dp1.getValue() == null ? LocalDate.now() : dp1.getValue();
        LocalDate date2 = dp2.getValue() == null ? LocalDate.now() : dp2.getValue();
        
        String pagesHome = home.isSelected() ? HOME : "";
        String pagesSyllabus = syllabus.isSelected() ? SYLLABUS : "";
        String pagesSchedule = schedule.isSelected() ? SCHEDULE : "";
        String pagesHws = hws.isSelected() ? HOMEWORKS : "";
        
        String homeLink = "index.html";
        String syllabusLink = "syllabus.html";
        String scheduleLink = "schedule.html";
        String hwsLink = "hws.html";

        //SYLLABUS TAB STUFF
        
        String description = ((TextArea)app.getGUIModule().getGUINode(DESCRIPTION_TEXTAREA)).getText();//String
        String topics = ((TextArea)app.getGUIModule().getGUINode(TOPICS_TEXTAREA)).getText(); //array
        String prereqs = ((TextArea)app.getGUIModule().getGUINode(PREREQ_TEXTAREA)).getText();  //string
        String outcomes = ((TextArea)app.getGUIModule().getGUINode(OUTCOME_TEXTAREA)).getText();    //array
        String textbooks = ((TextArea)app.getGUIModule().getGUINode(TEXTBOOK_TEXTAREA)).getText();  //array
        String gradedComponents = ((TextArea)app.getGUIModule().getGUINode(GRADED_COMP_TEXTAREA)).getText(); //array
        String gradingNote = ((TextArea)app.getGUIModule().getGUINode(GRADING_NOTE_TEXTAREA)).getText();    //string
        String acaDis = ((TextArea)app.getGUIModule().getGUINode(ACADEMIC_DISHONESTY_TEXTAREA)).getText();  //string
        String specAs = ((TextArea)app.getGUIModule().getGUINode(SPECIAL_ASSISTANCE_TEXTAREA)).getText();   //string

          
//            is = new ByteArrayInputStream(topics.getBytes());
//            JsonReader topicsReader = Json.createReader(is);
//            JsonArray topicsArray = topicsReader.readArray();
//            
//            is = new ByteArrayInputStream(outcomes.getBytes());
//            JsonReader outcomesReader  = Json.createReader(is);
//            JsonArray outcomesArray = outcomesReader.readArray();
//
//       
//            is = new ByteArrayInputStream(textbooks.getBytes());
//            JsonReader textReader  = Json.createReader(is);
//            JsonArray textbookArray = textReader.readArray();
//        
//            is = new ByteArrayInputStream(gradedComponents.getBytes());
//            JsonReader gradedCompReader  = Json.createReader(is);
//            JsonArray gradedCompArray = gradedCompReader.readArray();
//            
//        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .add(JSON_SUBJECT, subjectString)
                .add(JSON_NUMBER, numberString)
                .add(JSON_YEAR, yearString)
                .add(JSON_SEMESTER, semesterString)
                .add(JSON_TITLE, titleString)
                .add(JSON_INSTRUCTOR, Json.createObjectBuilder()
                    .add(JSON_INSTRUCTOR_NAME, instructorName)
                    .add(JSON_INSTRUCTOR_EMAIL, instructorEmail)
                    .add(JSON_INSTRUCTOR_ROOM, instructorRoom)
                    .add(JSON_INSTRUCTOR_HOMEPAGE, instructorHomePage)
                    .add(JSON_INSTRUCTOR_HOURS, readArray(instructorOfficeHours))
                )
                .add(JSON_LOGOS, Json.createObjectBuilder()
                        .add(JSON_FAVICON, Json.createObjectBuilder()
                            .add(JSON_HREF_IMAGEVIEW, CSGWorkspace.faviconFilePath))
                        .add(JSON_NAVBAR, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.navbarFilePath))
                        .add(JSON_BOTTOM_LEFT, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.bottomLeftFilePath))
                        .add(JSON_BOTTOM_RIGHT, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.bottomRightFilePath)))
                .add(JSON_PAGES, Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                              .add(JSON_PAGES_NAME, pagesHome)
                              .add(JSON_PAGES_LINK, homeLink))
                        .add(Json.createObjectBuilder()
                              .add(JSON_PAGES_NAME, pagesSyllabus)
                              .add(JSON_PAGES_LINK, syllabusLink))
                        .add(Json.createObjectBuilder()
                                .add(JSON_PAGES_NAME, pagesSchedule)
                                .add(JSON_PAGES_LINK, scheduleLink))
                        .add(Json.createObjectBuilder()
                            .add(JSON_PAGES_NAME, pagesHws)
                            .add(JSON_PAGES_LINK, hwsLink))
                )
                .add(JSON_SYLLABUS, Json.createObjectBuilder()
                    .add(JSON_SYL_DESCRIPTION, description)
                    .add(JSON_SYL_TOPICS, readArray(topics))
                    .add(JSON_SYL_PREREQS, prereqs)
                    .add(JSON_SYL_OUTCOMES, readArray(outcomes))
                    .add(JSON_SYL_TEXTBOOKS, readArray(textbooks))
                    .add(JSON_SYL_GRADEDCOMP, readArray(gradedComponents))
                    .add(JSON_SYL_GRADINGNOTE, gradingNote)
                    .add(JSON_SYL_ACADIS, acaDis)
                    .add(JSON_SYL_SPECAS, specAs)
                )
                .add(JSON_MEETING_TIMES, Json.createObjectBuilder()
                    .add(JSON_LECTURE, lectureArray)
                    .add(JSON_RECITATIONS, recitationArray)
                    .add(JSON_LABS, labArray)
                )
                .add(JSON_STARTING_MONDAY_MONTH, Integer.toString(date1.getMonthValue()))
                .add(JSON_STARTING_MONDAY_DAY, Integer.toString(date1.getDayOfMonth()))
                .add(JSON_ENDING_FRIDAY_MONTH, Integer.toString(date2.getMonthValue()))
                .add(JSON_ENDING_FRIDAY_DAY, Integer.toString(date2.getDayOfMonth()))
                .add(JSON_HOLIDAYS, holidayArray)
                .add(JSON_LECTURES, lectureScheduleArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_RECITATIONS, recitationScheduleArray)
                .add(JSON_HWS, hwsArray)
		.build();
        
        
        
        // NOW CREATE INDIVIDUAL OBJECTS
        JsonObject ohData = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray).build();
        
        
        JsonObject siteData = Json.createObjectBuilder()
                .add(JSON_SUBJECT, subjectString)
                .add(JSON_NUMBER, numberString)
                .add(JSON_YEAR, yearString)
                .add(JSON_SEMESTER, semesterString)
                .add(JSON_TITLE, titleString)
                .add(JSON_INSTRUCTOR, Json.createObjectBuilder()
                    .add(JSON_INSTRUCTOR_NAME, instructorName)
                    .add(JSON_INSTRUCTOR_EMAIL, instructorEmail)
                    .add(JSON_INSTRUCTOR_ROOM, instructorRoom)
                    .add(JSON_INSTRUCTOR_HOMEPAGE, instructorHomePage)
                    .add(JSON_INSTRUCTOR_HOURS, readArray(instructorOfficeHours))
                )
                .add(JSON_LOGOS, Json.createObjectBuilder()
                        .add(JSON_FAVICON, Json.createObjectBuilder()
                            .add(JSON_HREF_IMAGEVIEW, CSGWorkspace.faviconFilePath))
                        .add(JSON_NAVBAR, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.navbarFilePath))
                        .add(JSON_BOTTOM_LEFT, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.bottomLeftFilePath))
                        .add(JSON_BOTTOM_RIGHT, Json.createObjectBuilder()
                             .add(JSON_HREF_IMAGEVIEW, "http://www.cs.stonybrook.edu")
                             .add(JSON_SRC_IMAGEVIEW, CSGWorkspace.bottomRightFilePath)))
                .add(JSON_PAGES, Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                              .add(JSON_PAGES_NAME, pagesHome)
                              .add(JSON_PAGES_LINK, homeLink))
                        .add(Json.createObjectBuilder()
                              .add(JSON_PAGES_NAME, pagesSyllabus)
                              .add(JSON_PAGES_LINK, syllabusLink))
                        .add(Json.createObjectBuilder()
                                .add(JSON_PAGES_NAME, pagesSchedule)
                                .add(JSON_PAGES_LINK, scheduleLink))
                        .add(Json.createObjectBuilder()
                            .add(JSON_PAGES_NAME, pagesHws)
                            .add(JSON_PAGES_LINK, hwsLink))
                ).build();
        
        JsonObject meetingTimesObject = Json.createObjectBuilder()
                .add(JSON_MEETING_TIMES, Json.createObjectBuilder()
                    .add(JSON_LECTURE, lectureArray)
                    .add(JSON_RECITATIONS, recitationArray)
                    .add(JSON_LABS, labArray)
                ).build();
        
        JsonObject scheduleObject = Json.createObjectBuilder()
                .add(JSON_STARTING_MONDAY_MONTH, date1.getMonthValue())
                .add(JSON_STARTING_MONDAY_DAY, date1.getDayOfMonth())
                .add(JSON_ENDING_FRIDAY_MONTH, date2.getMonthValue())
                .add(JSON_ENDING_FRIDAY_DAY, date2.getDayOfMonth())
                .add(JSON_HOLIDAYS, holidayArray)
                .add(JSON_LECTURES, lectureScheduleArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_RECITATIONS, recitationScheduleArray)
                .add(JSON_HWS, hwsArray)
		.build();
        
        JsonObject syllabusObject = Json.createObjectBuilder()
                    .add(JSON_SYL_DESCRIPTION, description)
                    .add(JSON_SYL_TOPICS, readArray(topics))
                    .add(JSON_SYL_PREREQS, prereqs)
                    .add(JSON_SYL_OUTCOMES, readArray(outcomes))
                    .add(JSON_SYL_TEXTBOOKS, readArray(textbooks))
                    .add(JSON_SYL_GRADEDCOMP, readArray(gradedComponents))
                    .add(JSON_SYL_GRADINGNOTE, gradingNote)
                    .add(JSON_SYL_ACADIS, acaDis)
                    .add(JSON_SYL_SPECAS, specAs).build();
                
                
                
      // NOW OUTPUT TO THE JSON DIRECTORY
        outputJsonFile(ohData, "./json/OfficeHoursData.json");
        outputJsonFile(siteData, "./json/PageData.json");
        outputJsonFile(meetingTimesObject, "./json/SectionsData.json");
        outputJsonFile(scheduleObject, "./json/ScheduleData.json");
        outputJsonFile(syllabusObject, "./json/SyllabusData.json");
                
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
        //LET'S SAVE THE OPTIONS FROM COMBO BOXES
        ObservableList subjectOptions = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SUBJECT_CBOX)).getItems();
        ObservableList numberOptions = ((ComboBox)app.getGUIModule().getGUINode(BANNER_NUMBER_CBOX)).getItems();
        ObservableList semesterOptions = ((ComboBox)app.getGUIModule().getGUINode(BANNER_SEMESTER_CBOX)).getItems();
        ObservableList yearOptions = ((ComboBox)app.getGUIModule().getGUINode(BANNER_YEAR_CBOX)).getItems();
        
        JsonArrayBuilder subjectArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder numberArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder semesterArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder yearArrayBuilder = Json.createArrayBuilder();
        
        //ITERATE THROUGH OBSERVABLE LISTS, AND CREATE JSON OBJECTS, ADD TO JSON ARRAYS.
        JsonArray subjectArray = addtoIterator(subjectOptions, subjectArrayBuilder).build();
        JsonArray numberArray = addtoIterator(numberOptions, numberArrayBuilder).build();
        JsonArray semesterArray = addtoIterator(semesterOptions, semesterArrayBuilder).build();
        JsonArray yearArray = addtoIterator(yearOptions, yearArrayBuilder).build();
        
        //PUT ALL THE OPTIONS TOGETHER IN A JSON OBJECT
        JsonObject optionsArray = Json.createObjectBuilder()
                .add(JSON_SUBJECT, subjectArray)
                .add(JSON_NUMBER, numberArray)
                .add(JSON_SEMESTER, semesterArray)
                .add(JSON_YEAR, yearArray)
                .build();
        
//        Map<String, Object> properties = new HashMap<>(1);
//	properties.put(JsonGenerator.PRETTY_PRINTING, true);
//	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
//	StringWriter sw = new StringWriter();
//	JsonWriter jsonWriter = writerFactory.createWriter(sw);
//	jsonWriter.writeObject(dataManagerJSO);
//	jsonWriter.close();

                
        Map<String, Object> optionsProperties = new HashMap<>(1);
        optionsProperties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jwfactory = Json.createWriterFactory(optionsProperties);
        StringWriter strw = new StringWriter();
        JsonWriter jWriter = jwfactory.createWriter(strw);
	jWriter.writeObject(optionsArray);
	jWriter.close();
        
//        OutputStream os = new FileOutputStream(filePath);
//	JsonWriter jsonFileWriter = Json.createWriter(os);
//	jsonFileWriter.writeObject(dataManagerJSO);
//	String prettyPrinted = sw.toString();
//	PrintWriter pw = new PrintWriter(filePath);
//	pw.write(prettyPrinted);
//	pw.close();
        
        OutputStream outputStream = new FileOutputStream(OPTIONS_SAVE_DIRECTORY);
        JsonWriter jFileWriter = Json.createWriter(outputStream);
        jFileWriter.writeObject(optionsArray);
        String prettyPrint = strw.toString();
        PrintWriter printWriter = new PrintWriter(OPTIONS_SAVE_DIRECTORY);
        printWriter.write(prettyPrint);
        printWriter.close();
    }
    
    private JsonArrayBuilder addtoIterator(ObservableList list , JsonArrayBuilder arrayBuilder){
        Iterator it = list.iterator();
        while(it.hasNext()){
            JsonObject object = Json.createObjectBuilder()
                                .add(JSON_OPTION, it.next().toString()).build();
            arrayBuilder.add(object);
        }
        return arrayBuilder;
    }
    
    private void outputJsonFile(JsonObject dataManagerJSO, String filePath) throws IOException{
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //FileUtils utils = new FileUtils();
        // copyFile
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // first make export directory
        String exportDirectory = ((Label)app.getGUIModule().getGUINode(BANNER_EXPORT_LABEL)).getText();
        
        props.removeProperty(APP_EXPORT_PAGE);
        props.addProperty(APP_EXPORT_PAGE, exportDirectory + "/index.html");
        
        File websiteDir = new File(exportDirectory);       // public_html
        
//        File hwsBuilder = new File("./js/HWsBuilder.js");
//        File ohBuilder = new File("./js/OfficeHoursBuilder.js");
//        File pageBuilder = new File("./js/PageBuilder.js");
//        File schedBuilder = new File("./js/ScheduleBuilder.js");
//        File sectionsBuilder = new File("./js/SectionsBuilder.js");
//        File syllabusBuilder = new File("./js/SyllabusBuilder.js");
//        File jquery = new File("./js/jquery.min.js");

        
        
        File hwsHTML = new File("./export/hws.html");
        File indexHTML = new File("./export/index.html");
        File scheduleHTML = new File("./export/schedule.html");
        File syllabusHTML = new File("./export/syllabus.html");
        
        File jsonDirectory = new File(exportDirectory+"/js/");
        File cssDirectory = new File(exportDirectory + "/css/");
        File imagesDirectory = new File(exportDirectory + "/images/");
        
        File javaScriptDir = new File("./js");
        
        websiteDir.mkdirs();
        
        FileUtils.copyDirectoryToDirectory(new File("./js/"), websiteDir);
        FileUtils.copyDirectory(new File("./export/images/"), imagesDirectory);
        FileUtils.copyDirectory(new File("./export/css/"), cssDirectory);
        
        // COPIED THE HTML FILES TO THE DIRECTORY
        FileUtils.copyFileToDirectory(hwsHTML, websiteDir);
        FileUtils.copyFileToDirectory(indexHTML, websiteDir);
        FileUtils.copyFileToDirectory(scheduleHTML, websiteDir);
        FileUtils.copyFileToDirectory(syllabusHTML, websiteDir);
        
        // FINALLY COPY THE JSON
        FileUtils.copyFileToDirectory(new File("./json/OfficeHoursData.json"), jsonDirectory);
        FileUtils.copyFileToDirectory(new File("./json/PageData.json"), jsonDirectory);
        FileUtils.copyFileToDirectory(new File("./json/ScheduleData.json"), jsonDirectory);
        FileUtils.copyFileToDirectory(new File("./json/SectionsData.json"), jsonDirectory);
        FileUtils.copyFileToDirectory(new File("./json/SyllabusData.json"), jsonDirectory);
        
        
        ((Button) app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);
        
        
//        FileUtils.copyFileToDirectory(new File("./json/OfficeHoursData.json"), new File("./export/js/"));
//        FileUtils.copyFileToDirectory(new File("./json/PageData.json"), new File("./export/js/"));
//        FileUtils.copyFileToDirectory(new File("./json/ScheduleData.json"), new File("./export/js/"));
//        FileUtils.copyFileToDirectory(new File("./json/SectionsData.json"), new File("./export/js/"));
//        FileUtils.copyFileToDirectory(new File("./json/SyllabusData.json"), new File("./export/js/"));
        
    }
    
    public JsonArray readArray(String str){
        if(!str.isEmpty()){
        InputStream is = new ByteArrayInputStream(str.getBytes());
        JsonReader textReader  = Json.createReader(is);
        JsonArray array = textReader.readArray();
        return array;
        }
        else {
            JsonArray array = Json.createArrayBuilder().build();
            return array;
        }
    }
}

