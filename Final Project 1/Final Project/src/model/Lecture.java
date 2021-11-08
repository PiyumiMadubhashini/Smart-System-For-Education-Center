package model;

public class Lecture {
    private String lecture_id ;
    private String lecture_name ;
    private int seats ;

    public Lecture() {
    }

    public Lecture(String lecture_id, String lecture_name, int seats) {
        this.lecture_id = lecture_id;
        this.lecture_name = lecture_name;
        this.seats = seats;
    }

    public String getLecture_id() {
        return lecture_id;
    }

    public void setLecture_id(String lecture_id) {
        this.lecture_id = lecture_id;
    }

    public String getLecture_name() {
        return lecture_name;
    }

    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "lecture_id='" + lecture_id + '\'' +
                ", lecture_name='" + lecture_name + '\'' +
                ", seats=" + seats +
                '}';
    }
}
