package org.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.ProductCategory;
import org.mystore.model.Subcategory;
import org.mystore.service.SubcategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubcategoryControllerTest {

    @Mock
    private SubcategoryService subcategoryService;

    @InjectMocks
    private SubcategoryController subcategoryController;

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
    void getAllSubcategories_returnsOkWithSubcategoryList() {
        when(subcategoryService.getAllSubcategories()).thenReturn(Arrays.asList(subcategory1, subcategory2));
        ResponseEntity<List<Subcategory>> response = subcategoryController.getAllSubcategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(subcategoryService, times(1)).getAllSubcategories();
    }

    @Test
    void getSubcategoryById_existingId_returnsOkWithSubcategory() {
        when(subcategoryService.getSubcategoryById(10L)).thenReturn(Optional.of(subcategory1));
        ResponseEntity<Subcategory> response = subcategoryController.getSubcategoryById(10L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Smartphones", response.getBody().getName());
        verify(subcategoryService, times(1)).getSubcategoryById(10L);
    }

    @Test
    void getSubcategoryById_nonExistingId_returnsNotFound() {
        when(subcategoryService.getSubcategoryById(100L)).thenReturn(Optional.empty());
        ResponseEntity<Subcategory> response = subcategoryController.getSubcategoryById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(subcategoryService, times(1)).getSubcategoryById(100L);
    }

    @Test
    void createSubcategory_returnsCreatedWithSubcategory() {
        when(subcategoryService.createSubcategory(any(Subcategory.class))).thenReturn(subcategory1);
        ResponseEntity<Subcategory> response = subcategoryController.createSubcategory(new Subcategory("Smartphones", "Mobile phones", category1));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Smartphones", response.getBody().getName());
        verify(subcategoryService, times(1)).createSubcategory(any(Subcategory.class));
    }

    @Test
    void updateSubcategory_existingId_returnsOkWithUpdatedSubcategory() {
        when(subcategoryService.updateSubcategory(eq(10L), any(Subcategory.class))).thenReturn(Optional.of(subcategory1));
        ResponseEntity<Subcategory> response = subcategoryController.updateSubcategory(10L, new Subcategory("Updated Phones", "New description", category1));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Smartphones", response.getBody().getName()); // Assuming service returns the original updated
        verify(subcategoryService, times(1)).updateSubcategory(eq(10L), any(Subcategory.class));
    }

    @Test
    void updateSubcategory_nonExistingId_returnsNotFound() {
        when(subcategoryService.updateSubcategory(eq(100L), any(Subcategory.class))).thenReturn(Optional.empty());
        ResponseEntity<Subcategory> response = subcategoryController.updateSubcategory(100L, new Subcategory("Updated Phones", "New description", category1));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(subcategoryService, times(1)).updateSubcategory(eq(100L), any(Subcategory.class));
    }

    @Test
    void deleteSubcategory_existingId_returnsNoContent() {
        when(subcategoryService.deleteSubcategory(10L)).thenReturn(true);
        ResponseEntity<Void> response = subcategoryController.deleteSubcategory(10L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subcategoryService, times(1)).deleteSubcategory(10L);
    }

    @Test
    void deleteSubcategory_nonExistingId_returnsNotFound() {
        when(subcategoryService.deleteSubcategory(100L)).thenReturn(false);
        ResponseEntity<Void> response = subcategoryController.deleteSubcategory(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(subcategoryService, times(1)).deleteSubcategory(100L);
    }
}
