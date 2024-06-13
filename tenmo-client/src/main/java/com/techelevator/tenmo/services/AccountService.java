package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    API_BASE_URL + "balance",
                    HttpMethod.GET,
                    makeAuthEntity(user),
                    BigDecimal.class
            );
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public TransferRequest[] getTransferHistory(AuthenticatedUser user) {
        TransferRequest[] transfers = null;
        try {
            ResponseEntity<TransferRequest[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/",
                    HttpMethod.GET,
                    makeAuthEntity(user),
                    TransferRequest[].class
            );
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public TransferRequest getTransferDetails(AuthenticatedUser user, long transferId) {
        TransferRequest transfer = null;
        try {
            ResponseEntity<TransferRequest> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/" + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(user),
                    TransferRequest.class
            );
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public String getAccountUserNameById(AuthenticatedUser user, int id) {
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                    API_BASE_URL + id,
                    HttpMethod.GET,
                    makeAuthEntity(user),
                    String.class
            );
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return response.getBody();
    }

    public boolean sendTransfer(AuthenticatedUser user, TransferRequest transferRequest) {
        boolean success = false;
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    makeTransferEntity(user, transferRequest),
                    Void.class
            );
            success = response.getStatusCode() == HttpStatus.CREATED;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<String> makeAuthEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferRequest> makeTransferEntity(AuthenticatedUser user, TransferRequest transferRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transferRequest, headers);
    }
}

