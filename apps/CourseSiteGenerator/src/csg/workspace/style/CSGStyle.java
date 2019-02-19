package csg.workspace.style;

/**
 * This class lists all CSS style types for this application. These
 * are used by JavaFX to apply style properties to controls like
 * buttons, labels, and panes.

 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class CSGStyle {
    public static final String EMPTY_TEXT = "";
    public static final int BUTTON_TAG_WIDTH = 75;

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS M3Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    // NOTE THAT FOUR CLASS STYLES ALREADY EXIST:
    // top_toolbar, toolbar, toolbar_text_button, toolbar_icon_button
    
    public static final String CLASS_OH_PANE          = "oh_pane";
    public static final String CLASS_OH_BOX           = "oh_box";            
    public static final String CLASS_OH_HEADER_LABEL  = "oh_header_label";
    public static final String CLASS_OH_PROMPT        = "oh_prompt";
    public static final String CLASS_OH_TEXT_FIELD    = "oh_text_field";
    public static final String CLASS_OH_TEXT_FIELD_ERROR = "oh_text_field_error";
    public static final String CLASS_OH_BUTTON        = "oh_button";
    public static final String CLASS_OH_RADIO_BOX     = "oh_radio_box";
    public static final String CLASS_OH_RADIO_BUTTON  = "oh_radio_button";
    public static final String CLASS_OH_TAB_PANE      = "oh_tab_pane";
    public static final String CLASS_OH_TABLE_VIEW    = "oh_table_view";
    public static final String CLASS_OH_COLUMN        = "oh_column";
    public static final String CLASS_OH_CENTERED_COLUMN = "oh_centered_column";
    public static final String CLASS_OH_OFFICE_HOURS_TABLE_VIEW = "oh_office_hours_table_view";
    public static final String CLASS_OH_TIME_COLUMN = "oh_time_column";
    public static final String CLASS_OH_DAY_OF_WEEK_COLUMN = "oh_day_of_week_column";
    
    // FOR THE DIALOG
    public static final String CLASS_OH_DIALOG_GRID_PANE = "oh_dialog_grid_pane";
    public static final String CLASS_OH_DIALOG_HEADER = "oh_dialog_header"; 
    public static final String CLASS_OH_DIALOG_PROMPT = "oh_dialog_prompt"; 
    public static final String CLASS_OH_DIALOG_TEXT_FIELD = "oh_dialog_text_field";
    public static final String CLASS_OH_DIALOG_RADIO_BUTTON = "oh_dialog_radio_button";
    public static final String CLASS_OH_DIALOG_BOX = "oh_dialog_box";
    public static final String CLASS_OH_DIALOG_BUTTON = "oh_dialog_button";
    
    //FOR THE TABS
    public static final String CLASS_TAB_HBOX = "csg_header_box";
    public static final String CLASS_TABPANE = "csg_tab_pane";
    public static final String CLASS_TABs = "csg_tabs";

    //COMBO BOXES
    public static final String CLASS_START_TIME_COMBO_BOX = "csg_combo_box";
    public static final String CLASS_END_TIME_COMBO_BOX = "csg_combo_box";
    public static final String CLASS_COMBO_BOX           = "csg_combo_header";  
    public static final String START_COMBO_BOX_LABEL = "csg_start_combo_box_label";
    public static final String END_COMBO_BOX_LABEL = "csg_end_combo_box_label";
    public static final String CSG_BANNER_HBOX = "csg_banner_hbox";
    public static final String CSG_BANNER = "csg_banner";
    public static final String CSG_SUBJECT_CBOX = "csg_subject_cbox";
    public static final String CSG_NUMBER_CBOX = "csg_number_cbox";
    public static final String CSG_YEAR_CBOX = "csg_year_cbox";
    public static final String CSG_SEMESTER_CBOX = "csg_semester_cbox";
    public static final String SITE_SUBJECT_LABEL = "csg_site_subject";
    public static final String SITE_NUMBER_LABEL = "csg_site_number";
    public static final String SITE_SEMESTER_LABEL = "csg_site_semester";
    public static final String SITE_YEAR_LABEL = "csg_site_year";
    public static final String SITE_EXPORT_LABEL = "csg_site_export";
    public static final String SITE_TITLE_LABEL = "csg_site_title";
    public static final String SITE_TITLE_TEXTFIELD = "csg_site_title_textfield";
    public static final String SITE_PAGES_HBOX = "csg_pages_hbox";
    public static final String SITE_PAGES_LABEL = "csg_pages_label";
    public static final String SITE_PAGES_HOME = "csg_pages_home";
    public static final String SITE_PAGES_SYLLABUS = "csg_pages_syllabus";
    public static final String SITE_PAGES_SCHEDULE = "csg_pages_schedule";
    public static final String SITE_PAGES_HWS = "csg_pages_hws";
    public static final String IV_STYLE = "csg_image_view";
    public static final String MEETING_TIMES_BUTTONS = "meeting_times_buttons";
    public static final String MEETING_TIMES_BOX = "meeting_times_box";

    
    //STYLES GRIDPANE
    public static final String SITE_STYLES_GRIDPANE = "csg_styles_gridpane";
    public static final String STYLES_LABEL = "csg_style_label";
    public static final String STYLES_FAVICON_LABEL = "csg_button_favicon";
    public static final String STYLES_NAVBAR_IMAGE = "csg_button_navbar";
    public static final String STYLES_LEFT_FOOTER = "csg_button_left_footer";
    public static final String STYLES_RIGHT_FOOTER = "csg_button_right_footer";
    public static final String STYLES_FONTSCOLORS_LABEL = "csg_styles_fonts_colors_label";
    public static final String STYLES_CBOX = "csg_styles_combobox";
    public static final String STYLES_WARNING_LABEL = "csg_styles_warning";
    
    //Instrucor GridPane
    public static final String INS_LABEL = "instructor_label";
    public static final String INS_NAME = "instructor_name";
    public static final String INS_ROOM = "instructor_room";
    public static final String INS_EMAIL = "instructor_email";
    public static final String INS_HOMEPAGE = "instructor_homepage";
    public static final String INS_OH_LABEL = "instructor_oh";
    public static final String INS_TEXTAREA = "instructor_oh_text_area";
    public static final String INS_GRIDPANE = "instructor_gridpane";
    public static final String INS_OH_BUTTON = "instructor_oh_text_area_button";
    
    public static final String INSTRUCTOR_NAME_TF = "instructor_name_textfield";
    public static final String INSTRUCTOR_EMAIL_TF = "instructor_email_textfield";
    public static final String INSTRUCTOR_ROOM_TF = "instructor_room_textfield";
    public static final String INSTRUCTOR_HOMEPAGE_TF = "instructor_homepage_textfield";
    
    public static final String SYL_DESCRIPTION = "syl_desc";
    public static final String SYL_TOPICS = "syllabus_topics";
    public static final String SYL_PREREQ = "syl_prereq";
    public static final String SYL_OUTCOMES = "syl_outcomes";
    public static final String SYL_TEXTBOOKS = "syl_textbooks";
    public static final String SYL_GRADED = "syl_graded";
    public static final String SYL_GRADEDCOMP = "syl_graded_comp";
    public static final String SYL_GRADINGNOTES = "syl_grading_notes";
    public static final String SYL_AD = "syl_ad";
    public static final String SYL_SA = "syl_sa";
    
    public static final String SYL_DESCRIPTION_TA = "syllabus_desc_ta";
    public static final String SYL_TOPICS_TA = "syllabus_desc_ta";
    public static final String SYL_PREREQ_TA = "syl_prereq_ta";
    public static final String SYL_OUTCOMES_TA = "syl_outcomes_ta";
    public static final String SYL_TEXTBOOKS_TA = "syl_textbooks_ta";
    public static final String SYL_GRADED_TA = "syl_graded_ta";
    public static final String SYL_GRADEDCOMP_TA = "syl_graded_comp_ta";
    public static final String SYL_GRADINGNOTES_TA = "syl_grading_notes_ta";
    public static final String SYL_AD_TA = "syl_ad_ta";
    public static final String SYL_SA_TA = "syl_sa_ta";
    public static final String NOTHING = "csg_nothing";

    //MEETING TIMES
    public static final String MEETING_TIMES_TABLES = "meeting_times";
    public static final String MEETING_TIMES_TITLEDPANES = "meeting_times_panes";
    public static final String MEETING_TIMES_COLUMN = "meeting_times_column";

}