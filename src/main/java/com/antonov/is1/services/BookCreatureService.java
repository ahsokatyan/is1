package com.antonov.is1.services;


import com.antonov.is1.entities.*;
import com.antonov.is1.repos.*;
import com.antonov.is1.websocket.CreaturesWebSocket;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PrePersist;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
public class BookCreatureService {

    @EJB
    private BookCreatureRepository bookCreatureRepo;

    @EJB
    private CoordinatesRepository coordinatesRepo;

    @EJB
    private MagicCityRepository magicCityRepo;

    @EJB
    private RingRepository ringRepo;


    /**
     * Получение BookCreature по ID
     */
    public Optional<BookCreature> getBookCreatureById(Long id) {
        return bookCreatureRepo.findById(id);
    }

    /**
     * Получение всех BookCreature с пагинацией
     */
    public List<BookCreature> getAllBookCreatures(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookCreatureRepo.findAll(offset, pageSize);
    }

    /**
     * Получение всех BookCreature без пагинации
     */
    public List<BookCreature> getAllBookCreatures() {
        return bookCreatureRepo.findAll();
    }


    /**
     * Создание BookCreature со связями (координаты, город, кольцо)
     * Все связанные объекты сохраняются каскадно
     */

    public BookCreature createBookCreatureWithoutRelations(String name,
                                                        Coordinates coordinates,
                                                        Long age,
                                                        BookCreatureType creatureType,
                                                        MagicCity creatureLocation,
                                                        Long attackLevel,
                                                        Ring ring) {
        Coordinates managedCoordinates = coordinatesRepo.findById(coordinates.getId())
                .orElseThrow(() -> new EntityNotFoundException("Coordinates not found"));

        MagicCity managedCreatureLocation = magicCityRepo.findById(creatureLocation.getId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        Ring managedRing = ringRepo.findById(ring.getId())
                .orElseThrow(() -> new EntityNotFoundException("Ring not found"));

        BookCreature creature = new BookCreature();
        creature.setName(name);
        creature.setCoordinates(managedCoordinates);
        creature.setAge(age);
        creature.setCreatureType(creatureType);
        creature.setCreatureLocation(managedCreatureLocation);
        creature.setAttackLevel(attackLevel);
        creature.setRing(managedRing);

        return bookCreatureRepo.save(creature);
    }
    /**
     * Получение количества всех BookCreature (для пагинации)
     */
    public long getBookCreaturesCount() {
        return bookCreatureRepo.count();
    }


    public boolean assignRingToCreature(Long creatureId, Long ringId) {
        // Проверяем, свободно ли кольцо
        if (ringRepo.isRingUsed(ringId)) {
            throw new IllegalStateException("Кольцо уже используется другим существом");
        }

        BookCreature creature = bookCreatureRepo.findById(creatureId).orElse(null);
        Ring ring = ringRepo.findById(ringId).orElse(null);

        creature.setRing(ring);
        bookCreatureRepo.merge(creature);

        return true;
    }
    /**
     * Создание нового BookCreature с автоматической установкой creationDate
     */
    public BookCreature createBookCreature(BookCreature creature) {

        // creationDate автоматически установится благодаря @PrePersist
        if (creature.getCreatureLocation() != null) {
            MagicCity managedCreatureLocation = magicCityRepo.findById(creature.getCreatureLocation().getId())
                    .orElseThrow(() -> new EntityNotFoundException("City not found"));
            creature.setCreatureLocation(managedCreatureLocation);
        }
        if (creature.getRing() != null) {
            Ring managedRing = ringRepo.findById(creature.getRing().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Ring not found"));
            creature.setRing(managedRing);
        }


        return bookCreatureRepo.save(creature);

    }
    /**
     * Обновление BookCreature
     */
    public BookCreature updateBookCreature(BookCreature creature) {
        return bookCreatureRepo.update(creature);
    }

    /**
     * Удаление BookCreature по ID
     */
    public boolean deleteBookCreature(Long id) {
        Optional<BookCreature> creature = bookCreatureRepo.findById(id);
        if (creature.isPresent()) {
            bookCreatureRepo.delete(creature.get());
            return true;
        }
        return false;
    }

    public List<BookCreature> findBookCreaturesByType(BookCreatureType type) {
        return bookCreatureRepo.findByType(type);
    }

}