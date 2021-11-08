package view.tm;

public class paymentTm {
    private String payment_id ;
    private String batch_id ;
    private String month ;
    private int expected_fee ;
    private int last_payment ;

    public paymentTm() {
    }

    public paymentTm(String payment_id, String batch_id, String month, int expected_fee, int last_payment) {
        this.payment_id = payment_id;
        this.batch_id = batch_id;
        this.month = month;
        this.expected_fee = expected_fee;
        this.last_payment = last_payment;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getExpected_fee() {
        return expected_fee;
    }

    public void setExpected_fee(int expected_fee) {
        this.expected_fee = expected_fee;
    }

    public int getLast_payment() {
        return last_payment;
    }

    public void setLast_payment(int last_payment) {
        this.last_payment = last_payment;
    }

    @Override
    public String toString() {
        return "paymentTm{" +
                "payment_id='" + payment_id + '\'' +
                ", batch_id='" + batch_id + '\'' +
                ", month='" + month + '\'' +
                ", expected_fee=" + expected_fee +
                ", last_payment=" + last_payment +
                '}';
    }
}
