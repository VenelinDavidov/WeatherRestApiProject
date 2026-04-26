package com.skyapi.weatherforecast.location.repository;

import com.skyapi.weatherforecast.common.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FilterableLocationRepository {


    public Page<Location> listWithFilter(Pageable pageable, Map <String, Object> filterFields);
}
