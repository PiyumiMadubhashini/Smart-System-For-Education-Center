package controller;

import Util.Validation;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Lecture;
import model.ScheduleDetail;
import view.tm.lectureTm;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class LectureScheduleFormController {
    public ComboBox cmdLecturesName;
    public ComboBox cmdTeacherID;
    public ComboBox cmdBatchID;
    public TextField txtLecID;
    public TextField txtSeats;
    public TextField txtScheduleNumber;
    public TableView<lectureTm> tblLecture;
    public TableColumn colLecId;
    public TableColumn cilLecName;
    public TableColumn colSeat;
    public TextField txtDateAndTime;
    public JFXTimePicker time;
    public JFXDatePicker date;
    public ComboBox cmdLecNameUpdate;
    public TextField txtSeatsUpdate;
    public ComboBox cmdLecIDUpdate1;
    public Button btnAdd;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern seats = Pattern.compile("^[0-9]{2,4}$");

    public void initialize() {

        storeValidations();
        btnAdd.setDisable(true);

        cmdLecIDUpdate1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setUpdateData(newValue);
        });

        colLecId.setCellValueFactory(new PropertyValueFactory<>("lecture_id"));
        cilLecName.setCellValueFactory(new PropertyValueFactory<>("lecture_name"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("seats"));

        try {
            loadTeacherIds();
            loadBatchIds();
            loadSubNames();
            setLectureId();
            setTxtScheduleNumberId();
            loadAllLectures();
            loadLecId();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void storeValidations() {
        allValidation.put(txtSeats, seats);
    }

    private void setUpdateData(Object lecture_id) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            Statement stm = con.createStatement();
            String query = "SELECT * FROM lecture WHERE lecture_id ='" + cmdLecIDUpdate1.getValue() + "'";


            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                cmdLecNameUpdate.setValue(set.getString(2));
                txtSeatsUpdate.setText(String.valueOf(set.getInt(3)));

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDateAndTime() {
        String tempTime = time.getValue().toString();
        String tempDate = date.getValue().toString();

        txtDateAndTime.setText(tempDate + "          " + tempTime);

    }

    public void setLectureId() throws SQLException, ClassNotFoundException {
        txtLecID.setText(getLectureID());
    }

    public String getLectureID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Lecture_id FROM lecture ORDER BY Lecture_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "L-00" + tempId;
            } else {
                return tempId < 99 ? "L-0" + tempId : "L-" + tempId;
            }
        } else {
            return "L-001";
        }

    }

    public String getScheduleNo() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Number_of_Schedule FROM time_table ORDER BY Number_of_Schedule DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "LS-00" + tempId;
            } else {
                return tempId < 99 ? "LS-0" + tempId : "LS-" + tempId;
            }
        } else {
            return "LS-001";
        }

    }

    public void setTxtScheduleNumberId() throws SQLException, ClassNotFoundException {
        txtScheduleNumber.setText(getScheduleNo());
    }

    private void loadTeacherIds() throws SQLException, ClassNotFoundException {
        List<String> teacherId = new TeacherManagementFormController().getAllTeacherIds();
        cmdTeacherID.getItems().addAll(teacherId);
    }

    private void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> batchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchID.getItems().addAll(batchId);
    }

    public List<String> getAllLecIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM lecture").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    private void loadSubNames() throws SQLException, ClassNotFoundException {
        List<String> subjectNames = new SubjectManagementFormController().getAllSubjectName();
        cmdLecturesName.getItems().addAll(subjectNames);
        cmdLecNameUpdate.getItems().addAll(subjectNames);
    }

    private void loadLecId() throws SQLException, ClassNotFoundException {
        List<String> lec_ids = getAllLecIds();
        cmdLecIDUpdate1.getItems().addAll(lec_ids);
    }

    Connection con = null ;
    public boolean addLecture(Lecture l) throws SQLException, ClassNotFoundException {


                con = DBConnection.getInstance().getConnection();
                con.setAutoCommit(false);


        String query = "INSERT INTO lecture VALUES(?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);

        stm.setObject(1, l.getLecture_id());
        stm.setObject(2, l.getLecture_name());
        stm.setObject(3, l.getSeats());


        return stm.executeUpdate() > 0;

    }
    Connection con2 = null;
    public boolean addLectureDetail(ScheduleDetail s) throws SQLException, ClassNotFoundException {

        con2 = DBConnection.getInstance().getConnection();

        String query = "INSERT INTO time_table VALUES(?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);

        stm.setObject(1, s.getNumber_of_Schedule());
        stm.setObject(2, s.getLecture_id());
        stm.setObject(3, s.getTeacher_id());
        stm.setObject(4, s.getBatch_id());
        stm.setObject(5, s.getDate_time());

        return stm.executeUpdate() > 0;

    }

    public void btnAddScheduleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Lecture l1 = new Lecture(txtLecID.getText(), cmdLecturesName.getValue().toString(), Integer.parseInt(txtSeats.getText()));

        if (addLecture(l1)) {
            new Alert().confirmationMsg("New Lecture saved..");
            loadAllLectures();
            setTxtScheduleNumberId();
            cmdLecIDUpdate1.getItems().clear();
            loadLecId();

        } else {
           new Alert().ErrMsg("Something went wrong , Try again !!");
            con.rollback();


        }
        con.setAutoCommit(true);

        ScheduleDetail s1 = new ScheduleDetail(txtScheduleNumber.getText(), txtLecID.getText(), cmdTeacherID.getValue().toString(),
                cmdBatchID.getValue().toString(), txtDateAndTime.getText());


        if (addLectureDetail(s1)) {

            clear();
            setTxtScheduleNumberId();


        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
            con2.rollback();
        }
        setLectureId();
        con2.setAutoCommit(true);


    }

    public void loadAllLectures() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM lecture");
        ResultSet rst = stm.executeQuery();
        ObservableList<lectureTm> observableList = FXCollections.observableArrayList();
        while (rst.next()) {
            observableList.add(new lectureTm(rst.getString(1), rst.getString(2),
                    rst.getInt(3)));
        }
        tblLecture.setItems(observableList);
    }

    public void clear() {
        cmdBatchID.setValue(" ");
        cmdTeacherID.setValue(" ");
        txtSeats.clear();
        txtDateAndTime.clear();
        cmdLecturesName.setValue(" ");
    }

    public void btnOpenScheduleDetailsForm(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/ScheduleDetailsForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Schedule Detail");
        stage.show();
    }

    public void btnSetTimeAndDate(ActionEvent actionEvent) {
        loadDateAndTime();
    }

    public List<String> getAllLecNames() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM lecture").executeQuery();

        List<String> lecName = new ArrayList<>();
        while (rst.next()){
            lecName.add((rst.getString(2)));
        }
        return lecName;
    }

    public List<String> getTimes() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM time_table").executeQuery();

        List<String> time = new ArrayList<>();
        while (rst.next()){
            time.add((rst.getString(5)));
        }
        return time;
    }

    public boolean deleteLecture(String lecture_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM lecture WHERE Lecture_id='"+lecture_id+"'").executeUpdate() > 0 ;
    }

    public void btnRemoveLecOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        lectureTm lecture = tblLecture.getSelectionModel().getSelectedItem();
        String lecture_id = lecture.getLecture_id();
        if (deleteLecture(lecture_id)) {
            loadAllLectures();
            tblLecture.refresh();
            new Alert().ErrMsg("Do you want to delete selected lecture");
            setLectureId();
            cmdLecIDUpdate1.getItems().clear();
            loadLecId();

        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }

    }

    public void btnUpdateLecOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempLecId = cmdLecIDUpdate1.getValue().toString();
        String tempLecName = cmdLecNameUpdate.getValue().toString();
        int tempSeat = Integer.parseInt(txtSeatsUpdate.getText());

        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE lecture SET Lecture_name=?, Seat=? WHERE Lecture_id=?");

        stm.setObject(1,tempLecName);
        stm.setObject(2,tempSeat);
        stm.setObject(3,tempLecId);

        if (stm.executeUpdate() > 0) {
              new Alert().confirmationMsg("Batch Updated successfully..");
            loadAllLectures();

        } else {
            new Alert().ErrMsg("Something went wrong..Try Again");
        }
        cmdLecturesName.setValue(" ");
        txtSeatsUpdate.clear();

    }

    public void txtFieldsKeReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Done");
            }
        }
    }
}

