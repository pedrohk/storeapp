package org.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Address;
import org.mystore.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        address1 = new Address("123 Main St", "Anytown", "CA", "12345");
        address1.setId(1L);
        address2 = new Address("456 Oak Ave", "Otherville", "NY", "67890");
        address2.setId(2L);
    }

    @Test
    void getAddressById_existingId_returnsOkWithAddress() {
        when(addressService.getAddressById(1L)).thenReturn(Optional.of(address1));
        ResponseEntity<Address> response = addressController.getAddressById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("123 Main St", response.getBody().getStreet());
        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    void getAddressById_nonExistingId_returnsNotFound() {
        when(addressService.getAddressById(100L)).thenReturn(Optional.empty());
        ResponseEntity<Address> response = addressController.getAddressById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(addressService, times(1)).getAddressById(100L);
    }

    @Test
    void createAddress_returnsCreatedWithAddress() {
        when(addressService.createAddress(any(Address.class))).thenReturn(address1);
        ResponseEntity<Address> response = addressController.createAddress(new Address("123 Main St", "Anytown", "CA", "12345"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("123 Main St", response.getBody().getStreet());
        verify(addressService, times(1)).createAddress(any(Address.class));
    }

    @Test
    void updateAddress_existingId_returnsOkWithUpdatedAddress() {
        when(addressService.updateAddress(eq(1L), any(Address.class))).thenReturn(Optional.of(address1));
        ResponseEntity<Address> response = addressController.updateAddress(1L, new Address("Updated St", "Updated City", "UT", "00000"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("123 Main St", response.getBody().getStreet()); // Assuming service returns the original updated
        verify(addressService, times(1)).updateAddress(eq(1L), any(Address.class));
    }

    @Test
    void updateAddress_nonExistingId_returnsNotFound() {
        when(addressService.updateAddress(eq(100L), any(Address.class))).thenReturn(Optional.empty());
        ResponseEntity<Address> response = addressController.updateAddress(100L, new Address("Updated St", "Updated City", "UT", "00000"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(addressService, times(1)).updateAddress(eq(100L), any(Address.class));
    }

    @Test
    void deleteAddress_existingId_returnsNoContent() {
        when(addressService.deleteAddress(1L)).thenReturn(true);
        ResponseEntity<Void> response = addressController.deleteAddress(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(addressService, times(1)).deleteAddress(1L);
    }

    @Test
    void deleteAddress_nonExistingId_returnsNotFound() {
        when(addressService.deleteAddress(100L)).thenReturn(false);
        ResponseEntity<Void> response = addressController.deleteAddress(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(addressService, times(1)).deleteAddress(100L);
    }
}
