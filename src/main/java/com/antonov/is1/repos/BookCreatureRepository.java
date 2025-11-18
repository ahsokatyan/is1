package com.antonov.is1.repos;

import com.antonov.is1.entities.BookCreature;
import com.antonov.is1.entities.BookCreatureType;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@NoArgsConstructor
public class BookCreatureRepository extends BasicRepository<BookCreature> {
    @Override
    protected Class<BookCreature> getEntityClass() {
        return BookCreature.class;
    }

    /**
     * Находит существо по ID кольца
     */
    public Optional<BookCreature> findByRingId(Long ringId) {
        List<BookCreature> creatures = findAll();
        return creatures.stream()
                .filter(creature -> creature.getRing() != null &&
                        creature.getRing().getId().equals(ringId))
                .findFirst();
    }

    /**
     * Находит всех существ в указанном городе
     */
    public List<BookCreature> findByCityId(Long cityId) {
        return em.createQuery("SELECT b FROM BookCreature b WHERE b.creatureLocation.id = :cityId", BookCreature.class)
                .setParameter("cityId", cityId)
                .getResultList();
    }

    /**
     * Находит существ без колец
     */
    public List<BookCreature> findCreaturesWithoutRings() {
        List<BookCreature> allCreatures = findAll();
        return allCreatures.stream()
                .filter(creature -> creature.getRing() == null)
                .collect(Collectors.toList());
    }

    /**
     * Находит существ без колец, исключая указанное существо
     */
    public List<BookCreature> findCreaturesWithoutRings(Long excludeCreatureId) {
        List<BookCreature> allCreatures = findAll();
        return allCreatures.stream()
                .filter(creature -> creature.getRing() == null &&
                        !creature.getId().equals(excludeCreatureId))
                .collect(Collectors.toList());
    }
}