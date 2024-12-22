package com.main.service;

import com.main.entity.Roles;
import com.main.repository.RoleRepository;
import com.main.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
public class RoleServiceImplTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleService;

    private Roles role;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindAllRoles() {
        List<Roles> rolesList = roleService.findAllRoles();

        assertNotNull(rolesList);
        assertEquals(1, rolesList.size());
        assertEquals("ROLE_USER", rolesList.get(0).getNameRole());
    }

    @Test
    public void testFindByNameRole() {
        Roles foundRole = roleService.findByNameRole("ROLE_USER");

        assertNotNull(foundRole);
        assertEquals("ROLE_USER", foundRole.getNameRole());
    }

    @Test
    public void testSaveRole() {
        Roles newRole = new Roles();
        newRole.setNameRole("ROLE_ADMIN");

        Roles savedRole = roleService.save(newRole);

        assertNotNull(savedRole);
        assertEquals("ROLE_ADMIN", savedRole.getNameRole());
        assertNotNull(savedRole.getId());
    }
}
