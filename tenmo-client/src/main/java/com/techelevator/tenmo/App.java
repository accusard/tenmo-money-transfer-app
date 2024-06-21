package com.techelevator.tenmo;

import com.techelevator.tenmo.excpetion.InvalidAccountIdException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.util.BasicLogger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AccountService accountService = new AccountService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final RestTemplate restTemplate = new RestTemplate();


    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
                viewTransferDetail();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                consoleService.printUsers(viewAccounts());
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }



    private void viewCurrentBalance() {
        try {

            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, makeEntityForCurrentUser(), BigDecimal.class);
            BigDecimal balance = response.getBody();
            consoleService.printBalance(balance);

        } catch (RestClientResponseException e) {
            BasicLogger.log("HTTP error while retrieving balance: " + e.getMessage());
            System.err.println("An error occurred while retrieving the balance");
        }
	}

	private void viewTransferHistory() {
        TransferRequest[] transfers = accountService.getTransferHistory(currentUser);
        consoleService.printTransfersList(currentUser, accountService, List.of(transfers));

	}

    private void viewTransferDetail() {
        int transferId = -1;

        do {
            transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            try {
                TransferRequest body = accountService.getTransferDetails(currentUser, transferId);

                if(body != null) {
                    consoleService.printTransferDetails(currentUser, accountService, body);
                    consoleService.pause();
                    viewTransferHistory();
                }
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
        } while (transferId > 0);
    }

    private void viewPendingRequests() {
        try {
            ResponseEntity<TransferRequest[]> response = restTemplate.exchange(API_BASE_URL + "account/transfers/pending", HttpMethod.GET, makeEntityForCurrentUser(), TransferRequest[].class);
            TransferRequest[] pendingRequests = response.getBody();
            if (pendingRequests != null && pendingRequests.length > 0) {
                // consoleService.displayPendingTransfers(pendingRequests);
                consoleService.printTransfersList(currentUser, accountService, List.of(pendingRequests));
                handleTransferApprovalOrRejection(pendingRequests);

            } else {
                System.out.println("No pending requests.Please check case 5");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            consoleService.printErrorMessage();
        }
    }
    private void handleTransferApprovalOrRejection(TransferRequest[] pendingRequests) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter transfer ID to approve/reject (0 to cancel): ");
        int transferId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (transferId == 0) {
            System.out.println("Action cancelled.");
            return;
        }

        TransferRequest selectedTransfer = Arrays.stream(pendingRequests)
                .filter(request -> request.getTransferId() == transferId)
                .findFirst()
                .orElse(null);

        if (selectedTransfer == null) {
            System.out.println("Invalid transfer ID. Please try again.");
            return;
        }

        displayApprovalOptions(transferId);
    }

    private void displayApprovalOptions(int transferId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use case 9: Approve or reject pending transfer");
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("-------");
        System.out.print("Please choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                updateTransferStatus(transferId, "approve");
                break;
            case 2:
                updateTransferStatus(transferId, "reject");
                break;
            case 0:
                System.out.println("No action taken.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                displayApprovalOptions(transferId); // Retry if invalid option
                break;
        }
    }
    private void updateTransferStatus(int transferId, String action) {
        try {
            String url = API_BASE_URL + "account/transfers/" + transferId + "/" + action;
            restTemplate.put(url, makeEntityForCurrentUser());
            System.out.println("Transfer " + action + "d successfully.");
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            consoleService.printErrorMessage();
        }
    }


    private void sendBucks() {
        try {
            int accountToId = consoleService.promptForAccountId();
            validateAccountId(accountToId);
            BigDecimal amount = consoleService.promptForAmount();
            TransferRequest transferRequest = new TransferRequest(amount, accountToId);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(API_BASE_URL+"send", HttpMethod.POST, makeEntityForTransfer(transferRequest), Void.class);

        } catch (InvalidAccountIdException e) {
            BasicLogger.log("Invalid Account Id: " + e.getMessage());
            System.err.println(e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(e);
        }

    }

    private void requestBucks() {
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL + "accounts", HttpMethod.GET, makeEntityForCurrentUser(), Account[].class);
            List<Account> accounts = List.of(response.getBody());

            if (accounts != null && !accounts.isEmpty()) {
                consoleService.printUsers(accounts);

                int accountToId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
                if (accountToId == 0) {
                    return;
                }
                BigDecimal amount = consoleService.promptForBigDecimal("Enter amount you want to request: ");
                TransferRequest transferRequest = new TransferRequest(amount, accountToId);
                ResponseEntity<Void> responseEntity = restTemplate.exchange(API_BASE_URL + "account/request", HttpMethod.POST, makeEntityForTransfer(transferRequest), Void.class);
                if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println("Request sent successfully.");
                } else {
                    consoleService.printErrorMessage();
                }
            } else {
                System.out.println("No accounts found.");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            consoleService.printErrorMessage();
        }
    }

    private List<Account> viewAccounts() {
        ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL+"accounts", HttpMethod.GET, makeEntityForCurrentUser(), Account[].class);
        List<Account> accounts = List.of(response.getBody());
        return accounts;
    }

    private void validateAccountId(int accountId) throws InvalidAccountIdException {
        if (!getAccountsIds(viewAccounts()).contains(accountId))
            throw new InvalidAccountIdException("Account Id does not exist");
    }

    private List<Integer> getAccountsIds (List<Account> accounts) {
        return accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toList());
    }

    private HttpEntity<String> makeEntityForCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        String token = currentUser.getToken();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferRequest> makeEntityForTransfer(TransferRequest transferRequest) {
        System.out.println(transferRequest.getAccountToId());
        HttpHeaders headers = new HttpHeaders();
        String token = currentUser.getToken();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(transferRequest, headers);
    }

    private void handleBadRequest(HttpClientErrorException.BadRequest e) {
        String errorMessage = e.getResponseBodyAsString();
        try {
            BasicLogger.log(e.getMessage());
            JSONObject error = new JSONObject(errorMessage);
            String message = error.getString("message");
            System.err.println(message);
        } catch (JSONException e1) {
            BasicLogger.log(e1.getMessage());
        }
    }

}
