package model;

public class Batch {
    private String batch_id ;
    private String batch_name ;

    public Batch(String batch_id, String batch_name) {
        this.batch_id = batch_id;
        this.batch_name = batch_name;
    }

    public Batch() {
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
        return "Batch{" +
                "batch_id='" + batch_id + '\'' +
                ", batch_name='" + batch_name + '\'' +
                '}';
    }
}
