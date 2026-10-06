package com.shipmentdisruptionrisk.sdrdemo.service;

import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);
    boolean updateUser(UserRequestDTO userRequestDTO);
    boolean deleteById(Long id);
    boolean deleteByUsername(String username);
    UserRequestDTO findByUsername(String username);
    UserRequestDTO findById(Long id);
    List<UserRequestDTO> getAll();
}
