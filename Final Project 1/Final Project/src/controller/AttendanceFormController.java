package controller;

import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.Attendance;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.attendanceTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceFormController {
    public Label lblDate;
    public Label lblTime;
    public ComboBox cmdAttendanceStatus;
    public ComboBox cmdStudentId;
    public ComboBox cmdBatchId;
    public TableView<attendanceTm> tblAttendance;
    public TableColumn colAttendanceId;
    public TableColumn colStudentId;
    public TableColumn colStatus;
    public TextField txtAttendanceId;


    public void initialize() {
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendance_id"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmdAttendanceStatus.getItems().addAll("Present", "Absent");
        cmdBatchId.setValue(" ");

        loadDateAndTime();

        try {

            loadAllDetail();

            loadBatchIds();

            setAttendanceId();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmdBatchId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStudentId(newValue);
        });

    }

    public void setAttendanceId() throws SQLException, ClassNotFoundException {
        txtAttendanceId.setText(getAttendanceID());
    }

    public String getAttendanceID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Attendence_id FROM attendence ORDER BY Attendence_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "A-00" + tempId;
            } else {
                return tempId < 99 ? "A-0" + tempId : "A-" + tempId;
            }
        } else {
            return "A-001";
        }

    }

    private void setStudentId(Object BatchId) {
        List<String> StudentId = null;
        try {

            StudentId = getAllStudentIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmdStudentId.getItems().addAll(StudentId);

    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> BatchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchId.getItems().addAll(BatchId);

    }

    public List<String> getAllStudentIds() throws SQLException, ClassNotFoundException {

        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Student WHERE Batch_id ='" + cmdBatchId.getValue() + "'").executeQuery();

        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(rst.getString(1));

        }

        return ids;
    }

    public void btnMarkAttendanceOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Attendance a1 = new Attendance(
                txtAttendanceId.getText(), cmdStudentId.getValue().toString(), cmdAttendanceStatus.getValue().toString());

        if (addAttendanceDetail(a1)) {
            new Alert().confirmationMsg("New Detail saved..");
            setAttendanceId();


        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
        loadAllDetail();
    }

    private void loadAllDetail() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM attendence");
        ResultSet rst = stm.executeQuery();
        ObservableList<attendanceTm> observableList = FXCollections.observableArrayList();
        while (rst.next()) {
            observableList.add(new attendanceTm(rst.getString(1), rst.getString(2), rst.getString(3)));
        }
        tblAttendance.setItems(observableList);
    }

    public boolean addAttendanceDetail(Attendance a) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO attendence VALUES(?,?,?)");
        stm.setObject(1, a.getAttendance_id());
        stm.setObject(2, a.getStudent_id());
        stm.setObject(3, a.getStatus());

        return stm.executeUpdate() > 0;
    }

    public void btnClearOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        cmdAttendanceStatus.setValue(" ");
        cmdStudentId.setValue("");
        cmdBatchId.setValue("");


        setAttendanceId();
        cmdStudentId.getItems().clear();
    }

    public boolean deleteDetail(String attendance_Id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM attendence WHERE Attendence_id='" + attendance_Id + "'").executeUpdate() > 0;
    }

    public void btnRemoveDetailOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        attendanceTm attendanceTm = tblAttendance.getSelectionModel().getSelectedItem();
        String attendance_id = attendanceTm.getAttendance_id();
        if (deleteDetail(attendance_id)) {
            loadAllDetail();
            tblAttendance.refresh();
            new Alert().ErrMsg("Do you want to delete selected detail");
            setAttendanceId();
        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
    }

    public void btnAttendancePrintOnClick(MouseEvent mouseEvent) {
        try {

            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/Attendance.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            ObservableList<attendanceTm> items = tblAttendance.getItems();

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, new JRBeanArrayDataSource(items.toArray()));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
}


