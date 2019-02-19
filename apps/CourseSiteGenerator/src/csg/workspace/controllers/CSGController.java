package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import djf.ui.dialogs.AppDialogsFacade;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import csg.CSGApp;
import csg.CSGPropertyType;
import static csg.CSGPropertyType.*;
import static csg.CSGPropertyType.D_TITLE;
import static csg.CSGPropertyType.D_TYPE_COMBO;
import static csg.CSGPropertyType.OH_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.OH_FOOLPROOF_SETTINGS;
import static csg.CSGPropertyType.OH_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.OH_NO_TA_SELECTED_CONTENT;
import static csg.CSGPropertyType.OH_NO_TA_SELECTED_TITLE;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_TAS_TABLE_VIEW;
import static csg.CSGPropertyType.OH_TA_EDIT_DIALOG;
import csg.data.CSGData;
import csg.data.Lab;
import csg.data.Lecture;
import csg.data.Recitation;
import csg.data.ScheduleObject;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import csg.transactions.AddLab_Transaction;
import csg.transactions.AddLecture_Transaction;
import csg.transactions.AddRecitation_Transaction;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.AddTA_Transaction;
import csg.transactions.EditSchedule_Transaction;
import csg.transactions.EditTA_Transaction;
import csg.transactions.RemoveLab_Transaction;
import csg.transactions.RemoveLecture_Transaction;
import csg.transactions.RemoveRecitation_Transaction;
import csg.transactions.RemoveSchedule_Transaction;
import csg.transactions.ToggleCSG_Transaction;
import csg.workspace.dialogs.TADialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author McKillaGorilla
 */
public class CSGController {

    CSGApp app;

