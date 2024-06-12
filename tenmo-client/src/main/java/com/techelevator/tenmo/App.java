package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

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
                viewAccounts();
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

        ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, makeEntityForCurrentUser(), BigDecimal.class);
        BigDecimal balance = response.getBody();
        System.out.println("Your current account balance is: $" + balance);

	}

	private void viewTransferHistory() {
		ResponseEntity<TransferRequest[]> response = restTemplate.exchange(API_BASE_URL + "transfers/list", HttpMethod.GET, makeEntityForCurrentUser(), TransferRequest[].class);
        consoleService.printTransfersList(List.of(response.getBody()));

	}

    private void viewTransferDetail() {
        int transferId = -1;

        do {
            transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            try {
                ResponseEntity<TransferRequest> response = restTemplate.exchange(API_BASE_URL  + "transfers/?id=" + transferId, HttpMethod.GET, makeEntityForCurrentUser(), TransferRequest.class);
                TransferRequest body = response.getBody();

                if(body != null) {
                    consoleService.printTransferDetails(body);
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
            ResponseEntity<TransferRequest[]> response = restTemplate.exchange(API_BASE_URL + "transfers/pending", HttpMethod.GET, makeEntityForCurrentUser(), TransferRequest[].class);
            TransferRequest[] pendingRequests = response.getBody();
            if (pendingRequests != null && pendingRequests.length > 0) {
                consoleService.printTransfersList(List.of(pendingRequests));
            } else {
                System.out.println("No pending requests.");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            consoleService.printErrorMessage();
        }
    }

	private void sendBucks() {

        int accountToId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount you want to send: ");
        TransferRequest transferRequest = new TransferRequest(amount, accountToId);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(API_BASE_URL+"send", HttpMethod.POST, makeEntityForTransfer(transferRequest), Void.class);

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
                TransferRequest transferRequest = new TransferRequest();

                ResponseEntity<Void> responseEntity = restTemplate.exchange(API_BASE_URL + "request", HttpMethod.POST, makeEntityForTransfer(transferRequest), Void.class);
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

    private void viewAccounts() {

        ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL+"accounts", HttpMethod.GET, makeEntityForCurrentUser(), Account[].class);
        List<Account> accounts = List.of(response.getBody());
        consoleService.printUsers(accounts);
    }

    private HttpEntity<String> makeEntityForCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        String token = currentUser.getToken();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferRequest> makeEntityForTransfer(TransferRequest tranferRequest) {
        HttpHeaders headers = new HttpHeaders();
        String token = currentUser.getToken();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(tranferRequest, headers);
    }
}
