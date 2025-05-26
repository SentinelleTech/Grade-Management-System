module com.stech.grade_mgt_system_gui_1.grade_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stech.grade_mgt_system_gui_1.grade_app to javafx.fxml;
    exports com.stech.grade_mgt_system_gui_1.grade_app;
}