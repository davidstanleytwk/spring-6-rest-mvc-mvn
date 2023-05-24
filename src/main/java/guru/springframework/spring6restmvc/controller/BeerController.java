package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<BeerDTO> listBeers()
    {
        return beerService.listBeers();
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity addBeer(@RequestBody BeerDTO b)
    {
        BeerDTO newBeerDTO = beerService.addBeer(b);
        HttpHeaders h = new HttpHeaders();
        h.add("Location","/api/v1/beer/"+ newBeerDTO.getId().toString());

        return new ResponseEntity(h,HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateBeer( @PathVariable("beerId") UUID id, @RequestBody BeerDTO b)
    {
        beerService.updateBeer(id, b);


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
        beerService.deleteBeer(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
