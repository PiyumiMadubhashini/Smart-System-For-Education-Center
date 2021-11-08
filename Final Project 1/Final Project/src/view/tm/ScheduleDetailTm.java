package view.tm;

public class ScheduleDetailTm {
    private String Number_of_Schedule ;
    private String Lecture_id ;
    private String Teacher_id ;
    private String Batch_id ;
    private String Date_time ;

    public ScheduleDetailTm() {
    }

    public ScheduleDetailTm(String number_of_Schedule, String lecture_id, String teacher_id, String batch_id, String date_time) {
        Number_of_Schedule = number_of_Schedule;
        Lecture_id = lecture_id;
        Teacher_id = teacher_id;
        Batch_id = batch_id;
        Date_time = date_time;
    }

    public String getNumber_of_Schedule() {
        return Number_of_Schedule;
    }

    public void setNumber_of_Schedule(String number_of_Schedule) {
        Number_of_Schedule = number_of_Schedule;
    }

    public String getLecture_id() {
        return Lecture_id;
    }

    public void setLecture_id(String lecture_id) {
        Lecture_id = lecture_id;
    }

    public String getTeacher_id() {
        return Teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        Teacher_id = teacher_id;
    }

    public String getBatch_id() {
        return Batch_id;
    }

    public void setBatch_id(String batch_id) {
        Batch_id = batch_id;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    @Override
    public String toString() {
        return "ScheduleDetailTm{" +
                "Number_of_Schedule='" + Number_of_Schedule + '\'' +
                ", Lecture_id='" + Lecture_id + '\'' +
                ", Teacher_id='" + Teacher_id + '\'' +
                ", Batch_id='" + Batch_id + '\'' +
                ", Date_time='" + Date_time + '\'' +
                '}';
    }
}
