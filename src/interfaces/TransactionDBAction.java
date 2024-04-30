package interfaces;

import models.Transaction;

import java.util.List;

public interface TransactionDBAction {
    List<Transaction> getAll();
    Transaction get(int id);
    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction, boolean newPatient);
    boolean delete(int id);
    boolean verifyTransaction(int id);
    boolean verifyPatient(int id);
}
