package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    //@Autowired
    //BeerController beerController;

    @Autowired
    MockMvc  mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl=new BeerServiceImpl();
    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, false, 1, 25).toList().get(0);

        // configure the mock bean to return a beer given an ID
        given(beerService.getBeerById(testBeerDTO.getId()))
                .willReturn(Optional.of(testBeerDTO));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeerDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName",is(testBeerDTO.getBeerName())));


    }

    @Test
    void listBeers() throws Exception {
        List<BeerDTO> beerDTOList = List.of(
                BeerDTO.builder()
                    .id(UUID.randomUUID())
                    .beerName("Beer1")
                    .build(),
                BeerDTO.builder()
                        .id(UUID.randomUUID())
                        .beerName("Beer2")
                        .build(),
                BeerDTO.builder()
                        .id(UUID.randomUUID())
                        .beerName("Beer3")
                        .build(),
                BeerDTO.builder()
                        .id(UUID.randomUUID())
                        .beerName("Beer4")
                        .build()
        );

        given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(new PageImpl<> (beerDTOList));

        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[1].beerName",is("Beer2")))
                .andExpect(jsonPath("$.content.length()", is(beerDTOList.size())));
    }

    @Test
    void createBeer() throws Exception {

        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("London Pride")
                .beerStyle(BeerStyle.ALE)
                .upc("xxx")
                .price(new BigDecimal("4.30"))
                .quantityOnHand(333)
                .build();

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(b);

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void createEmptyBeerName() throws Exception {

        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerStyle(BeerStyle.ALE)
                .price(new BigDecimal("4.30"))
                .upc("xxx")
                .quantityOnHand(333)
                .build();

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(b);

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isBadRequest());

        b.setBeerName("  ");

         MvcResult mvcResult =mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void createEmptyBeerStyle() throws Exception {

        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Some Beer")

                .upc("xxx")
                .price(new BigDecimal("4.30"))
                .quantityOnHand(333)
                .build();

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(b);

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isBadRequest()).andReturn();


        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    void updateBeer() throws Exception {
        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("London Pride")
                .beerStyle(BeerStyle.ALE)
                .upc("xxx")
                .price(new BigDecimal("4.30"))
                .quantityOnHand(333)
                .build();

        //willDoNothing().given(beerService).updateBeer(any(UUID.class),any(Beer.class));
        given(beerService.updateBeer(b.getId(),b)).willReturn(Optional.of(b));

        mockMvc.perform(
                            put(BeerController.BEER_PATH_ID,b.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(b))
                    ).andExpect(status().isNoContent());

        verify(beerService,times(1)).updateBeer(b.getId(),b);
    }
    @Test
    void updateEmptyBeerName() throws Exception {
        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())

                .beerStyle(BeerStyle.ALE)
                .price(new BigDecimal("4.30"))
                .upc("xxx")
                .quantityOnHand(333)
                .build();

        //willDoNothing().given(beerService).updateBeer(any(UUID.class),any(Beer.class));
        given(beerService.updateBeer(b.getId(),b)).willReturn(Optional.of(b));

        mockMvc.perform(
                put(BeerController.BEER_PATH_ID,b.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(1)))
                .andExpect(jsonPath("$.beerName", containsString( BeerDTO.VALIDATION_ERROR.BLANK )))
                .andExpect(jsonPath("$.beerName", containsString( BeerDTO.VALIDATION_ERROR.NULL )));

        verify(beerService,times(0)).updateBeer(b.getId(),b);
    }

    @Test
    void deleteBeer() throws Exception {
        BeerDTO b = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("London Pride")
                .beerStyle(BeerStyle.ALE)
                .price(new BigDecimal("4.30"))
                .quantityOnHand(333)
                .build();

        //willDoNothing().given(beerService).updateBeer(any(UUID.class),any(Beer.class));
        given(beerService.deleteBeer(b.getId())).willReturn(Boolean.TRUE);

        mockMvc.perform(
                delete(BeerController.BEER_PATH_ID,b.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isNoContent());

        verify(beerService,times(1)).deleteBeer(b.getId());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeer(uuidArgumentCaptor.capture());
        assertEquals(b.getId(),uuidArgumentCaptor.getValue(), "Compare UUID used to delete");
    }

    @Test
    void patchBeer() throws Exception {

        UUID beerId=UUID.randomUUID();

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName","London Pride UPDATED");

        BeerDTO patchBeerDTO = objectMapper.convertValue(beerMap, BeerDTO.class);


        BeerDTO beerOutput = BeerDTO.builder()
                .id(beerId)
                .beerName(patchBeerDTO.getBeerName())
                .beerStyle(BeerStyle.ALE)
                .price(new BigDecimal("4.30"))
                .quantityOnHand(333)
                .build();


        given(beerService.patchBeer(beerOutput.getId(), patchBeerDTO)).willReturn(Optional.of(beerOutput));

        mockMvc.perform(
                        patch(BeerController.BEER_PATH_ID,beerOutput.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchBeerDTO))
                        )
                        .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<BeerDTO> beerArgumentCaptor = ArgumentCaptor.forClass(BeerDTO.class);

        verify(beerService,times(1)).patchBeer(beerOutput.getId(), patchBeerDTO);

        verify(beerService).patchBeer(uuidArgumentCaptor.capture(),beerArgumentCaptor.capture());
        assertEquals(beerOutput.getId(),uuidArgumentCaptor.getValue(), "UUID Compare");
        assertEquals(patchBeerDTO,beerArgumentCaptor.getValue(), "BeerMap compare");

    }


    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(
                    get(BeerController.BEER_PATH_ID,UUID.randomUUID())
                ).andExpect(status().isNotFound());

    }

}