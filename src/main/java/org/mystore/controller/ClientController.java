package org.mystore.controller;


import org.mystore.model.Address;
import org.mystore.model.Client;
import org.mystore.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        Optional<Client> updated = clientService.updateClient(id, updatedClient);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (clientService.deleteClient(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{clientId}/addresses")
    public ResponseEntity<Client> addAddressToClient(@PathVariable Long clientId, @RequestBody Address address) {
        Optional<Client> updatedClient = clientService.addAddressToClient(clientId, address);
        return updatedClient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{clientId}/addresses/{addressId}")
    public ResponseEntity<Client> removeAddressFromClient(@PathVariable Long clientId, @PathVariable Long addressId) {
        Optional<Client> updatedClient = clientService.removeAddressFromClient(clientId, addressId);
        if (updatedClient.isPresent()) {
            return ResponseEntity.ok(updatedClient.get()); // Return the updated client
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
