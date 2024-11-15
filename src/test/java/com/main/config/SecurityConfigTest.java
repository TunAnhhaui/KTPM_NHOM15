package com.main.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.security.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Autowired
    private MockMvc mockMvc; // Để Spring tự động cấu hình MockMvc

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Không cần MockMvcBuilders.standaloneSetup(securityConfig)
    }

    @Test
    void testPasswordEncoder() {
        // Kiểm tra mã hóa mật khẩu
        passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void testLoginWithJsonBody() throws Exception {
        // Tạo JSON body cho yêu cầu
        String jsonBody = new ObjectMapper().writeValueAsString(Map.of(
                "username", "nhanvien@gmail.com",
                "password", "123123"
        ));

        mockMvc.perform(get("/admin")
                        .contentType("application/json")         // Đặt Content-Type là JSON
                        .content(jsonBody)                       // Truyền JSON body
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Thêm CSRF token nếu CSRF được bật
                .andExpect(status().isOk());             // Kiểm tra trạng thái phản hồi mong đợi
    }

    @Test
    void testAuthorizedAccessWithRole() throws Exception {
        mockMvc.perform(get("/quan-tri/dashboard")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        // Kiểm tra đăng xuất thành công
        mockMvc.perform(logout("/dang-xuat"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/redirect-logout"));
    }

}
