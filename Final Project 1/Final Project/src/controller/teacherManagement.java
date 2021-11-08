package controller;

import model.teacher;

import java.sql.SQLException;
import java.util.List;

public interface teacherManagement {
    public boolean addTeacher(teacher t) throws SQLException, ClassNotFoundException;
    public void loadAllTeacher() throws SQLException, ClassNotFoundException;
    public boolean deleteTeacher(String teacher_id) throws SQLException, ClassNotFoundException;
    public List<String>getAllTeacherIds() throws SQLException, ClassNotFoundException;
}
