package model;

public class Subject {
    private String subject_id ;
    private String subject_name ;
    private String teacher_id ;

    public Subject(String subject_id, String subject_name, String teacher_id) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.teacher_id = teacher_id;
    }

    public Subject() {
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subject_id='" + subject_id + '\'' +
                ", subject_name='" + subject_name + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                '}';
    }
}
