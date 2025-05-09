package org.mystore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.ProductCategory;
import org.mystore.model.Subcategory;
import org.mystore.repository.SubcategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubcategoryServiceTest {

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private SubcategoryService subcategoryService;

    private ProductCategory category1;
    private Subcategory subcategory1;
    private Subcategory subcategory2;

    @BeforeEach
    void setUp() {
        category1 = new ProductCategory("Electronics", "Electronic devices");
        category1.setId(1L);
        subcategory1 = new Subcategory("Smartphones", "Mobile phones", category1);
        subcategory1.setId(10L);
        subcategory2 = new Subcategory("Laptops", "Portable computers", category1);
        subcategory2.setId(11L);
    }

    @Test
    void getAllSubcategories() {
        when(subcategoryRepository.findAll()).thenReturn(Arrays.asList(subcategory1, subcategory2));
        List<Subcategory> subcategories = subcategoryService.getAllSubcategories();
        assertEquals(2, subcategories.size());
        verify(subcategoryRepository, times(1)).findAll();
    }

    @Test
    void getSubcategoryById_existingId() {
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory1));
        Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(10L);
        assertTrue(subcategory.isPresent());
        assertEquals("Smartphones", subcategory.get().getName());
        assertEquals(category1, subcategory.get().getCategory());
        verify(subcategoryRepository, times(1)).findById(10L);
    }

    @Test
    void getSubcategoryById_nonExistingId() {
        when(subcategoryRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<Subcategory> subcategory = subcategoryService.getSubcategoryById(100L);
        assertFalse(subcategory.isPresent());
        verify(subcategoryRepository, times(1)).findById(100L);
    }

    @Test
    void createSubcategory() {
        when(subcategoryRepository.save(any(Subcategory.class))).thenReturn(subcategory1);
        Subcategory createdSubcategory = subcategoryService.createSubcategory(new Subcategory("Smartphones", "Mobile phones", category1));
        assertEquals("Smartphones", createdSubcategory.getName());
        assertEquals(category1, createdSubcategory.getCategory());
        verify(subcategoryRepository, times(1)).save(any(Subcategory.class));
    }

    @Test
    void updateSubcategory_existingId() {
        Subcategory updatedSubcategory = new Subcategory("Updated Phones", "New description", new ProductCategory(1L, "Electronics", ""));
        when(subcategoryRepository.findById(10L)).thenReturn(Optional.of(subcategory1));
        when(productCategoryService.getProductCategoryById(1L)).thenReturn(Optional.of(category1));
        when(subcategoryRepository.save(any(Subcategory.class))).thenReturn(updatedSubcategory);

        Optional<Subcategory> result = subcategoryService.updateSubcategory(10L, updatedSubcategory);

        assertTrue(result.isPresent());
        assertEquals("Updated Phones", result.get().getName());
        assertEquals("New description", result.get().getDescription());
        verify(subcategoryRepository, times(1)).findById(10L);
        verify(productCategoryService, times(1)).getProductCategoryById(1L);
        verify(subcategoryRepository, times(1)).save(any(Subcategory.class));
    }

    @Test
    void updateSubcategory_nonExistingId() {
        Subcategory updatedSubcategory = new Subcategory("Updated Phones", "New description", category1);
        when(subcategoryRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Subcategory> result = subcategoryService.updateSubcategory(100L, updatedSubcategory);

        assertFalse(result.isPresent());
        verify(subcategoryRepository, times(1)).findById(100L);
        verify(subcategoryRepository, never()).save(any(Subcategory.class));
    }

    @Test
    void deleteSubcategory_existingId() {
        when(subcategoryRepository.existsById(10L)).thenReturn(true);
        doNothing().when(subcategoryRepository).deleteById(10L);

        boolean result = subcategoryService.deleteSubcategory(10L);

        assertTrue(result);
        verify(subcategoryRepository, times(1)).existsById(10L);
        verify(subcategoryRepository, times(1)).deleteById(10L);
    }

    @Test
    void deleteSubcategory_nonExistingId() {
        when(subcategoryRepository.existsById(100L)).thenReturn(false);

        boolean result = subcategoryService.deleteSubcategory(100L);

        assertFalse(result);
        verify(subcategoryRepository, times(1)).existsById(100L);
        verify(subcategoryRepository, never()).deleteById(anyLong());
    }
}
