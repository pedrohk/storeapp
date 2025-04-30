package org.mystore.service;

import org.mystore.model.Address;
import org.mystore.model.Client;
import org.mystore.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(updatedClient.getName());
                    client.setEmail(updatedClient.getEmail());
                    client.setPhone(updatedClient.getPhone());
                    return clientRepository.save(client);
                });
    }

    public boolean deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional
    public Optional<Client> addAddressToClient(Long clientId, Address address) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    client.addAddress(address);
                    return clientRepository.save(client);
                });
    }

    @Transactional
    public Optional<Client> removeAddressFromClient(Long clientId, Long addressId) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    client.getAddresses().removeIf(address -> address.getId().equals(addressId));
                    return clientRepository.save(client);
                });
    }

}
