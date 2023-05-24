package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void run() throws Exception {
        BootstrapData bootstrapData = new BootstrapData(beerRepository,customerRepository);
        bootstrapData.run();

        assertEquals(4,beerRepository.count());
        assertEquals(3,customerRepository.count());
    }
}