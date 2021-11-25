package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ScheduleFormController {


    public ListView<String> listView;

    public void initialize()  {
        try {

            loadDetailToList();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void imgOpenSchedulingForm(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/LectureSceduleForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("Readme/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Lecture Scheduling");
        stage.show();

    }

    public void btnOpenScheduleDetails(MouseEvent mouseEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/ScheduleDetailsForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("Readme/desafio.png");
        stage.getIcons().add(image);
        stage.setTitle("Schedule Detail");
        stage.show();
    }

    private void loadDetailToList() throws SQLException, ClassNotFoundException, SQLException {

        List<String> lecNames = new LectureScheduleFormController().getAllLecNames();
        List<String> time = new LectureScheduleFormController().getTimes();

        String toPrint = " " ;
        int c = 0 ;
        for (int i=0 ; i < lecNames.size() ; i++){
            toPrint = "Lecture Name :"+" " + lecNames.get(c)+" "+"-------"+" "+"Time And Date : "+" "+time.get(c);

            listView.getItems().add(toPrint);
            c++ ;

        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        listView.getItems().clear();
        loadDetailToList();
    }


    /*public void imgsearchOnClick(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        listView.getItems().clear();
        listView.getItems().addAll(searchList(txtSearchBar.getText(),lecNames));

    }

    private List<String>searchList(String searchWord, List<String> listOfStrings){
        List<String> searchWordsArray = Arrays.asList(searchWord.trim().split(" "));
        return searchWordsArray.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(lecNames ->
                    input.toLowerCase().contains(lecNames.toLowerCase()));
        }).collect(Collectors.toList());

    }

     */
}











