package interfaces.impl;

import interfaces.TransactionAction;
import models.Transaction;

public class TransactionActionImpl implements TransactionAction {

    @Override
    public void displayDetails(Transaction transaction) {
        System.out.println("Transaction ID: " + transaction.getId());
        System.out.println("Transaction Type: " + transaction.getTransactionType().getType());
        System.out.println("Patient ID: " + transaction.getPatient().getId());
        System.out.println("Patient Name: " + transaction.getPatient().getFirstName() + " " + transaction.getPatient().getMiddleName() + " " + transaction.getPatient().getLastName());
        System.out.println("Patient Age: " + transaction.getPatient().getAge());
        System.out.println("Patient Phone Number: " + transaction.getPatient().getPhoneNumber());
        System.out.println("Patient Date of Birth: " + transaction.getPatient().getDateOfBirth());
        System.out.println("Findings: " + transaction.getFindings());
        System.out.println("Remarks: " + transaction.getRemarks());
        System.out.println("Transaction Date: " + transaction.getDate());
        System.out.println("#-------------------------------------------#");
    }
}
