package controller;

import model.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentManagement {
    public boolean addStudent(Student s) throws SQLException, ClassNotFoundException;
    public List<String>getAllStudentIds() throws SQLException, ClassNotFoundException;
    public boolean deleteStudent(String batch_id) throws SQLException, ClassNotFoundException;




}
