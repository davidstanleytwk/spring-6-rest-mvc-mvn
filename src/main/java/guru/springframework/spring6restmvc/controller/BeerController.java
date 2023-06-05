package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping(BeerController.BEER_PATH)
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH+"/{beerId}";
    private final BeerService beerService;

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFoundException()
//    {
//        log.debug("BeerController::HandleNotFoundException called");
//        return ResponseEntity.notFound().build();
//    }

    @GetMapping(value=BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId")  UUID beerId)
    {
        log.debug("Controller is getting beer by id: "+beerId.toString());

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_PATH)
    public Page<BeerDTO> listBeers(
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false)  BeerStyle beerStyle,
            @RequestParam(value = "showInventory", required = false)  Boolean showInventory,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize)
    {
        return beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity addBeer(@Validated @RequestBody BeerDTO b)
    {
        BeerDTO newBeerDTO = beerService.createBeer(b);
        HttpHeaders h = new HttpHeaders();
        h.add("Location",BEER_PATH+ "/"+newBeerDTO.getId().toString());

        return new ResponseEntity(h,HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateBeer( @PathVariable("beerId") UUID id, @Validated @RequestBody BeerDTO b)
    {
        if(beerService.updateBeer(id, b).isEmpty() )
        {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeer( @PathVariable("beerId") UUID id, @RequestBody BeerDTO b)
    {
        beerService.patchBeer(id, b);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeer(@PathVariable("beerId") UUID beerId)
    {
        if( !beerService.deleteBeer(beerId))
        {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
