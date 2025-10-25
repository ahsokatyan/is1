package com.antonov.is1.beans;

import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.services.MagicCityService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class MagicCityBean implements Serializable {

    @Inject
    private MagicCityService magicCityService;

    private List<MagicCity> cities;
    private MagicCity selectedCity;
    private MagicCity newCity;

    @PostConstruct
    public void init() {
        loadCities();
        initNewCity();
    }

    public void loadCities() {
        cities = magicCityService.getAllMagicCities();
    }

    private void initNewCity() {
        newCity = new MagicCity();
    }

    public void createCity() {
        try {
            magicCityService.createMagicCity(
                    newCity.getName(), newCity.getArea(), newCity.getPopulation(),
                    newCity.getEstablishmentDate(), newCity.getGovernor(),
                    newCity.getCapital(), newCity.getPopulationDensity()
            );
            addMessage("Успех", "Город создан успешно!");
            loadCities();
            initNewCity();
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось создать город: " + e.getMessage());
        }
    }

    public void updateCity() {
        try {
            magicCityService.updateMagicCity(selectedCity);
            addMessage("Успех", "Город обновлен успешно!");
            loadCities();
            selectedCity = null;
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось обновить город: " + e.getMessage());
        }
    }

    public void deleteCity() {
        if (selectedCity != null) {
            try {
                magicCityService.deleteMagicCity(selectedCity.getId());
                addMessage("Успех", "Город удален успешно!");
                loadCities();
                selectedCity = null;
            } catch (Exception e) {
                addMessage("Ошибка", "Не удалось удалить город: " + e.getMessage());
            }
        }
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

}