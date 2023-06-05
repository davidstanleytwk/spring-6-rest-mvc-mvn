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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor

public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(
                customerMapper.customerToCustomerDTO(
                customerRepository.findById(id).orElse(null)));
    }

    @Override
    public List<CustomerDTO> listCustomer() {


        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO c) {
        return customerMapper
                .customerToCustomerDTO(
                        customerRepository
                                .save(customerMapper.customerDTOToCusomter(c)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO c) {

        AtomicReference<Optional<CustomerDTO>> atomicReference= new AtomicReference<>();
        customerRepository.findById(id).ifPresentOrElse(customer -> {

            customer.setCustomerName(c.getCustomerName());
            customer.setEmail(c.getEmail());


            atomicReference.set(
                        Optional.of(
                                customerMapper.customerToCustomerDTO(
                                        customerRepository.save(customer)
                                ))
                    );
        },
        ()->{
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustomer(UUID cId) {

        if( customerRepository.existsById(cId))
        {
            customerRepository.deleteById(cId);
            return true;
        }
        return false;

    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO c) {

    }
}
