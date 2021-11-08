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
import javafx.scene.layout.AnchorPane;
import model.teacher;
import view.tm.teacherTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;


public class TeacherManagementFormController implements teacherManagement {
    public TableView<teacherTm> tblTeacher;
    public TableColumn colTID;
    public TableColumn colFName;
    public TableColumn colGender;
    public TableColumn colCity;
    public TableColumn colQualification;
    public TableColumn colExperience;
    public TableColumn colSame;
    public TextField txtTeacherID;
    public TextField txtExperience;
    public TextField txtTeacherCity;
    public TextField txtTsunami;
    public TextField txtQualification;
    public TextField txtTeacherFname;
    public ComboBox cmdGender;
    public Button btnSave;
    public AnchorPane root;

    int count = 0;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern FirstNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern SureNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern city = Pattern.compile("^[A-z ]{3,20}$");
    Pattern qualification = Pattern.compile("^[A-z ]{3,50}$");
    Pattern experience = Pattern.compile("^[A-z0-9 ]{3,50}$");


    public void initialize() throws SQLException, ClassNotFoundException {
        btnSave.setDisable(true);
        storeValidations();

        cmdGender.setValue(" ");
        cmdGender.getItems().addAll("Female", "Male");
        tblTeacher.refresh();
        colTID.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));
        colFName.setCellValueFactory(new PropertyValueFactory<>("teacher_firstName"));
        colSame.setCellValueFactory(new PropertyValueFactory<>("teacher_sureName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("teacher_gender"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("teacher_city"));
        colQualification.setCellValueFactory(new PropertyValueFactory<>("teacher_qualification"));
        colExperience.setCellValueFactory(new PropertyValueFactory<>("teacher_experience"));
        loadAllTeacher();

        setTeacherId();
    }

    private void storeValidations() {
        allValidation.put(txtTeacherFname, FirstNamePattern);
        allValidation.put(txtTsunami, SureNamePattern);
        allValidation.put(txtTeacherCity, city);
        allValidation.put(txtQualification, qualification);
        allValidation.put(txtExperience, experience);
    }

    public boolean addTeacher(teacher t) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO teacher VALUES(?,?,?,?,?,?,?)");
        stm.setObject(1,t.getTeacher_id());
        stm.setObject(2,t.getTeacher_firstName());
        stm.setObject(3,t.getTeacher_sureName());
        stm.setObject(4,t.getTeacher_gender());
        stm.setObject(5,t.getTeacher_city());
        stm.setObject(6,t.getTeacher_qualification());
        stm.setObject(7,t.getTeacher_experience());


        return stm.executeUpdate()>0 ;
    }

    public void loadAllTeacher() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM teacher");
        ResultSet rst = stm.executeQuery();
        ObservableList<teacherTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new teacherTm(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)));
        }
        tblTeacher.setItems(observableList);
    }

    public void btnSaveTeacherOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        teacher t1 = new teacher(
                txtTeacherID.getText(), txtTeacherFname.getText(),txtTsunami.getText(),cmdGender.getValue().toString(),txtTeacherCity.getText(),
                txtQualification.getText(),txtExperience.getText());

        if (addTeacher(t1)) {
            new Alert().confirmationMsg("New Teacher saved..");
            setTeacherId();
            clear();

        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
            clear();
            txtTeacherID.requestFocus();
        }

        loadAllTeacher();

    }

    public boolean deleteTeacher(String teacher_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM teacher WHERE Teacher_id='"+teacher_id+"'").executeUpdate() > 0 ;

    }

    @Override
    public List<String> getAllTeacherIds() throws SQLException, ClassNotFoundException {
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM teacher").executeQuery();
        List<String> ids = new ArrayList<>();

        while (set.next()){

            ids.add(set.getString(1)
            );

        }

        return ids ;

    }

    public void clear(){

        txtTeacherFname.clear();
        txtTsunami.clear();
        cmdGender.setValue(" ");
        txtTeacherCity.clear();
        txtExperience.clear();
        txtQualification.clear();
    }

    public void btnRemoveTeacherOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        teacherTm teacher = tblTeacher.getSelectionModel().getSelectedItem();
        String teacherId = teacher.getTeacher_id();
        if (deleteTeacher(teacherId)) {
            loadAllTeacher();
            tblTeacher.refresh();
            new Alert().confirmationMsg("Do you want to delete selected Teacher");

        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
    }

    public void btnUpdateTeacherOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempId = txtTeacherID.getText();
        String tempFName = txtTeacherFname.getText();
        String tempSName = txtTsunami.getText();
        String tempGender = cmdGender.getValue().toString();
        String tempCity = txtTeacherCity.getText();
        String tempQualification = txtQualification.getText();
        String tempExperience = txtExperience.getText();


        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE teacher SET Teacher_fname=? , Teacher_sname=?,Gender=?," +
                "City=?, Qualification=?, Experience=? WHERE Teacher_id=?");

        stm.setObject(1, tempFName);
        stm.setObject(2, tempSName);
        stm.setObject(3, tempGender);
        stm.setObject(4, tempCity);
        stm.setObject(5, tempQualification);
        stm.setObject(6, tempExperience);
        stm.setObject(7,tempId);

        if (stm.executeUpdate() > 0) {
            new Alert().confirmationMsg("Teacher Updated successfully..");
            loadAllTeacher();
            setTeacherId();

        } else {
           new Alert().ErrMsg("Something went wrong..Try Again");
        }
        clear();
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode()== KeyCode.ENTER) {
            try {
                Connection con = DBConnection.getInstance().getConnection();
                Statement stm = con.createStatement();
                String query = "SELECT * FROM teacher WHERE teacher_id='" + txtTeacherID.getText() + "'";

                ResultSet set = stm.executeQuery(query);
                if (set.next()) {
                    txtTeacherFname.setText(set.getString(2));
                    txtTsunami.setText(set.getString(3));
                    cmdGender.setValue(set.getString(4));
                    txtTeacherCity.setText(set.getString(5));
                    txtQualification.setText(set.getString(6));
                    txtExperience.setText(set.getString(7));
                } else {
                    new Alert().ErrMsg("Something Went Wrong");
                    clear();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public int showTeacherCount() throws SQLException, ClassNotFoundException {
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM teacher").executeQuery();
        while (set.next()){
            count=count+1;
        }

        return count ;
    }

    public void setTeacherId() throws SQLException, ClassNotFoundException {
        txtTeacherID.setText(getTeacherID());
    }

    public String getTeacherID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Teacher_id FROM teacher ORDER BY Teacher_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "T-00" + tempId;
            } else {
                return tempId < 99 ? "T-0" + tempId : "T-" + tempId;
            }
        } else {
            return "T-001";
        }

    }

    public void textFieldOnKeyReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnSave);

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
