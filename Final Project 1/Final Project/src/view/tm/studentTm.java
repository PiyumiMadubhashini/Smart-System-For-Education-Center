package view.tm;

public class studentTm {
    private String student_id;
    private String first_name;
    private String sure_name;
    private String student_nic;
    private String gender;
    private String telephone_number;
    private String batch_id;


    public studentTm() {
    }

    public studentTm(String student_id, String first_name, String sure_name, String student_nic, String gender, String telephone_number, String batch_id) {
        this.student_id = student_id;
        this.first_name = first_name;
        this.sure_name = sure_name;
        this.student_nic = student_nic;
        this.gender = gender;
        this.telephone_number = telephone_number;
        this.batch_id = batch_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSure_name() {
        return sure_name;
    }

    public void setSure_name(String sure_name) {
        this.sure_name = sure_name;
    }

    public String getStudent_nic() {
        return student_nic;
    }

    public void setStudent_nic(String student_nic) {
        this.student_nic = student_nic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    @Override
    public String toString() {
        return "studentTm{" +
                "student_id='" + student_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", sure_name='" + sure_name + '\'' +
                ", student_nic='" + student_nic + '\'' +
                ", gender='" + gender + '\'' +
                ", telephone_number='" + telephone_number + '\'' +
                ", batch_id='" + batch_id + '\'' +
                '}';
    }
}