package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomerService customerService;

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO c = CustomerDTO.builder()
                    .id(UUID.randomUUID())
                    .customerName("Cust1")
                    .version(3)
                    .build();

        given(customerService.getCustomerById(c.getId())).willReturn(Optional.of(c));

        mockMvc.perform(
                            get(CustomerController.CUSTOMER_PATH_ID,c.getId().toString())
                            .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName",is(c.getCustomerName())));
    }

    @Test
    void listCustomers() throws Exception {
        List<CustomerDTO> cList = List.of(
          CustomerDTO.builder()
                  .id(UUID.randomUUID())
                  .customerName("Customer1")
                  .build(),

                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Customer2")
                        .build(),
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Customer3")
                        .build(),
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Customer4")
                        .build(),
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Customer5")
                        .build()
                );

        given(customerService.listCustomer()).willReturn(cList);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(cList.size())))
                .andExpect(jsonPath("$.[0].customerName",is(cList.get(0).getCustomerName())));

    }

    @Test
    void createCustomer() throws Exception {
        CustomerDTO c= CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Testy McTesty")
                .build();

        given( customerService.addCustomer(any(CustomerDTO.class))).willReturn(c);

        mockMvc.perform(
                        post(CustomerController.CUSTOMER_PATH)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(c))
                    )

                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDTO c= CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Testy McTesty")
                .build();

        given(customerService.updateCustomer(c.getId(),c)).willReturn(Optional.of(c));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID,c.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c))
            ).andExpect(status().isNoContent());

        verify(customerService).updateCustomer(c.getId(),c);
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDTO c= CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Testy McTesty")
                .build();

        given(customerService.deleteCustomer(c.getId())).willReturn(Boolean.TRUE);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID,c.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(c.getId());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomer(uuidArgumentCaptor.capture());
        assertEquals(c.getId(),uuidArgumentCaptor.getValue(),"Check cusotmer delete UUID is value passed");


    }

    @Test
    void patchCustomer() throws Exception {
        CustomerDTO c= CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Testy McTesty")
                .build();

        Map<String, Object> custMap= new HashMap<>();

        custMap.put("customerName","McTesty UPDATED");
        CustomerDTO updatedCustomerDTO = objectMapper.convertValue(custMap, CustomerDTO.class);

        willDoNothing().given(customerService).patchCustomer(c.getId(), updatedCustomerDTO);

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID,c.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomerDTO))

        ).andExpect(status().isNoContent());

        verify(customerService).patchCustomer(c.getId(), updatedCustomerDTO);

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<CustomerDTO> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);

        verify(customerService).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertEquals(c.getId(),uuidArgumentCaptor.getValue(),"Patch UUID is value passed");
        assertEquals(updatedCustomerDTO, customerArgumentCaptor.getValue(), "Customer update matches");

    }

    @Test
    void customerNotFoundException() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());


        mockMvc.perform(
                    get(CustomerController.CUSTOMER_PATH_ID,UUID.randomUUID())
                ).andExpect(status().isNotFound());

    }

}