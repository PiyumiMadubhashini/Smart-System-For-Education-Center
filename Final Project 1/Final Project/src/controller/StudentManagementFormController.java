package controller;

import Util.Validation;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.tm.studentTm;


import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class StudentManagementFormController  {
    public AnchorPane root;

    public TableView <studentTm> tblStudent;
    public TableColumn colSId;
    public TableColumn colfName;
    public TableColumn colSname;
    public TableColumn colNic;
    public TableColumn colGender;
    public TableColumn colTnum;
    public TableColumn colBID;
    public TextField txtSureName;
    public TextField txtNic;
    public TextField txtTpNumber;
    public TextField txtFirstName;
    public ComboBox cmdBatchID;
    public ComboBox cmdGender;
    public ComboBox cmdStudentID;
    public Button btnUpdate;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern FirstNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern SureNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern NicNumberPattern = Pattern.compile("^[0-9]{9}[v]|[0-9]{12}$");
    Pattern ContactNumberPattern = Pattern.compile("^[0-9]{3}[-][0-9]{7}$");


    public void initialize(){
        btnUpdate.setDisable(true);
        storeValidations();


        cmdStudentID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStudentData(newValue);
        });

        cmdGender.getItems().addAll("Male","Female");

        try {
            colSId.setCellValueFactory(new PropertyValueFactory<>("student_id"));
            colfName.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            colSname.setCellValueFactory(new PropertyValueFactory<>("sure_name"));
            colNic.setCellValueFactory(new PropertyValueFactory<>("student_nic"));
            colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colTnum.setCellValueFactory(new PropertyValueFactory<>("telephone_number"));
            colBID.setCellValueFactory(new PropertyValueFactory<>("batch_id"));

            loadAllStudent();
            loadBatchIds();
            loadStudentIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void storeValidations() {
        allValidation.put(txtFirstName, FirstNamePattern);
        allValidation.put(txtSureName, SureNamePattern);
        allValidation.put(txtNic, NicNumberPattern);
        allValidation.put(txtTpNumber, ContactNumberPattern);
    }

    private void loadStudentIds() throws SQLException, ClassNotFoundException {
        List<String> TeacherIds = new AddNewStudentFormController().getAllStudentIds();
        cmdStudentID.getItems().addAll(TeacherIds);
    }

    private void setStudentData(Object student_id) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            Statement stm = con.createStatement();
            String query = "SELECT * FROM Student WHERE student_id ='" + cmdStudentID.getValue() + "'";


            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                txtFirstName.setText(set.getString(2));
                txtSureName.setText(set.getString(3));
                txtNic.setText(set.getString(4));
                cmdGender.setValue(set.getString(5));
                txtTpNumber.setText(set.getString(6));
                cmdBatchID.setValue(set.getString(7));

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM student");
        ResultSet rst = stm.executeQuery();
        ObservableList<studentTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new studentTm(rst.getString(1),rst.getString(2),
                    rst.getString(3),rst.getString(4), rst.getString(5),
                    rst.getString(6),rst.getString(7)));
        }
        tblStudent.setItems(observableList);


    }

    public int showStudentCount() throws SQLException, ClassNotFoundException {
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM student").executeQuery();
        int count = 0 ;
        while (set.next()){
            count=count+1;
        }

        return count ;
    }

    public void btnRemoveStudentOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        studentTm student = tblStudent.getSelectionModel().getSelectedItem();
        String student_id = student.getStudent_id();
        if (new AddNewStudentFormController().deleteStudent(student_id)) {
            loadAllStudent();
            tblStudent.refresh();
            new Alert().ErrMsg("Do you want to delete selected Student");
        }else {
            new Alert().ErrMsg("Something went wrong..Try Again");
        }

    }

    public void clear (){
        cmdStudentID.setValue(" ");
        txtFirstName.clear();
        cmdBatchID.setValue(" ");
        txtSureName.clear();
        txtNic.clear();
        cmdGender.setValue(" ");
        txtTpNumber.clear();
    }

    private void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> BatchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchID.getItems().addAll(BatchId);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempId = cmdStudentID.getValue().toString();
        String tempFName = txtFirstName.getText();
        String tempSName = txtSureName.getText();
        String tempNic = txtNic.getText();
        String tempGender = cmdGender.getValue().toString();
        String tempTPNumber = txtTpNumber.getText();
        String tempBatchId = cmdBatchID.getValue().toString();


        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE student SET Student_fname=?, Student_sname=?," +
                "Student_nic=?, Gender=?, Telephone_number=?, Batch_id=? WHERE Student_id=?");

        stm.setObject(1, tempFName);
        stm.setObject(2, tempSName);
        stm.setObject(3, tempNic);
        stm.setObject(4, tempGender);
        stm.setObject(5, tempTPNumber);
        stm.setObject(6, tempBatchId);
        stm.setObject(7,tempId);

        if (stm.executeUpdate() > 0) {
            new Alert().confirmationMsg("Student Updated successfully..");
            loadAllStudent();
        } else {
            new Alert().ErrMsg("Something went wrong..Try Again");
        }
        clear();

    }

    public void textFieldEvent(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnUpdate);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Added").showAndWait();
                System.out.println("Done");
            }
        }
    }
}
