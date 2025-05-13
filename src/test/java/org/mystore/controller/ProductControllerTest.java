package org.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Product;
import org.mystore.model.ProductCategory;
import org.mystore.model.Subcategory;
import org.mystore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductCategory category1;
    private Subcategory subcategory1;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        category1 = new ProductCategory("Electronics", "Electronic devices");
        category1.setId(1L);
        subcategory1 = new Subcategory("Smartphones", "Mobile phones", category1);
        subcategory1.setId(10L);
        product1 = new Product("iPhone 15", "Latest iPhone", new BigDecimal("999.00"), 100, "IPH15-128", true, subcategory1);
        product1.setId(100L);
        product2 = new Product("Samsung Galaxy S23", "New Samsung phone", new BigDecimal("899.00"), 150, "SGS23-256", true, subcategory1);
        product2.setId(101L);
    }

    @Test
    void getAllProducts_returnsOkWithProductList() {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));
        ResponseEntity<List<Product>> response = productController.getAllProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_existingId_returnsOkWithProduct() {
        when(productService.getProductById(100L)).thenReturn(Optional.of(product1));
        ResponseEntity<Product> response = productController.getProductById(100L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("iPhone 15", response.getBody().getName());
        verify(productService, times(1)).getProductById(100L);
    }

    @Test
    void getProductById_nonExistingId_returnsNotFound() {
        when(productService.getProductById(200L)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).getProductById(200L);
    }

    @Test
    void createProduct_returnsCreatedWithProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(product1);
        ResponseEntity<Product> response = productController.createProduct(new Product("iPhone 15", "Latest iPhone", new BigDecimal("999.00"), 100, "IPH15-128", true, subcategory1));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("iPhone 15", response.getBody().getName());
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_existingId_returnsOkWithUpdatedProduct() {
        when(productService.updateProduct(eq(100L), any(Product.class))).thenReturn(Optional.of(product1));
        ResponseEntity<Product> response = productController.updateProduct(100L, new Product("iPhone 16", "Newer iPhone", new BigDecimal("1099.00"), 90, "IPH16-128", true, subcategory1));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("iPhone 15", response.getBody().getName()); // Assuming service returns the original updated
        verify(productService, times(1)).updateProduct(eq(100L), any(Product.class));
    }

    @Test
    void updateProduct_nonExistingId_returnsNotFound() {
        when(productService.updateProduct(eq(200L), any(Product.class))).thenReturn(Optional.empty());
        ResponseEntity<Product> response = productController.updateProduct(200L, new Product("iPhone 16", "Newer iPhone", new BigDecimal("1099.00"), 90, "IPH16-128", true, subcategory1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).updateProduct(eq(200L), any(Product.class));
    }

    @Test
    void deleteProduct_existingId_returnsNoContent() {
        when(productService.deleteProduct(100L)).thenReturn(true);
        ResponseEntity<Void> response = productController.deleteProduct(100L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(100L);
    }

    @Test
    void deleteProduct_nonExistingId_returnsNotFound() {
        when(productService.deleteProduct(200L)).thenReturn(false);
        ResponseEntity<Void> response = productController.deleteProduct(200L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(200L);
    }
}
