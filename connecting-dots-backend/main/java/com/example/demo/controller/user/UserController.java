package com.example.demo.controller.user;

import com.example.demo.dto.user.UserRegisterRequest;
import com.example.demo.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/api/user/register")
    public ResponseEntity<UserResponse> create(@RequestBody UserRegisterRequest request) {
        System.out.println("u call this");
        System.out.println("request = " + request);
        return null;
    }
}
