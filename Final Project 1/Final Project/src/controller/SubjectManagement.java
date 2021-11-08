package controller;

import model.Subject;

import java.sql.SQLException;
import java.util.List;

public interface SubjectManagement  {
    public boolean addSubject(Subject s) throws SQLException, ClassNotFoundException;
    public boolean deleteSubject(String subject_id) throws SQLException, ClassNotFoundException;
    public int showSubjectCount() throws SQLException, ClassNotFoundException;
    public List<String> getAllSubjectName() throws SQLException, ClassNotFoundException;
}
