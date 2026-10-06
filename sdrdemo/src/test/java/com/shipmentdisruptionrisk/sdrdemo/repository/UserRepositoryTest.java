package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired private UserRepository repository;
    @Autowired private TestEntityManager entityManager;

    @Test
    void SaveAndFindById(){
        String hashed="123#bvD";
        User user=User.builder().username("abc").password(hashed).email("abc@gmail.com").role(Roles.USER).build();
        entityManager.persistAndFlush(user);
        Optional<User> found=repository.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getUsername(),found.get().getUsername());

    }

    @Test
    void fundByUsername_shouldReturnUser(){
        String hashed="123#bvD";
        User user=User.builder().username("abc").password(hashed).email("abc@gmail.com").role(Roles.USER).build();
        entityManager.persistAndFlush(user);
        Optional<User> found=repository.findByUsername(user.getUsername());
        assertTrue(found.isPresent());
        assertEquals(found.get().getEmail(),user.getEmail());
    }

    @Test
    void findByUsernameShouldReturnEmptyWhenNotExists(){
        User user=User.builder().username("abc").password("Ha#12shed").email("abc@gmail.com").role(Roles.USER).build();
        entityManager.persistAndFlush(user);
        Optional<User> found=repository.findByUsername(user.getUsername());

        assertTrue(found.isPresent());
    }

}
