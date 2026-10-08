package com.shipmentdisruptionrisk.sdrdemo.service;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import com.shipmentdisruptionrisk.sdrdemo.mapper.UserMapper;
import com.shipmentdisruptionrisk.sdrdemo.repository.UserRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock private UserRepository repository;
    @Mock private PasswordEncoder encoder;
    @Mock private UserMapper mapper;
    @InjectMocks private UserServiceImpl service;

    @Test
    void addUser_shouldHashPasswordAndPassItToMapper(){

        String pwd="abc#123D";
        String hashed= "12#xsw";
        UserRequestDTO dto=new UserRequestDTO("abc","abc@gmail.com", pwd);
        User user=User.builder().username("abc").password(hashed).email("abc@gmail.com").role(Roles.USER).build();

        when(encoder.encode(pwd)).thenReturn(hashed);
        when(mapper.toEntity(dto,hashed)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(new UserResponseDTO());
        service.addUser(dto);

        verify(encoder,times(1)).encode(pwd);
        verify(mapper,times(1)).toEntity(dto,hashed);
        verify(repository,times(1)).save(user);

    }


    @Test
    void addUser_shouldReturnNullWhenDtoIsNull(){
        UserResponseDTO dto=service.addUser(null);
        assertNull(dto);
        verify(encoder,never()).encode(anyString());
        verify(mapper,never()).toEntity(any(),anyString());
        verify(repository,never()).save(any());
    }

    @Test
    void addUser_shouldReturnResponseDTOFromMapper(){
        String pwd="abc#123D";
        String hashed="12#xsw";
        UserRequestDTO dto=new UserRequestDTO("abc","abc@gmail.com",pwd);
        dto.setRole(Roles.USER);
        User user=User.builder().username("abc").password(hashed).email("abc@gmail.com").role(Roles.USER).build();
        UserResponseDTO response=UserResponseDTO.builder()
                .username("abc")
                        .email("abc@gmail.com")
                                .role(Roles.USER)
                                        .build();
        when(encoder.encode(pwd)).thenReturn(hashed);
        when(mapper.toEntity(dto,hashed)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(response);

        UserResponseDTO result=service.addUser(dto);

        assertNotNull(result);
        assertEquals(response,result);
        assertEquals(response.getUsername(),result.getUsername());
        assertEquals(response.getEmail(),result.getEmail());

        verify(mapper,times(1)).toDTO(user);
    }

}
