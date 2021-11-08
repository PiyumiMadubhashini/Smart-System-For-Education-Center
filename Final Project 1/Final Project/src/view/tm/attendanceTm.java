package view.tm;

public class attendanceTm {
    private String attendance_id ;
    private String student_id ;
    private String status ;

    public attendanceTm() {
    }

    public attendanceTm(String attendance_id, String student_id, String status) {
        this.attendance_id = attendance_id;
        this.student_id = student_id;
        this.status = status;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "attendance{" +
                "attendance_id='" + attendance_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
