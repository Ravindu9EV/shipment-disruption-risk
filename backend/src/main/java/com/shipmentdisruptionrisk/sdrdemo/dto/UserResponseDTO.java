package com.shipmentdisruptionrisk.sdrdemo.dto;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.Utility.Status;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Roles role;
    private LocalDateTime createdAt;
    private Status status;

    @Builder
    public UserResponseDTO(Long id,String username,String email,Roles role,LocalDateTime createdAt,Status status){
        this.id=id;
        this.username=username;
        this.email=email;
        this.role=role;
        this.createdAt=createdAt;
        this.status=status;
    }
}
