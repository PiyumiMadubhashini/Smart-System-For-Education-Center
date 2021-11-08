package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentPaymentFormController {
    public ComboBox cmdBatchId;
    public ComboBox cmdStudentId;


    public void initialize() {
        try {


            loadBatchIds();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmdBatchId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStudentId(newValue);
        });
    }

    private void setStudentId(Object newValue) {
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

    private void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> BatchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchId.getItems().addAll(BatchId);

    }

    public List<String> getAllStudentIds() throws SQLException, ClassNotFoundException {

        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement( "SELECT * FROM Student WHERE Batch_id ='" + cmdBatchId.getValue() + "'" ).executeQuery();

        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));

        }

        return ids;
    }
    }

