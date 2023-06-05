package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor

public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH+"/{customerId}";
    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId)
    {
        log.debug("Controller: Getting cusotmer id:"+customerId.toString());
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers()
    {
        return customerService.listCustomer();
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity addCustomer(@RequestBody CustomerDTO c)
    {
        CustomerDTO newCustomerDTO = customerService.addCustomer(c);

        HttpHeaders h = new HttpHeaders();
        h.add("Location", CUSTOMER_PATH+"/"+ newCustomerDTO.getId());

        return new ResponseEntity(h, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO c)
    {
        if( customerService.updateCustomer(id, c).isEmpty())
        {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomer(@PathVariable("customerId") UUID cId)
    {
        if( !customerService.deleteCustomer(cId))
        {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO c)
    {
        customerService.patchCustomer(id,c);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
