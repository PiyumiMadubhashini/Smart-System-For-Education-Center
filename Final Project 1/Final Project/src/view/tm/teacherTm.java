package view.tm;

public class teacherTm {
    private String teacher_id ;
    private String teacher_firstName;
    private String teacher_sureName ;
    private String teacher_gender ;
    private String teacher_city ;
    private String teacher_qualification ;
    private String teacher_experience;

    public teacherTm() {
    }

    public teacherTm(String teacher_id, String teacher_firstName, String teacher_sureName, String teacher_gender, String teacher_city, String teacher_qualification, String teacher_experience) {
        this.teacher_id = teacher_id;
        this.teacher_firstName = teacher_firstName;
        this.teacher_sureName = teacher_sureName;
        this.teacher_gender = teacher_gender;
        this.teacher_city = teacher_city;
        this.teacher_qualification = teacher_qualification;
        this.teacher_experience = teacher_experience;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setTeacher_firstName(String teacher_firstName) {
        this.teacher_firstName = teacher_firstName;
    }

    public void setTeacher_sureName(String teacher_sureName) {
        this.teacher_sureName = teacher_sureName;
    }

    public void setTeacher_gender(String teacher_gender) {
        this.teacher_gender = teacher_gender;
    }

    public void setTeacher_city(String teacher_city) {
        this.teacher_city = teacher_city;
    }

    public void setTeacher_qualification(String teacher_qualification) {
        this.teacher_qualification = teacher_qualification;
    }

    public void setTeacher_experience(String teacher_experience) {
        this.teacher_experience = teacher_experience;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_firstName() {
        return teacher_firstName;
    }

    public String getTeacher_sureName() {
        return teacher_sureName;
    }

    public String getTeacher_gender() {
        return teacher_gender;
    }

    public String getTeacher_city() {
        return teacher_city;
    }

    public String getTeacher_qualification() {
        return teacher_qualification;
    }

    public String getTeacher_experience() {
        return teacher_experience;
    }

    @Override
    public String toString() {
        return "teacherTm{" +
                "teacher_id='" + teacher_id + '\'' +
                ", teacher_firstName='" + teacher_firstName + '\'' +
                ", teacher_sureName='" + teacher_sureName + '\'' +
                ", teacher_gender='" + teacher_gender + '\'' +
                ", teacher_city='" + teacher_city + '\'' +
                ", teacher_qualification='" + teacher_qualification + '\'' +
                ", teacher_experience='" + teacher_experience + '\'' +
                '}';
    }
}
