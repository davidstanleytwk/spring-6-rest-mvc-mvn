package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<BeerDTO> listBeers() {
        return null;
    }

    @Override
    public BeerDTO addBeer(BeerDTO b) {
        return null;
    }

    @Override
    public void updateBeer(UUID id, BeerDTO b) {

    }

    @Override
    public void deleteBeer(UUID beerId) {

    }

    @Override
    public void patchBeer(UUID id, BeerDTO b) {

    }
}
