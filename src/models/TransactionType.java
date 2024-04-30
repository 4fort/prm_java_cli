package models;

public class TransactionType extends GenericObjectModel{
    private String type;

    public TransactionType(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
