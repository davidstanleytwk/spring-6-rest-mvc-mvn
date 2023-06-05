package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    void listBeersByNameInventoryTruePage1() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber","2")
                        .queryParam("pageSize", "50")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.content.size()",is(50)));
    }


    @Test
    void listBeersByNameInventory() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize","1000")
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void listBeersByNameNoInventory() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize","1000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
    }
    @Test
    void listBeersByName() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerName","IPA")
                        .queryParam("pageSize","1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)));
    }


    @Test
    void listBeersByNamePageSize50() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("pageSize","50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(50)));
    }

    @Test
    void listBeersByStyleAndName() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName","IPA")
                        .queryParam("beerStyle",BeerStyle.IPA.name())
                        .queryParam("pageSize","1000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(310)));
    }
    @Test
    void listBeersByStyle() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle",BeerStyle.IPA.name())
                        .queryParam("pageSize","1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(547)));
    }
    @Test
    void patchBeerBadName() throws Exception {

        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName","012345678901234567890123456789012345678901234567890123456789");

        BeerDTO patchBeerDTO = objectMapper.convertValue(beerMap, BeerDTO.class);

        MvcResult mvcResult = mockMvc.perform(
                        patch(BeerController.BEER_PATH_ID,beer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void listBeers() {
        Page<BeerDTO> beerDTOList = beerController.listBeers(null,null, false, 1, 25);

        assertEquals(2414,beerDTOList.getTotalElements());

    }

    @Test
    @Transactional
    @Rollback
    void emptyList() {
        beerRepository.deleteAll();

        Page<BeerDTO> beerDTOList = beerController.listBeers(null,null, false, 1, 25);

        assertEquals(0,beerDTOList.getTotalElements());
    }

    @Test
    void getById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        assertNotNull(beerDTO);
    }

    @Test
    void getByIdNotFound() {

        assertThrows(NotFoundException.class,()-> {
            BeerDTO beerDTO = beerController.getBeerById(UUID.randomUUID());
        });

    }
    @Transactional
    @Rollback
    @Test
    void newBeer() {
        BeerDTO newBeerDTO = BeerDTO.builder()
                    .beerName("New beer")
                    .upc("UPC-NEW")
                    .price(new BigDecimal("2.18"))
                    .build();

        ResponseEntity responseEntity= beerController.addBeer(newBeerDTO);

        assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());

        assertNotNull(responseEntity.getHeaders().getLocation());
        String newBeerLoc[] = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID newBeerUUID=UUID.fromString(newBeerLoc[4]);

        Beer beer = beerRepository.findById(newBeerUUID).get();
        assertNotNull(beer);

    }

    @Transactional
    @Rollback
    @Test
    void updateBeer() {

        Beer b =beerRepository.findAll().get(0);
        BeerDTO updateBeerDTO = BeerDTO.builder()
                .beerName(b.getBeerName()+"UPDATED")
                .beerStyle(BeerStyle.STOUT)
                .upc("xxx")
                .quantityOnHand(1010101)
                .price(new BigDecimal("17.28"))
                .build();

        ResponseEntity responseEntity = beerController.updateBeer(b.getId(), updateBeerDTO);



        assertEquals(HttpStatusCode.valueOf(204),responseEntity.getStatusCode());

        Beer updatedBeer = beerRepository.findById(b.getId()).get();

        assertEquals(updateBeerDTO.getBeerName(),updatedBeer.getBeerName());
        assertEquals(updateBeerDTO.getBeerStyle(),updatedBeer.getBeerStyle());
        assertEquals(updateBeerDTO.getUpc(),updatedBeer.getUpc());
        assertEquals(updateBeerDTO.getQuantityOnHand(),updatedBeer.getQuantityOnHand());
        assertEquals(updateBeerDTO.getPrice(),updatedBeer.getPrice());
    }


    @Test
    void updateInvalidBeerId() {

        BeerDTO updateBeerDTO = BeerDTO.builder()
                .build();

        assertThrows(NotFoundException.class,()->{
            ResponseEntity responseEntity = beerController.updateBeer(UUID.randomUUID(), updateBeerDTO);
        });
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeerFound() {
        List<Beer> beers = beerRepository.findAll();

        ResponseEntity responseEntity=beerController.deleteBeer(beers.get(0).getId());

        assertEquals(HttpStatus.NO_CONTENT,responseEntity.getStatusCode());
        assertThrows(NotFoundException.class,()->{
            beerController.getBeerById(beers.get(0).getId());
        });

        assertEquals(beers.size()-1,beerRepository.findAll().size());
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeerNotFound() {



        assertThrows(NotFoundException.class,()->{
            ResponseEntity responseEntity=beerController.deleteBeer(UUID.randomUUID());
        });


    }
}