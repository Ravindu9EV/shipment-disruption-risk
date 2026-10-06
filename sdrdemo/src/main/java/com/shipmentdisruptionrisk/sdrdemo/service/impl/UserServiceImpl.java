package com.shipmentdisruptionrisk.sdrdemo.service.impl;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import com.shipmentdisruptionrisk.sdrdemo.mapper.UserMapper;
import com.shipmentdisruptionrisk.sdrdemo.repository.UserRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    @Override
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        

        if(userRequestDTO !=null) {




            String pwd=encoder.encode(userRequestDTO.getPassword());
            User user=mapper.toEntity(userRequestDTO,pwd);
            user=repository.save(user);
            UserResponseDTO res=mapper.toDTO(user);
            log.info("Success\n"+res);
            return res;
        }else{
            log.info("Failed to add user");
            return null;
        }

    }

    @Override
    public boolean updateUser(UserRequestDTO userRequestDTO) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return false;
    }

    @Override
    public UserRequestDTO findByUsername(String username) {
        return null;
    }

    @Override
    public UserRequestDTO findById(Long id) {
        return null;
    }

    @Override
    public List<UserRequestDTO> getAll() {
        return List.of();
    }
}
