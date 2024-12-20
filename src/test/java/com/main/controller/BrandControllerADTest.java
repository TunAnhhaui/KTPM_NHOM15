package com.main.controller;

import com.main.controller.admin.BrandControllerAD;
import com.main.entity.ProductBrands;
import com.main.service.ProductBrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BrandControllerADTest {

    @Autowired
    private BrandControllerAD brandControllerAD;

    @Autowired
    private ProductBrandService productBrandService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testIsStatusDeleteProductBrandValue() {
        List<String> expectedStatusList = List.of("Đang hoạt động", "Ngưng hoạt động");

        List<String> actualStatusList = brandControllerAD.isStatusDeleteProductBrandValue();

        assertEquals(expectedStatusList, actualStatusList, "Danh sách trạng thái không đúng");
    }
}
