package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID id);

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    BeerDTO createBeer(BeerDTO b);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO b);

    Boolean deleteBeer(UUID beerId);

    Optional<BeerDTO> patchBeer(UUID id, BeerDTO b);
}
