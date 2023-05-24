package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void createBeer() {
        Beer newBeer = beerRepository.save(Beer.builder().beerName("Pale Ale").build());

        assertNotNull(newBeer);

        assertNotNull(newBeer.getId());

        System.out.println(">>>"+newBeer.toString());
    }
}