package com.shipmentdisruptionrisk.sdrdemo.mapper;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class UserMapper {
    public UserResponseDTO toDTO(User user){
        return UserResponseDTO.builder()
                .username(user.getUsername())

                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }



    public User toEntity(UserRequestDTO user, String pwd){
        return new User(user.getUsername(),  pwd,user.getEmail(),Roles.USER);
    }
}
