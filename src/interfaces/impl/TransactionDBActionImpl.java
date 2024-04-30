package interfaces.impl;

import db.DBConnection;
import interfaces.TransactionDBAction;
import models.Patient;
import models.Transaction;
import models.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDBActionImpl implements TransactionDBAction {
    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();

        try {
            Connection conn = DBConnection.connectToDB();

            String query = "SELECT * FROM transaction_records " +
                    "JOIN transaction_types ON transaction_records.transaction_type_id = transaction_types.id " +
                    "JOIN patients ON transaction_records.patient_id = patients.id";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                Transaction transaction = new Transaction();
                setTransaction(transaction, result);

                transactions.add(transaction);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    @Override
    public Transaction get(int id) {
        Transaction transaction = new Transaction();

        try {
            Connection conn = DBConnection.connectToDB();

            String query = "SELECT * FROM transaction_records " +
                    "JOIN transaction_types ON transaction_records.transaction_type_id = transaction_types.id " +
                    "JOIN patients ON transaction_records.patient_id = patients.id " +
                    "WHERE transaction_records.id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                setTransaction(transaction, result);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transaction;
    }

    private void setTransaction(Transaction transaction, ResultSet result) throws SQLException {
        TransactionType transactionType = new TransactionType();
        Patient patient = new Patient();

        transaction.setId(result.getInt("transaction_records.id"));
        transaction.setRemarks(result.getString("remarks"));
        transaction.setFindings(result.getString("findings"));
        transaction.setDate(result.getDate("date").toString());
        transactionType.setId(result.getInt("transaction_type_id"));
        transactionType.setType(result.getString("type"));
        patient.setId(result.getInt("patient_id"));
        patient.setFirstName(result.getString("first_name"));
        patient.setMiddleName(result.getString("middle_name"));
        patient.setLastName(result.getString("last_name"));
        patient.setDateOfBirth(result.getDate("date_of_birth").toString());
        patient.setPhoneNumber(result.getString("phone_number"));
        patient.setAge(result.getInt("age"));

        transaction.setTransactionType(transactionType);
        transaction.setPatient(patient);
    }

    @Override
    public Transaction create(Transaction transaction) {
        Connection conn = DBConnection.connectToDB();
        try {
            conn.setAutoCommit(false);

            Patient patient = transaction.getPatient();
            patient.setId(addPatient(patient, conn));

            if (patient.getId() == 0) {
                conn.rollback();
                return null;
            }

            String query = "INSERT INTO transaction_records VALUES(null, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, transaction.getTransactionType().getId());
            stmt.setInt(2, patient.getId());
            stmt.setString(3, transaction.getRemarks());
            stmt.setString(4, transaction.getFindings());
            stmt.setString(5, transaction.getDate());

            if (stmt.executeUpdate() == 0) {
                conn.rollback();
                return null;
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                conn.commit();
                return get(generatedKeys.getInt(1));
            } else {
                return null;
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private int addPatient(Patient patient, Connection conn) throws SQLException {
        String query = "INSERT INTO patients VALUES(null, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, patient.getFirstName());
        stmt.setString(2, patient.getMiddleName());
        stmt.setString(3, patient.getLastName());
        stmt.setString(4, patient.getDateOfBirth());
        stmt.setString(5, patient.getPhoneNumber());
        stmt.setInt(6, patient.getAge());

        if (stmt.executeUpdate() == 0) {
            return 0;
        }

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if(generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            return 0;
        }
    }

    @Override
    public boolean update(Transaction transaction, boolean newPatient) {
        Connection conn = DBConnection.connectToDB();
        try {
            conn.setAutoCommit(false);

            String query = "UPDATE transaction_records SET transaction_type_id = ?, patient_id = ?, remarks = ?, findings = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, transaction.getTransactionType().getId());
            if(newPatient) {
                stmt.setInt(2, addPatient(transaction.getPatient(), conn));
            }else {
                stmt.setInt(2, transaction.getPatient().getId());
            }
            stmt.setString(3, transaction.getRemarks());
            stmt.setString(4, transaction.getFindings());

            if(stmt.executeUpdate() == 1) {
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            Connection conn = DBConnection.connectToDB();

            String query = "DELETE FROM transaction_records WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            if (stmt.executeUpdate() == 1) {
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
