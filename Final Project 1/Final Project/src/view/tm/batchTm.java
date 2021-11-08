package view.tm;

public class batchTm {
    private String batch_id ;
    private String batch_name ;

    public batchTm(String batch_id, String batch_name) {
        this.batch_id = batch_id;
        this.batch_name = batch_name;
    }

    public batchTm() {
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    @Override
    public String toString() {
        return "batchTm{" +
                "batch_id='" + batch_id + '\'' +
                ", batch_name='" + batch_name + '\'' +
                '}';
    }
}
