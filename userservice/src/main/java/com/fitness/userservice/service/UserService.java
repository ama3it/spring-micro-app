package com.fitness.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public UserResponse register(RegisterRequest request) {

        if(repository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist.");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser=repository.save(user);
        UserResponse userresponse= new UserResponse();
        userresponse.setId(savedUser.getId());
        userresponse.setEmail(savedUser.getEmail());
        userresponse.setPassword(savedUser.getPassword());
        userresponse.setFirstName(savedUser.getFirstName());
        userresponse.setLastName(savedUser.getLastName());
        userresponse.setCreatedAt(savedUser.getCreatedAt());
        userresponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userresponse;

    }

    public UserResponse getUserProfile(String userid) {
        User user=repository.findById(userid)
                  .orElseThrow(()->new RuntimeException("User not found."));

        UserResponse userresponse= new UserResponse();
        userresponse.setId(user.getId());
        userresponse.setEmail(user.getEmail());
        userresponse.setPassword(user.getPassword());
        userresponse.setFirstName(user.getFirstName());
        userresponse.setLastName(user.getLastName());
        userresponse.setCreatedAt(user.getCreatedAt());
        userresponse.setUpdatedAt(user.getUpdatedAt());
        return userresponse;
    }

}
