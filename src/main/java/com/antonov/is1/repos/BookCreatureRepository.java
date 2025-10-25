package com.antonov.is1.repos;

import com.antonov.is1.entities.BookCreature;
import com.antonov.is1.entities.BookCreatureType;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
@NoArgsConstructor
public class BookCreatureRepository extends BasicRepository<BookCreature> {
    @Override
    protected Class<BookCreature> getEntityClass() {
        return BookCreature.class;
    }

}