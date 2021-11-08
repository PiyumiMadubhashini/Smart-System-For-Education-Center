package controller;

import Util.Validation;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Batch;
import model.Payment;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.batchTm;
import view.tm.paymentTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class BatchPaymentFormController {

    public ComboBox cmdBatchIds;
    public ComboBox cmdMonth;
    public TextField txtPaymentId;
    public TextField txtExpectedPay;
    public TextField txtlLastPay;
    public TableView<paymentTm> tblPayment;
    public TableColumn colPaymentId;
    public TableColumn colBatchId;
    public TableColumn colMonth;
    public TableColumn colExpectedFee;
    public TableColumn colLastPayment;
    public Button btnPay;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern payment = Pattern.compile("^[0-9]{3,10}$");

    public void initialize() {

        storeValidations();
        btnPay.setDisable(true);



        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        colBatchId.setCellValueFactory(new PropertyValueFactory<>("batch_id"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colExpectedFee.setCellValueFactory(new PropertyValueFactory<>("expected_fee"));
        colLastPayment.setCellValueFactory(new PropertyValueFactory<>("last_payment"));


        cmdBatchIds.setValue(" ");
        cmdMonth.getItems().addAll(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        );

        loadBatchIds();

        try {

            loadAllPayments();

            setPaymentId();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmdBatchIds.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setExpectedFee(newValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


    }

    private void storeValidations() {
        allValidation.put(txtlLastPay, payment);
    }

    private void setFields() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            Statement stm = con.createStatement();
            String query = "SELECT * FROM payment  WHERE Payment_id='" + txtPaymentId.getText() + "'";

            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                cmdBatchIds.setValue(set.getString(2));
                cmdMonth.setValue(set.getString(3));
                txtExpectedPay.setText(String.valueOf(set.getInt(4)));
                txtlLastPay.setText(String.valueOf(set.getInt(5)));
            } else {
                new Alert().ErrMsg("Something went wrong..Try Again");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setExpectedFee(Object newValue) throws SQLException, ClassNotFoundException {
        int expected_fee = 0;
        int fee = 2000;
        int i = 0;
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM student WHERE Batch_id ='" + cmdBatchIds.getValue() + "'").executeQuery();

        while (rst.next()) {
            i++;
        }
        expected_fee = 2000 * i;
        txtExpectedPay.setText(String.valueOf(expected_fee));

    }

    public void setPaymentId() throws SQLException, ClassNotFoundException {
        txtPaymentId.setText(getPaymentID());
    }

    private void loadBatchIds() {
        try {

            List<String> batchIds = new AddNewBatchFormController().getAllBatchIds();
            cmdBatchIds.getItems().addAll(batchIds);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPaymentID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Payment_id FROM payment ORDER BY Payment_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "P-00" + tempId;
            } else {
                return tempId < 99 ? "P-0" + tempId : "P-" + tempId;
            }
        } else {
            return "P-001";
        }

    }

    public boolean addBatchPayment(Payment p) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO payment VALUES(?,?,?,?,?)");
        stm.setObject(1, p.getPayment_id());
        stm.setObject(2, p.getBatch_id());
        stm.setObject(3, p.getMonth());
        stm.setObject(4, p.getExpected_fee());
        stm.setObject(5, p.getLast_payment());

        return stm.executeUpdate() > 0;
    }

    public void btnPayOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int expect_Fee = Integer.parseInt(txtExpectedPay.getText());
        int last_Fee = Integer.parseInt(txtlLastPay.getText());

        if (expect_Fee < last_Fee) {
            new Alert().ErrMsg("Invalid Value..");
            txtlLastPay.requestFocus();
        } else {
            Payment p1 = new Payment(
                    txtPaymentId.getText(), cmdBatchIds.getValue().toString(),
                    cmdMonth.getValue().toString(), Integer.parseInt(txtExpectedPay.getText()),
                    Integer.parseInt(txtlLastPay.getText()));

            if (addBatchPayment(p1)) {
                new Alert().confirmationMsg("New payment saved..");
                setPaymentId();

            } else {
                new Alert().ErrMsg("Something went wrong , Try again !!");
            }

            loadAllPayments();
        }

    }

    private void loadAllPayments() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM payment");
        ResultSet rst = stm.executeQuery();
        ObservableList<paymentTm> observableList = FXCollections.observableArrayList();
        while (rst.next()) {
            observableList.add(new paymentTm(rst.getString(1), rst.getString(2),
                    rst.getString(3), rst.getInt(4), rst.getInt(5)));
        }
        tblPayment.setItems(observableList);

    }

    public boolean deletePayment(String payment_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM payment WHERE Payment_id='" + payment_id + "'").executeUpdate() > 0;
    }

    public void clear() {
        cmdBatchIds.setValue(" ");
        cmdMonth.setValue(" ");
        txtlLastPay.clear();
        txtExpectedPay.clear();
        txtPaymentId.requestFocus();
    }

    public void brnRemoveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        paymentTm paymentTm = tblPayment.getSelectionModel().getSelectedItem();
        String paymentId = paymentTm.getPayment_id();
        if (deletePayment(paymentId)) {
            loadAllPayments();
            tblPayment.refresh();
            new Alert().ErrMsg("Do you want to delete selected Payment");
            setPaymentId();
        } else {
            new Alert().ErrMsg("Something went wrong, Try again");
        }
    }

    public void txtUpdateOnAction(ActionEvent actionEvent) {
        setFields();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempId = txtPaymentId.getText();
        String tempBatchId = cmdBatchIds.getValue().toString();
        String tempMonth = cmdMonth.getValue().toString();
        int tempExFee = Integer.parseInt(txtExpectedPay.getText());
        int tempLastFee = Integer.parseInt(txtlLastPay.getText());

        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE payment SET Batch_id=?, Month=?, Expected_fee=?, Last_payment=? WHERE Payment_id=?");

        stm.setObject(1, tempBatchId);
        stm.setObject(2, tempMonth);
        stm.setObject(3, tempExFee);
        stm.setObject(4, tempLastFee);
        stm.setObject(5,tempId);

        if (stm.executeUpdate() > 0) {
            new Alert().confirmationMsg("Payment Updated successfully..");
            loadAllPayments();
            setPaymentId();
        } else {
            new Alert().ErrMsg("Something went wrong..Try Again");
        }
        clear();

    }

    public void btnPrintBillOnClicked(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/BatchPayment.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            String paymentId = txtPaymentId.getText();
            String batchId = cmdBatchIds.getValue().toString();
            String month = cmdMonth.getValue().toString();
            int expectedFee = Integer.parseInt(txtExpectedPay.getText());
            int payment = Integer.parseInt(txtlLastPay.getText());


            HashMap map = new HashMap();
            map.put("paymentId", paymentId);
            map.put("batchId", batchId);
            map.put("month", month);
            map.put("expectedFee", expectedFee);
            map.put("payment", payment);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        }

        clear();

    }

    public void textFieldKeyReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnPay);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Done");
            }
        }
    }
}


