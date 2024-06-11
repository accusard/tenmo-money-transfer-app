package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();


    public void createAccount(AuthenticatedUser user, double startingBalance) {
        if(user != null) {
            String userName = user.getUser().getUsername();
            String url = API_BASE_URL + userName + "/balance";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(user.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Double> entity = new HttpEntity<>(startingBalance, headers); // Set initial balance to 1000 TE Bucks
            try {
                ResponseEntity<AuthenticatedUser> response = restTemplate.exchange(url, HttpMethod.PUT, entity, AuthenticatedUser.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    System.out.println("Registration successful. You can now login.");
                } else {
                    System.out.println("Failed to set initial balance. Status code: " + response.getStatusCodeValue());
                }
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
        }
    }
    public BigDecimal getBalance(AuthenticatedUser user) {
        String url = API_BASE_URL + "account/" + user.getUser().getId() + "/balance";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
}
