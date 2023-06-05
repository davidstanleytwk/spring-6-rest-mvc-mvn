package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> listCustomer();

    CustomerDTO addCustomer(CustomerDTO c);

    Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO c);

    Boolean deleteCustomer(UUID cId);

    void patchCustomer(UUID id, CustomerDTO c);
}
