package com.antonov.is1.services;

import com.antonov.is1.entities.BookCreature;
import com.antonov.is1.entities.Ring;
import com.antonov.is1.repos.RingRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;

@Stateless
public class RingService {

    @EJB
    private RingRepository ringRepo;

    public Ring createRing(String name, Long power, Double weight) {
        Ring ring = new Ring();
        ring.setName(name);
        ring.setPower(power);
        ring.setWeight(weight);
        return ringRepo.save(ring);
    }

    public List<Ring> getAllRings(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return ringRepo.findAll(offset, pageSize);
    }

    public Optional<Ring> getRingById(Long id) {
        return ringRepo.findById(id);
    }

    public List<Ring> getAllRings() {
        return ringRepo.findAll();
    }

    public Ring updateRing(Ring ring) {
        return ringRepo.update(ring);
    }

    public boolean deleteRing(Long id) {
        Optional<Ring> ring = ringRepo.findById(id);
        if (ring.isPresent()) {
            ringRepo.delete(ring.get());
            return true;
        }
        return false;
    }

    public List<Ring> getFreeRings(){
        return ringRepo.getFreeRings();
    }
}
