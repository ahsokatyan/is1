package com.antonov.is1.repos;

import com.antonov.is1.entities.Coordinates;
import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.entities.BookCreatureType;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
@NoArgsConstructor
public class MagicCityRepository extends BasicRepository<MagicCity> {
    @Override
    protected Class<MagicCity> getEntityClass() {
        return MagicCity.class;
    }

    /**
     * Находит города, где правителем является эльф
     */
    public List<MagicCity> findCitiesWithElfGovernor() {
        return em.createQuery(
                "SELECT mc FROM MagicCity mc WHERE mc.governor = :governor", MagicCity.class)
                .setParameter("governor", BookCreatureType.ELF)
                .getResultList();
    }
}