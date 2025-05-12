package org.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.ProductCategory;
import org.mystore.service.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryControllerTest {

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductCategoryController productCategoryController;

    private ProductCategory category1;
    private ProductCategory category2;

    @BeforeEach
    void setUp() {
        category1 = new ProductCategory("Electronics", "Electronic devices");
        category1.setId(1L);
        category2 = new ProductCategory("Books", "Printed and digital books");
        category2.setId(2L);
    }

    @Test
    void getAllProductCategories_returnsOkWithCategoryList() {
        when(productCategoryService.getAllProductCategories()).thenReturn(Arrays.asList(category1, category2));
        ResponseEntity<List<ProductCategory>> response = productCategoryController.getAllProductCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(productCategoryService, times(1)).getAllProductCategories();
    }

    @Test
    void getProductCategoryById_existingId_returnsOkWithCategory() {
        when(productCategoryService.getProductCategoryById(1L)).thenReturn(Optional.of(category1));
        ResponseEntity<ProductCategory> response = productCategoryController.getProductCategoryById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
        verify(productCategoryService, times(1)).getProductCategoryById(1L);
    }

    @Test
    void getProductCategoryById_nonExistingId_returnsNotFound() {
        when(productCategoryService.getProductCategoryById(100L)).thenReturn(Optional.empty());
        ResponseEntity<ProductCategory> response = productCategoryController.getProductCategoryById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productCategoryService, times(1)).getProductCategoryById(100L);
    }

    @Test
    void createProductCategory_returnsCreatedWithCategory() {
        when(productCategoryService.createProductCategory(any(ProductCategory.class))).thenReturn(category1);
        ResponseEntity<ProductCategory> response = productCategoryController.createProductCategory(new ProductCategory("Electronics", "Electronic devices"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
        verify(productCategoryService, times(1)).createProductCategory(any(ProductCategory.class));
    }

    @Test
    void updateProductCategory_existingId_returnsOkWithUpdatedCategory() {
        when(productCategoryService.updateProductCategory(eq(1L), any(ProductCategory.class))).thenReturn(Optional.of(category1));
        ResponseEntity<ProductCategory> response = productCategoryController.updateProductCategory(1L, new ProductCategory("Updated Electronics", "Updated description"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName()); // Assuming service returns the original updated
        verify(productCategoryService, times(1)).updateProductCategory(eq(1L), any(ProductCategory.class));
    }

    @Test
    void updateProductCategory_nonExistingId_returnsNotFound() {
        when(productCategoryService.updateProductCategory(eq(100L), any(ProductCategory.class))).thenReturn(Optional.empty());
        ResponseEntity<ProductCategory> response = productCategoryController.updateProductCategory(100L, new ProductCategory("Updated Electronics", "Updated description"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productCategoryService, times(1)).updateProductCategory(eq(100L), any(ProductCategory.class));
    }

    @Test
    void deleteProductCategory_existingId_returnsNoContent() {
        when(productCategoryService.deleteProductCategory(1L)).thenReturn(true);
        ResponseEntity<Void> response = productCategoryController.deleteProductCategory(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productCategoryService, times(1)).deleteProductCategory(1L);
    }

    @Test
    void deleteProductCategory_nonExistingId_returnsNotFound() {
        when(productCategoryService.deleteProductCategory(100L)).thenReturn(false);
        ResponseEntity<Void> response = productCategoryController.deleteProductCategory(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productCategoryService, times(1)).deleteProductCategory(100L);
    }
}
