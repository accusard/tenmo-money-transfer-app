package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.UserCredentials;


import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    final String LINE = "--------------------------------------";

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void printUsers(List<Account> accounts) {
        System.out.println(LINE);
        System.out.println("Users");
        System.out.printf("%-12s %s\n", "ID", "Name");
        System.out.println(LINE);

        for (Account account : accounts) {
            System.out.printf("%-12d %s\n", account.getAccountId(), account.getName());
        }
        System.out.println(LINE);
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printTransfersList(AuthenticatedUser user, AccountService service, List<TransferRequest> transfers) {
        final String FROM_STRING = "From: ";
        final String TO_STRING = "To: ";

        System.out.println(LINE);
        System.out.println("Transfers");
        System.out.println(String.format("%-10s %-15s %5s", "ID", "From/To", "Amount"));
        System.out.println(LINE);

        for (TransferRequest t : transfers) {
            long id = t.getTransferId();

            StringBuilder typeString = new StringBuilder();
            final String PREFIX = t.getType().equals("Send") ? TO_STRING : FROM_STRING;

            typeString.append(PREFIX);
            typeString.append(service.getUserNameByAccountId(user, t.getAccountToId()));
            BigDecimal amount = t.getAmount();
            System.out.println(String.format("%-10d %-15s $ %.2f", id, typeString, amount));
        }

        System.out.println("-------");
    }

//    public StringBuilder formatSendReceive(TransferRequest t) {
//        // incomplete
//        final String FROM_STRING = "From: ";
//        final String TO_STRING = "To: ";
//
//        StringBuilder typeString = new StringBuilder();
//        String type = t.getType();
//        final String PREFIX = type.equals("Send") ? TO_STRING : FROM_STRING;
//
//        return typeString
//    }

    public void printTransferDetails(AuthenticatedUser user, AccountService service, TransferRequest transfer) {
        System.out.println(LINE);
        System.out.println("Transfer Details");
        System.out.println(LINE);
        String detailsStr = String.format(
                "Id: %d\n" +
                        "From: %s\n" +
                        "To: %s\n" +
                        "Type: %s\n" +
                        "Status: %s\n" +
                        "Amount: $%.2f\n",
                transfer.getTransferId(),
                service.getUserNameByAccountId(user, transfer.getAccountFromId()),
                service.getUserNameByAccountId(user, transfer.getAccountToId()),
                transfer.getType(), transfer.getStatus(), transfer.getAmount());

        System.out.println(detailsStr);
    }

    public void displayPendingTransfers(TransferRequest[] transfers) {
        if (transfers.length == 0) {
            System.out.println("There are no pending transfers.");
        } else {
            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID          To                     Amount");
            System.out.println("-------------------------------------------");
            for (TransferRequest transfer : transfers) {
                System.out.printf("%-12d %-22s $ %.2f%n", transfer.getTransferId(), transfer.getAccountFromId(), transfer.getAmount());
            }
            System.out.println("-------------------------------------------");
        }


        //  // Method for displaying a list of user accounts, prompting for user input for account selection and request amounts, waiting for user input to continue, and handling error messages.

    }
}
