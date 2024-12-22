package com.main.repository;

import com.main.entity.Roles;
import com.main.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // Setup: Tạo dữ liệu
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("12345");
        user.setFullname("Test User");
        user.setAcctive(true);
        userRepository.save(user);

        // Kiểm tra phương thức
        Optional<Users> result = userRepository.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getFullname());
    }


    @Test
    public void testFindById() {
        // Setup: Tạo dữ liệu
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("12345");
        user.setFullname("Test User");
        userRepository.save(user);

        // Kiểm tra phương thức
        Users result = userRepository.findById(user.getId());
        assertNotNull(result);
        assertEquals("Test User", result.getFullname());
    }

    @Test
    public void testFindByPhoneNumber() {
        // Setup: Tạo một người dùng với số điện thoại cụ thể
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("12345");
        user.setFullname("Test User");
        user.setPhoneNumber("0123456789");
        user.setAcctive(true);
        userRepository.save(user);

        // Kiểm tra phương thức
        Users foundUser = userRepository.findByPhoneNumber("0123456789");

        // Xác nhận kết quả
        assertNotNull(foundUser, "User should not be null");
        assertEquals("Test User", foundUser.getFullname(), "Fullname should match");
        assertEquals("0123456789", foundUser.getPhoneNumber(), "Phone number should match");
    }


}
