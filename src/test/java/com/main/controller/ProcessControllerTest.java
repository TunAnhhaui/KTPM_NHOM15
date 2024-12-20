package com.main.controller;

import com.main.controller.admin.ProcessController;
import com.main.service.RevenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProcessControllerTest {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ProcessController processController;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void testGenerateEmptyProfitData() {
        List<BigDecimal> emptyProfitData = processController.generateEmptyProfitData();

        assertNotNull(emptyProfitData);
        assertEquals(12, emptyProfitData.size());

        for (BigDecimal value : emptyProfitData) {
            assertEquals(BigDecimal.ZERO, value);
        }
    }

    @Test
    public void testFilterOrdersByUniqueYear() {
        List<Integer> years = processController.filterOrdersByUniqueYear();

        assertNotNull(years);
        assertTrue(years.size() == 0);

        for (Integer year : years) {
            assertTrue(year == 0);
        }
    }
}
