package org.mystore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Address;
import org.mystore.repository.AddressRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        address1 = new Address("123 Main St", "Anytown", "CA", "12345");
        address1.setId(1L);
        address2 = new Address("456 Oak Ave", "Minetown", "NY", "67890");
        address2.setId(2L);
    }

    @Test
    void getAddressById_existingId() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));
        Optional<Address> address = addressService.getAddressById(1L);
        assertTrue(address.isPresent());
        assertEquals("123 Main St", address.get().getStreet());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void getAddressById_nonExistingId() {
        when(addressRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<Address> address = addressService.getAddressById(100L);
        assertFalse(address.isPresent());
        verify(addressRepository, times(1)).findById(100L);
    }

    @Test
    void createAddress() {
        when(addressRepository.save(address1)).thenReturn(address1);
        Address createdAddress = addressService.createAddress(address1);
        assertEquals("123 Main St", createdAddress.getStreet());
        verify(addressRepository, times(1)).save(address1);
    }

    @Test
    void updateAddress_existingId() {
        Address updatedAddress = new Address("Updated St", "Updated City", "UT", "00000");
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));
        when(addressRepository.save(address1)).thenReturn(address1);

        Optional<Address> result = addressService.updateAddress(1L, updatedAddress);

        assertTrue(result.isPresent());
        assertEquals("Updated St", result.get().getStreet());
        assertEquals("Updated City", result.get().getCity());
        assertEquals("UT", result.get().getState());
        assertEquals("00000", result.get().getZipCode());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(address1);
    }

    @Test
    void updateAddress_nonExistingId() {
        Address updatedAddress = new Address("Updated St", "Updated City", "UT", "00000");
        when(addressRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Address> result = addressService.updateAddress(100L, updatedAddress);

        assertFalse(result.isPresent());
        verify(addressRepository, times(1)).findById(100L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void deleteAddress_existingId() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        boolean result = addressService.deleteAddress(1L);

        assertTrue(result);
        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAddress_nonExistingId() {
        when(addressRepository.existsById(100L)).thenReturn(false);

        boolean result = addressService.deleteAddress(100L);

        assertFalse(result);
        verify(addressRepository, times(1)).existsById(100L);
        verify(addressRepository, never()).deleteById(anyLong());
    }
}