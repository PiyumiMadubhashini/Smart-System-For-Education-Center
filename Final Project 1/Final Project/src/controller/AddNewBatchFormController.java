
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
import model.Batch;
import view.tm.batchTm;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AddNewBatchFormController implements BatchManagement {

    public TextField txtYear;
    public TextField txtName;
    public Label showNewBatch;
    public TableColumn colBatchId;
    public TableColumn colBatchName;
    public TableView<batchTm> tblBatch;
    public TextField txtNameTwo;
    public ComboBox cmdBatchId;
    public Button btnAdd;

    LinkedHashMap<TextField , Pattern> allValidation = new LinkedHashMap<>();
    Pattern BatchYear = Pattern.compile("^[A-Z]{1}[-]?[0-9]{4}$");
    Pattern BatchName = Pattern.compile("^[0-9]{4}[-][A-z]{3,8}$");



    public void initialize() throws SQLException, ClassNotFoundException {
        storeValidations();
        btnAdd.setDisable(true);

        tblBatch.refresh();
        showNewBatch.setText("New Added Batch");
        colBatchId.setCellValueFactory(new PropertyValueFactory<>("batch_id"));
        colBatchName.setCellValueFactory(new PropertyValueFactory<>("batch_name"));
        loadAllBatch();
        loadBatchIds();

        cmdBatchId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setUpdateData(newValue);
        });

    }

    private void storeValidations() {
        allValidation.put(txtYear, BatchYear);
        allValidation.put(txtName, BatchName);

    }

    private void setUpdateData(Object newValue) {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            Statement stm = con.createStatement();
            String query = "SELECT * FROM Batch WHERE batch_id ='" + cmdBatchId.getValue() + "'";


            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                txtNameTwo.setText(set.getString(2));

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean addBatch(Batch b) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO Batch VALUES(?,?)");
        stm.setObject(1,b.getBatch_id());
        stm.setObject(2,b.getBatch_name());

        return stm.executeUpdate()>0 ;
    }

    public void AddBatch(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Batch b1 = new Batch(
                txtYear.getText(), txtName.getText());

        if (addBatch(b1)) {
            new Alert().confirmationMsg("Batch Added Successfully");
        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
        showNewBatch.setText("New Added Batch");
        clear();
        loadAllBatch();
    }

    public void loadAllBatch() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM Batch");
        ResultSet rst = stm.executeQuery();
        ObservableList<batchTm>observableList= FXCollections.observableArrayList();
        while (rst.next()){
            observableList.add(new batchTm(rst.getString(1),rst.getString(2)));
        }
        tblBatch.setItems(observableList);
    }


    @Override
    public boolean deleteBatch(String batch_id) throws SQLException, ClassNotFoundException {

        return DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM batch WHERE Batch_id='"+batch_id+"'").executeUpdate() > 0 ;
    }

    @Override
    public List<String> getAllBatchIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM batch").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(rst.getString(1));
        }
        return ids;
    }

    private void loadBatchIds() throws SQLException, ClassNotFoundException {
        List<String> BatchId = new AddNewBatchFormController().getAllBatchIds();
        cmdBatchId.getItems().addAll(BatchId);
    }

    void clear() {
        txtYear.clear();
        txtName.clear();
    }

    public void btnRemoveBatchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        batchTm batch = tblBatch.getSelectionModel().getSelectedItem();
        String batchId = batch.getBatch_id();
        if (deleteBatch(batchId)) {
            loadAllBatch();
            tblBatch.refresh();
            new Alert().ErrMsg("Selected Batch is deleted");
        }else {
            new Alert().ErrMsg("Something went wrong , Try again !!");
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String tempId = cmdBatchId.getValue().toString();
        String tempName = txtNameTwo.getText();

        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("UPDATE batch SET Batch_name=? WHERE Batch_id=?");

        stm.setObject(1, tempName);
        stm.setObject(2,tempId);

        if (stm.executeUpdate() > 0) {
            new Alert().confirmationMsg("Batch Updated successfully..");
            loadAllBatch();
            cmdBatchId.setValue(" ");
            txtNameTwo.clear();
        } else {
            new Alert().ErrMsg("Something went wrong..Try Again");
        }
        txtNameTwo.clear();
        cmdBatchId.setValue(" ");
    }

    public void displayBatchName(ActionEvent actionEvent) {
        String name = txtName.getText();

        showNewBatch.setText("Batch Name :"+" "+name);
    }

    public void textFieldsOnKeyReleased(KeyEvent keyEvent) {
        Object response = Validation.validate(allValidation, btnAdd);

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