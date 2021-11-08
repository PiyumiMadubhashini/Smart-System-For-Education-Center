package controller;

import model.Batch;
import view.tm.batchTm;

import java.sql.SQLException;
import java.util.List;

public interface BatchManagement {
    public boolean addBatch(Batch b) throws SQLException, ClassNotFoundException;
    public void loadAllBatch() throws SQLException, ClassNotFoundException;
    public boolean deleteBatch(String batch_id) throws SQLException, ClassNotFoundException;
    public List<String>getAllBatchIds() throws SQLException, ClassNotFoundException;
    //public boolean updateBatch(batchTm batch) throws SQLException, ClassNotFoundException;



}
