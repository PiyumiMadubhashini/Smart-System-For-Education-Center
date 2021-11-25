package controller;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class DashBoardFormController {


    public Label lblDate;
    public Label lblTime;
    public Label lblTeacherCount;
    public Label lblSubjectCount;
    public Label lblStudentCount;
    public ImageView imgStudentManagement;
    public AnchorPane root;
    public Label lblRegStudentCount1;


    public void initialize(){
        loadDateAndTime();
        showTeacherCount();
        showSubjectCount();
        showStudentCount();
        showRegStudentCount();
    }


    private void showStudentCount(){
        int count = 0;
        try {
            count = new StudentManagementFormController().showStudentCount();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblStudentCount.setText(String.valueOf(count));

    }

    private void showRegStudentCount(){
        int count = 0;
        try {
            count = new AddNewStudentFormController().showRegStudentCount();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblRegStudentCount1.setText(String.valueOf(count));

    }

    private void showSubjectCount(){
        int count = 0;
        try {
            count = new SubjectManagementFormController().showSubjectCount();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblSubjectCount.setText(String.valueOf(count));

    }

    private void showTeacherCount(){
        int count = 0;
        try {
            count = new TeacherManagementFormController().showTeacherCount();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblTeacherCount.setText(String.valueOf(count));

    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":"+ currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void imgViewStudentManagement(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/StudentManagementForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Student Management");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                showStudentCount();
            }
        });
        stage.show();
    }

    public void imgViewSubjectManagement(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/SubjectManagementForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Subject Management");
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                showSubjectCount();
            }
        });

    }

    public void imgAddStudentOnClick(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/AddNewStudentForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Add New Student");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                showStudentCount();
                showRegStudentCount();
            }
        });
        stage.show();
    }

    public void imgViewTeacherManagement(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/TeacherManagementForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Teacher Management");
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                showTeacherCount();
            }
        });
    }

    public void imgViewAttendanceOnClick(MouseEvent mouseEvent) throws IOException {

        Parent load = FXMLLoader.load(getClass().getResource("../view/AttendanceForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Attendance Sheet");
        stage.show();
    }

    public void lblOPenAddNewBatchForm(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/AddNewBatchForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Add New Batch");
        stage.show();
    }

    public void imgOPeCreateIdFormOnClick(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/CreateIdForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Create ID Form");
        stage.show();
    }

    public void imgOPenScheduleForm(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/ScheduleForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Desafio Schedule");
        stage.show();
    }

    public void imgViewFinancialManagement(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/FinancialManagementForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Financial Management");
        stage.show();
    }

    public void imgOpenHelp(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/HelpFileForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image= new Image("assest/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Help");
        stage.show();
    }

    public void btnExit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void imgExitOnClick(MouseEvent mouseEvent) throws IOException {
        Stage window = (Stage) root.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/HomePageForm.fxml"))));
        window.centerOnScreen();
    }
}

