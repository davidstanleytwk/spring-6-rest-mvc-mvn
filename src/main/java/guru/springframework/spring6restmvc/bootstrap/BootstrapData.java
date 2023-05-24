package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;

import guru.springframework.spring6restmvc.model.BeerStyle;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;


    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData()
    {
        log.debug("BootstrapData::Loading Beer Data");
        Beer[] b1 = {

                Beer.builder()
                        .beerName("Spitfire")
                        .price(new BigDecimal("3.90"))
                        .beerStyle(BeerStyle.ALE)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .quantityOnHand(70)
                        .upc("192989")
                        .build(),

                Beer.builder()
                        .beerName("Fosters")
                        .price(new BigDecimal("3.10"))
                        .beerStyle(BeerStyle.LAGER)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .quantityOnHand(20)
                        .upc("392989")
                        .build(),

                Beer.builder()
                        .beerName("Applymuck")
                        .price(new BigDecimal("2.20"))
                        .beerStyle(BeerStyle.CIDER)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .quantityOnHand(3)
                        .upc("9992989")
                        .build(),

                Beer.builder()

                        .beerName("Fosters Light")
                        .price(new BigDecimal("2.60"))
                        .beerStyle(BeerStyle.LAGER)
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .quantityOnHand(20)
                        .upc("39220001")
                        .build()
        };


        beerRepository.saveAll(Arrays.asList(b1));
    }

    private void loadCustomerData()
    {
        log.debug("BootstrapData::Loading Customer Data");

        Customer[] cArray = {
                Customer.builder()
                        .id(UUID.randomUUID())
                        .customerName("Fred Smith")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build(),
                Customer.builder()
                        .id(UUID.randomUUID())
                        .customerName("Jo Jones")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build(),
                Customer.builder()
                        .id(UUID.randomUUID())
                        .customerName("Arnold Dog")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build()
        };

        customerRepository.saveAll(Arrays.asList(cArray));
    }
}
