package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilterableLocationRepositoryTest {

    @Autowired
    private LocationRepository repository;


    @Test
    public void testListWithDefaults(){
        int pageSize = 5;
        int pageNum = 0;

        String sortField = "code";

        Sort sort = Sort.by (sortField).ascending ();

        Pageable pageable = PageRequest.of (pageNum,pageSize,sort);

        Page <Location> page = repository.listWithFilter (pageable, Collections.emptyMap ());
        List <Location> content = page.getContent ();

        assertThat(content).size ().isEqualTo (pageSize);
        assertThat(content).isSortedAccordingTo (new Comparator <Location> () {
            @Override
            public int compare(Location o1, Location o2) {
                 return o1.getCode ().compareTo (o2.getCode ());
            }
        });

        content.forEach (System.out::println);
    }
}
