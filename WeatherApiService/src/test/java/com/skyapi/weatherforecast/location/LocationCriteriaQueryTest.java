package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationCriteriaQueryTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCriteriaQuery(){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder ();
        CriteriaQuery <Location> query = criteriaBuilder.createQuery (Location.class);

        Root <Location> root = query.from (Location.class);

        //Add Where clause
        Predicate predicate = criteriaBuilder.equal (root.get ("countryCode"), "US");
        query.where (predicate);

        //add ORDER By clause
        query.orderBy (criteriaBuilder.asc (root.get ("cityName")));

        TypedQuery <Location> typeQuery = entityManager.createQuery (query);

        //Add pagination
        typeQuery.setFirstResult (0);
        typeQuery.setMaxResults (3);


        List <Location> resultList = typeQuery.getResultList ();

        assertThat(resultList).isNotEmpty ();

        resultList.forEach (System.out::println);
    }

    @Test
    public void testJPQL(){

        String jpql = "FROM Location WHERE countryCode = 'US' ORDER BY cityName";

        TypedQuery <Location> typeQuery = entityManager.createQuery (jpql, Location.class);
        List <Location> resultList = typeQuery.getResultList ();

        assertThat(resultList).isNotEmpty ();

        resultList.forEach (System.out::println);
    }
}
