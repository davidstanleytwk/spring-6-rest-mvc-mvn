package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;

import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import guru.springframework.spring6restmvc.model.BeerStyle;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCSVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    private final BeerCSVService beerCSVService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();

        loadCSVData();
    }


    private void loadCSVData() throws FileNotFoundException {
        if( beerRepository.count()<100)
        {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> beerCSVRecordList= beerCSVService.convertCsv(file);
            beerCSVRecordList.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer
                        .builder()
                                .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(),50))
                                .beerStyle( beerStyle)
                                .price(BigDecimal.TEN)
                                .upc(beerCSVRecord.getRow().toString())
                                .quantityOnHand(beerCSVRecord.getCount())

                        .build());
            });
        }
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
