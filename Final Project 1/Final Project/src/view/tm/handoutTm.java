package view.tm;

public class handoutTm {
    private String handout_id ;
    private int handout_amount ;
    private String date_time ;

    public handoutTm() {
    }

    public handoutTm(String handout_id, int handout_amount, String date_time) {
        this.handout_id = handout_id;
        this.handout_amount = handout_amount;
        this.date_time = date_time;
    }

    public String getHandout_id() {
        return handout_id;
    }

    public void setHandout_id(String handout_id) {
        this.handout_id = handout_id;
    }

    public int getHandout_amount() {
        return handout_amount;
    }

    public void setHandout_amount(int handout_amount) {
        this.handout_amount = handout_amount;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    @Override
    public String toString() {
        return "handoutTm{" +
                "handout_id='" + handout_id + '\'' +
                ", handout_amount=" + handout_amount +
                ", date_time='" + date_time + '\'' +
                '}';
    }
}
