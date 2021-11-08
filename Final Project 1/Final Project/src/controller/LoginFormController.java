package controller;

import com.jfoenix.controls.JFXCheckBox;
import db.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane root;
    public TextField txtUserId;
    public PasswordField txtPassword;
    public JFXCheckBox ckBox;
    public Label lblWarnLable;
    String Password ;

    public static String enteredUserName;
    public static String enteredUserID;

    public void initialize(){
        lblWarnLable.setVisible(false);
    }

    public void btnOpenDashboardOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        String userName = txtUserId.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from admin where User_name = ? and Password = ?");
            preparedStatement.setObject(1, userName);
            preparedStatement.setObject(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isExist = resultSet.next();
            if (isExist) {

                enteredUserID = resultSet.getString(1);
                enteredUserName = resultSet.getString(2);

                Stage window = (Stage) root.getScene().getWindow();
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));
                window.centerOnScreen();

            } else {
                //new Alert().ErrMsg("Invalid User Name or Password");
                txtUserId.setStyle("-fx-border-color: red");
                txtPassword.setStyle("-fx-border-color: red");
                lblWarnLable.setVisible(true);
                txtUserId.clear();
                txtPassword.clear();
                txtUserId.requestFocus();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lblOpenSignUpFormOnClick(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/userAuthentication.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("User Authentication");
        stage.show();


    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void cKBoxOnAction(ActionEvent actionEvent) {
        if (ckBox.isSelected()){
            Password = txtPassword.getText();
            txtPassword.clear();
            txtPassword.setPromptText(Password);
        }else {
            txtPassword.setText(Password);
            txtPassword.setPromptText("Password");
        }
    }
}
