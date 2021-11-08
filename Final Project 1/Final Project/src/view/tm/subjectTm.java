package view.tm;

public class subjectTm {
    private String subject_id ;
    private String getSubject_name ;
    private String teacher_id ;

    public subjectTm() {
    }

    public subjectTm(String subject_id, String getSubject_name, String teacher_id) {
        this.subject_id = subject_id;
        this.getSubject_name = getSubject_name;
        this.teacher_id = teacher_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getGetSubject_name() {
        return getSubject_name;
    }

    public void setGetSubject_name(String getSubject_name) {
        this.getSubject_name = getSubject_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    @Override
    public String toString() {
        return "subjectTm{" +
                "subject_id='" + subject_id + '\'' +
                ", getSubject_name='" + getSubject_name + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                '}';
    }
}
