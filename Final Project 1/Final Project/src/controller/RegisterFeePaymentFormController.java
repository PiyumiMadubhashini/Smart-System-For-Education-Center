package controller;

import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Handout;
import view.tm.handoutTm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class RegisterFeePaymentFormController {
    public TextField txtCollectedAmount;
    public TableView <handoutTm>tblHandOut;
    public TableColumn colHandOutId;
    public TableColumn colHandAmount;
    public TableColumn colDateAndTime;
    public Label lblHid;
    public Label lblTime;
    public Label lblDate;


    public void initialize(){

        colHandOutId.setCellValueFactory(new PropertyValueFactory<>("handout_id"));
        colHandAmount.setCellValueFactory(new PropertyValueFactory<>("handout_amount"));
        colDateAndTime.setCellValueFactory(new PropertyValueFactory<>("date_time"));

        loadTimeAndDate();
        setTxtCollectedAmount();
        try {

            setHandoutId();
            loadAllHandout();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadTimeAndDate() {
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

    public boolean addHandOut(Handout h) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO  reg_handout VALUES(?,?,?)");
        stm.setObject(1,h.getHandout_id());
        stm.setObject(2,h.getHandout_amount());
        stm.setObject(3,h.getDate_time());

        return stm.executeUpdate()>0 ;
    }

    public void loadAllHandout() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM reg_handout");
        ResultSet rst = stm.executeQuery();
        ObservableList<handoutTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new handoutTm(rst.getString(1),rst.getInt(2), rst.getString(3)));
        }
        tblHandOut.setItems(observableList);
    }

    private void setTxtCollectedAmount(){

        int final_tot = 0;
        try {

            final_tot = new RegisterFormController().getAllTot();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        txtCollectedAmount.setText(String.valueOf(final_tot));

    }

    public void btnHandOutOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {


        Handout h1 = new Handout(
                lblHid.getText(), Integer.parseInt(txtCollectedAmount.getText()), lblTime.getText()+"    "+lblDate.getText());

        if (addHandOut(h1)) {
            new Alert().confirmationMsg("Handout Added Successfully");

        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }

        loadAllHandout();

    }

    public String getHandoutID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT HandOut_id FROM reg_handout ORDER BY HandOut_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "C-00" + tempId;
            } else {
                return tempId < 99 ? "C-0" + tempId : "C-" + tempId;
            }
        } else {
            return "C-001";
        }

    }

    public void setHandoutId() throws SQLException, ClassNotFoundException {
        lblHid.setText(getHandoutID());
    }

    public boolean deleteHandout(String handout_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM reg_handout WHERE HandOut_id='"+handout_id+"'").executeUpdate() > 0 ;
    }

    public void btnRemoveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        handoutTm handout = tblHandOut.getSelectionModel().getSelectedItem();
        String handoutId = handout.getHandout_id();
        if (deleteHandout(handoutId)) {
            loadAllHandout();
            tblHandOut.refresh();
            new Alert().ErrMsg("Selected Handout is deleted");
        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
    }

}
