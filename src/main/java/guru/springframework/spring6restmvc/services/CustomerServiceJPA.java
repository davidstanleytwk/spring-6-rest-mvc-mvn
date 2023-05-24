package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor

public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        return null;
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO c) {
        return null;
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO c) {

    }

    @Override
    public void deleteCustomer(UUID cId) {

    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO c) {

    }
}