    public CSGController(CSGApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CSGData data = (CSGData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processVerifyTA() {

    }
    
    public void processAddLecture(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        Lecture lec = new Lecture("?","?","?","?");
        AddLecture_Transaction addLecTransaction = new AddLecture_Transaction(data, lec);
        app.processTransaction(addLecTransaction);
    }
    
    public void processRemoveLecture(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        TableView lectureTable = ((TableView) gui.getGUINode(CSGPropertyType.LECTURES_TABLE));
        Lecture lec = (Lecture)(lectureTable.getSelectionModel().getSelectedItem());
        RemoveLecture_Transaction r = new RemoveLecture_Transaction(data, lec);
        app.processTransaction(r);
        
    }
    
    public void processAddRecitation(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        TableView recTable = ((TableView) gui.getGUINode(CSGPropertyType.RECITATION_TABLE));
        Recitation rec = new Recitation("?", "?", "?", "?", "?");
        AddRecitation_Transaction addRecitationTransaction = new AddRecitation_Transaction(data, rec);
        app.processTransaction(addRecitationTransaction);
    }
    
    public void processRemoveRecitation(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        TableView recTable = ((TableView) gui.getGUINode(CSGPropertyType.RECITATION_TABLE));
        Recitation rec = (Recitation)(recTable.getSelectionModel().getSelectedItem());
        RemoveRecitation_Transaction transaction = new RemoveRecitation_Transaction(data, rec);
        app.processTransaction(transaction);
    }
    
    public void processAddLab(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData)app.getDataComponent();
        TableView labTable = ((TableView) gui.getGUINode(CSGPropertyType.LABS_TABLE));
        Lab lab = new Lab("?","?","?","?","?");
        AddLab_Transaction addLabTransaction = new AddLab_Transaction(data, lab);
        app.processTransaction(addLabTransaction);
        
    }
    
    public void processRemoveLab(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        TableView labTable = ((TableView) gui.getGUINode(CSGPropertyType.LABS_TABLE));
        Lab lab = (Lab)(labTable.getSelectionModel().getSelectedItem());
        RemoveLab_Transaction removeLabTransaction = new RemoveLab_Transaction(data, lab);
        app.processTransaction(removeLabTransaction);
    }
    
    public void processAddScheduleItem(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        TableView scheduleTable = ((TableView) gui.getGUINode(CSGPropertyType.SCHEDULE_ITEMS_TABLE));
        
        //GET THE COMBO BOX OPTION, DATEPICKER AND TEXT FIELD ITEMS
            ComboBox itemType = (ComboBox) gui.getGUINode(D_TYPE_COMBO);
            String comboChoice = (String) itemType.getValue();
            DatePicker dp = (DatePicker) gui.getGUINode(CSGPropertyType.D_DATE_DATEPICKER);
            LocalDate date = dp.getValue() == null ? LocalDate.now() : dp.getValue();
            String title = ((TextField) gui.getGUINode(D_TITLE_TEXTFIELD)).getText();
            String topic = ((TextField) gui.getGUINode(D_TOPIC_TEXTFIELD)).getText();
            String link = ((TextField) gui.getGUINode(D_LINK_TEXTFIELD)).getText();
            ScheduleObject initObject = new ScheduleObject(comboChoice, date, title, topic, link);
            // DO ADD TRANSACTION
        if ((ScheduleObject)(scheduleTable.getSelectionModel().getSelectedItem()) == null) {
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(data, initObject);
            app.processTransaction(transaction);
            scheduleTable.refresh();
            // CLEAR TFS AND RESET OPTIONS
            dp.getEditor().clear();
            itemType.getSelectionModel().selectFirst();
            ((TextField) gui.getGUINode(D_TITLE_TEXTFIELD)).clear();
            ((TextField) gui.getGUINode(D_TOPIC_TEXTFIELD)).clear();
            ((TextField) gui.getGUINode(D_LINK_TEXTFIELD)).clear();
        }
        else{
            ScheduleObject object = (ScheduleObject) (scheduleTable.getSelectionModel().getSelectedItem());
            // DO EDIT TRANSACTION
            EditSchedule_Transaction transaction = new EditSchedule_Transaction(object, comboChoice, date, title, topic, link);
            app.processTransaction(transaction);
            scheduleTable.refresh();
            dp.getEditor().clear();
            itemType.getSelectionModel().selectFirst();
            dp.getEditor().clear();
            ((TextField) gui.getGUINode(D_TITLE_TEXTFIELD)).clear();
            ((TextField) gui.getGUINode(D_TOPIC_TEXTFIELD)).clear();
            ((TextField) gui.getGUINode(D_LINK_TEXTFIELD)).clear();
        }
    }
    
    public void fillFields(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        TableView scheduleTable = ((TableView) gui.getGUINode(CSGPropertyType.SCHEDULE_ITEMS_TABLE));
        ScheduleObject schedule = (ScheduleObject)scheduleTable.getSelectionModel().getSelectedItem();
        
        // SET THE TEXT FIELDS
        ((TextField) gui.getGUINode(D_TITLE_TEXTFIELD)).setText(schedule.getTitle());
        ((TextField) gui.getGUINode(D_TOPIC_TEXTFIELD)).setText(schedule.getTopic());
        ((TextField) gui.getGUINode(D_LINK_TEXTFIELD)).setText(schedule.getLink());
        
        //SET THE COMBO BOX OPTIONS
        ((DatePicker) gui.getGUINode(CSGPropertyType.D_DATE_DATEPICKER)).getEditor().setText(schedule.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yy", Locale.US)));
        ((ComboBox) gui.getGUINode(D_TYPE_COMBO)).getSelectionModel().select(schedule.getType());
        
    }
    
    public void processRemoveScheduleItem(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        TableView scheduleTable = ((TableView) gui.getGUINode(CSGPropertyType.SCHEDULE_ITEMS_TABLE));
        ScheduleObject schedule = (ScheduleObject)scheduleTable.getSelectionModel().getSelectedItem();
        RemoveSchedule_Transaction transaction = new RemoveSchedule_Transaction(data, schedule);
        app.processTransaction(transaction);
        scheduleTable.refresh();
    }
    
    public void processClearSchedule(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        TableView scheduleTable = ((TableView) gui.getGUINode(CSGPropertyType.SCHEDULE_ITEMS_TABLE));
        scheduleTable.getItems().clear();
    }
    
    
    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CSGData data = (CSGData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleCSG_Transaction transaction = new ToggleCSG_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }

    public void processEditTA() {
        CSGData data = (CSGData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }

}