package com.main.repository;

import com.main.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductCategoriesRepository productCategoriesRepository;

    @Autowired
    private ProductTypesRepository productTypesRepository;

    @Test
    public void testFindAllById() {
        Products product = new Products();
        product.setId("P001");
        product.setProductName("Solar Panel 1");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setQuantity(10);
        productsRepository.save(product);

        List<Products> products = productsRepository.findAllById("P001");

        assertNotNull(products);
        assertEquals(1, products.size(), "Should return exactly one product");
        assertEquals("P001", products.get(0).getId(), "Product ID should match");
    }

    @Test
    public void testFindByCategoryId() {
        // Setup: Tạo sản phẩm và danh mục liên quan
        ProductCategories category = new ProductCategories();
        category.setId(1);
        category.setCategoryName("Solar Products");
        productCategoriesRepository.save(category);

        ProductTypes productType = new ProductTypes();
        productType.setId(1);
        productType.setProductTypeName("Panels");
        productType.setCategoryId(1);
        productTypesRepository.save(productType);

        Products product = new Products();
        product.setId("P002");
        product.setProductName("Solar Panel 2");
        product.setPrice(BigDecimal.valueOf(2000));
        product.setProductTypeId(1);
        productsRepository.save(product);

        List<Object[]> products = productsRepository.findByCategoryId(1);

        assertNotNull(products);
        assertFalse(products.isEmpty(), "Should return at least one product");
        assertEquals("Solar Panel 2", ((Products) products.get(0)[0]).getProductName(), "Product name should match");
    }


    @Test
    @Transactional
    public void testFindByProductTypeId() {
        ProductTypes productType = new ProductTypes();
        productType.setCategoryId(1);
        productType.setProductTypeName("Solar Panels");
        productTypesRepository.save(productType);

        Products product1 = new Products();
        product1.setId("P001");
        product1.setProductName("Solar Panel 1");
        product1.setPrice(BigDecimal.valueOf(1000));
        product1.setProductTypeId(productType.getId());
        productsRepository.save(product1);

        Products product2 = new Products();
        product2.setId("P002");
        product2.setProductName("Solar Panel 2");
        product2.setPrice(BigDecimal.valueOf(1200));
        product2.setProductTypeId(productType.getId());
        productsRepository.save(product2);

        List<Products> products = productsRepository.findByProductTypeId(productType.getId());

        assertNotNull(products, "The result should not be null");
        assertEquals(2, products.size(), "The result should contain 2 products");

        assertTrue(products.stream().allMatch(p -> p.getProductTypeId() == productType.getId()),
                "All products should belong to product type ID 1");

        assertEquals("Solar Panel 1", products.get(0).getProductName(), "Product 1 name should match");
        assertEquals("Solar Panel 2", products.get(1).getProductName(), "Product 2 name should match");
    }

}
