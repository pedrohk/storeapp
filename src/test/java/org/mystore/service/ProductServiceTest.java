package org.mystore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Product;
import org.mystore.model.ProductCategory;
import org.mystore.model.Subcategory;
import org.mystore.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubcategoryService subcategoryService;

    @InjectMocks
    private ProductService productService;

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
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_existingId() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product1));
        Optional<Product> product = productService.getProductById(100L);
        assertTrue(product.isPresent());
        assertEquals("iPhone 15", product.get().getName());
        assertEquals(subcategory1, product.get().getSubcategory());
        verify(productRepository, times(1)).findById(100L);
    }

    @Test
    void getProductById_nonExistingId() {
        when(productRepository.findById(200L)).thenReturn(Optional.empty());
        Optional<Product> product = productService.getProductById(200L);
        assertFalse(product.isPresent());
        verify(productRepository, times(1)).findById(200L);
    }

    @Test
    void createProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        Product createdProduct = productService.createProduct(new Product("iPhone 15", "Latest iPhone", new BigDecimal("999.00"), 100, "IPH15-128", true, subcategory1));
        assertEquals("iPhone 15", createdProduct.getName());
        assertEquals(subcategory1, createdProduct.getSubcategory());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_existingId() {
        Product updatedProduct = new Product("iPhone 16", "Newer iPhone", new BigDecimal("1099.00"), 90, "IPH16-128", true, new Subcategory(10L, "Smartphones", "", category1));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product1));
        when(subcategoryService.getSubcategoryById(10L)).thenReturn(Optional.of(subcategory1));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Optional<Product> result = productService.updateProduct(100L, updatedProduct);

        assertTrue(result.isPresent());
        assertEquals("iPhone 16", result.get().getName());
        assertEquals(new BigDecimal("1099.00"), result.get().getUnitPrice());
        verify(productRepository, times(1)).findById(100L);
        verify(subcategoryService, times(1)).getSubcategoryById(10L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_nonExistingId() {
        Product updatedProduct = new Product("iPhone 16", "Newer iPhone", new BigDecimal("1099.00"), 90, "IPH16-128", true, subcategory1);
        when(productRepository.findById(200L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(200L, updatedProduct);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(200L);
        verify(subcategoryService, never()).getSubcategoryById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_existingId() {
        when(productRepository.existsById(100L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(100L);

        boolean result = productService.deleteProduct(100L);

        assertTrue(result);
        verify(productRepository, times(1)).existsById(100L);
        verify(productRepository, times(1)).deleteById(100L);
    }

    @Test
    void deleteProduct_nonExistingId() {
        when(productRepository.existsById(200L)).thenReturn(false);

        boolean result = productService.deleteProduct(200L);

        assertFalse(result);
        verify(productRepository, times(1)).existsById(200L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
