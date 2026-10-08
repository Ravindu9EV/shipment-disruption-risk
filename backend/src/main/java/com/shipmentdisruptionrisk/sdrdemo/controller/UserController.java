package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserController {

   private final UserService service;


    public void add(UserRequestDTO data){
        UserResponseDTO res=service.addUser(data);
        System.out.println(new BCryptPasswordEncoder().encode("abc#123D"));
        log.info("Action: "+res);
    }
}
