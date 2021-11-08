package controller;

import model.RegisterFee;

import java.sql.SQLException;

public interface RegistrationService {
    public String getRegID() throws SQLException, ClassNotFoundException;
    public boolean addRegStudent(RegisterFee r) throws SQLException, ClassNotFoundException;
}
