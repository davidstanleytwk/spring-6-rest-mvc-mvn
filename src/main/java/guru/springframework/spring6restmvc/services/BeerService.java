package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> listBeers();

    BeerDTO addBeer(BeerDTO b);

    void updateBeer(UUID id, BeerDTO b);

    void deleteBeer(UUID beerId);

    void patchBeer(UUID id, BeerDTO b);
}
