package com.skyapi.weatherforecast.location.repository.impl;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.repository.FilterableLocationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Repository
public class FilterableLocationRepositoryImpl implements FilterableLocationRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public Page <Location> listWithFilter(Pageable pageable, Map <String, Object> filterFields) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder ();
        CriteriaQuery <Location> query = builder.createQuery (Location.class);

        Root <Location> root = query.from (Location.class);

        TypedQuery <Location> typeQuery = entityManager.createQuery (query);
        typeQuery.setFirstResult ((int) pageable.getOffset ());
        typeQuery.setMaxResults (pageable.getPageSize ());

        List <Location>listResult = typeQuery.getResultList ();

        int totalRows = 0;

        return new PageImpl <> (listResult, pageable, totalRows);
    }
}
