package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private static final Integer MAX_PAGE_SIZE = 1000;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        return Optional.ofNullable(beerMapper.beerToBeerDTO(
                                        beerRepository.findById(id)
                                                .orElse(null)));
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        Page<Beer>beerPage;

        PageRequest pageRequest = buildPageRequst(pageNumber, pageSize);

        if(StringUtils.hasText(beerName) && beerStyle!=null)
        {
            beerPage= getBeerListByNameAndStyle(beerName, beerStyle, pageRequest);
        }
        else if(StringUtils.hasText(beerName) && beerStyle==null)
        {
            beerPage= getBeerListByName(beerName, pageRequest);
        }
        else if (!StringUtils.hasText(beerName) && beerStyle!=null)
        {
            beerPage = getBeerListByStyle(beerStyle, pageRequest);
        }
        else
        {
            beerPage=beerRepository.findAll(pageRequest);
        }

        if( showInventory!=null && !showInventory)
        {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDTO);

    }

    private PageRequest buildPageRequst(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize=DEFAULT_PAGE_SIZE;

        if( pageNumber!=null && pageNumber>0)
        {
            queryPageNumber=pageNumber-1;
        }
        else
        {
            queryPageNumber= DEFAULT_PAGE_NUMBER;
        }

        if( pageSize!=null && pageSize>0)
        {
            if( pageSize>MAX_PAGE_SIZE)
            {
                pageSize=MAX_PAGE_SIZE;
            }
            else {
                queryPageSize=pageSize;
            }
        }
        else
        {
            queryPageSize=DEFAULT_PAGE_SIZE;
        }

        return PageRequest.of(queryPageNumber,queryPageSize);
    }

    public Page<Beer> getBeerListByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerStyleAndBeerNameIsLikeIgnoreCase( beerStyle, "%"+beerName+"%",pageRequest );
    }

    private Page<Beer> getBeerListByStyle(BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    public Page<Beer> getBeerListByName(String beerName, PageRequest pageRequest) {

        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%"+beerName+"%", pageRequest);
    }

    @Override
    public BeerDTO createBeer(BeerDTO b) {

        Beer newBeer =beerRepository.save(beerMapper.beerDTOToBeer(b));

        return beerMapper.beerToBeerDTO(newBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO b) {
       AtomicReference<Optional<BeerDTO>> beerDTOOptional = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse( foundBeer->{
            foundBeer.setBeerName(b.getBeerName());
            foundBeer.setBeerStyle(b.getBeerStyle());
            foundBeer.setPrice(b.getPrice());
            foundBeer.setQuantityOnHand(b.getQuantityOnHand());
            foundBeer.setUpc(b.getUpc());

            beerDTOOptional.set( Optional.of(beerMapper.beerToBeerDTO(beerRepository.save(foundBeer))));

        },
        ()->{

            beerDTOOptional.set(Optional.empty());

        });

        return beerDTOOptional.get();
    }

    @Override
    public Boolean deleteBeer(UUID beerId) {

        if( beerRepository.existsById(beerId))
        {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<BeerDTO> patchBeer(UUID id, BeerDTO b) {

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(b.getBeerName())){
                foundBeer.setBeerName(b.getBeerName());
            }
            if (b.getBeerStyle() != null){
                foundBeer.setBeerStyle(b.getBeerStyle());
            }
            if (StringUtils.hasText(b.getUpc())){
                foundBeer.setUpc(b.getUpc());
            }
            if (b.getPrice() != null){
                foundBeer.setPrice(b.getPrice());
            }
            if (b.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(b.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDTO(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();

    }
}
