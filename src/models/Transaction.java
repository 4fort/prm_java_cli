package models;

import java.util.Date;

public class Transaction extends GenericObjectModel{
    private TransactionType type;
    private Patient patient;
    private String remarks;
    private String findings;
    private String date;

    public TransactionType getTransactionType() {
        return type;
    }

    public void setTransactionType(TransactionType type) {
        this.type = type;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
