package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateIdFormController {
    public ComboBox cmdStudentId;
    public TextField txtIdSId;
    public TextField txtIdSname;
    public TextField txtIDBatchId;
    public TextField txtIdContactnum;
    public TextField txtStudentFname;
    public TextField txtStudentNFame;
    public TextField txtContactNumber;
    public TextField txtBatchId;
    public Label lblBarcode;

    public void initialize(){
        try {

            loadStudentIds();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmdStudentId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setStudentData(newValue);
        });

    }

    private void setStudentData(Object studentId) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            Statement stm = con.createStatement();
            String query = "SELECT * FROM Student WHERE student_id ='" + cmdStudentId.getValue() + "'";


            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                txtStudentNFame.setText(set.getString(2));
                txtStudentFname.setText(set.getString(3));
                txtBatchId.setText(set.getString(4));
                txtContactNumber.setText(set.getString(6));
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public List<String> getAllRegisteredStudentIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM register_fee").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(2));
        }
        return ids;
    }

    private void loadStudentIds() throws SQLException, ClassNotFoundException {
        List<String> StudentId = getAllRegisteredStudentIds();
        cmdStudentId.getItems().addAll(StudentId);

    }

    public void bynCreateIdOnAction(ActionEvent actionEvent) {
        txtIdSId.setText(cmdStudentId.getValue().toString());
        txtIdSname.setText(txtStudentNFame.getText()+" "+txtStudentFname.getText());
        txtIDBatchId.setText(txtBatchId.getText());
        txtIdContactnum.setText(txtContactNumber.getText());
        lblBarcode.setText("Student Barcode by Desafio Higher Education Center "+txtIdSId.getText());

        clear();
        lblBarcode.setVisible(true);
    }

    public void clear(){
        cmdStudentId.setValue(" ");
        txtStudentNFame.clear();
        txtStudentFname.clear();
        txtBatchId.clear();
        txtContactNumber.clear();
    }

    public void btnPrintIDOnAction(MouseEvent mouseEvent) throws JRException {

        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/ID.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            String id = txtIdSId.getText();
            String name = txtIdSname.getText();
            String batchId = txtIDBatchId.getText();
            String number = txtIdContactnum.getText();
            String code = lblBarcode.getText();


            HashMap map = new HashMap();
            map.put("id", id);
            map.put("name", name);
            map.put("batchId", batchId);
            map.put("phoneNumber", number);
            map.put("barcode", code);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        }

        txtIdSId.clear(); txtIdSname.clear(); txtIDBatchId.clear(); txtIdContactnum.clear();
        lblBarcode.setVisible(false);

    }
}
