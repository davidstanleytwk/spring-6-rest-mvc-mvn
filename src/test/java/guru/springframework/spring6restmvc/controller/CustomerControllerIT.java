package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void getCustomerById() {
        Customer c = customerRepository.findAll().get(0);

        CustomerDTO gotCust = customerController.getCustomerById(c.getId());

        assertNotNull(gotCust);

    }

    @Test
    void getCustByIdNotfound() {

        assertThrows(NotFoundException.class,()->
            {customerController.getCustomerById(UUID.randomUUID());
        });
    }


    @Test
    void getList() {


        List<CustomerDTO> customerDTOList = customerController.listCustomers();

        assertEquals(3, customerDTOList.size());
    }


    @Test
    @Transactional
    @Rollback
    void getEmptyList() {

        customerRepository.deleteAll();;
        List<CustomerDTO> customerDTOList = customerController.listCustomers();

        assertEquals(0, customerDTOList.size());
    }

    @Transactional
    @Rollback
    @Test
    void newCustomer() {

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Person")
                .build();


        ResponseEntity responseEntity=customerController.addCustomer(customerDTO);
        assertEquals(HttpStatusCode.valueOf(201),responseEntity.getStatusCode());

        String loc[] = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID id = UUID.fromString(loc[4]);

        Customer c =customerRepository.findById(id).get();

        assertNotNull(c);

        assertEquals(customerDTO.getCustomerName(),c.getCustomerName());

    }

    @Transactional
    @Rollback
    @Test
    void updateCustomer() {

        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = CustomerDTO
                .builder()
                .customerName(customer.getCustomerName()+"UPDATED")
                .build();

        ResponseEntity responseEntity = customerController.updateCustomer(customer.getId(),customerDTO);
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());

        Customer c = customerRepository.findById(customer.getId()).get();

        assertNotNull(c);
        assertEquals(c.getCustomerName(),customerDTO.getCustomerName());
    }


    @Test
    void updateInvalidCustomerId() {
        CustomerDTO customerDTO = CustomerDTO
                .builder()
                .build();

        assertThrows( NotFoundException.class,()->{

                ResponseEntity responseEntity = customerController.updateCustomer(UUID.randomUUID(),customerDTO);
        });
    }

    @Test
    void deleteCustomerNotFound() {

        assertThrows(NotFoundException.class,()->{
            customerController.deleteCustomer(UUID.randomUUID());
        });

    }

    @Transactional
    @Rollback
    @Test
    void deleteCustomerFound() {
        List<Customer> customerList = customerRepository.findAll();

        ResponseEntity responseEntity =customerController.deleteCustomer(customerList.get(0).getId());
        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());

        assertThrows(NotFoundException.class,()->{
           customerController.getCustomerById(customerList.get(0).getId()) ;
        });

        assertEquals(customerList.size()-1,customerController.listCustomers().size());
    }
}