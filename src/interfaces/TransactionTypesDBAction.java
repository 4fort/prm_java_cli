package interfaces;

import models.TransactionType;

import java.util.List;

public interface TransactionTypesDBAction {
    List<TransactionType> getAll();
}
