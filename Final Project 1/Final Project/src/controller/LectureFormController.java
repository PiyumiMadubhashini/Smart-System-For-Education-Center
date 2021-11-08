package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Lecture;
import model.RegisterFee;
import view.tm.lectureTm;
import view.tm.registerTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LectureFormController {

    public TextField txtLecId;
    public ComboBox cmdSeats;
    public ComboBox cmbLecName;
    public TableView <lectureTm> tblLecture;
    public TableColumn colLecId;
    public TableColumn colLecName;
    public TableColumn colSeat;

    public void initialize(){

        colLecId.setCellValueFactory(new PropertyValueFactory<>("lecture_id"));
        colLecName.setCellValueFactory(new PropertyValueFactory<>("lecture_name"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("seats"));

        try {

            loadSubjectName();
            setLectureId();
            loadAllLectures();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmdSeats.getItems().addAll("1000","750","500","250","100");




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

    public void setLectureId() throws SQLException, ClassNotFoundException {
        txtLecId.setText(getLectureID());
    }

    private void loadSubjectName() throws SQLException, ClassNotFoundException {
        List<String> SubName = new SubjectManagementFormController().getAllSubjectName();
        cmbLecName.getItems().addAll(SubName);
    }

    public boolean addLecture(Lecture l) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO lecture VALUES(?,?,?)");
        stm.setObject(1, l.getLecture_id());
        stm.setObject(2, l.getLecture_name());
        stm.setObject(3, l.getSeats());


        return stm.executeUpdate() > 0;
    }

    public void btnOrganiseLecOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Lecture l1 = new Lecture(txtLecId.getText(), cmbLecName.getValue().toString(),
                Integer.parseInt(cmdSeats.getValue().toString()));

        if (addLecture(l1)) {
            new Alert().confirmationMsg("Lecture Added Successfully");
            loadAllLectures();

        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
        clear();
        setLectureId();

    }

    public void loadAllLectures() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM lecture");

        ResultSet rst = stm.executeQuery();
        ObservableList<lectureTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new lectureTm(rst.getString(1),rst.getString(2),
                    rst.getInt(3)));
        }
        tblLecture.setItems(observableList);
    }

    public void clear (){
        cmdSeats.setValue(" ");
        cmbLecName.setValue(" ");
    }

    public boolean deleteLecture(String lecture_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM lecture WHERE Lecture_id='"+lecture_id+"'").executeUpdate() > 0 ;
    }

    public void btnRemoveLecOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        lectureTm lec = tblLecture.getSelectionModel().getSelectedItem();
        String lec_id = lec.getLecture_id();
        if (deleteLecture(lec_id)) {
            loadAllLectures();
            tblLecture.refresh();
            new Alert().ErrMsg("Do you want to delete selected Lecture");
        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        cmbLecName.setValue(" ");
        cmdSeats.setValue(" ");
    }
}
