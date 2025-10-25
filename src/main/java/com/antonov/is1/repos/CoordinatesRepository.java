package com.antonov.is1.repos;


import com.antonov.is1.entities.Coordinates;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@NoArgsConstructor
public class CoordinatesRepository extends BasicRepository<Coordinates> {
    @Override
    protected Class<Coordinates> getEntityClass() {
        return Coordinates.class;

    }

}