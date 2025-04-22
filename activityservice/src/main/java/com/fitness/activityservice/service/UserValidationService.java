package com.fitness.activityservice.service;


import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidationService {
   private final WebClient userServiceClient;

   public boolean validateUser(String userId){

    try {
        return userServiceClient.get()
                                .uri( "/api/users/validate/{userId}",userId)
                                .retrieve()
                                .bodyToMono(Boolean.class)
                                .block();

    } catch (WebClientResponseException e) {
        if(e.getStatusCode()==HttpStatusCode.valueOf(404))
            throw new RuntimeException("User Not Found"+ userId);
        if (e.getStatusCode()==HttpStatusCode.valueOf(400)) {
            throw new RuntimeException("Invalid Request"+ userId);
            
        }    
        return false;
    }
        
   }
}
