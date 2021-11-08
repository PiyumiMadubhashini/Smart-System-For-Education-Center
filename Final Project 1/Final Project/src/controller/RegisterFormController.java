package controller;

import Util.Validation;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.RegisterFee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterFormController implements RegistrationService {

    public Button btnSave;
    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern amount = Pattern.compile("^[0-9]{3,10}$");

    public TextField txtRegId;
    public TextField txtSub;
    public TextField txtAmount;
    public ComboBox cmdStudentId;
    public Label lblRegId;
    public ComboBox cmdSubName;


    public void initialize() {
        storeValidations();
        btnSave.setDisable(true);

        try {
            setRegId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {

            loadStudentIds();
            loadSubName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void storeValidations() {
        allValidation.put(txtAmount, amount);
    }

    public void setRegId() throws SQLException, ClassNotFoundException {
        String regID = getRegID();
        lblRegId.setText(regID);
        txtRegId.setText(regID);
    }

    public String getRegID() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT Register_id  FROM register_fee ORDER BY Register_id DESC LIMIT 1").executeQuery();
        if (rst.next()) {
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            ++tempId;
            if (tempId <= 9) {
                return "R-00" + tempId;
            } else {
                return tempId < 99 ? "R-0" + tempId : "R-" + tempId;
            }
        } else {
            return "R-001";
        }

    }

    private void loadStudentIds() throws SQLException, ClassNotFoundException {
        List<String> StudentId = new AddNewStudentFormController().getAllStudentIds();
        cmdStudentId.getItems().addAll(StudentId);

    }

    private void loadSubName() throws SQLException, ClassNotFoundException {
        List<String> SubName = new SubjectManagementFormController().getAllSubjectName();
        cmdSubName.getItems().addAll(SubName);

    }

    public boolean addRegStudent(RegisterFee r) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO register_fee VALUES(?,?,?,?)");
        stm.setObject(1, r.getRegister_id());
        stm.setObject(2, r.getStudent_id());
        stm.setObject(3, r.getSubject());
        stm.setObject(4, r.getAmount());

        return stm.executeUpdate() > 0;
    }

    public void btnSaveTo(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        RegisterFee r1 = new RegisterFee(txtRegId.getText(), cmdStudentId.getValue().toString(),
                cmdSubName.getValue().toString(), Double.parseDouble(txtAmount.getText()));

        if (addRegStudent(r1)) {
            new controller.Alert().confirmationMsg("Success..");



        } else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }

        setRegId();

    }

    public int getAllTot() throws SQLException, ClassNotFoundException {

        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM register_fee").executeQuery();
        int tot = 0 ;
        while (rst.next()){
            int amount = Integer.parseInt(rst.getString(4));
            tot=tot+amount;
        }
        return tot ;
    }

    public void clear(){
        txtRegId.clear();
        cmdSubName.setValue(" ");
        txtAmount.clear();
        cmdStudentId.setValue(" ");
    }

    public void btnPrintBillOnClicked(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/Admission.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            String RegId = txtRegId.getText();
            String studentId = cmdStudentId.getValue().toString();
            String subject = cmdSubName.getValue().toString();
            double amount = Double.parseDouble(txtAmount.getText());


            HashMap map = new HashMap();
            map.put("RegId", RegId);
            map.put("studentId", studentId);
            map.put("subject", subject);
            map.put("amount", amount);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);


        } catch (JRException e) {
            e.printStackTrace();
        }
        clear();

    }

    public void textFieldsKeyReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnSave);

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





