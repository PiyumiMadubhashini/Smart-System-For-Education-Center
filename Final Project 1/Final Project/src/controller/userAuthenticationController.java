package controller;

import com.jfoenix.controls.JFXPasswordField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userAuthenticationController {
    public static String enteredPassword;
    public JFXPasswordField pswFieldId;
    public Button okbtnId;
    public AnchorPane root;


    public void btnOkOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        String Password = pswFieldId.getText();


        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from admin where Password = ?");

        preparedStatement.setObject(1, Password);

        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isExist = resultSet.next();

        if (isExist) {
            enteredPassword = resultSet.getString(1);
            new Alert().confirmationMsg("Password matched");

            Stage window = (Stage) root.getScene().getWindow();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CreateNewAccountForm.fxml"))));
            window.centerOnScreen();

        } else {
            new Alert().ErrMsg("Password not matched");
            pswFieldId.requestFocus();
        }
    }
}

