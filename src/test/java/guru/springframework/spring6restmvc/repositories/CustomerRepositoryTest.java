package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void create()
    {
        Customer newCust = customerRepository.save(Customer.builder().customerName("Mr Testy").build());

        assertNotNull(newCust);

        assertNotNull(newCust.getId());
        System.out.println(">>>>>>>>>>>>>>>>"+newCust.toString());
    }


}