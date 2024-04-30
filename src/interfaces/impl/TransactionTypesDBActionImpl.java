package interfaces.impl;

import db.DBConnection;
import interfaces.TransactionTypesDBAction;
import models.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionTypesDBActionImpl implements TransactionTypesDBAction {

    @Override
    public List<TransactionType> getAll() {
        List<TransactionType> transactionTypes = new ArrayList<>();

        try {
            Connection conn = DBConnection.connectToDB();

            String query = "SELECT * FROM transaction_types";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                TransactionType transactionType = new TransactionType();

                transactionType.setId(result.getInt("id"));
                transactionType.setType(result.getString("type"));

                transactionTypes.add(transactionType);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactionTypes;
    }
}
