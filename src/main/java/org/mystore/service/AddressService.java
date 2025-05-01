package org.mystore.service;

import org.mystore.model.Address;
import org.mystore.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<Address> updateAddress(Long id, Address updatedAddress) {
        return addressRepository.findById(id)
                .map(address -> {
                    address.setStreet(updatedAddress.getStreet());
                    address.setCity(updatedAddress.getCity());
                    address.setState(updatedAddress.getState());
                    address.setZipCode(updatedAddress.getZipCode());
                    return addressRepository.save(address);
                });
    }

    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

