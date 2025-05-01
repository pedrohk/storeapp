package org.mystore.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Address;
import org.mystore.model.Client;
import org.mystore.repository.ClientRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client1;
    private Client client2;
    private Address address1;


    @BeforeEach
    void setUp() {
        client1 = new Client("John Doe", "john.doe@example.com", "123-456-7890");
        client1.setId(1L);
        client2 = new Client("Jane Smith", "jane.smith@example.com", "987-654-3210");
        client2.setId(2L);
        address1 = new Address("123 Main St", "Anytown", "CA", "12345");
        address1.setId(10L);
    }

    @Test
    void getAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));
        List<Client> clients = clientService.getAllClients();
        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void getClientById_existingId() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        Optional<Client> client = clientService.getClientById(1L);
        assertTrue(client.isPresent());
        assertEquals("John Doe", client.get().getName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void getClientById_nonExistingId() {
        when(clientRepository.findById(100L)).thenReturn(Optional.empty());
        Optional<Client> client = clientService.getClientById(100L);
        assertFalse(client.isPresent());
        verify(clientRepository, times(1)).findById(100L);
    }

    @Test
    void createClient() {
        when(clientRepository.save(client1)).thenReturn(client1);
        Client createdClient = clientService.createClient(client1);
        assertEquals("John Doe", createdClient.getName());
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void updateClient_existingId() {
        Client updatedClient = new Client("Updated Name", "updated.email@example.com", "111-222-3333");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.save(client1)).thenReturn(client1);

        Optional<Client> result = clientService.updateClient(1L, updatedClient);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("updated.email@example.com", result.get().getEmail());
        assertEquals("111-222-3333", result.get().getPhone());
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void updateClient_nonExistingId() {
        Client updatedClient = new Client("Updated Name", "updated.email@example.com", "111-222-3333");
        when(clientRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.updateClient(100L, updatedClient);

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(100L);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void deleteClient_existingId() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(1L);

        boolean result = clientService.deleteClient(1L);

        assertTrue(result);
        verify(clientRepository, times(1)).existsById(1L);
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClient_nonExistingId() {
        when(clientRepository.existsById(100L)).thenReturn(false);

        boolean result = clientService.deleteClient(100L);

        assertFalse(result);
        verify(clientRepository, times(1)).existsById(100L);
        verify(clientRepository, never()).deleteById(anyLong());
    }

    @Test
    void addAddressToClient_existingClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.save(client1)).thenReturn(client1);

        Optional<Client> result = clientService.addAddressToClient(1L, address1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getAddresses().size());
        assertEquals(address1, result.get().getAddresses().get(0));
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void addAddressToClient_nonExistingClient() {
        when(clientRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.addAddressToClient(100L, address1);

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(100L);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void removeAddressFromClient_existingClientAndAddress() {
        client1.addAddress(address1); // Add address to client
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.save(client1)).thenReturn(client1);

        Optional<Client> result = clientService.removeAddressFromClient(1L, address1.getId());

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getAddresses().size());
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void removeAddressFromClient_nonExistingClient() {
        when(clientRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.removeAddressFromClient(100L, address1.getId());

        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(100L);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void removeAddressFromClient_nonExistingAddress() {
        client1.addAddress(address1);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
        when(clientRepository.save(client1)).thenReturn(client1);

        Optional<Client> result = clientService.removeAddressFromClient(1L, 999L); // Non-existing address ID

        assertTrue(result.isPresent()); // Client should still be present
        assertEquals(1, result.get().getAddresses().size()); // Address should not be removed
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client1);
    }
}
