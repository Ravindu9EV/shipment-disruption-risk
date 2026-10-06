package com.shipmentdisruptionrisk.sdrdemo.dto;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.Utility.Status;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    private Long id;
    private String username;

    private String password;
    private String email;
    private Roles role;
    private LocalDateTime createdAt;
    private Status status;

    @Builder
    public UserRequestDTO(String username,String email,String password){

        this.username=username;
        this.password=password;
        this.email=email;
        this.role=Roles.USER;

    }



}
