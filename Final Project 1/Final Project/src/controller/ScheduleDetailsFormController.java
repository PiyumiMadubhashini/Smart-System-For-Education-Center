package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.ScheduleDetailTm;
import view.tm.batchTm;
import view.tm.lectureTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleDetailsFormController {

    public TableView <ScheduleDetailTm> tblLecDetail;
    public TableColumn colScheduleID;
    public TableColumn colLecId;
    public TableColumn colTeacherId;
    public TableColumn colBatchId;
    public TableColumn colDateAndTime;

    public void initialize(){
        colScheduleID.setCellValueFactory(new PropertyValueFactory<>("Number_of_Schedule"));
        colLecId.setCellValueFactory(new PropertyValueFactory<>("Lecture_id"));
        colTeacherId.setCellValueFactory(new PropertyValueFactory<>("Teacher_id"));
        colBatchId.setCellValueFactory(new PropertyValueFactory<>("Batch_id"));
        colDateAndTime.setCellValueFactory(new PropertyValueFactory<>("Date_time"));


        try {
            loadAllDetail();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void loadAllDetail() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM time_table");
        ResultSet rst = stm.executeQuery();
        ObservableList<ScheduleDetailTm> observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new ScheduleDetailTm(rst.getString(1),rst.getString(2),
                    rst.getString(3),rst.getString(4), rst.getString(5)));
        }
        tblLecDetail.setItems(observableList);
    }

    public void btnPrintReportOnClick(MouseEvent mouseEvent) {

        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/Schedule.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
