package com.antonov.is1.repos;

import com.antonov.is1.entities.BookCreature;
import com.antonov.is1.entities.BookCreatureType;
import com.antonov.is1.entities.MagicCity;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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

    public List<BookCreature> findByType(BookCreatureType type) {
        if (type == null) {
            // Если тип не указан, возвращаем пустой список
            return new ArrayList<>();
        }

        try {
            return em.createQuery(
                            "SELECT b FROM BookCreature b WHERE b.creatureType = :type", BookCreature.class)
                    .setParameter("type", type)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Находит всех существ указанного типа с кольцами
     */
    public List<BookCreature> findByTypeWithRings(BookCreatureType type) {
        if (type == null) {
            return new ArrayList<>();
        }

        try {
            return em.createQuery(
                            "SELECT b FROM BookCreature b WHERE b.creatureType = :type AND b.ring IS NOT NULL", BookCreature.class)
                    .setParameter("type", type)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Находит всех существ, проживающих в указанном городе
     */
    public List<BookCreature> findByCity(MagicCity city) {
        if (city == null) {
            return new ArrayList<>();
        }

        try {
            return em.createQuery(
                            "SELECT b FROM BookCreature b WHERE b.creatureLocation = :city", BookCreature.class)
                    .setParameter("city", city)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}