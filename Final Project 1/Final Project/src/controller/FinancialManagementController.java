package controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class FinancialManagementController {

    public PieChart pieChart;

    public void initialize(){
        try {

            setDataToPieChart();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void lblBatchPaymentOnClick(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/BatchPaymentForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("Readme/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Batch Payment Sheet");
        stage.show();
    }

    public void lblRegFeePaymentOnClick(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/RegisterFeePaymentForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("Readme/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Register Fee Payment Sheet");
        stage.show();
    }

    public void setDataToPieChart() throws SQLException, ClassNotFoundException {
        int Student = new StudentManagementFormController().showStudentCount();
        int Subject = new SubjectManagementFormController().showSubjectCount();
        int Teacher = new TeacherManagementFormController().showTeacherCount();
        int RegStudent = new AddNewStudentFormController().showRegStudentCount();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Student", Student),
                new PieChart.Data("Subject", Subject),
                new PieChart.Data("Teacher", Teacher),
                new PieChart.Data("Registered student", RegStudent)

        );
        pieChart.setData(pieChartData);
    }
}


