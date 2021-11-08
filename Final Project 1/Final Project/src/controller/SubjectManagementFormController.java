package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Subject;
import view.tm.subjectTm;
import view.tm.teacherTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class SubjectManagementFormController implements SubjectManagement {


    public Label subId;
    public AnchorPane subRoot;
    public ComboBox cmbteacherId;
    public TextField txtSubName;
    public TextField txtSubId;
    public TableColumn colTeacherId;
    public TableColumn colSubjectName;
    public TableColumn colSubjectId;
    public TableView <subjectTm> tblSubject;


    public void initialize(){

        try {

            loadTeacherIds();
            loadAllSubject();
            setSubjectId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        colSubjectId.setCellValueFactory(new PropertyValueFactory<>("subject_id"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("getSubject_name"));
        colTeacherId.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));



    }

    public void setSubjectId() throws SQLException, ClassNotFoundException {
        txtSubId.setText(getSubjectID());
    }

    public String getSubjectID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Subject_id FROM subject ORDER BY Subject_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "SB-00" + tempId;
            } else {
                return tempId < 99 ? "SB-0" + tempId : "SB-" + tempId;
            }
        } else {
            return "SB-001";
        }

    }

    public boolean addSubject(Subject s) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO subject VALUES(?,?,?)");
        stm.setObject(1,s.getSubject_id());
        stm.setObject(2,s.getSubject_name());
        stm.setObject(3,s.getTeacher_id());

        return stm.executeUpdate()>0 ;
    }

    private void loadTeacherIds() throws SQLException, ClassNotFoundException {
        List<String> TeacherIds = new TeacherManagementFormController().getAllTeacherIds();
        cmbteacherId.getItems().addAll(TeacherIds);
    }

    public void btnAddSubjectOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Subject s1 = new Subject(txtSubId.getText(), txtSubName.getText(), cmbteacherId.getSelectionModel().getSelectedItem().toString());

        if (addSubject(s1)) {
           new Alert().confirmationMsg("New Subject saved..");
            setSubjectId();
        }else {
           new Alert().ErrMsg("Something went wrong , Try again !!");
        }
         clear();
        loadAllSubject();
    }

    private void loadAllSubject() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM subject");
        ResultSet rst = stm.executeQuery();
        ObservableList<subjectTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new subjectTm(rst.getString(1),rst.getString(2), rst.getString(3)));
        }
        tblSubject.setItems(observableList);
    }

    public void clear(){
        txtSubName.clear();
        cmbteacherId.setValue(" ");
        txtSubId.requestFocus();
    }

    public int showSubjectCount() throws SQLException, ClassNotFoundException {
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM subject").executeQuery();
        int count = 0 ;
        while (set.next()){
            count=count+1;
        }

        return count ;
    }

    public void txtSubjectIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection con = DBConnection.getInstance().getConnection();
        Statement stm = con.createStatement();
        String query = "SELECT * FROM subject WHERE subject_id ='" + txtSubId.getText() + "'";

        ResultSet set = stm.executeQuery(query);
        if (set.next()) {
            txtSubName.setText(set.getString(2));
            cmbteacherId.setValue(set.getString(3));

        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
            clear();
        }
    }

    public void updateSubOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempId = txtSubId.getText();
        String tempSubName = txtSubName.getText();
        String tempTeacherId = cmbteacherId.getSelectionModel().getSelectedItem().toString();

        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE subject SET Subject_name=? , Teacher_id=? WHERE Subject_id=?");

        stm.setObject(1, tempSubName);
        stm.setObject(2, tempTeacherId);
        stm.setObject(3, tempId);

        if (stm.executeUpdate() > 0) {
            new Alert().confirmationMsg("Subject Updated successfully..");
            loadAllSubject();
            setSubjectId();
        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
        clear();

    }

    public boolean deleteSubject(String subject_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM subject WHERE subject_id='"+subject_id+"'").executeUpdate() > 0 ;

    }

    public void btnDeleteSubjectOnAction(ActionEvent actionEvent) {
        subjectTm subject = tblSubject.getSelectionModel().getSelectedItem();
        String subjectId = subject.getSubject_id();
        try {
            if (deleteSubject(subjectId)) {
                loadAllSubject();
                tblSubject.refresh();
                new Alert().ErrMsg("Do you want to delete selected Subject");
                setSubjectId();

            }else {
                new Alert().ErrMsg("Something went wrong , Try again !!");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllSubjectName() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM subject").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(2));
        }
        return ids;
    }
}
