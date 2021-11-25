package controller;
import Util.Validation;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Student;
import view.tm.registerTm;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;


public class AddNewStudentFormController implements StudentManagement {
    public Pane regRoot;

    public TextField txtStudentId;
    public TextField txtSureName;
    public TextField txtNic;
    public TextField txtTpNumber;
    public TextField txtFirstName;
    public ComboBox cmdGender;
    public ComboBox cmdBatchID;
    public TableView <registerTm> tblReg;
    public TableColumn colSid;
    public TableColumn colRegId;
    public TableColumn colSub;
    public TableColumn colAmount;
    public Label txtamount;
    public Button btnAddStudent;


    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern FirstNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern SureNamePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern NicNumberPattern = Pattern.compile("^[0-9]{9}[v]|[0-9]{12}$");
    Pattern ContactNumberPattern = Pattern.compile("^[0-9]{3}[-][0-9]{7}$");


    public void initialize() {
        btnAddStudent.setDisable(true);
        storeValidations();


        colRegId.setCellValueFactory(new PropertyValueFactory<>("register_id"));
        colSid.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colSub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        

        try {

            loadAllRegStudent();
            setTot();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            setStudentId();
            loadBatchIds();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmdGender.getItems().addAll("Female", "Male");

        try {
            loadAllRegStudent();
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

    public int showRegStudentCount() throws SQLException, ClassNotFoundException {
        ResultSet set = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM register_fee").executeQuery();
        int count = 0 ;
        while (set.next()){
            count=count+1;
        }

        return count ;
    }

    public void setStudentId() throws SQLException, ClassNotFoundException {
        txtStudentId.setText(getStudentID());
    }

    public String getStudentID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Student_id FROM student ORDER BY Student_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "S-00" + tempId;
            } else {
                return tempId < 99 ? "S-0" + tempId : "S-" + tempId;
            }
        } else {
            return "S-001";
        }

    }

    private void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> BatchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchID.getItems().addAll(BatchId);
    }

    public void btnAddStudentOnAction(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        Student s1 = new Student(txtStudentId.getText(), txtFirstName.getText(), txtSureName.getText(), txtNic.getText(),
                cmdGender.getValue().toString(), txtTpNumber.getText(), cmdBatchID.getValue().toString());

        if (addStudent(s1)) {
            new Alert().confirmationMsg("New Student saved..");

        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
        clear();
        setStudentId();


    }

    @Override
    public boolean addStudent(Student s) throws SQLException, ClassNotFoundException {

        Connection con = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO student VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);

        stm.setObject(1, s.getStudent_id());
        stm.setObject(2, s.getFirst_name());
        stm.setObject(3, s.getSure_name());
        stm.setObject(4, s.getStudent_nic());
        stm.setObject(5, s.getGender());
        stm.setObject(6, s.getTelephone_number());
        stm.setObject(7, s.getBatch_id());

        return stm.executeUpdate() > 0;
    }

    @Override
    public List<String> getAllStudentIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM student").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public boolean deleteStudent(String student_id) throws SQLException, ClassNotFoundException {
        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM student WHERE Student_id='"+student_id+"'").executeUpdate() > 0 ;
    }

    public void clear (){

        txtFirstName.clear();
        cmdBatchID.setValue(" ");
        txtSureName.clear();
        txtNic.clear();
        cmdGender.setValue(" ");
        txtTpNumber.clear();
    }

    public void loadRegForm() throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/RegisterForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Register Form");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                try {

                    loadAllRegStudent();
                    setTot();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.show();
    }

    public void btnOpenRegForm(ActionEvent actionEvent) {

        try {
            loadRegForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllRegStudent() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM register_fee");
        ResultSet rst = stm.executeQuery();
        ObservableList<registerTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new registerTm(rst.getString(1),rst.getString(2),
                    rst.getString(3),rst.getDouble(4)));
        }
        tblReg.setItems(observableList);
    }

    public boolean deleteRegisterStudent(String Register_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM register_fee WHERE Register_id='"+Register_id+"'").executeUpdate() > 0 ;
    }

    public void btnRemoveStudentOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        registerTm reg = tblReg.getSelectionModel().getSelectedItem();
        String RegId = reg.getRegister_id();
        if (deleteRegisterStudent(RegId)) {
            loadAllRegStudent();
            tblReg.refresh();
            new Alert().ErrMsg("Do you want to delete selected Student");
            setTot();
        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }

    }

    public void setTot() throws SQLException, ClassNotFoundException {
        int allTot = new RegisterFormController().getAllTot();
        txtamount.setText(String.valueOf(allTot));
        
    }

    public void textFieldsKeyRelease(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnAddStudent);

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

        




