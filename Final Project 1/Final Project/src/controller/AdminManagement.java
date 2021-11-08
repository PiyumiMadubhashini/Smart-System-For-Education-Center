package controller;

import model.Admin;

import java.sql.SQLException;

public interface AdminManagement {
    public boolean addAdmin(Admin a) throws SQLException, ClassNotFoundException;
}
