package com.main.service;

import com.main.entity.ProductTypes;
import com.main.repository.ProductTypesRepository;
import com.main.service.ProductTypeService;
import com.main.service.impl.ProductTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductTypeServiceImplTest {

    @Autowired
    private ProductTypesRepository repo;

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    private ProductTypes productType;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testFindAll() {
        productType = new ProductTypes();
        productType.setId(1);
        productType.setProductTypeName("Solar Panels");
        productType.setCategoryId(1);

        repo.save(productType);

        List<ProductTypes> productTypes = productTypeService.findAll();

        assertNotNull(productTypes);
        assertEquals(1, productTypes.size());
        assertEquals("Solar Panels", productTypes.get(0).getProductTypeName());
    }

    @Test
    public void testFindByCategoryId() {
        List<ProductTypes> productTypes = productTypeService.findByCategoryId(1);

        assertNotNull(productTypes);
        assertEquals(1, productTypes.size());
        assertEquals("Solar Panels", productTypes.get(0).getProductTypeName());
    }



}
