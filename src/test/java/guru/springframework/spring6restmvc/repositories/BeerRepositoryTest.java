package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerCSVServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BeerCSVServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void getBeerByBeerStyle() {
        Page<Beer> beerPage = beerRepository.findAllByBeerStyle(BeerStyle.ALE, Pageable.unpaged());

        assertEquals(401,beerPage.getTotalElements());
    }


    @Test
    void getBeerByNameList() {
        Page<Beer> beerPage = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%",Pageable.unpaged() );

        assertEquals(336,beerPage.getTotalElements());
    }

    @Test
    void createBeer() {
        Beer newBeer = beerRepository.save(Beer
                .builder()
                .beerName("Pale Ale")
                        .beerStyle(BeerStyle.ALE)
                        .upc("xx")
                        .quantityOnHand(100)
                        .price(new BigDecimal("3.21"))
                .build());

        beerRepository.flush();

        assertNotNull(newBeer);

        assertNotNull(newBeer.getId());

        System.out.println(">>>"+newBeer.toString());
    }

    @Test
    void createBeerNameTooLong() {


        jakarta.validation.ConstraintViolationException e =assertThrows(jakarta.validation.ConstraintViolationException.class,()->{

            Beer newBeer = beerRepository.save(Beer
                    .builder()
                    .beerName("012345678901234567890123456789012345678901234567890123456789")
                    .beerStyle(BeerStyle.ALE)
                    .upc("xx")
                    .quantityOnHand(100)
                    .price(new BigDecimal("3.21"))
                    .build());

            beerRepository.flush();
        });


        assertTrue(e.getMessage().contains("size must be between 0 and 50"));
        assertTrue(e.getMessage().contains("beerName"));
        System.out.println(e.toString());

    }
}