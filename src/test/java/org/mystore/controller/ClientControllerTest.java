package org.mystore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mystore.model.Address;
import org.mystore.model.Client;
import org.mystore.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client1;
    private Client client2;
    private Address address1;

    @BeforeEach
    void setUp() {
        client1 = new Client("Pedro Doe", "john.doe@example.com", "123-456-7890");
        client1.setId(1L);
        client2 = new Client("Tania Smith", "jane.smith@example.com", "987-654-3210");
        client2.setId(2L);
        address1 = new Address("123 Main St", "Anytown", "CA", "12345");
        address1.setId(10L);
    }

    @Test
    void getAllClients_returnsOkWithClientList() {
        when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));
        ResponseEntity<List<Client>> response = clientController.getAllClients();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void getClientById_existingId_returnsOkWithClient() {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client1));
        ResponseEntity<Client> response = clientController.getClientById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedro Doe", response.getBody().getName());
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void getClientById_nonExistingId_returnsNotFound() {
        when(clientService.getClientById(100L)).thenReturn(Optional.empty());
        ResponseEntity<Client> response = clientController.getClientById(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).getClientById(100L);
    }

    @Test
    void createClient_returnsCreatedWithClient() {
        when(clientService.createClient(any(Client.class))).thenReturn(client1);
        ResponseEntity<Client> response = clientController.createClient(new Client("John Henrique", "john.henrique@example.com", "123-456-7890"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pedro Doe", response.getBody().getName());
        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    void updateClient_existingId_returnsOkWithUpdatedClient() {
        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(Optional.of(client1));
        ResponseEntity<Client> response = clientController.updateClient(1L, new Client("Updated Name", "updated@example.com", "111-222-3333"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedro Doe", response.getBody().getName()); // Assuming service returns the original updated
        verify(clientService, times(1)).updateClient(eq(1L), any(Client.class));
    }

    @Test
    void updateClient_nonExistingId_returnsNotFound() {
        when(clientService.updateClient(eq(100L), any(Client.class))).thenReturn(Optional.empty());
        ResponseEntity<Client> response = clientController.updateClient(100L, new Client("Updated Name", "updated@example.com", "111-222-3333"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).updateClient(eq(100L), any(Client.class));
    }

    @Test
    void deleteClient_existingId_returnsNoContent() {
        when(clientService.deleteClient(1L)).thenReturn(true);
        ResponseEntity<Void> response = clientController.deleteClient(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void deleteClient_nonExistingId_returnsNotFound() {
        when(clientService.deleteClient(100L)).thenReturn(false);
        ResponseEntity<Void> response = clientController.deleteClient(100L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).deleteClient(100L);
    }

    @Test
    void addAddressToClient_existingClient_returnsOkWithUpdatedClient() {
        when(clientService.addAddressToClient(eq(1L), any(Address.class))).thenReturn(Optional.of(client1));
        ResponseEntity<Client> response = clientController.addAddressToClient(1L, address1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedro Doe", response.getBody().getName());
        verify(clientService, times(1)).addAddressToClient(eq(1L), any(Address.class));
    }

    @Test
    void addAddressToClient_nonExistingClient_returnsNotFound() {
        when(clientService.addAddressToClient(eq(100L), any(Address.class))).thenReturn(Optional.empty());
        ResponseEntity<Client> response = clientController.addAddressToClient(100L, address1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).addAddressToClient(eq(100L), any(Address.class));
    }

    @Test
    void removeAddressFromClient_existingClientAndAddress_returnsOkWithUpdatedClient() {
        when(clientService.removeAddressFromClient(eq(1L), eq(10L))).thenReturn(Optional.of(client1));
        ResponseEntity<Client> response = clientController.removeAddressFromClient(1L, 10L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedro Doe", response.getBody().getName());
        verify(clientService, times(1)).removeAddressFromClient(eq(1L), eq(10L));
    }

    @Test
    void removeAddressFromClient_nonExistingClient_returnsNotFound() {
        when(clientService.removeAddressFromClient(eq(100L), eq(10L))).thenReturn(Optional.empty());
        ResponseEntity<Client> response = clientController.removeAddressFromClient(100L, 10L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).removeAddressFromClient(eq(100L), eq(10L));
    }

    @Test
    void removeAddressFromClient_existingClientNonExistingAddress_returnsNotFound() {
        when(clientService.removeAddressFromClient(eq(1L), eq(999L))).thenReturn(Optional.empty());
        ResponseEntity<Client> response = clientController.removeAddressFromClient(1L, 999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientService, times(1)).removeAddressFromClient(eq(1L), eq(999L));
    }
}
