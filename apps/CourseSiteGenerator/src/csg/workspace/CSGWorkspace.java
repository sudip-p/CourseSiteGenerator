package csg.workspace;

import com.sun.media.jfxmedia.MetadataParser;
import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;
import csg.CSGApp;
import csg.CSGPropertyType;
import static csg.CSGPropertyType.*;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.ScheduleObject;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.transactions.EditLab_Transaction;
import csg.transactions.EditLecture_Transaction;
import csg.transactions.EditRecitation_Transaction;
import csg.workspace.controllers.CSGController;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.CSGFoolproofDesign;
import csg.workspace.style.CSGStyle;
import static csg.workspace.style.CSGStyle.*;
import static djf.AppPropertyType.SAVE_BUTTON;
import djf.AppTemplate;
import djf.ui.dialogs.AppDialogsFacade;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 *
 * @author McKillaGorilla
 */
public class CSGWorkspace extends AppWorkspaceComponent {
    
    public static String faviconFilePath = "file:./images/white_shield.png";
    public static String navbarFilePath = "file:./images/SBUDarkRedShieldLogo.png";
    public static String bottomLeftFilePath = "file:./images/SBUWhiteShieldLogo.jpg";
    public static String bottomRightFilePath = "file:./images/SBUCSLogo.png";
    
    private static String lectureSection;
    private static String lectureDays;
    private static String lectureTime;
    private static String lectureRoom;
    
    public CSGWorkspace(CSGApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CSGApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();

        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        ohBuilder.buildLabel(CSGPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        HBox typeHeaderBox = ohBuilder.buildHBox(OH_GRAD_UNDERGRAD_TAS_PANE, tasHeaderBox, CLASS_OH_RADIO_BOX, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        ohBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, true);
        ohBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        ohBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, typeHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        
        
        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_CENTERED_COLUMN);
        TableColumn typeColumn = ohBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        
        //BUILD COMBO BOXES HERE
        ArrayList<String> start_time = new ArrayList<String>();
        ArrayList<String> end_time = new ArrayList<>();
        
        for(int i = 9; i < 21; i++){
            start_time.add(i+":00");
            end_time.add(i+":00");
        }
        
        props.addPropertyOptionsList("startTime", start_time);
        props.addPropertyOptionsList("endTime", end_time);
        
        props.addProperty("start", "9:00");
        props.addProperty("end","20:00");
        
        
        HBox comboBoxHeader = ohBuilder.buildHBox(COMBO_BOX_HBOX, officeHoursHeaderBox, CLASS_COMBO_BOX, ENABLED);
        Label start = ohBuilder.buildLabel(START_TIME_LABEL, comboBoxHeader, CLASS_START_TIME_COMBO_BOX, ENABLED);
        ComboBox startTime = ohBuilder.buildComboBox(START_TIME_COMBO_BOX, "startTime", "start", comboBoxHeader, CLASS_START_TIME_COMBO_BOX, ENABLED);
        Label end = ohBuilder.buildLabel(END_TIME_LABEL, comboBoxHeader, CLASS_END_TIME_COMBO_BOX, ENABLED);
        ComboBox endTime = ohBuilder.buildComboBox(END_TIME_COMBO_BOX, "endTime", "end", comboBoxHeader, CLASS_END_TIME_COMBO_BOX, ENABLED);
        comboBoxHeader.setAlignment(Pos.CENTER_RIGHT);
        comboBoxHeader.setPadding(new Insets(0,0,0,1000));
        start.setPadding(new Insets(0,10,0,0));
        end.setPadding(new Insets(0,10,0,10));
        
        
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN, "friday");

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setOrientation(Orientation.VERTICAL);
        sPane.setDividerPositions(.4);
        workspace = new BorderPane();
        //workspace = new VBox();
        
        workspace.prefWidthProperty().bind(workspace.widthProperty());
        

        // AND PUT EVERYTHING IN THE WORKSPACE
        //((BorderPane) workspace).setCenter(sPane);
        
        HBox tabHeaderBox = ohBuilder.buildHBox(TAB_HEADER_BOX, null, CLASS_TAB_HBOX, ENABLED);
        //workspace.getChildren().add(tabHeaderBox);
        ((BorderPane) workspace).setTop(tabHeaderBox);
        TabPane tabPane = ohBuilder.buildTabPane(TAB_PANE, tabHeaderBox, CLASS_TABPANE, ENABLED);

        Tab site = ohBuilder.buildTab(TAB_SITE, tabPane, ENABLED);
        Tab syllabus = ohBuilder.buildTab(TAB_SYLLABUS, tabPane, ENABLED);
        Tab meetingTimes = ohBuilder.buildTab(TAB_MEETING_TIMES, tabPane, ENABLED);
        Tab officeHours = ohBuilder.buildTab(TAB_OFFICE_HOURS, tabPane, ENABLED);
        Tab schedule = ohBuilder.buildTab(TAB_SCHEDULE, tabPane, ENABLED);
        
        site.setStyle("-fx-background-radius: 20 20 0 0;");
        syllabus.setStyle("-fx-background-radius: 20 20 0 0;");
        meetingTimes.setStyle("-fx-background-radius: 20 20 0 0;");
        officeHours.setStyle("-fx-background-radius: 20 20 0 0;");
        schedule.setStyle("-fx-background-radius: 20 20 0 0;");
        
        tabPane.getTabs().addAll(site, syllabus, meetingTimes, officeHours, schedule);
        
        tabPane.getSelectionModel().select(site);
        
        ((BorderPane) workspace).setCenter(tabPane);
        
        tabPane.tabMinWidthProperty().bind(((BorderPane) workspace).widthProperty().divide(tabPane.getTabs().size()).subtract(45));
        
        
        
