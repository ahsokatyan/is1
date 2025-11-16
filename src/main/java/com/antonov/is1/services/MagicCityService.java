package com.antonov.is1.services;


import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.entities.BookCreatureType;
import com.antonov.is1.entities.Ring;
import com.antonov.is1.repos.MagicCityRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
public class MagicCityService {

    @EJB
    private MagicCityRepository magicCityRepo;

    public MagicCity createMagicCity(String name, Float area, Long population,
                                     LocalDateTime establishmentDate, BookCreatureType governor,
                                     Boolean capital, Float populationDensity) {
        MagicCity city = new MagicCity();
        city.setName(name);
        city.setArea(area);
        city.setPopulation(population);
        city.setEstablishmentDate(establishmentDate);
        city.setGovernor(governor);
        city.setCapital(capital);
        city.setPopulationDensity(populationDensity);

        return magicCityRepo.save(city);
    }

    public Optional<MagicCity> getMagicCityById(Long id) {
        return magicCityRepo.findById(id);
    }

    public List<MagicCity> getAllMagicCities() {
        return magicCityRepo.findAll();
    }

    public List<MagicCity> getAllMagicCities(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return magicCityRepo.findAll(offset, pageSize);
    }

    public MagicCity updateMagicCity(MagicCity city) {
        return magicCityRepo.update(city);
    }

    public boolean deleteMagicCity(Long id) {
        Optional<MagicCity> city = magicCityRepo.findById(id);
        if (city.isPresent()) {
            magicCityRepo.delete(city.get());
            return true;
        }
        return false;
    }

}
