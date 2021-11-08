package controller;

import Util.Validation;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Admin;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class CreateNewAccountFormController implements AdminManagement{
    public TextField txtUserName;
    public PasswordField txtPassword;
    public PasswordField txtReEnterPassword;
    public Button btnCreate;
    public Label lblWarnPw;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern userName = Pattern.compile("^[A-z ]{3,20}$");
    Pattern password = Pattern.compile("^[A-z0-9]{3,30}$");


    public void initialize() {
        btnCreate.setDisable(true);
        storeValidations();
        lblWarnPw.setVisible(false);

    }

    private void storeValidations() {
        allValidation.put(txtUserName, userName);
        allValidation.put(txtPassword, password);
    }

    public void btnToSaveDataInDatabase(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        if (txtReEnterPassword.getText().equals(txtPassword.getText())){

            Admin a1 = new Admin(
                    txtUserName.getText(), txtPassword.getText());

            if (addAdmin(a1)) {
                new controller.Alert().confirmationMsg("New Admin saved..");
                clear();
                lblWarnPw.setVisible(false);
            } else {
                new Alert().ErrMsg("Something went wrong , Try again !!");
            }
        }else {
            lblWarnPw.setVisible(true);
            txtPassword.requestFocus();
            txtReEnterPassword.clear();
        }


    }

    @Override
    public boolean addAdmin(Admin a) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO admin VALUES(?,?)");
        stm.setObject(1,a.getUser_name());
        stm.setObject(2,a.getPassword());

        return stm.executeUpdate()>0 ;
    }

    public void clear(){
        txtPassword.clear();
        txtUserName.clear();
        txtReEnterPassword.clear();

    }

    public void textFieldsKeyEvent(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnCreate);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {

                System.out.println("Done");
            }
        }

    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        txtReEnterPassword.requestFocus();
    }
}