        officeHours.setContent(sPane);
        site.setContent(initSiteTab());
        syllabus.setContent(initSyllabus());
        meetingTimes.setContent(initMeetingTimes());
        schedule.setContent(initSchedule());
    }
    
    private ScrollPane initSiteTab(){
       // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        ScrollPane sp = new ScrollPane();
        
        VBox main = new VBox();
        
       //NOW BUILD THE COMBO BOX ARRAYLIST
        ArrayList<String> cseCourses = new ArrayList<>();
        cseCourses.add("214");
        cseCourses.add("219");
        cseCourses.add("220");
//        cseCourses.add("301");
//        cseCourses.add("310");
//        cseCourses.add("345");
        
        ArrayList<String> amsCourses = new ArrayList<>();
        amsCourses.add("110");
        amsCourses.add("310");
        amsCourses.add("301");
        
        ArrayList<String> courses = new ArrayList<>();
        courses.add("CSE");
        
        ArrayList<String> sem = new ArrayList<>();
        sem.add("Fall");
        sem.add("Spring");
        
        ArrayList<String> years = new ArrayList<>();
        years.add(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        years.add(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)+1));
       
        props.addProperty("CSE","CSE");
        props.addProperty("219","219");
        props.addProperty("Fall","Fall");
        props.addProperty("2018","2018");
        props.addPropertyOptionsList("CSE", cseCourses);
        props.addPropertyOptionsList("AMS",amsCourses);
        props.addPropertyOptionsList("courses",courses);
        props.addPropertyOptionsList("semesters", sem);
        props.addPropertyOptionsList("years", years);
        
        //BANNER
        VBox banner = ohBuilder.buildVBox(SITE_BANNER_BOX, workspace, CSG_BANNER_HBOX, ENABLED);

        GridPane bannerGridPane = ohBuilder.buildGridPane(BANNER_GRIDPANE, banner, NOTHING, ENABLED);
        Label banner_label = ohBuilder.buildLabel(SITE_BANNER, bannerGridPane, 0, 0, 1, 1, CSG_BANNER, ENABLED);
        Label subject = ohBuilder.buildLabel(BANNER_SUBJECT, bannerGridPane, 0, 1, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        ComboBox subjectCbox = ohBuilder.buildComboBox(BANNER_SUBJECT_CBOX, bannerGridPane, 1, 1, 1, 1, CSG_SUBJECT_CBOX, ENABLED, "courses", "CSE");
        subjectCbox.setEditable(true);
        Label number = ohBuilder.buildLabel(BANNER_NUMBER, bannerGridPane, 2, 1, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        ComboBox numberCbox = ohBuilder.buildComboBox(BANNER_NUMBER_CBOX, bannerGridPane, 3, 1, 1, 1, CSG_SUBJECT_CBOX, ENABLED, "CSE", "219");
        numberCbox.setEditable(true);
        Label semester = ohBuilder.buildLabel(BANNER_SEMESTER, bannerGridPane, 0, 2, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        ComboBox semCbox=ohBuilder.buildComboBox(BANNER_SEMESTER_CBOX, bannerGridPane, 1, 2, 1, 1, CSG_SUBJECT_CBOX, ENABLED, "semesters", "Fall");
        semCbox.setEditable(true);
        Label year = ohBuilder.buildLabel(BANNER_YEAR, bannerGridPane, 2, 2, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        ComboBox yearCbox = ohBuilder.buildComboBox(BANNER_YEAR_CBOX, bannerGridPane, 3, 2, 1, 1, CSG_SUBJECT_CBOX, ENABLED, "years", "2018");
        yearCbox.setEditable(true);
        Label title = ohBuilder.buildLabel(BANNER_TITLE, bannerGridPane, 0, 3, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        TextField titleTF = ohBuilder.buildTextField(BANNER_TITLE_TEXTFIELD, bannerGridPane, 1, 3, 1, 1, CSG_SUBJECT_CBOX, ENABLED);
        Label export = ohBuilder.buildLabel(BANNER_EXPORT, bannerGridPane, 0, 4, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        Label dir = ohBuilder.buildLabel(BANNER_EXPORT_LABEL, bannerGridPane, 1, 4, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        
        subjectCbox.getEditor().textProperty().addListener(e -> {
            dir.setText("./export/"+subjectCbox.getEditor().getText()+"_"+numberCbox.getEditor().getText()+"_"+semCbox.getEditor().getText()+"_"+yearCbox.getEditor().getText()+"/public_html/");
            ((Button)app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);
        });
        subjectCbox.focusedProperty().addListener(e -> {
            if(!(subjectCbox.getEditor().getText().isEmpty()) && !(subjectCbox.getItems().contains(subjectCbox.getEditor().getText()))){
                subjectCbox.getItems().add(subjectCbox.getEditor().getText());
            }
        });
        numberCbox.getEditor().textProperty().addListener(e -> {
            dir.setText("./export/"+subjectCbox.getEditor().getText()+"_"+numberCbox.getEditor().getText()+"_"+semCbox.getEditor().getText()+"_"+yearCbox.getEditor().getText()+"/public_html/");
            ((Button)app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);
        });
        numberCbox.focusedProperty().addListener(e -> {
                if(!(numberCbox.getItems().contains(numberCbox.getEditor().getText())) && !(numberCbox.getEditor().getText().isEmpty())){
                    numberCbox.getItems().add(numberCbox.getEditor().getText());
                }
        });
        semCbox.getEditor().textProperty().addListener(e -> {
            dir.setText("./export/"+subjectCbox.getEditor().getText()+"_"+numberCbox.getEditor().getText()+"_"+semCbox.getEditor().getText()+"_"+yearCbox.getEditor().getText()+"/public_html/");
            ((Button)app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);

        });
        semCbox.focusedProperty().addListener(e -> {
            if(!(semCbox.getItems().contains(semCbox.getEditor().getText())) && !(semCbox.getEditor().getText().isEmpty())){
                semCbox.getItems().add(semCbox.getEditor().getText());
            }
        });
        yearCbox.getEditor().textProperty().addListener(e -> {
           dir.setText("./export/"+subjectCbox.getEditor().getText()+"_"+numberCbox.getEditor().getText()+"_"+semCbox.getEditor().getText()+"_"+yearCbox.getEditor().getText()+"/public_html/");
           ((Button)app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);
        });
        yearCbox.focusedProperty().addListener(e -> {
            if(!(yearCbox.getItems().contains(yearCbox.getEditor().getText())) && !(yearCbox.getEditor().getText().isEmpty())){
                yearCbox.getItems().add(yearCbox.getEditor().getText());
            }
        });
        
        bannerGridPane.setHgap(20);
        bannerGridPane.setVgap(20);
        
        banner.setPadding(new Insets(0,0,20,20));
        
        HBox pages = ohBuilder.buildHBox(PAGES_HBOX, workspace, SITE_PAGES_HBOX, ENABLED);
        Label pagesLabel = ohBuilder.buildLabel(SITE_PAGES, pages, SITE_PAGES_LABEL, ENABLED);
        CheckBox home = ohBuilder.buildCheckBox(PAGES_HOME, pages, SITE_PAGES_HOME, ENABLED);
        CheckBox syllabus = ohBuilder.buildCheckBox(PAGES_SYLLABUS,pages,SITE_PAGES_SYLLABUS,ENABLED);
        CheckBox schedule = ohBuilder.buildCheckBox(PAGES_SCHEDULE, pages, SITE_PAGES_SCHEDULE, ENABLED);
        CheckBox hws = ohBuilder.buildCheckBox(PAGES_HOMEWORK, pages, SITE_PAGES_HWS, ENABLED);
        
        pages.setPadding(new Insets(30,0,30,20));
        pagesLabel.setPadding(new Insets(0,25,0,10));
        home.setPadding(new Insets(0,25,0,0));
        syllabus.setPadding(new Insets(0,25,0,0));
        schedule.setPadding(new Insets(0,25,0,0));
        hws.setPadding(new Insets(0,25,0,0));
        
        //NOW BUILD STYLE
         ArrayList<String> cssFiles = new ArrayList<>();
         File cssDirectory = new File("./css/");
         File[] files = cssDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".css");
            }
        });
         
         System.out.println(System.getProperty("user.dir"));
         
         for(File cssFile : files){
             cssFiles.add(cssFile.getName());
         }
         props.addProperty("sea_wolf.css","sea_wolf.css");
         props.addPropertyOptionsList("css", cssFiles);
        
         GridPane styleGridPane = ohBuilder.buildGridPane(STYLES_GRIDPANE, null, SITE_STYLES_GRIDPANE, ENABLED);
        
        Label style = ohBuilder.buildLabel(SITE_STYLE, styleGridPane, 0, 0, 1, 1, STYLES_LABEL, ENABLED);
        Button favicon = ohBuilder.buildTextButton(STYLES_FAVICON, styleGridPane, 0, 1, 1, 1, STYLES_FAVICON_LABEL, ENABLED);
        Button navbar = ohBuilder.buildTextButton(STYLES_NAVBAR, styleGridPane, 0, 2, 1, 1, STYLES_NAVBAR_IMAGE, ENABLED);
        Button leftFooter = ohBuilder.buildTextButton(STYLES_LEFT, styleGridPane, 0, 3, 1, 1, STYLES_LEFT_FOOTER, ENABLED);
        Button rightFooter = ohBuilder.buildTextButton(STYLES_RIGHT, styleGridPane, 0, 4, 1, 1, STYLES_RIGHT_FOOTER, ENABLED);
        Label fontsAndColors = ohBuilder.buildLabel(STYLES_CSS_LABEL, styleGridPane, 0, 5, 1, 1, STYLES_FONTSCOLORS_LABEL, ENABLED);
        ComboBox cssComboBox = ohBuilder.buildComboBox(STYLES_COMBO, styleGridPane, 1, 5, 1, 1, STYLES_CBOX, ENABLED, "css", "sea_wolf.css");
        Label warning = ohBuilder.buildLabel(STYLES_WARNING, styleGridPane, 0, 6, 1, 1, STYLES_WARNING_LABEL, ENABLED);
        
        ImageView faviconIV = ohBuilder.buildImageView(FAVICON_IV, "file:./images/white_shield.png", styleGridPane, 1, 1, 5, 1, IV_STYLE, ENABLED);
        ImageView navbarIV = ohBuilder.buildImageView(NAVBAR_IV, "file:./images/SBUDarkRedShieldLogo.png", styleGridPane, 1, 2, 5, 1, IV_STYLE, ENABLED);
        ImageView leftIV = ohBuilder.buildImageView(LEFT_IV, "file:./images/SBUWhiteShieldLogo.jpg", styleGridPane, 1, 3, 5, 1, IV_STYLE, ENABLED);
        ImageView rightIV = ohBuilder.buildImageView(RIGHT_IV, "file:./images/SBUCSLogo.png", styleGridPane, 1, 4, 5, 1, IV_STYLE, ENABLED);
        
        
        styleGridPane.setPadding(new Insets(10,0,0,20));
        
        styleGridPane.setVgap(10);
        styleGridPane.setHgap(10);
        
        GridPane instructorPane = ohBuilder.buildGridPane(INSTRUCTOR_GRIDPANE, workspace, SITE_STYLES_GRIDPANE, ENABLED);
        Label instructor = ohBuilder.buildLabel(INSTRUCTOR_LABEL, instructorPane, 0, 0, 1, 1, INS_LABEL, ENABLED);
        Label name = ohBuilder.buildLabel(INSTRUCTOR_NAME, instructorPane, 0, 1, 1, 1, INS_NAME, ENABLED);
        TextField nameTF = ohBuilder.buildTextField(INS_NAME_TF, instructorPane, 1, 1, 1, 1, INSTRUCTOR_NAME_TF, ENABLED);
        Label room = ohBuilder.buildLabel(INSTRUCTOR_ROOM, instructorPane, 2, 1, 1, 1, INS_ROOM, ENABLED);
        TextField roomTF = ohBuilder.buildTextField(INS_ROOM_TF, instructorPane, 3, 1, 1, 1, INSTRUCTOR_ROOM_TF, ENABLED);
        Label email = ohBuilder.buildLabel(INSTRUCTOR_EMAIL, instructorPane, 0, 2, 1, 1, INS_EMAIL, ENABLED);
        TextField emailTF = ohBuilder.buildTextField(INS_EMAIL_TF, instructorPane, 1, 2, 1, 1, INSTRUCTOR_EMAIL_TF, ENABLED);
        Label homePage = ohBuilder.buildLabel(INSTRUCTOR_HOME_PAGE, instructorPane, 2, 2, 1, 1, INS_HOMEPAGE, ENABLED);
        TextField homepageTF = ohBuilder.buildTextField(INS_HOMEPAGE_TF, instructorPane, 3, 2, 1, 1, INSTRUCTOR_HOMEPAGE_TF, ENABLED);
        Label oh = ohBuilder.buildLabel(INSTRUCTOR_OFFICEHOURS, instructorPane, 0, 3, 1, 1, INS_OH_LABEL, ENABLED);
        Button expand = ohBuilder.buildTextButton(INS_OH_BT, instructorPane, 1,3,1,1, INS_OH_BUTTON, ENABLED);
        TextArea ohJSONTextArea = ohBuilder.buildTextArea(INSTRUCTOR_OH_TEXTAREA, instructorPane, 0, 5, 10, 10, INS_TEXTAREA, !ENABLED);
        ohJSONTextArea.setPrefColumnCount(2);
        ohJSONTextArea.setWrapText(false);
        boolean disabled = true;
        instructorPane.setPadding(new Insets(10,0,0,20));
        
        instructorPane.setVgap(20);
        instructorPane.setHgap(20);
        
        expand.setOnAction(e -> {
            if(ohJSONTextArea.isDisable()){
                ohJSONTextArea.setDisable(false);
                expand.setText("-");
            }
            else{
                ohJSONTextArea.setDisable(true);
                 expand.setText("+");
            }
        });
       
        main.getChildren().addAll(banner,pages,styleGridPane, instructorPane);
        
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setContent(main);
        
        return sp;
    }
    
    public ScrollPane initSyllabus(){
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        ScrollPane sp = new ScrollPane();
        
        VBox main = new VBox();
        main.setStyle("-fx-background-color:   SWATCH_NEUTRAL;");
        
        TitledPane description = ohBuilder.buildTitledPane(DESCRIPTION_LABEL, main, SYL_DESCRIPTION, ENABLED);
        TitledPane topics = ohBuilder.buildTitledPane(TOPICS_LABEL, main, SYL_TOPICS, ENABLED);
        TitledPane prereq = ohBuilder.buildTitledPane(PREREQ_LABEL, main, SYL_PREREQ, ENABLED);
        TitledPane outcomes = ohBuilder.buildTitledPane(OUTCOME_LABEL, main, SYL_OUTCOMES, ENABLED);
        TitledPane textbooks = ohBuilder.buildTitledPane(TEXTBOOK_LABEL, main, SYL_TEXTBOOKS, ENABLED);
        TitledPane gradedComp = ohBuilder.buildTitledPane(GRADED_COMP_LABEL, main, SYL_GRADEDCOMP, ENABLED);
        TitledPane gradingNote = ohBuilder.buildTitledPane(GRADING_NOTE_LABEL, main, SYL_GRADINGNOTES, ENABLED);
        TitledPane ad = ohBuilder.buildTitledPane(ACADEMIC_DISHONESTY_LABEL, main, SYL_AD, ENABLED);
        TitledPane sa = ohBuilder.buildTitledPane(SPECIAL_ASSISTANCE_LABEL, main, SYL_SA, ENABLED);
        
        TextArea descTA = ohBuilder.buildTextArea(DESCRIPTION_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        
        TextArea topicsTA = ohBuilder.buildTextArea(TOPICS_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea prereqTA = ohBuilder.buildTextArea(PREREQ_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea outcomesTA = ohBuilder.buildTextArea(OUTCOME_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea textbooksTA = ohBuilder.buildTextArea(TEXTBOOK_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea gradedCompTA = ohBuilder.buildTextArea(GRADED_COMP_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea gradingNoteTA = ohBuilder.buildTextArea(GRADING_NOTE_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea adTA = ohBuilder.buildTextArea(ACADEMIC_DISHONESTY_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        TextArea saTA = ohBuilder.buildTextArea(SPECIAL_ASSISTANCE_TEXTAREA, null, SYL_DESCRIPTION_TA, ENABLED);
        
        descTA.setPadding(new Insets(5,5,5,5));
        
        description.setContent(descTA);
        topics.setContent(topicsTA);
        prereq.setContent(prereqTA);
        outcomes.setContent(outcomesTA);
        textbooks.setContent(textbooksTA);
        gradedComp.setContent(gradedCompTA);
        gradingNote.setContent(gradingNoteTA);
        ad.setContent(adTA);
        sa.setContent(saTA);
        
        ((TitledPane) main.getChildren().get(0)).setExpanded(true);
        
        for (int i = 1; i < main.getChildren().size(); i++) {
         ((TitledPane) main.getChildren().get(i)).setExpanded(false);
        }
        
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setContent(main);
        
        sp.vvalueProperty().bind(main.heightProperty());
        
        return sp;
    }
    
    private ScrollPane initMeetingTimes(){
         PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        ScrollPane sp = new ScrollPane();
        
        VBox main = new VBox();
        main.setStyle("-fx-background-color:   SWATCH_NEUTRAL;");
        
        TitledPane lecture = ohBuilder.buildTitledPane(LECTURES_LABEL, main, MEETING_TIMES_TITLEDPANES, ENABLED);
        TitledPane recitation = ohBuilder.buildTitledPane(RECITATION_LABEL, main, MEETING_TIMES_TITLEDPANES, ENABLED);
        TitledPane labs = ohBuilder.buildTitledPane(LABS_LABEL, main, MEETING_TIMES_TITLEDPANES, ENABLED);
        
        VBox lecTableBox = ohBuilder.buildVBox(LECTURE_VBOX, null, MEETING_TIMES_BOX, ENABLED);
        VBox recTableBox = ohBuilder.buildVBox(REC_VBOX, null, MEETING_TIMES_BOX, ENABLED);
        VBox labTableBox = ohBuilder.buildVBox(LAB_VBOX, null, MEETING_TIMES_BOX, ENABLED);
        
        // FOR THE BUTTONS CREATE AN HBOX
        GridPane lectureGridPane = ohBuilder.buildGridPane(LECTURE_GRIDPANE, null, MEETING_TIMES_BOX, ENABLED);
        GridPane recGridPane = ohBuilder.buildGridPane(REC_GRIDPANE, null, MEETING_TIMES_BOX, ENABLED);
        GridPane labGridPane = ohBuilder.buildGridPane(LECTURE_GRIDPANE, null, MEETING_TIMES_BOX, ENABLED);

//        Button addLectureBt = ohBuilder.buildIconButton(ADD_LECTURE_BUTTON, lectureButtonBox, MEETING_TIMES_BUTTONS, ENABLED);
//        Button addRecBt = ohBuilder.buildIconButton(ADD_REC_BUTTON, recButtonBox, MEETING_TIMES_BUTTONS, ENABLED);
//        Button addLabBt = ohBuilder.buildIconButton(ADD_LAB_BUTTON, labButtonBox, MEETING_TIMES_BUTTONS, ENABLED);
//        
        Button addLectureBt = ohBuilder.buildTextButton(ADD_LECTURE_BUTTON, lectureGridPane, 0, 0, 1, 1, MEETING_TIMES_BUTTONS, ENABLED);
        Button addRecBt = ohBuilder.buildTextButton(ADD_REC_BUTTON, recGridPane, 0, 0, 1, 1, MEETING_TIMES_BUTTONS, ENABLED);
        Button addLabBt = ohBuilder.buildTextButton(ADD_LAB_BUTTON, labGridPane, 0, 0, 1, 1, MEETING_TIMES_BUTTONS, ENABLED);
        
        Button removeLectureButton = ohBuilder.buildTextButton(REMOVE_LECTURE_BUTTON, lectureGridPane, 1, 0, 1, 1, MEETING_TIMES_BUTTONS, !ENABLED);
        Button removeRecButton = ohBuilder.buildTextButton(REMOVE_REC_BUTTON, recGridPane, 1, 0, 1, 1, MEETING_TIMES_BUTTONS, !ENABLED);
        Button removeLabButton = ohBuilder.buildTextButton(REMOVE_LAB_BUTTON, labGridPane, 1, 0, 1, 1, MEETING_TIMES_BUTTONS, !ENABLED);
        
//        addLectureBt.setPadding(new Insets(10,10,10,10));
//        removeLectureButton.setPadding(new Insets(10,10,10,10));

       
        //LECTURES_TABLE
//        TableView<String> lecTable = ohBuilder.buildTableView(LECTURES_TABLE, lecTableBox, MEETING_TIMES_TABLES, ENABLED);
        TableView<Lecture> lecTable = ohBuilder.buildTableView(LECTURES_TABLE, lectureGridPane, 0, 1, 145, 30, MEETING_TIMES_TABLES, ENABLED);
        TableColumn lecSecCol = ohBuilder.buildTableColumn(SECTION_LEC, lecTable, MEETING_TIMES_COLUMN);
        TableColumn lecDaysCol = ohBuilder.buildTableColumn(DAYS_LEC, lecTable, MEETING_TIMES_COLUMN);
        TableColumn lecTimeCol = ohBuilder.buildTableColumn(TIME_LEC, lecTable, MEETING_TIMES_COLUMN);
        TableColumn lecRoomCol = ohBuilder.buildTableColumn(ROOM_LEC, lecTable, MEETING_TIMES_COLUMN);
        lecSecCol.setCellValueFactory(new PropertyValueFactory<Lecture, String>("section"));
        lecDaysCol.setCellValueFactory(new PropertyValueFactory<Lecture, String>("days"));
        lecTimeCol.setCellValueFactory(new PropertyValueFactory<Lecture, String>("time"));
        lecRoomCol.setCellValueFactory(new PropertyValueFactory<Lecture, String>("room"));
        lecSecCol.prefWidthProperty().bind(lecTable.widthProperty().multiply(1.0 / 4.0));
        lecDaysCol.prefWidthProperty().bind(lecTable.widthProperty().multiply(1.0 / 4.0));
        lecTimeCol.prefWidthProperty().bind(lecTable.widthProperty().multiply(1.0 / 4.0));
        lecRoomCol.prefWidthProperty().bind(lecTable.widthProperty().multiply(1.0 / 4.0));
        
        //RECITATION TABLE
//        TableView<String> recTable = ohBuilder.buildTableView(RECITATION_TABLE, recTableBox, MEETING_TIMES_TABLES, ENABLED);
        TableView<Recitation> recTable = ohBuilder.buildTableView(RECITATION_TABLE, recGridPane, 0, 1, 145, 30, MEETING_TIMES_TABLES, ENABLED);
        TableColumn recSecCol = ohBuilder.buildTableColumn(SECTION_REC, recTable, MEETING_TIMES_COLUMN);
        TableColumn recDaysTimeCol = ohBuilder.buildTableColumn(DAYS_TIME_REC, recTable, MEETING_TIMES_COLUMN);
        TableColumn recRoomCol = ohBuilder.buildTableColumn(ROOM_REC, recTable, MEETING_TIMES_COLUMN);
        TableColumn recTA1 = ohBuilder.buildTableColumn(TA1_REC, recTable, MEETING_TIMES_COLUMN);
        TableColumn recTA2 = ohBuilder.buildTableColumn(TA2_REC, recTable, MEETING_TIMES_COLUMN);
        recSecCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("section"));
        recDaysTimeCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("daysAndTime"));
        recRoomCol.setCellValueFactory(new PropertyValueFactory<Recitation, String>("room"));
        recTA1.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta1"));
        recTA2.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta2"));

        recSecCol.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        recDaysTimeCol.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        recRoomCol.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        recTA1.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));
        recTA2.prefWidthProperty().bind(recTable.widthProperty().multiply(1.0 / 5.0));

        TableView<Lab> labTable = ohBuilder.buildTableView(LABS_TABLE, labGridPane, 0, 1, 145, 30, MEETING_TIMES_TABLES, ENABLED);
        TableColumn labSecCol = ohBuilder.buildTableColumn(SECTION_LABS, labTable, MEETING_TIMES_COLUMN);
        TableColumn labDaysTimeCol = ohBuilder.buildTableColumn(DAYS_TIME_LABS, labTable, MEETING_TIMES_COLUMN);
        TableColumn labRoomCol = ohBuilder.buildTableColumn(ROOM_LABS, labTable, MEETING_TIMES_COLUMN);
        TableColumn labTA1 = ohBuilder.buildTableColumn(TA1_LABS, labTable, MEETING_TIMES_COLUMN);
        TableColumn labTA2 = ohBuilder.buildTableColumn(TA2_LABS, labTable, MEETING_TIMES_COLUMN);
        labSecCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("section"));
        labDaysTimeCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("daysAndTime"));
        labRoomCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("room"));
        labTA1.setCellValueFactory(new PropertyValueFactory<Lab, String>("ta1"));
        labTA2.setCellValueFactory(new PropertyValueFactory<Lab, String>("ta2"));
        labSecCol.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        labDaysTimeCol.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        labRoomCol.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        labTA1.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        labTA2.prefWidthProperty().bind(labTable.widthProperty().multiply(1.0 / 5.0));
        
        lectureGridPane.setHgap(10);
        recGridPane.setHgap(10);
        labGridPane.setHgap(10);
        
        lectureGridPane.setVgap(10);
        recGridPane.setVgap(10);
        labGridPane.setVgap(10);
        
        // FIRST, DEAL WITH THE LECTURE TABLE
        lecTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        labTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        
        lecSecCol.setEditable(ENABLED);
        lecDaysCol.setEditable(ENABLED);
        lecTimeCol.setEditable(ENABLED);
        lecRoomCol.setEditable(ENABLED);
        
        lecTable.setEditable(ENABLED);
        lecSecCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lecDaysCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lecTimeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lecRoomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //NOW DEAL WITH RECITATION
       
        recSecCol.setEditable(ENABLED);
        recDaysTimeCol.setEditable(ENABLED);
        recRoomCol.setEditable(ENABLED);
        recTA1.setEditable(ENABLED);
        recTA2.setEditable(ENABLED);
        
        recTable.setEditable(ENABLED);
        recSecCol.setCellFactory(TextFieldTableCell.forTableColumn());
        recDaysTimeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        recRoomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        recTA1.setCellFactory(TextFieldTableCell.forTableColumn());
        recTA2.setCellFactory(TextFieldTableCell.forTableColumn());

        // NOW FINALLY LABS
        
        labSecCol.setEditable(ENABLED);
        labDaysTimeCol.setEditable(ENABLED);
        labRoomCol.setEditable(ENABLED);
        labTA1.setEditable(ENABLED);
        labTA2.setEditable(ENABLED);
        
        labTable.setEditable(ENABLED);
        labSecCol.setCellFactory(TextFieldTableCell.forTableColumn());
        labDaysTimeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        labRoomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        labTA1.setCellFactory(TextFieldTableCell.forTableColumn());
        labTA2.setCellFactory(TextFieldTableCell.forTableColumn());
        
        lecture.setContent(lectureGridPane);
        recitation.setContent(recGridPane);
        labs.setContent(labGridPane);
        
        lecture.setPadding(new Insets(10,10,10,10));
        recitation.setPadding(new Insets(10,10,10,10));
        labs.setPadding(new Insets(10,10,10,10));
        
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setContent(main);
        
        return sp;
    }
    
    private ScrollPane initSchedule(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        
        ScrollPane sp = new ScrollPane();
        
        VBox main = new VBox();
        
        /**
         * Need a gridPane (calendar boundaries), titlePane and gridPane
         */
        
        GridPane calBounds = ohBuilder.buildGridPane(CALENDAR_GRIDPANE, main, CSG_BANNER_HBOX, ENABLED);
        TitledPane schedItems = ohBuilder.buildTitledPane(SCHEDULE_ITEMS, main, SYL_DESCRIPTION, ENABLED);
        GridPane addEditPane = ohBuilder.buildGridPane(ADD_EDIT_LABEL, main, CSG_BANNER_HBOX, ENABLED);
        
        Label calendarBoundaries = ohBuilder.buildLabel(CALENDAR_BOUNDARIES_LABEL, calBounds, 0, 0, 1, 1, CSG_BANNER, ENABLED);
        Label startingMonday = ohBuilder.buildLabel(STARTING_MONDAY, calBounds, 0, 1, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        Label endingFriday = ohBuilder.buildLabel(ENDING_FRIDAY, calBounds, 2,1,1,1, SITE_SUBJECT_LABEL, ENABLED);
        DatePicker mondayDp = ohBuilder.buildDatePicker(CB_SM_DATEPICKER, calBounds, 1, 1, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        DatePicker fridayDp = ohBuilder.buildDatePicker(CB_EF_DATEPICKER, calBounds, 3, 1, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        calBounds.setPadding(new Insets(10,10,10,10));
        
        calBounds.setVgap(50);
        calBounds.setHgap(50);
        
        GridPane scheduleTableGridPane = ohBuilder.buildGridPane(SCHEDULE_TABLE_GRIDPANE, null, MEETING_TIMES_BOX, ENABLED);
        Button removeItemBt = ohBuilder.buildTextButton(REMOVE_SCHEDULE_BUTTON, scheduleTableGridPane, 0, 0, 1, 1, MEETING_TIMES_BUTTONS, !ENABLED);
//        Label noteLabel = ohBuilder.buildLabel(SCHEDULE_NOTE, scheduleTableGridPane, 1, 0, 5, 1, STYLES_LABEL, ENABLED);
        TableView<ScheduleObject> schedTable = ohBuilder.buildTableView(SCHEDULE_ITEMS_TABLE, scheduleTableGridPane, 0, 1, 155, 40, MEETING_TIMES_TABLES, ENABLED);
        TableColumn typeCol = ohBuilder.buildTableColumn(TYPE, schedTable, MEETING_TIMES_COLUMN);
        TableColumn dateCol = ohBuilder.buildTableColumn(DATE, schedTable, MEETING_TIMES_COLUMN);
        TableColumn titleCol = ohBuilder.buildTableColumn(TITLE, schedTable, MEETING_TIMES_COLUMN);
        TableColumn topicCol = ohBuilder.buildTableColumn(TOPIC, schedTable, MEETING_TIMES_COLUMN);
        typeCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("date"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("title"));
        topicCol.setCellValueFactory(new PropertyValueFactory<Lab, String>("topic"));
        typeCol.prefWidthProperty().bind(schedTable.widthProperty().multiply(1.0 / 4.0));
        dateCol.prefWidthProperty().bind(schedTable.widthProperty().multiply(1.0 / 4.0));
        titleCol.prefWidthProperty().bind(schedTable.widthProperty().multiply(1.0 / 4.0));
        topicCol.prefWidthProperty().bind(schedTable.widthProperty().multiply(1.0 / 4.0));
        
        scheduleTableGridPane.setHgap(10);
        scheduleTableGridPane.setVgap(10);
        
        schedItems.setContent(scheduleTableGridPane);
        
        
        ArrayList<String> typeComboOptions = new ArrayList<>();
        typeComboOptions.add("Holidays");
        typeComboOptions.add("Lectures");
        typeComboOptions.add("References");
        typeComboOptions.add("Recitations");
        typeComboOptions.add("Hws");
        
        props.addPropertyOptionsList("Options", typeComboOptions);  // COMBO BOX LIST  
        props.addProperty("Lec","Lectures");  // DEFAULT
        
        Label addEditLabel = ohBuilder.buildLabel(ADD_EDIT_LABEL, addEditPane, 0, 0, 1, 1, STYLES_LABEL, ENABLED);
        Label typeLabel = ohBuilder.buildLabel(D_TYPE, addEditPane, 0, 1, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        ComboBox typeCombo = ohBuilder.buildComboBox(D_TYPE_COMBO, addEditPane, 1, 1, 1, 1, SITE_SUBJECT_LABEL, ENABLED, "Options", "Lec");
        Label date = ohBuilder.buildLabel(D_DATE, addEditPane, 0, 2, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        DatePicker dp = ohBuilder.buildDatePicker(D_DATE_DATEPICKER, addEditPane, 1, 2, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        Label title = ohBuilder.buildLabel(D_TITLE, addEditPane, 0, 3, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        TextField titleTF = ohBuilder.buildTextField(D_TITLE_TEXTFIELD, addEditPane, 1, 3, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        Label topic = ohBuilder.buildLabel(D_TOPIC, addEditPane, 0, 4, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        TextField topicTF = ohBuilder.buildTextField(D_TOPIC_TEXTFIELD, addEditPane, 1, 4, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        Label link = ohBuilder.buildLabel(D_LINK, addEditPane, 0, 5, 1, 1, SITE_SUBJECT_LABEL, ENABLED);
        TextField linkTF = ohBuilder.buildTextField(D_LINK_TEXTFIELD, addEditPane, 1, 5, 1, 1, SITE_TITLE_TEXTFIELD, ENABLED);
        
        Button addUpdateBt = ohBuilder.buildTextButton(ADD_UPDATE_BUTTON, addEditPane, 0, 6, 1, 1, STYLES_FAVICON_LABEL, ENABLED);
        Button clearBt = ohBuilder.buildTextButton(CLEAR_BUTTON, addEditPane, 1, 6, 1, 1, STYLES_FAVICON_LABEL, !ENABLED);
        addEditPane.setHgap(10);
        addEditPane.setVgap(20);
        
        addEditPane.setPadding(new Insets(10,10,10,10));
        
        
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.setContent(main);
        
        return sp;
    }

    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }

    public void initControllers() {
        CSGController controller = new CSGController((CSGApp) app);
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();
        AppGUIModule gui = app.getGUIModule();

        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        
        Button favicon = (Button)gui.getGUINode(STYLES_FAVICON);
        Button navBar = (Button) gui.getGUINode(STYLES_NAVBAR);
        Button left = (Button) gui.getGUINode(STYLES_LEFT);
        Button right = (Button) gui.getGUINode(STYLES_RIGHT);
        
        ImageView imgFav = (ImageView) gui.getGUINode(FAVICON_IV);
        ImageView imgNavBar = (ImageView) gui.getGUINode(NAVBAR_IV);
        ImageView imgLeftFooter = (ImageView) gui.getGUINode(LEFT_IV);
        ImageView imgRightFooter =  (ImageView) gui.getGUINode(RIGHT_IV);
        GridPane styleGridPane = (GridPane) gui.getGUINode(STYLES_GRIDPANE);
        
        favicon.setOnAction(e -> {
            chooseImage(imgFav, "favicon");
        });
        
        navBar.setOnAction(e -> {
           chooseImage(imgNavBar, "navbar");
        });
        
        left.setOnAction(e -> {
            chooseImage(imgLeftFooter, "bottom_left");
        });
        
        right.setOnAction(e -> {
           chooseImage(imgRightFooter, "bottom_right");
        });
        
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });
        
        //MEETING TIMES
        Button addLectureBt = (Button)(app.getGUIModule().getGUINode(ADD_LECTURE_BUTTON));
        Button addRecBt = (Button)(app.getGUIModule().getGUINode(ADD_REC_BUTTON));
        Button addLabBt = (Button)(app.getGUIModule().getGUINode(ADD_LAB_BUTTON));
        
        Button removeLectureBt = (Button) app.getGUIModule().getGUINode(REMOVE_LECTURE_BUTTON);
        Button removeRecBt = (Button) app.getGUIModule().getGUINode(REMOVE_REC_BUTTON);
        Button removeLabBt = (Button) app.getGUIModule().getGUINode(REMOVE_LAB_BUTTON);
        
        
        
        addLectureBt.setOnAction(e -> {
            controller.processAddLecture();
        });
        
        removeLectureBt.setOnAction(e -> {
            controller.processRemoveLecture();
        });
        
        
        addRecBt.setOnAction(eh -> {
            controller.processAddRecitation();
        });
        
        removeRecBt.setOnAction(eh -> {
            controller.processRemoveRecitation();
        });
        
        addLabBt.setOnAction(e -> {
            controller.processAddLab();
        });
        
        addLabBt.setOnAction(eh -> {
            controller.processAddLab();
        });
        
        removeLabBt.setOnAction(e -> {
        
            controller.processRemoveLab();
        
        });
        
        TableView lectureTableView = (TableView) gui.getGUINode(LECTURES_TABLE);

        
        removeLectureBt.disableProperty().bind(Bindings.isEmpty(lectureTableView.getSelectionModel().getSelectedItems()));
        
        TableView recitationTableView = (TableView) gui.getGUINode(RECITATION_TABLE);
        removeRecBt.disableProperty().bind(Bindings.isEmpty(recitationTableView.getSelectionModel().getSelectedItems()));

        TableView labTableView = (TableView) gui.getGUINode(LABS_TABLE);
        removeLabBt.disableProperty().bind(Bindings.isEmpty(labTableView.getSelectionModel().getSelectedItems()));
 
        TableColumn lecture_section = (TableColumn)(lectureTableView.getColumns().get(0));
        TableColumn lecture_days = (TableColumn)(lectureTableView.getColumns().get(1));
        TableColumn lecture_time = (TableColumn)(lectureTableView.getColumns().get(2));
        TableColumn lecture_room = (TableColumn)(lectureTableView.getColumns().get(3));
 
        
        lecture_section.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Lecture,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                Lecture lectureToEdit = ((Lecture)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLecture_Transaction transaction = new EditLecture_Transaction(lectureToEdit, t.getNewValue(), lectureToEdit.getDays(), lectureToEdit.getTime(), lectureToEdit.getRoom());
                app.processTransaction(transaction);
                lectureTableView.refresh();
            }
        });
        
        lecture_days.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lecture , String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                Lecture lectureToEdit = ((Lecture)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLecture_Transaction transaction = new EditLecture_Transaction(lectureToEdit, lectureToEdit.getSection(), t.getNewValue(), lectureToEdit.getTime(), lectureToEdit.getRoom());
                app.processTransaction(transaction);
                lectureTableView.refresh();
            }
        });
        
        lecture_time.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lecture , String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                Lecture lectureToEdit = ((Lecture)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLecture_Transaction transaction = new EditLecture_Transaction(lectureToEdit, lectureToEdit.getSection(), lectureToEdit.getDays(), t.getNewValue(), lectureToEdit.getRoom());
                app.processTransaction(transaction);
                lectureTableView.refresh();
            }
        });
        
        lecture_days.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lecture , String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                Lecture lectureToEdit = ((Lecture)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLecture_Transaction transaction = new EditLecture_Transaction(lectureToEdit, lectureToEdit.getSection(), t.getNewValue(), lectureToEdit.getTime(), lectureToEdit.getRoom());
                app.processTransaction(transaction);
                lectureTableView.refresh();
            }
        });
        
        lecture_room.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lecture , String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lecture, String> t) {
                Lecture lectureToEdit = ((Lecture)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLecture_Transaction transaction = new EditLecture_Transaction(lectureToEdit, lectureToEdit.getSection(), lectureToEdit.getDays(), lectureToEdit.getTime(), t.getNewValue());
                app.processTransaction(transaction);
                lectureTableView.refresh();
            }
        });
        
        TableColumn recitation_section = (TableColumn)(recitationTableView.getColumns().get(0));
        TableColumn recitation_days_time = (TableColumn)(recitationTableView.getColumns().get(1));
        TableColumn recitation_room = (TableColumn)(recitationTableView.getColumns().get(2));
        TableColumn recitation_ta1 = (TableColumn)(recitationTableView.getColumns().get(3));
        TableColumn recitation_ta2 = (TableColumn)(recitationTableView.getColumns().get(4));
        
        recitation_section.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recitation,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                Recitation recToEdit = ((Recitation)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditRecitation_Transaction transaction = 
                        new EditRecitation_Transaction(recToEdit, 
                                t.getNewValue(), 
                        recToEdit.getDaysAndTime(), 
                                recToEdit.getRoom(), 
                                recToEdit.getTa1(), 
                                recToEdit.getTa2());
                app.processTransaction(transaction);
                recitationTableView.refresh();
            }
        });
        
        recitation_days_time.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recitation,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                 Recitation recToEdit = ((Recitation)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditRecitation_Transaction transaction = 
                        new EditRecitation_Transaction(recToEdit, 
                                recToEdit.getSection(), 
                                t.getNewValue(), 
                                recToEdit.getRoom(), 
                                recToEdit.getTa1(), 
                                recToEdit.getTa2());
                app.processTransaction(transaction);
                recitationTableView.refresh();
            }
        });
        
        recitation_room.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recitation,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                Recitation recToEdit = ((Recitation)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditRecitation_Transaction transaction = 
                        new EditRecitation_Transaction(recToEdit, 
                                recToEdit.getSection(), 
                                recToEdit.getDaysAndTime(), 
                                t.getNewValue(), 
                                recToEdit.getTa1(), 
                                recToEdit.getTa2());
                app.processTransaction(transaction);
                recitationTableView.refresh();
            }
        });
        
        recitation_ta1.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recitation,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                Recitation recToEdit = ((Recitation)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditRecitation_Transaction transaction = 
                        new EditRecitation_Transaction(recToEdit, 
                                recToEdit.getSection(), 
                                recToEdit.getDaysAndTime(), 
                                recToEdit.getRoom(), 
                                t.getNewValue(), 
                                recToEdit.getTa2());
                app.processTransaction(transaction);
                recitationTableView.refresh();
            }
        });
        
        recitation_ta2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Recitation,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Recitation, String> t) {
                Recitation recToEdit = ((Recitation)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditRecitation_Transaction transaction = 
                        new EditRecitation_Transaction(recToEdit, 
                                recToEdit.getSection(), 
                                recToEdit.getDaysAndTime(), 
                                recToEdit.getRoom(), 
                                recToEdit.getTa1(), 
                                t.getNewValue());
                app.processTransaction(transaction);
                recitationTableView.refresh();
            }
        });
        
        TableColumn lab_section = (TableColumn)(labTableView.getColumns().get(0));
        TableColumn lab_days_time = (TableColumn)(labTableView.getColumns().get(1));
        TableColumn lab_room = (TableColumn)(labTableView.getColumns().get(2));
        TableColumn lab_ta1 = (TableColumn)(labTableView.getColumns().get(3));
        TableColumn lab_ta2 = (TableColumn)(labTableView.getColumns().get(4));
        
         lab_section.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lab,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                Lab labToEdit = ((Lab)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLab_Transaction transaction = 
                        new EditLab_Transaction(labToEdit, 
                                t.getNewValue(), 
                        labToEdit.getDaysAndTime(), 
                                labToEdit.getRoom(), 
                                labToEdit.getTa1(), 
                                labToEdit.getTa2());
                app.processTransaction(transaction);
                labTableView.refresh();
            }
        });
        
        lab_days_time.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lab,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                 Lab labToEdit = ((Lab)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLab_Transaction transaction = 
                        new EditLab_Transaction(labToEdit, 
                                labToEdit.getSection(), 
                                t.getNewValue(), 
                                labToEdit.getRoom(), 
                                labToEdit.getTa1(), 
                                labToEdit.getTa2());
                app.processTransaction(transaction);
                labTableView.refresh();
            }
        });
        
        lab_room.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lab,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                Lab labToEdit = ((Lab)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLab_Transaction transaction = 
                        new EditLab_Transaction(labToEdit, 
                                labToEdit.getSection(), 
                                labToEdit.getDaysAndTime(), 
                                t.getNewValue(), 
                                labToEdit.getTa1(), 
                                labToEdit.getTa2());
                app.processTransaction(transaction);
                labTableView.refresh();
            }
        });
        
        lab_ta1.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lab,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                Lab labToEdit = ((Lab)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLab_Transaction transaction = 
                        new EditLab_Transaction(labToEdit, 
                                labToEdit.getSection(), 
                                labToEdit.getDaysAndTime(), 
                                labToEdit.getRoom(), 
                                t.getNewValue(), 
                                labToEdit.getTa2());
                app.processTransaction(transaction);
                labTableView.refresh();
            }
        });
        
        lab_ta2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Lab,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<Lab, String> t) {
                Lab labToEdit = ((Lab)t.getTableView().getItems().get(t.getTablePosition().getRow()));
                EditLab_Transaction transaction = 
                        new EditLab_Transaction(labToEdit, 
                                labToEdit.getSection(), 
                                labToEdit.getDaysAndTime(), 
                                labToEdit.getRoom(), 
                                labToEdit.getTa1(), 
                                t.getNewValue());
                app.processTransaction(transaction);
                labTableView.refresh();
            }
        });
        
        // NOW HANDLE THE SCHEDULE STUFF
        
        Button addUpdateButton = (Button) app.getGUIModule().getGUINode(ADD_UPDATE_BUTTON);
        Button removeButton = (Button) app.getGUIModule().getGUINode(REMOVE_SCHEDULE_BUTTON);
        Button clearButton = (Button) app.getGUIModule().getGUINode(CLEAR_BUTTON);
        
        TableView scheduleTable = (TableView) app.getGUIModule().getGUINode(SCHEDULE_ITEMS_TABLE);
        
        removeButton.disableProperty().bind(Bindings.isEmpty(scheduleTable.getSelectionModel().getSelectedItems()));
        clearButton.disableProperty().bind(Bindings.isEmpty(scheduleTable.getItems()));
        
        addUpdateButton.setOnAction(e -> {
            controller.processAddScheduleItem();
        });
        
        scheduleTable.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                scheduleTable.getSelectionModel().clearSelection();
                e.consume();
            }
            else if(e.getClickCount() == 2){
                controller.fillFields();
                e.consume();
            }
        });
        
        removeButton.setOnAction(e -> {
            controller.processRemoveScheduleItem();
            
        });
        
        clearButton.setOnAction(e -> {
            controller.processClearSchedule();
        });
        
        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });
        
        //DATE PICKERS
        DatePicker startDate = (DatePicker)gui.getGUINode(CB_SM_DATEPICKER);
        DatePicker endDate = (DatePicker) gui.getGUINode(CB_EF_DATEPICKER);
        DatePicker selectionDate = (DatePicker) gui.getGUINode(D_DATE_DATEPICKER);
        endDate.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                try{
                super.updateItem(date, empty);
                LocalDate startingMonday = startDate.getValue();
                setDisable(empty || date.compareTo(startingMonday) < 0);
                }catch(Exception ex){
                    
                }
                
            }
        });
        
        selectionDate.setDayCellFactory(picker -> new DateCell(){
        
            public void updateItem(LocalDate date, boolean empty){
                try{
                super.updateItem(date, empty);
                LocalDate startingMonday = startDate.getValue();
                LocalDate endingFriday = endDate.getValue();
                setDisable(empty || date.compareTo(startingMonday) < 0 || date.compareTo(endingFriday) > 0);
                }catch(Exception ex){
                    
                }
            }
        
        
        });
        

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
        });
    }
    
    public void chooseImage(ImageView img, String filePath){
        
        //OPEN FILE CHOOSER    
        FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter allFilterJPG = 
                    new FileChooser.ExtensionFilter("All Files", "*.*");
            FileChooser.ExtensionFilter extFilterJPG = 
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = 
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = 
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng = 
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fc.getExtensionFilters()
                    .addAll(allFilterJPG,extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
            //Show open file dialog
            File file = fc.showOpenDialog(app.getGUIModule().getWindow());
            fc.setInitialDirectory(new File(System.getProperty("user.dir")+"\\images"));
            fc.setTitle(PropertiesManager.getPropertiesManager().getProperty(CHOOSE_IMAGE_TITLE));
            try {
                BufferedImage bi = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bi, null);
                img.setImage(image);
                if(filePath.equals("favicon")){
                    faviconFilePath = "file:"+ file.getAbsolutePath();
                }
                else if(filePath.equals("navbar")){
                    navbarFilePath = "file:"+ file.getAbsolutePath();
                }
                else if(filePath.equals("bottom_left")){
                    bottomLeftFilePath = "file:"+ file.getAbsolutePath();
                }
                else if(filePath.equals("bottom_right")){
                    bottomRightFilePath = "file:"+ file.getAbsolutePath();
                }
                //System.out.println(faviconFilePath);
                ((Button)app.getGUIModule().getGUINode(SAVE_BUTTON)).setDisable(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch(IllegalArgumentException ex){
                //ex.printStackTrace();
            } catch(NullPointerException ex){
                Stage window = app.getGUIModule().getWindow();
                AppDialogsFacade.showMessageDialog(window, INVALID_IMAGE_FILE_TITLE, INVALID_IMAGE_FILE_CONTENT);
            } catch(Exception ex){
                Stage window = app.getGUIModule().getWindow();
                AppDialogsFacade.showMessageDialog(window, INVALID_IMAGE_FILE_TITLE, INVALID_IMAGE_FILE_CONTENT);
            }
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CSGFoolproofDesign((CSGApp) app));
    }
  
    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
}
