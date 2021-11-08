package model;

public class RegisterFee {
    private String register_id;
    private String student_id ;
    private String subject;
    private double amount;

    public RegisterFee(String register_id, String student_id, String subject, double amount) {
        this.register_id = register_id;
        this.student_id = student_id;
        this.subject = subject;
        this.amount = amount;
    }

    public RegisterFee() {
    }

    public String getRegister_id() {
        return register_id;
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RegisterFee{" +
                "register_id='" + register_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", subject='" + subject + '\'' +
                ", amount=" + amount +
                '}';
    }
}