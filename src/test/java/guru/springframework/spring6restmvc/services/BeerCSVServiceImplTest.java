package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeerCSVServiceImplTest {

    BeerCSVService beerCSVService = new BeerCSVServiceImpl();

    @Test
    void convertCsv() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> beerCSVRecordList = beerCSVService.convertCsv(file);

        System.out.println(beerCSVRecordList.size());

        assertTrue(beerCSVRecordList.size()>0);

    }
}