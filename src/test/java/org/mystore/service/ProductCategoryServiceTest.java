package org.mystore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.ProductCategory;
import org.mystore.repository.ProductCategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryService productCategoryService;

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
    void getAllProductCategories() {
        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        List<ProductCategory> categories = productCategoryService.getAllProductCategories();
        assertEquals(2, categories.size());
        verify(productCategoryRepository, times(1)).findAll();
    }

    @Test
    void getProductCategoryById_existingId() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        Optional<ProductCategory> category = productCategoryService.getProductCategoryById(1L);
        assertTrue(category.isPresent());
        assertEquals("Electronics", category.get().getName());
        verify(productCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void getProductCategoryById_nonExistingId() {
        when(productCategoryRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<ProductCategory> category = productCategoryService.getProductCategoryById(100L);
        assertFalse(category.isPresent());
        verify(productCategoryRepository, times(1)).findById(100L);
    }

    @Test
    void createProductCategory() {
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(category1);
        ProductCategory createdCategory = productCategoryService.createProductCategory(new ProductCategory("Electronics", "Electronic devices"));
        assertEquals("Electronics", createdCategory.getName());
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @Test
    void updateProductCategory_existingId() {
        ProductCategory updatedCategory = new ProductCategory("Updated Electronics", "Updated description");
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(updatedCategory);

        Optional<ProductCategory> result = productCategoryService.updateProductCategory(1L, updatedCategory);

        assertTrue(result.isPresent());
        assertEquals("Updated Electronics", result.get().getName());
        assertEquals("Updated description", result.get().getDescription());
        verify(productCategoryRepository, times(1)).findById(1L);
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @Test
    void updateProductCategory_nonExistingId() {
        ProductCategory updatedCategory = new ProductCategory("Updated Electronics", "Updated description");
        when(productCategoryRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<ProductCategory> result = productCategoryService.updateProductCategory(100L, updatedCategory);

        assertFalse(result.isPresent());
        verify(productCategoryRepository, times(1)).findById(100L);
        verify(productCategoryRepository, never()).save(any(ProductCategory.class));
    }

    @Test
    void deleteProductCategory_existingId() {
        when(productCategoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productCategoryRepository).deleteById(1L);

        boolean result = productCategoryService.deleteProductCategory(1L);

        assertTrue(result);
        verify(productCategoryRepository, times(1)).existsById(1L);
        verify(productCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProductCategory_nonExistingId() {
        when(productCategoryRepository.existsById(100L)).thenReturn(false);

        boolean result = productCategoryService.deleteProductCategory(100L);

        assertFalse(result);
        verify(productCategoryRepository, times(1)).existsById(100L);
        verify(productCategoryRepository, never()).deleteById(anyLong());
    }
}
