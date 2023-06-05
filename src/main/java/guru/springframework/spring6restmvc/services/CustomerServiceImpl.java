package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;
    public CustomerServiceImpl() {
        log.debug("Initialising customer data");
        CustomerDTO[] cArray = {
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Fred Smith")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build(),
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Jo Jones")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build(),
                CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName("Arnold Dog")
                        .version(1)
                        .lastUpdatedDate(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .build()
        };
        customerMap = new HashMap<>();

        Arrays.stream(cArray).forEach(c-> customerMap.put(c.getId(),c));
        log.debug("End customer service init");
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("Service:: Get customer by id:"+id.toString());
        return Optional.of(customerMap.get(id));
    }
    @Override
    public List<CustomerDTO> listCustomer()
    {
        log.debug("Serice:: list customers");
        return new ArrayList<CustomerDTO>(customerMap.values());
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO c) {

        CustomerDTO newC= CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName(c.getCustomerName())
                .lastUpdatedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        customerMap.put(newC.getId(),newC);
        return newC;
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO c) {
        CustomerDTO cu=customerMap.get(id);
        cu.setCustomerName(c.getCustomerName());
        cu.setVersion(c.getVersion());
        cu.setLastUpdatedDate(LocalDateTime.now());

        return Optional.of(cu);

    }

    @Override
    public Boolean deleteCustomer(UUID cId) {
        log.debug("Customer Service::Deleting customer id:"+cId.toString());
        customerMap.remove(cId);

        return true;
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO c) {

        CustomerDTO uc = customerMap.get(id);
        if( c.getCustomerName()!=null && !c.getCustomerName().isEmpty())
        {
            uc.setCustomerName(c.getCustomerName());
        }

        if( c.getVersion()!=null )
        {
            uc.setVersion(c.getVersion());
        }
        uc.setLastUpdatedDate(LocalDateTime.now());

    }
}
