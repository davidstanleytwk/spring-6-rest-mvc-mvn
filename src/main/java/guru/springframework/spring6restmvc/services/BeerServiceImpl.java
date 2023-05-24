package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    HashMap<UUID, BeerDTO> beerHashMap = new HashMap<>();

    public BeerServiceImpl() {
        BeerDTO[] b1 = {
            BeerDTO.builder()
                    .id(UUID.randomUUID())
                    .beerName("Spitfire")
                    .price(new BigDecimal("3.90"))
                    .beerStyle(BeerStyle.ALE)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .quantityOnHand(70)
                    .upc("192989")
                    .build(),
            BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Fosters")
                .price(new BigDecimal("3.10"))
                .beerStyle(BeerStyle.LAGER)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(20)
                .upc("392989")
                .build(),

            BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Applymuck")
                .price(new BigDecimal("2.20"))
                .beerStyle(BeerStyle.CIDER)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(3)
                .upc("9992989")
                .build()};

        Arrays.asList(b1).forEach(b->beerHashMap.put(b.getId(),b));
    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Serice is getting beer id:"+id.toString());
        return Optional.of(beerHashMap.get(id));
    }
    @Override
    public List<BeerDTO> listBeers()
    {
        return new ArrayList<>(beerHashMap.values());
    }

    @Override
    public BeerDTO addBeer(BeerDTO b) {
        BeerDTO newBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerStyle(b.getBeerStyle())
                .beerName(b.getBeerName())
                .quantityOnHand(b.getQuantityOnHand())
                .price(b.getPrice())
                .upc(b.getUpc())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerHashMap.put(newBeerDTO.getId(), newBeerDTO);
        return newBeerDTO;
    }

    @Override
    public void updateBeer(UUID id, BeerDTO b) {
        BeerDTO bToUpdate = beerHashMap.get(id);

        bToUpdate.setBeerName(b.getBeerName());
        bToUpdate.setBeerStyle(b.getBeerStyle());
        bToUpdate.setUpdateDate(LocalDateTime.now());
        bToUpdate.setQuantityOnHand(b.getQuantityOnHand());
        bToUpdate.setVersion(b.getVersion());
        bToUpdate.setPrice(b.getPrice());
    }

    @Override
    public void deleteBeer(UUID beerId) {
        log.debug("BeerService:: Delete beer id:"+beerId);
        beerHashMap.remove(beerId);
    }

    @Override
    public void patchBeer(UUID id, BeerDTO b) {

        BeerDTO ub = beerHashMap.get(id);

        if( b.getBeerName()!=null && !b.getBeerName().isEmpty())
        {
            ub.setBeerName(b.getBeerName());
        }

        if( b.getPrice()!=null )
        {
            ub.setPrice(b.getPrice());
        }

        if( b.getBeerStyle()!=null )
        {
            ub.setBeerStyle(b.getBeerStyle());
        }

        if( b.getVersion()!=null)
        {
            ub.setVersion(b.getVersion());
        }

        ub.setUpdateDate(LocalDateTime.now());
    }
}
