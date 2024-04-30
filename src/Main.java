import interfaces.TransactionAction;
import interfaces.impl.TransactionActionImpl;
import interfaces.impl.TransactionDBActionImpl;
import interfaces.impl.TransactionTypesDBActionImpl;
import models.Patient;
import models.Transaction;
import models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        while(true){
            System.out.println("Welcome to Patient Record Management System");
            System.out.println("1. Add Patient Record");
            System.out.println("2. Update Patient Record");
            System.out.println("3. Delete Patient Record");
            System.out.println("4. View Patient Record");
            System.out.println("5. View All Patient Record");

            System.out.print("> ");

            switch(scanner.nextLine()){
                case "1":
                    addPatientRecord();
                    break;
                case "2":
                    updatePatientRecord();
                    break;
                case "3":
                    deletePatientRecord();
                    break;
                case "4":
                    viewPatientRecord();
                    break;
                case "5":
                    viewAllPatientRecord();
                    break;
                default:
                    System.out.println("\n\n\nSelect only from options below");
                    continue;
            }

            if(isQuit()){break;}
        }
    }

    public static void addPatientRecord() throws Exception {
        try {
            do {
                Transaction transaction = new Transaction();
                TransactionType transactionType = new TransactionType();
                Patient patient = new Patient();

                displayTransactionTypes(transactionType);
                System.out.println("Add New Patient Record");

                scanPatientDetails(patient);

                System.out.print("Enter Patient Remarks: ");
                transaction.setRemarks(scanner.next());
                System.out.print("Enter Patient Findings: ");
                transaction.setFindings(scanner.next());

                transaction.setTransactionType(transactionType);
                transaction.setPatient(patient);
                transaction.setDate(LocalDateTime.now().toString());

                TransactionDBActionImpl transactionDBActionImpl = new TransactionDBActionImpl();
                Transaction createdTransaction = transactionDBActionImpl.create(transaction);
                if (createdTransaction != null) {
                    TransactionAction transactionAction = new TransactionActionImpl();
                    System.out.println("\n\nPatient Record Added Successfully!");
                    transactionAction.displayDetails(createdTransaction);
                    scanner.nextLine();
                } else {
                    System.out.println("Failed to add Patient Record");
                    scanner.nextLine();
                }

            } while (!isQuit());
        } catch (Exception e) {
            System.out.println("Something went wrong, please try again. ");
            System.out.println(e.getMessage());
            main(null);
        }
    }

    public static void updatePatientRecord() throws Exception {
        TransactionDBActionImpl transactionDBActionImpl = new TransactionDBActionImpl();
        try {
            do {
                Transaction transaction = new Transaction();
                TransactionType transactionType = new TransactionType();
                Patient patient = new Patient();
                boolean newPatient;

                displayTransactionTypes(transactionType);
                System.out.println("Update Existing Patient Record");
                System.out.println("Display Patient Records? [Y/N]");
                scanner.nextLine();
                System.out.print("> ");
                if (scanner.nextLine().equalsIgnoreCase("Y")) {
                    viewAllPatientRecord();
                }

                while (true) {
                    System.out.print("Enter Record ID: ");
                    transaction.setId(scanner.nextInt());
                    if(transactionDBActionImpl.verifyTransaction(transaction.getId())) {
                        break;
                    }
                    else {System.out.println("\n\nRecord does not exist");}
                }

                while (true) {
                    try{
                        System.out.println("1. Enter new patient details");
                        System.out.println("2. Select existing patient by ID");
                        System.out.print("> ");

                        int input = scanner.nextInt();
                        if(input < 1 || input > 2) throw new Exception("Invalid Input");
                        else if (input == 1) {
                            scanPatientDetails(patient);
                            newPatient = true;
                            break;
                        } else if (input == 2) {
                            while (true) {
                                System.out.print("Enter Patient ID: ");
                                patient.setId(scanner.nextInt());
                                if(transactionDBActionImpl.verifyPatient(patient.getId())) {
                                    break;
                                }
                                else {System.out.println("\n\nRecord does not exist");}
                            }
                            newPatient = false;
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                System.out.print("Enter Patient Remarks: ");
                transaction.setRemarks(scanner.next());
                System.out.print("Enter Patient Findings: ");
                transaction.setFindings(scanner.next());

                transaction.setTransactionType(transactionType);
                transaction.setPatient(patient);

                Transaction updatedTransaction = transactionDBActionImpl.update(transaction, newPatient);
                if (updatedTransaction != null) {
                    TransactionAction transactionAction = new TransactionActionImpl();
                    System.out.println("\n\nPatient Record Added Successfully!");
                    transactionAction.displayDetails(updatedTransaction);
                    scanner.nextLine();
                } else {
                    System.out.println("Failed to add Patient Record");
                    scanner.nextLine();
                }
            } while (!isQuit());
        } catch (Exception e) {
            main(null);
            throw new Exception(e);
        }
    }



    public static void deletePatientRecord() throws Exception {
        try {
            do {
                System.out.println("Delete Existing Patient Record");
                System.out.println("Display Patient Records? [Y/N]");
                if (scanner.nextLine().equalsIgnoreCase("Y")) {
                    viewAllPatientRecord();
                }

                System.out.print("Enter Record ID: ");
                TransactionDBActionImpl transactionDBActionImpl = new TransactionDBActionImpl();
                if (transactionDBActionImpl.delete(scanner.nextInt())) {
                    System.out.println("Patient Record Deleted Successfully");
                    scanner.nextLine();
                } else {
                    System.out.println("Failed to Delete Patient Record");
                    scanner.nextLine();
                }
            } while (!isQuit());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void viewPatientRecord() throws Exception {
        try {
            System.out.println("\n\nView Patient Record");
            System.out.println("=================================");
            System.out.println("Enter Record ID: ");
            TransactionDBActionImpl transactionDBActionImpl = new TransactionDBActionImpl();
            TransactionAction transactionAction = new TransactionActionImpl();
            transactionAction.displayDetails(transactionDBActionImpl.get(scanner.nextInt()));

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void viewAllPatientRecord() throws Exception {
        try {
            System.out.println("\n\nView All Patient Record");
            System.out.println("=================================");
            TransactionDBActionImpl transactionDBActionImpl = new TransactionDBActionImpl();
            TransactionAction transactionAction = new TransactionActionImpl();
            List<Transaction> transactions = transactionDBActionImpl.getAll();
            for(Transaction transaction : transactions){
                transactionAction.displayDetails(transaction);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void scanPatientDetails(Patient patient) {
        System.out.println("Enter Patient Details:");
        System.out.print("First name: ");
        patient.setFirstName(scanner.next());
        scanner.nextLine();
        System.out.print("Middle name: ");
        patient.setMiddleName(scanner.next());
        scanner.nextLine();
        System.out.print("Last name: ");
        patient.setLastName(scanner.next());
        scanner.nextLine();
        System.out.print("Age: ");
        patient.setAge(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Date of Birth: ");
        patient.setDateOfBirth(scanner.next());
        scanner.nextLine();
        System.out.print("Phone number: ");
        patient.setPhoneNumber(scanner.next());
    }

    private static boolean isQuit() {
        System.out.println("Do you want to continue? [Y/N]");
        return scanner.nextLine().equalsIgnoreCase("N");
    }

    private static void displayTransactionTypes(TransactionType transactionType) {
        TransactionTypesDBActionImpl transactionTypesImpl = new TransactionTypesDBActionImpl();
        List<TransactionType> transactionTypes = transactionTypesImpl.getAll();

        while(true) {
            try {
                System.out.println("\n\nSelect Transaction Type");
                for (TransactionType type : transactionTypes) {
                    System.out.println(type.getId() + ". " + type.getType());
                }
                System.out.println(transactionTypes.size() + 1 + ". Cancel");

                System.out.print("> ");
                int input = scanner.nextInt();
                if (input == transactionTypes.size() + 1) {
                    scanner.nextLine();
                    System.out.println("Transaction Canceled\n\n");
                    main(null);
                } else if(input < 1 || input > transactionTypes.size()){
                    throw new Exception("Invalid Input");
                } else {
                    transactionType.setId(input);
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}