package com.shipmentdisruptionrisk.sdrdemo.mapper;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.Utility.Status;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.UserResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class UserMapperTest {

    private UserMapper mapper;
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp(){
        mapper=new UserMapper();
    }

    @Test
    void toEntity_shouldMapAllFields(){

        encoder=new BCryptPasswordEncoder();
//        public User=
        String pwd=    encoder.encode("abc123@D");

        UserRequestDTO dto=new UserRequestDTO();
        dto.setUsername("abed");
        dto.setEmail("abed@gmail.com");
        dto.setPassword("abc123@D");

        User user=mapper.toEntity(dto,pwd);

        assertEquals("abed",user.getUsername());
        assertEquals(pwd,user.getPassword());
        assertEquals("abed@gmail.com",user.getEmail());
        assertEquals(Roles.USER,user.getRole());
        assertEquals(Status.ACTIVE, user.getStatus());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getId());
    }



    void toDTOTest(){

        User u=User.builder().username("abc")
                .password(encoder.encode("abC#345d"))
                .email("abc@gmail.com").build();

        UserResponseDTO dto=mapper.toDTO(u);

        assertEquals("abc",dto.getUsername());
        assertEquals("abc@gmail.com",dto.getEmail());
        assertEquals(Roles.USER,dto.getRole());
        assertNotNull(dto.getCreatedAt());
        assertEquals(Status.ACTIVE,dto.getStatus());
    }

}
